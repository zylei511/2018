package com.paic.crm.sdk.ucmcore.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.IdRes;

import java.io.IOException;

/**
 * 播放声音的util
 * 
 * @author yueshaojun988
 * 把声音文件放在raw文件夹下
 */
public class SoundUtil {

	private Context ctx;

	private MediaPlayer mp;

	private static SoundUtil instance = null;

	private boolean isAllowPlay = true;

	private boolean playing = false;
	private int resourceId;

	private SoundUtil() {
	}

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static SoundUtil getInstance() {
		if (null == instance) {
			instance = new SoundUtil();
		}
		return instance;
	}

	/**
	 * 配置
	 * @param ctx
	 * @param resourceId 放在raw文件夹下
	 */

	public void build(Context ctx,@IdRes int resourceId){
		this.ctx = ctx;
		this.resourceId = resourceId;

	}
	/**
	 * 初始化媒体播放器
	 * 
	 * @param
	 */
	private MediaPlayer initMedia() {
		if(ctx==null){
			throw new NullPointerException();
		}
		MediaPlayer mediaPlayer;
		mediaPlayer = MediaPlayer.create(ctx, resourceId);
		return mediaPlayer;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (null != mp) {
			mp.release();
		}
	}
	/**
	 *  播放资源
	 */
	public void playVoice(final boolean isRepeat){
		new Thread(new Runnable() {
			@Override
			public void run() {
				mp = initMedia();
				if(!isAllowPlay){
					return;
				}
				if(mp!=null){
					mp.stop();
				}
				try {
					mp.prepare();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mp.start();
				mp.setLooping(isRepeat);
				playing = true;
			}
		}).start();
	}

	public void stopVoice(){
		playing = false;
		mp.release();
	}
	public void pauseVoice(){
		if(mp!=null&&mp.isPlaying()){
			mp.stop();
		}
	}

	public void setIsAllowPlay(boolean isAllowPlay) {
		this.isAllowPlay = isAllowPlay;
	}

	public boolean isPlaying() {
		return playing;
	}

	/**
	 * 开启手机系统自带铃声
	 */
	public void startAlarm(Context context) {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, getSystemDefultRingtoneUri(context));
		mediaPlayer.setLooping(false);
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}

	/**
	 * 获取系统自带铃声的uri
	 * @return RingtoneManager.getActualDefaultRingtoneUri(this,
			RingtoneManager.TYPE_RINGTONE)
	 */
	private Uri getSystemDefultRingtoneUri(Context context) {
		return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
	}
}
