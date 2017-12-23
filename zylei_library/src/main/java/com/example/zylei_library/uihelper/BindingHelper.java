package com.example.zylei_library.uihelper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;


import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;


/**
 * 模块管理的基类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class BindingHelper implements ViewActivable, View.OnClickListener {

    protected View view;
    protected int layoutId;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    /**
     * 储存HelperEntity
     */
    private static List<HelperEntity> helperEntities = new ArrayList<>();
    private int activeStateId;
    private int inActiveStateId;
    private Fragment container;
    private Fragment fragment;
    private View layout;

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


    /**
     * 将模块fragment和对应的view绑定在一起
     *  @param fragment 模块数据所在的fragment
     * @param view     控制模块状态的View
     */
    public BindingHelper bindView(Fragment container, Fragment fragment, int layoutId, View view,View layout) {
        this.view = view;
        this.layoutId = layoutId;
        this.container = container;
        this.fragment = fragment;
        this.layout = layout;
        return this;
    }

    /**
     * 设置选中状态
     *
     * @param resId
     * @return
     */
    public BindingHelper bindActiveState(int resId) {
        activeStateId = resId;
        return this;
    }

    /**
     * 设置非选中状态
     *
     * @param resId
     * @return
     */
    public BindingHelper bindInActiveState(int resId) {
        inActiveStateId = resId;
        return this;
    }

    public void create() {

        HelperEntity helperEntity = new HelperEntity();
        helperEntity.setBindView(view);
        helperEntity.setBindFragment(fragment);
        helperEntity.setViewState(ViewState.STATE_INACTIVE);
        helperEntity.setActiveStateId(activeStateId);
        helperEntity.setInActiveStateId(inActiveStateId);
        helperEntities.add(helperEntity);

        fragmentManager = container.getChildFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layoutId, fragment);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
        view.setOnClickListener(this);
    }

    /**
     * 接触View和Fragment的绑定
     */
    public void unBindView() {
        //移除所有Fragment
        for (HelperEntity entity : helperEntities) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(entity.getBindFragment());
            fragmentTransaction.commit();
        }

        //清空两个map
        helperEntities.clear();
    }

    @Override
    public synchronized void active(HelperEntity entity) {
        if (layout.getVisibility() != View.VISIBLE){
            layout.setVisibility(View.VISIBLE);
        }
        //设置控制器View的状态为ViewState.STATE_ACTIVE
        Fragment currentFragment = entity.getBindFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(currentFragment);
        fragmentTransaction.commit();

        //重置View的状态
        entity.setViewState(ViewState.STATE_ACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getActiveStateId());
    }

    @Override
    public synchronized void inActive(HelperEntity entity) {
        if (layout.getVisibility() == View.VISIBLE){
            layout.setVisibility(View.GONE);
        }
        //设置控制器View的状态为ViewState.STATE_INACTIVE
        Fragment currentFragment = entity.getBindFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currentFragment);
        fragmentTransaction.commit();

        //重置View的状态
        entity.setViewState(ViewState.STATE_INACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getInActiveStateId());
    }

    public void removeChildFragment(Fragment parentFragment) {
        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        KPSwitchConflictUtil.hidePanelAndKeyboard(layout);
        initFragment(v);
    }

    /**
     * 重置fragment的状态
     *
     * @param view
     */
    private void initFragment(View view) {
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

    // 隐藏软键盘
    public void hideInputManager(Context ct) {
        try {
            ((InputMethodManager) ct.getSystemService(ct.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
                    .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("test", "hideInputManager Catch error,skip it!", e);
        }
    }
}
