package com.example.paic.uihelper.entity;


import android.view.View;

import com.example.paic.uihelper.BindingHelper;

/**
 * 用于存放View和Fragment的相关数据
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class HelperEntity {
    private View bindView;
    private View bindLayout;
    private int activeStateId;
    private int inActiveStateId;
    private BindingHelper.ViewState viewState;
    private boolean isAlone = false;

    public View getBindView() {
        return bindView;
    }

    public void setBindView(View bindView) {
        this.bindView = bindView;
    }

    public View getBindLayout() {
        return bindLayout;
    }

    public void setBindLayout(View bindLayout) {
        this.bindLayout = bindLayout;
    }

    public int getActiveStateId() {
        return activeStateId;
    }

    public void setActiveStateId(int activeStateId) {
        this.activeStateId = activeStateId;
    }

    public int getInActiveStateId() {
        return inActiveStateId;
    }

    public void setInActiveStateId(int inActiveStateId) {
        this.inActiveStateId = inActiveStateId;
    }

    public BindingHelper.ViewState getViewState() {
        return viewState;
    }

    public void setViewState(BindingHelper.ViewState viewState) {
        this.viewState = viewState;
    }

    public boolean isAlone() {
        return isAlone;
    }

    public void setAlone(boolean alone) {
        isAlone = alone;
    }
}
