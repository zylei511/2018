package com.paic.crm.sdk.ucmcore.uihelper;

import android.support.v4.app.Fragment;

/**
 * 模块的处理接口
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public interface ActiveInterface {
    /**
     * 模块处于可用状态执行的方法
     * @param fragment
     */
    void active(Fragment fragment);

    /**
     * 模块处于不可用状态执行的方法
     * @param fragment
     */
    void inActive(Fragment fragment);

    /**
     * 获取模块所处的状态
     * @return 模块是否处于可用状态
     */
//    boolean getActive();
}
