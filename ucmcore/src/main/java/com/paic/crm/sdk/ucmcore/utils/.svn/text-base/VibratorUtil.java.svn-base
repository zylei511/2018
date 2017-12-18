package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by ex-zhangyuelei001 on 2017/8/4.
 * 震动控制类
 */

public class VibratorUtil {

    /**
     * 开启震动
     * @param context
     * @param vibrateTime 震动的时长
     */
    public static void vibrate(Context context, long vibrateTime) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(vibrateTime);
    }
}
