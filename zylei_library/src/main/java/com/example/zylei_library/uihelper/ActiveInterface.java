package com.example.zylei_library.uihelper;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 模块的处理接口
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public interface ActiveInterface {
    /**
     * 模块处于可用状态执行的方法
     * @param entity
     */
    void active(HelperEntity entity);

    /**
     * 模块处于不可用状态执行的方法
     * @param entity
     */
    void inActive(HelperEntity entity);

    /**
     * 获取模块所处的状态
     * @return 模块是否处于可用状态
     */
}
