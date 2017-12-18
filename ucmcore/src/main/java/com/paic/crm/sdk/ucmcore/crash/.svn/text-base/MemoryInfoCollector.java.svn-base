package com.paic.crm.sdk.ucmcore.crash;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by yueshaojun on 17/3/1.
 */

public class MemoryInfoCollector {
    private static int MAX_SIZE = 1024*8;
        public static synchronized String collectMemoInfo(Context context)
        throws IOException {
            StringBuffer result = new StringBuffer();
            String pid = getProcessPid(context);
            Process process = Runtime.getRuntime().exec(new String[]{"dumpsys","meminfo",pid});
            InputStream in = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader,MAX_SIZE);
            while (true) {
                String line = bufferedReader.readLine();
                //如果没有读取结束
                if(line==null){
                    break;
                }
                result.append(line).append("\n");
            }
            return result.toString();
        }
    private static String getProcessPid(Context context){
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos =  activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcessInfo :runningAppProcessInfos){
            if(packageName.equals(appProcessInfo.processName)){
                return appProcessInfo.pid+"";
            }
        }
        return null;
    }
}
