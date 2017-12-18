package com.paic.crm.utils;

import android.content.Context;

import com.paic.crm.common.CommonAdapter;

/**
 * Created by ex-zhangyuelei001 on 2017/11/9.
 * 业务线工具类
 */

public class BizSeriesUtil {
    /**
     * 判断是否是空中客服
     * @param context
     * @return
     */
    public static boolean isKZKF(Context context){
        return checkBizSeries(context, Constants.BIZ_SERIES_SDK_KZKF);
    }

    /**
     * 判断是否是某条业务线
     * @param context
     * @param bizSeries 业务线名字
     * @return
     */
    private static boolean checkBizSeries(Context context, String bizSeries){
        String PAD_BIZ_SERIES = (String) SpfUtil.getValue(context, SpfUtil.PAD_BIZ_SERIES, "");
        return bizSeries.equals(PAD_BIZ_SERIES);
    }
}
