package com.example.zylei_library.uihelper.util;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by ex-zhangyuelei001 on 2017/9/6.
 * 主要负责声音的录制、保存 工具类
 */

public class MediaRecorderManager {
    private MediaRecorder mMediaRecorder;
    //需要录制的类型
    private RecordType mRecordType;
    //文件路径
    private String mFilePath;
    //最长录音时长
    private static final int MAX_DURATION = 60 * 1000;
    //最小时间间隔
    private static final int MIN_SPACE_TIME = 1000;
    //最小刷新时间
    private static final int MIN_SPACE_REFRESH_TIME = 100;

    private RecordListener mRecordListener;
    //开始录制的时间
    private long mStartTime;

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateRecorder(mMediaRecorder);
        }
    };
    private String mAudioDir;
    private String mVideoDir;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean mIsOutOfMaxTime;
    //录音文件的后缀
    private static final String RECORD_FILE_NAME_SUFFIX_AUDIO = ".mp3";
    //视频文件的后缀
    private static final String RECORD_FILE_NAME_SUFFIX_VIDEO = ".mp4";

    public static final String CRM_NET_AUDIO = "crm_net_audio";
    public static final String CRM_CUSTOMER_LIST = "crm_customer_list";
    private RecordState recordState;

    public enum RecordType {
        //声音类型
        RECORDER_TYPE_AUDIO
        //视频类型
        , RECORDER_TYPE_VIDEO
    }

    /**
     * 初始化MediaRecorder
     *
     * @param recordType
     * @return
     */
    public MediaRecorder init(Context context, RecordType recordType, Camera camera, SurfaceHolder holder) {
        WeakReference<Context> weakReference = new WeakReference<Context>(context);
        mRecordType = recordType;
        mCamera = camera;
        mSurfaceHolder = holder;
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        } else {
            mMediaRecorder.reset();
        }

        if (mCamera != null) {
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
        }
        return prapareRecorder(weakReference.get(), mMediaRecorder, recordType);
    }

    public MediaRecorder init(Context context) {
        return init(context, RecordType.RECORDER_TYPE_AUDIO, null, null);
    }

    private MediaRecorder prapareRecorder(Context context, MediaRecorder mediaRecorder, RecordType recordType) {
        switch (recordType) {
            case RECORDER_TYPE_AUDIO:

                String temPath = mkdirs(context, String.valueOf(System.currentTimeMillis()));
                mediaRecorder = prapareAudioRecorder(mediaRecorder, temPath);

                break;
            case RECORDER_TYPE_VIDEO:
                mVideoDir = context.getExternalCacheDir().getPath() + File.separator + CRM_NET_AUDIO + File.separator;
                Log.e("TAG", "mVideoDir = " + mVideoDir);
                File videoDir = new File(mVideoDir);
                videoDir.mkdirs();

                //// TODO: 2017/9/12 如果是录制视频，可以在这里添加逻辑
                break;
            default:
                mediaRecorder = new MediaRecorder();
                break;
        }
        return mediaRecorder;
    }


    private MediaRecorder prapareAudioRecorder(MediaRecorder mediaRecorder, String temPath) {
        //设置录制文件的路径
//        mFilePath = mAudioDir + System.currentTimeMillis() + LocalStr.RECORD_FILE_NAME_SUFFIX_AUDIO;
        mFilePath = temPath;
        // 设置麦克风是声音来源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置所录制的音视频文件的格式：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //设置所录制的声音的编码格式：THREE_GPP/MPEG-4/RAW_AMR/Default
        // THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置录制的音频文件的保存位置
        mediaRecorder.setOutputFile(temPath);
        //设置最大的录音时长
        mediaRecorder.setMaxDuration(MAX_DURATION);
        return mediaRecorder;
    }

    /**
     * 开始录制
     */
    public void startRecorder() {
        if (mMediaRecorder == null) {
            return;
        }

        if (getRecordState() == RecordState.RECORD_STATE_PLAYING){
            return;
        }

        try {
            //准备录制
            mMediaRecorder.prepare();
            //开始录制
            mMediaRecorder.start();

            setStartTime();
            //更新录音状态
            updateRecorder(mMediaRecorder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 更新录音状态
     */
    private void updateRecorder(MediaRecorder mediaRecorder) {
        if (mediaRecorder == null) {
            return;
        }
        setRecordState(RecordState.RECORD_STATE_PLAYING);
        //获取在前一次调用此方法之后录音中出现的最大振幅
        int ratio = mediaRecorder.getMaxAmplitude();
        double decible = ratio > 0 ? 20 * Math.log10(ratio) : 0;
        float recordTime = recordTime();
        if (mRecordListener != null) {
            mRecordListener.onUpdate(decible, recordTime / MAX_DURATION);
        }
        //录音时间超出最大值
        if (recordTime >= MAX_DURATION){
            mIsOutOfMaxTime = true;
            stopRecorder();
            return;
        }
        mHandler.postDelayed(mUpdateMicStatusTimer, MIN_SPACE_REFRESH_TIME);
    }

    private void setStartTime() {
        mStartTime = System.currentTimeMillis();
    }


    /**
     * 停止录音
     */
    public void stopRecorder() {
        //如果是因为超出最大时间限制而停止的，执行
//        if (mIsOutOfMaxTime){
//            mIsOutOfMaxTime = false;
//            return;
//        }
        setRecordState(RecordState.RECORD_STATE_STOP);

        //取消录音时，执行
        if (mMediaRecorder == null ) {
            if (mRecordListener != null){
                mRecordListener.onFinished("", 0);
            }
            return;
        }

        try {
            mMediaRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            if (RecordType.RECORDER_TYPE_VIDEO == mRecordType) {
//                CameraUtil.stopPreview(mCamera);
//            }
        }

        if (recordTime() < MIN_SPACE_TIME) {
            File file = getRecordFile();
            if (file.exists()) {
                file.delete();
            }
        }

        if (mRecordListener != null) {
            mRecordListener.onFinished(mFilePath, recordTime() / MIN_SPACE_TIME);
        }
    }

    /**
     * 取消录音
     */
    public void cancelRecorder() {
        if (mMediaRecorder == null) {
            return;
        }
        setRecordState(RecordState.RECORD_STATE_STOP);
        try {
            mMediaRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
//            if (RecordType.RECORDER_TYPE_VIDEO == mRecordType) {
//                CameraUtil.stopPreview(mCamera);
//            }
            if (mRecordListener != null) {
                boolean isRetain = mRecordListener.onCancel();
                File file = getRecordFile();
                if (!isRetain && file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public static String mkdirs(Context context, String fileName) {
        String dirPath = context.getExternalCacheDir() + File.separator + CRM_NET_AUDIO + File.separator;
        File dir = new File(dirPath);
        dir.mkdirs();

        final String temPath = dirPath + fileName + RECORD_FILE_NAME_SUFFIX_AUDIO;
        return temPath;
    }

    public void setRecordListener(RecordListener recordListener) {
        mRecordListener = recordListener;
    }

    private long recordTime() {
        long recordTime = System.currentTimeMillis() - mStartTime;
        return recordTime > MAX_DURATION ? MAX_DURATION : recordTime;
    }

    private File getRecordFile() {
        return new File(mFilePath);
    }

    private void setRecordState(RecordState recordState){
        this.recordState = recordState;
    }

    private RecordState getRecordState(){
        return recordState;
    }

    public enum RecordState{
        RECORD_STATE_PLAYING,RECORD_STATE_STOP
    }


    public interface RecordListener {
        /**
         * 开始录音时调用
         *
         * @param decible 录制音量的大小
         * @param progress 录制时间/允许最大录制时间
         */

        void onUpdate(double decible, float progress);

        /**
         * 停止录音时调用
         * 用户取消操作时，path的值为""，recordTime的值为0
         * @param path 录音文件的地址
         * @param recordTime 录音文件的时间
         */
        void onFinished(String path, float recordTime);

        /**
         * 取消录音时调用,false：删除录音文件，true：保存录音文件
         */
        boolean onCancel();
    }


}
