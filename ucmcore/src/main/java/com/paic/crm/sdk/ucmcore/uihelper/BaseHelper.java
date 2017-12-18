package com.paic.crm.sdk.ucmcore.uihelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.paic.crm.sdk.ucmcore.utils.CRMLog;

import java.util.HashMap;


/**
 * 模块管理的基类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/8.
 */
public class BaseHelper extends AbstractHelper implements ActiveInterface, View.OnClickListener {

    protected Fragment fragment;
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
    private static HashMap<Fragment, ViewState> stateMap = new HashMap<>();

    protected BaseHelper() {
    }


    /**
     * 将模块fragment和对应的view绑定在一起
     *
     * @param fragment 模块数据所在的fragment
     * @param view     控制模块状态的View
     */
    public void bindView(FragmentActivity activity, Fragment fragment, int layoutId, View view) {
        this.fragment = fragment;
        this.view = view;
        this.layoutId = layoutId;

        //绑定view和fragment
        hashMap.put(view, fragment);
        //初始化fragment的状态，默认是不可用的状态
        stateMap.put(fragment, ViewState.STATE_INACTIVE);

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
    public void unBindView(){
        //移除所有Fragment
        for (Fragment fragment: stateMap.keySet()) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }

        //清空两个map
        hashMap.clear();
        stateMap.clear();
    }

    @Override
    public void active(Fragment fragment) {
        //设置控制器View的状态为ViewState.STATE_ACTIVE
        stateMap.put(fragment, ViewState.STATE_ACTIVE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

        CRMLog.LogDebug(getClass().getSimpleName(), fragment.getClass().getSimpleName() + " active()");
    }

    @Override
    public void inActive(Fragment fragment) {
        //设置控制器View的状态为ViewState.STATE_INACTIVE
        stateMap.put(fragment, ViewState.STATE_INACTIVE);
        if (fragment.isVisible()) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
        CRMLog.LogDebug(getClass().getSimpleName(), fragment.getClass().getSimpleName() + " inActive()");
    }

    @Override
    public void onClick(View v) {
        for (View view : hashMap.keySet()) {
            if (view == v) {
                Fragment fragment = hashMap.get(view);

                //初始化按钮状态
                initFragment(fragment);
                return;
            }
        }
    }

    /**
     * 除指定的fragment外，其他的都设置为不可用状态
     *
     * @param fragment
     */
    private void initFragment(Fragment fragment) {
        ViewState viewState = stateMap.get(fragment);
        //根据控制器的当前的状态，来达到不同的结果
        //如果是可用状态
        if (viewState == ViewState.STATE_ACTIVE) {
            inActive(fragment);

            //如果是不可用状态
        } else if (viewState == ViewState.STATE_INACTIVE) {
            for (Fragment otherFragment : stateMap.keySet()){
                if (otherFragment != fragment){
                    inActive(otherFragment);
                }
            }
            active(fragment);
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
