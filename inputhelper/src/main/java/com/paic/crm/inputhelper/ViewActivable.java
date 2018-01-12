package com.paic.crm.inputhelper;

import com.paic.crm.inputhelper.entity.HelperEntity;

/**
 * 模块的处理接口
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public interface ViewActivable {
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
