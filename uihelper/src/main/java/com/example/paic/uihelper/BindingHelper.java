package com.example.paic.uihelper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.example.paic.uihelper.entity.HelperEntity;
import com.example.paic.uihelper.kpswitch.IPanelHeightTarget;
import com.example.paic.uihelper.kpswitch.util.KPSwitchConflictUtil;
import com.example.paic.uihelper.kpswitch.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 模块管理的基类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class BindingHelper implements ViewActivable, View.OnClickListener {

    /**
     * 储存HelperEntity
     */
    private static List<HelperEntity> helperEntities = new ArrayList<>();
    private ViewGroup panelLayout;

    private BindingHelper() {
    }

    public static BindingHelper newInstance() {
        return BindingHelperHolder.bindingHelper;
    }

    static class BindingHelperHolder {
        static BindingHelper bindingHelper = new BindingHelper();
    }

    /**
     * 重置、初始化View的状态
     */
    public void reset() {
        for (HelperEntity entity : helperEntities) {
            //重置View的状态
            entity.setViewState(ViewState.STATE_INACTIVE);
            View view = entity.getBindView();
            view.setBackgroundResource(entity.getInActiveStateId());
        }
    }

    /**
     * 绑定panelLayout
     *
     * @param panelLayout
     * @return
     */
    public BindingHelper bindPanelLayout(Activity activity, ViewGroup panelLayout) {
        this.panelLayout = panelLayout;
        if (!(panelLayout instanceof IPanelHeightTarget)) {
            throw new IllegalArgumentException("use bindPanelLayout must ensure " +
                    "panelLayout be IPanelHeightTarget");
        }
        //保存keyboard的高度
        KeyboardUtil.attach(activity, (IPanelHeightTarget) panelLayout);
        return this;
    }

    /**
     * 将View和layout绑定的在一起
     *
     * @param bindView
     * @param bindLayout
     * @param activeResId
     * @param inActiveResId
     * @return
     */
    public BindingHelper bindView(View bindView, View bindLayout, int activeResId, int inActiveResId) {
        bindView(bindView, bindLayout, activeResId, inActiveResId, false);
        return this;
    }

    public BindingHelper bindView(View bindView, View bindLayout, int activeResId, int inActiveResId, boolean isHide) {
        HelperEntity helperEntity = new HelperEntity();
        helperEntity.setBindView(bindView);
        helperEntity.setBindLayout(bindLayout);
        helperEntity.setViewState(ViewState.STATE_INACTIVE);
        helperEntity.setActiveStateId(activeResId);
        helperEntity.setInActiveStateId(inActiveResId);
        helperEntity.setAlone(isHide);

        helperEntities.add(helperEntity);
        bindView.setOnClickListener(this);

        return this;
    }


    /**
     * 接触View和Fragment的绑定
     */
    public void unBindView() {
        //清空两个map
        helperEntities.clear();
    }

    @Override
    public synchronized void active(HelperEntity entity) {
        View layout = entity.getBindLayout();
        if (layout.getVisibility() != View.VISIBLE) {
            layout.setVisibility(View.VISIBLE);
        }

        if (panelLayout != null && !entity.isAlone()) {
            KPSwitchConflictUtil.showPanel(panelLayout);
        } else {
            KeyboardUtil.hideKeyboard(entity.getBindView());
        }

        //重置View的状态
        entity.setViewState(ViewState.STATE_ACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getActiveStateId());
    }

    @Override
    public synchronized void inActive(HelperEntity entity) {
        View layout = entity.getBindLayout();
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
        }

        //重置View的状态
        entity.setViewState(ViewState.STATE_INACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getInActiveStateId());
    }


    @Override
    public void onClick(View v) {
        initView(v);
    }

    /**
     * 更新panel的状态
     *
     * @param view
     */
    private void initView(View view) {
        int visible = View.GONE;
        for (HelperEntity entity : helperEntities) {
            ViewState viewState = entity.getViewState();
            //重置点击的View所对应panel的状态
            if (viewState == ViewState.STATE_ACTIVE && entity.getBindView() == view) {
                inActive(entity);
                continue;
            } else if (viewState == ViewState.STATE_INACTIVE && entity.getBindView() == view) {
                if (!entity.isAlone()){
                    visible = View.VISIBLE;
                }
                active(entity);
            }

            if (entity.getBindView() != view) {
                inActive(entity);
            }
        }
        //由于parentLayout是公用的，统一设置它的状态
        if (panelLayout != null) {
            panelLayout.setVisibility(visible);
        }
    }

    /**
     * 用于记录模块的状态
     * STATE_ACTIVE：可用状态
     * STATE_INACTIVE：不可用状态
     *
     * @author ex-zhangyuelei001
     * @date 2017/12/8.
     */

    public enum ViewState {
        //可用状态
        STATE_ACTIVE,
        //不可用状态
        STATE_INACTIVE

    }
}
