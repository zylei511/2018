package com.example.zylei_library.uihelper;


import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 用于存放View和Fragment的相关数据
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class HelperEntity {
    private View bindView;
    private Fragment bindFragment;
    private int activeStateId;
    private int inActiveStateId;
    private BindingHelper.ViewState viewState;

    public View getBindView() {
        return bindView;
    }

    public void setBindView(View bindView) {
        this.bindView = bindView;
    }

    public Fragment getBindFragment() {
        return bindFragment;
    }

    public void setBindFragment(Fragment bindFragment) {
        this.bindFragment = bindFragment;
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
}
