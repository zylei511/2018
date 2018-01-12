package com.paic.crm.inputhelper.util;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

import java.io.IOException;

/**
 * Created by ex-zhangyuelei001 on 2017/9/13.
 */

public class MediaPlayerManager {
    private MediaPlayer mediaPlayer ;
    private boolean isPlaying;

    private MediaPlayerManager(){}

    public static MediaPlayerManager newInstance(){
        return MediaPlayerManagerHolder.instance;
    }

    static class MediaPlayerManagerHolder{
        static MediaPlayerManager instance = new MediaPlayerManager();
    }

    private synchronized MediaPlayer createMedia() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        setListener();
        return mediaPlayer;
    }

    public int getDuration(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        int duration = 0;
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            duration = (int) Math.round(mediaPlayer.getDuration() / 1000 + 0.5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.release();

        return duration;
    }

    /**
     * 播放音乐
     * @param filePath
     */
    public synchronized void playSound(String filePath) {
        createMedia();
        //注释掉的内容是：正在播放时停止，再次点击才播放
//        if (!isPlaying){
//            isPlaying = true;
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
//        } else {
//            isPlaying = false;
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//        }
    }

    private synchronized void setListener() {
        //设置一个error监听器
        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPlaying = false;
                mp.reset();
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mp.stop();
                mp.reset();
                mediaPlayer = null;
            }
        });
    }


    public synchronized void stop(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer = null;
            isPlaying = false;
        }
    }


    /**
     * 释放资源
     */
    public synchronized void release() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }
}