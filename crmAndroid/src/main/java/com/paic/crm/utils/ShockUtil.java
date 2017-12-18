package com.paic.crm.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 震动工具类
 * @author yueshaojun
 */
public class ShockUtil {
    private static ShockUtil instance;
    private Vibrator vib;
    private boolean isAllowVib = true;
    private long [] pattern = {100,400,100,400};
    ShockUtil(Context context){
        vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }
    public static ShockUtil getInstance(Context context){
        if(null == instance){
            synchronized (ShockUtil.class){
                if(null == instance){
                    instance = new ShockUtil(context);
                }
            }
        }
        return instance;
    }

    /**
     * 自定义震动模式
     * 
     * @param pattern 数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。 单位是毫秒
     * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public  void Vibrate(boolean isRepeat) {
        if(!isAllowVib){
            return;
        }
        vib.vibrate(pattern,isRepeat? 0:-1);
    }

    public void setIsAllowVib(boolean isAllowVib) {
        this.isAllowVib = isAllowVib;
    }
}
