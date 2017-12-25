package com.example.zylei_library.uihelper;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;

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
    private View bindView;
    private ViewGroup bindLayout;
    private int activeResId;
    private int inActiveResId;
    private ViewGroup parentLayout;

    protected BindingHelper() {
    }

    private static BindingHelper baseHelper;

    public static BindingHelper newInstance() {
        if (baseHelper == null) {
            synchronized (BindingHelper.class) {
                if (baseHelper == null) {
                    baseHelper = new BindingHelper();
                }
            }
        }
        return baseHelper;
    }

    public void reset(){
        for (HelperEntity entity:helperEntities){
            inActive(entity);
        }
    }


    /**
     * 将View和layout绑定的在一起
     * @param bindView
     * @param bindLayout
     * @return
     */
    public BindingHelper bindView(View bindView,ViewGroup bindLayout,ViewGroup parentLayout) {
        this.bindView = bindView;
        this.bindLayout = bindLayout;
        this.parentLayout = parentLayout;
        return this;
    }

    public BindingHelper addStateResource(int activeResId,int inActiveResId){
        this.activeResId = activeResId;
        this.inActiveResId = inActiveResId;
        return this;
    }


    public void bind() {

        HelperEntity helperEntity = new HelperEntity();
        helperEntity.setBindView(bindView);
        helperEntity.setBindLayout(bindLayout);
        helperEntity.setViewState(ViewState.STATE_INACTIVE);
        helperEntity.setActiveStateId(activeResId);
        helperEntity.setInActiveStateId(inActiveResId);
        helperEntity.setParentLayout(parentLayout);
        helperEntities.add(helperEntity);

        bindView.setOnClickListener(this);
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
//        KeyboardUtil.hideKeyboard(entity.getBindView());

        ViewGroup layout = entity.getBindLayout();
        if (layout.getVisibility() != View.VISIBLE){
            layout.setVisibility(View.VISIBLE);
        }
        KPSwitchConflictUtil.showPanel(entity.getParentLayout());

        //重置View的状态
        entity.setViewState(ViewState.STATE_ACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getActiveStateId());
    }

    @Override
    public synchronized void inActive(HelperEntity entity) {
        ViewGroup layout = entity.getBindLayout();
        if (layout.getVisibility() == View.VISIBLE){
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
     * 重置fragment的状态
     *
     * @param view
     */
    private void initView(View view) {
        for (HelperEntity entity : helperEntities) {
            ViewState viewState = entity.getViewState();
            if (viewState == ViewState.STATE_ACTIVE && entity.getBindView() == view) {
                inActive(entity);
                continue;
            } else if (viewState == ViewState.STATE_INACTIVE && entity.getBindView() == view) {
                active(entity);
            }

            if (entity.getBindView() != view) {
                inActive(entity);
            }
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
