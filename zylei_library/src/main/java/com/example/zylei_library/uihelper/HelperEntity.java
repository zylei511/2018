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
    private int activiteStateId;
    private int inActiviteStateId;
    private BaseHelper.ViewState viewState;

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

    public int getActiviteStateId() {
        return activiteStateId;
    }

    public void setActiviteStateId(int activiteStateId) {
        this.activiteStateId = activiteStateId;
    }

    public int getInActiviteStateId() {
        return inActiviteStateId;
    }

    public void setInActiviteStateId(int inActiviteStateId) {
        this.inActiviteStateId = inActiviteStateId;
    }

    public BaseHelper.ViewState getViewState() {
        return viewState;
    }

    public void setViewState(BaseHelper.ViewState viewState) {
        this.viewState = viewState;
    }
}
