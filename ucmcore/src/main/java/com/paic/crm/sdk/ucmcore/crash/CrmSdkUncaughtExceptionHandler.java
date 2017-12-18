package com.paic.crm.sdk.ucmcore.crash;

import android.content.Context;


import com.paic.crm.sdk.ucmcore.utils.CRMLog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yueshaojun on 17/3/1.
 */

public class CrmSdkUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private static final String LINE = "==========================";

    public CrmSdkUncaughtExceptionHandler(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        Throwable cause = ex;
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String exceptionStr = result.toString();

        StringBuffer logInfo = new StringBuffer();
        String date = SimpleDateFormat.getDateTimeInstance().format(new Date());
        String memoinfo = "";
        try {
            memoinfo = MemoryInfoCollector.collectMemoInfo(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logInfo.append(LINE + date + LINE + "\n")
                .append("caughtException:\n")
                .append(exceptionStr)
                .append(LINE + "\n")
                .append("threadInfo:\n")
                .append(ExceptionThreadInfoCollector.colletTreadInfo(thread))
                .append(LINE + "\n")
                .append("deviceInfo:\n")
                .append(DeviceInfoCollector.collectDeviceInfo())
                .append(LINE + "\n")
                .append("memoInfo:\n")
                .append(memoinfo);
        CRMLog.LogInfo("tag", "caught:\n" + logInfo.toString());



//        VolleyRequest.getInstance().httpUploadCrashLog(
//                HttpUrls.HTTP_CRASH_LOG
//                , mContext
//                , "android"
//                , logInfo.toString()
//                , new VolleyInterface() {
//                    @Override
//                    public void onSusscess(Object obj) {
//                    }
//
//                    @Override
//                    public void onError(VolleyError ve) {
//                    }
//
//                    @Override
//                    public void onLogOutside() {
//
//                    }
//                });
    }
}
