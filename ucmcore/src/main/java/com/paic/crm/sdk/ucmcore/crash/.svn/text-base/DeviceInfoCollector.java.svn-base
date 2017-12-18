package com.paic.crm.sdk.ucmcore.crash;

import android.os.Build;

/**
 * Created by yueshaojun on 17/3/1.
 */

public class DeviceInfoCollector {
    public static String collectDeviceInfo(){
        StringBuffer deviceInfo = new StringBuffer();
         deviceInfo.append("api:").append(Build.VERSION.SDK_INT+"").append("\n")
                .append("board:").append(Build.BOARD).append("\n")
                .append("brand:").append(Build.BRAND).append("\n")
                .append("model:").append(Build.MODEL).append("\n")
                .append("device:").append(Build.DEVICE).append("\n")
                .append("hardware:").append(Build.HARDWARE).append("\n");
        return   deviceInfo.toString();
    }
}
