package com.paic.crm.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.paic.crm.android.BuildConfig;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Config;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.MessageNotification;
import com.paic.crm.utils.ShockUtil;
import com.paic.crm.utils.SoundUtil;
import com.paic.crmimlib.serivce.CrmImManager;
import com.pingan.core.im.ConfigEnum;
import com.pingan.paimkit.module.chat.bean.message.BaseChatMessage;
import com.pingan.paimkit.module.chat.manager.PMChatBaseManager;


/**
 * Created by hanyh on 16/1/25.
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initEnv();
        initDb();
        initNotification();
    }

    private void initNotification() {
        if (!isCurrentAppProcess()) {
            CRMLog.LogInfo(Constants.LOG_TAG, "This is Service process ~!");
        } else {
            CRMLog.LogInfo(Constants.LOG_TAG, "This is App process");

            // 接收消息处理
            PMChatBaseManager.getInstace().setMessageNotificationListener(
                    new PMChatBaseManager.MessageNotificationListener() {
                        @Override
                        public void notification(String jid,
                                                 BaseChatMessage baseChatMessage) {
                            //消息震动和通知声
                            ShockUtil.getInstance(getApplicationContext()).Vibrate(false);
                            SoundUtil.getInstance(getApplicationContext()).playVoice();
                            //唤醒屏幕
                            CRMLog.LogInfo("notification", "wakeUp");
                            //CommonUtils.wakeUpAndUnlock(getApplicationContext());
                            MessageNotification.getInstance(
                                    getApplicationContext()).notification(jid,
                                    baseChatMessage);
                        }
                    });
        }
    }

    private void initDb() {
        CrmDaoHolder.getInstance().initDb(this);
    }

    //初始化环境 STG2－－测试 PRD－－生产，由gradle配置。
    private void initEnv() {
        if("STG".equals(BuildConfig.ENV)){
            CRMLog.isLogOpen = true;
            //初始化整个IM库
            CrmImManager.getInstance().init(this,ConfigEnum.STG2);
            //初始化配置文件
            Config.init(getApplicationContext(),ConfigEnum.STG2);
        }else if("PRD".equals(BuildConfig.ENV)){
            CRMLog.isLogOpen = true;
            //初始化整个IM库
            CrmImManager.getInstance().init(this,ConfigEnum.PRD);
            //初始化配置文件
            Config.init(getApplicationContext(), ConfigEnum.PRD);
        }

    }

    /**
     * 判断是否为当前的app进程
     *
     * @return
     */
    private boolean isCurrentAppProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid && getPackageName().equals(appProcess.processName)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
