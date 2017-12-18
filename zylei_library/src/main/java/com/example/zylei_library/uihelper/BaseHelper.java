package com.example.zylei_library.uihelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 模块管理的基类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class BaseHelper extends AbstractHelper implements ActiveInterface, View.OnClickListener {

    protected View view;
    protected int layoutId;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    /**
     * 储存View和fragment的关系
     */
    private static HashMap<View, Fragment> hashMap = new HashMap<>();
    /**
     * 储存fragment与ViewState的关系
     */
    private static HashMap<View, ViewState> stateMap = new HashMap<>();
    /**
     * 储存HelperEntity
     */
    private static List<HelperEntity> helperEntities = new ArrayList<>();
    private int activiteStateId;
    private int inActiviteStateId;
    private FragmentActivity activity;
    private Fragment fragment;

    protected BaseHelper() {
    }


    /**
     * 将模块fragment和对应的view绑定在一起
     *
     * @param fragment 模块数据所在的fragment
     * @param view     控制模块状态的View
     */
    public BaseHelper bindView(FragmentActivity activity, Fragment fragment, int layoutId, View view) {
        this.view = view;
        this.layoutId = layoutId;
        this.activity = activity;
        this.fragment = fragment;
        return this;
    }

    /**
     * 设置选中状态
     *
     * @param resId
     * @return
     */
    public BaseHelper bindActiveState(int resId) {
        activiteStateId = resId;
        return this;
    }

    /**
     * 设置非选中状态
     *
     * @param resId
     * @return
     */
    public BaseHelper bindInActiveState(int resId) {
        inActiviteStateId = resId;
        return this;
    }

    public void create() {

        HelperEntity helperEntity = new HelperEntity();
        helperEntity.setBindView(view);
        helperEntity.setBindFragment(fragment);
        helperEntity.setViewState(ViewState.STATE_INACTIVE);
        helperEntity.setActiviteStateId(activiteStateId);
        helperEntity.setInActiviteStateId(inActiviteStateId);
        helperEntities.add(helperEntity);

        fragmentManager = activity.getSupportFragmentManager();
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
        for (Fragment fragment : hashMap.values()) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }

        //清空两个map
        hashMap.clear();
        stateMap.clear();
    }

    @Override
    public void active(HelperEntity entity) {
        //设置控制器View的状态为ViewState.STATE_ACTIVE
        Fragment currentFragment = entity.getBindFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(currentFragment);
        fragmentTransaction.commit();

        //重置View的状态
        entity.setViewState(ViewState.STATE_ACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getActiviteStateId());


    }

    @Override
    public void inActive(HelperEntity entity) {
        //设置控制器View的状态为ViewState.STATE_INACTIVE
        Fragment currentFragment = entity.getBindFragment();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(currentFragment);
        fragmentTransaction.commit();

        //重置View的状态
        entity.setViewState(ViewState.STATE_INACTIVE);
        View view = entity.getBindView();
        view.setBackgroundResource(entity.getInActiviteStateId());
    }

    @Override
    public void onClick(View v) {
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
}
