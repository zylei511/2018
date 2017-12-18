package com.paic.crm.sdk.ucmcore.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by hanyh on 16/2/24.
 */
public class CRMLog {
    public static boolean isLogOpen = true;
    public static final String LOG_TAG = "crm_log_info";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static void LogInfo(String logTag, String logMsg) {
        if (isLogOpen) {
            if (logTag != null && logMsg != null) {
                Log.i(LOG_TAG, logTag+"——>"+logMsg);
                writeLogToFile(logTag, logMsg);

            }
        }
    }

    public static void LogDebug(String logTag, String logMsg) {
        if (isLogOpen) {
            Log.d(LOG_TAG,logTag+"-->"+logMsg);
            writeLogToFile(logTag, logMsg);

        }
    }

    public static void LogError(String logTag, String logMsg) {
        if (isLogOpen) {
            Log.e(LOG_TAG,logTag+"-->"+logMsg);
            writeLogToFile(logTag, logMsg);

        }
    }

    private static String fileName = Environment.getExternalStorageDirectory() + File.separator + "crm_log";

    private static void writeLogToFile(final String logTag, final String logMsg) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String log = simpleDateFormat.format(new Date()) + ":" + logTag + ":" + logMsg + "\r\n";
                String status = Environment.getExternalStorageState();
                if (!TextUtils.equals(status, Environment.MEDIA_MOUNTED)) {
                    return;
                }
                //文件夹
                String main = Environment.getExternalStorageDirectory().getPath() + File.separator + "crm_log";
                File destDir = new File(main);
                if (!destDir.exists()) {
                    destDir.mkdirs();//在根创建了文件夹
                }
                //文件
                String fileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "crm_log.txt";
                try {
                    File file = new File(destDir, fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fileWriter = new FileWriter(file, true);
                    fileWriter.write(log);
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

}
