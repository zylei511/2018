package com.paic.crm.sdk.ucmcore.serivce;

import android.content.Context;

import com.paic.crm.sdk.ucmcore.serivce.IManager.ISjyManager;
import com.paic.crm.sdk.ucmcore.serivce.ManagerImp.SjyManagerImp;
import com.paic.crm.sdk.ucmcore.utils.CRMLog;
import com.paic.crm.sdk.ucmcore.utils.faceUtils.MsgQQFaceUtils;
import com.pingan.core.im.ConfigEnum;
import com.pingan.core.im.client.app.PAIMApi;
import com.pingan.paimkit.PAIMKitInfo;
import com.pingan.paimkit.module.login.listener.ILoginManagerListener;

/**
 * Created by yueshaojun on 2017/5/27.
 */

public class UCMCoreManager {
	private Context mContext ;
	private ISjyManager sjyManager;
	private UCMCoreManager(){
		sjyManager = new SjyManagerImp();
	}
	private static class InitCoreHolder{
		private static UCMCoreManager instance = new UCMCoreManager();
	}
	public void init(Context context, ConfigEnum configEnum){
		PAIMApi.getInstance().init(context);
		PAIMKitInfo.initPAIMKit(context, configEnum);
		mContext = context;
		if(configEnum == ConfigEnum.STG2) {
			CRMLog.isLogOpen = true;
		}else {
			CRMLog.isLogOpen = false;
		}
		MsgQQFaceUtils.initExpression(context);
	}
	public static UCMCoreManager getInstance(){
		return InitCoreHolder.instance;
	}

	public Context getContext(){
		return mContext;
	}
	public void loginSjy(String token, String customId, String sourcesys, String nikeName, String timestamp,ILoginManagerListener listener){
		sjyManager.loginSjy(token, customId, sourcesys, nikeName, timestamp, listener);
	}
	public void logoutSjy(){
		sjyManager.logoutSjy();
	}
}
