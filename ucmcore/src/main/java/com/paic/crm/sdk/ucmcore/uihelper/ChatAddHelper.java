package com.paic.crm.sdk.ucmcore.uihelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * 输入框，+号模块的管理类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class ChatAddHelper extends BaseHelper {
    private static ChatAddHelper baseHelper;

    public static ChatAddHelper newInstance() {
        if (baseHelper == null) {
            synchronized (ChatAddHelper.class) {
                if (baseHelper == null) {
                    baseHelper = new ChatAddHelper();
                }
            }
        }
        return baseHelper;
    }
}
