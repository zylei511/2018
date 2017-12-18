package com.paic.crm.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.paic.crm.presenter.BasePresenter;
import com.paic.crm.ui.IBaseView;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2017/11/8
 */

public abstract class MVPBaseFragment<V extends IBaseView,
        T extends BasePresenter<V>> extends Fragment {

    public T mBasePresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBasePresenter = createPresenter();
        mBasePresenter.attachView((V) this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBasePresenter.detachView();
    }

    public abstract T createPresenter();
}
