package com.paic.crm.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.paic.crm.android.R;

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

	private SoundUtil(Context ctx) {
		this.ctx = ctx;
		mp = initMedia();
	}

	/**
	 * 单例
	 * 
	 * @param ctx
	 * @return
	 */
	public static SoundUtil getInstance(Context ctx) {
		if (null == instance) {
			instance = new SoundUtil(ctx);
		}
		return instance;
	}

//	private boolean isSoundON() {
//		return (Boolean) SpfUtil.getValue(ctx, SpfUtil.getSetVoice(),
//				SpfUtil.DEFVALUE_SP_VOIVE);
//	}

	/**
	 * 初始化媒体播放器
	 * 
	 * @param
	 */
	private MediaPlayer initMedia() {
		MediaPlayer mediaPlayer;
		mediaPlayer = MediaPlayer.create(ctx,R.raw.msgcome);
		return mediaPlayer;
	}

	public void playSound(String musicName) {
		mp.reset();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		AssetFileDescriptor asset = null;
		try {
			asset = ctx.getAssets().openFd(musicName);
			mp.setDataSource(asset.getFileDescriptor(), asset.getStartOffset(),
					asset.getLength());
			play();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != asset) {
				try {
					asset.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

//		selectPlayMusic(musicName);
	}
//
//	private void selectPlayMusic(String musicName) {
//		if (MSGCOME.equals(musicName)&&!isSoundON()) {
//			return;
//		}else {
//			play();
//		}
//	}

	private void play() {
		if (null != mp) {
			try {
				if (null != mp) {
					mp.stop();
				}
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放资源
	 */
	public void release() {
		if (null != mp) {
			mp.release();
		}
	}

	public void playVoice(){
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
	}

	public void setIsAllowPlay(boolean isAllowPlay) {
		this.isAllowPlay = isAllowPlay;
	}
}
