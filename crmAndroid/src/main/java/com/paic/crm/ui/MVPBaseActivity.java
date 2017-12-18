package com.paic.crm.ui;

import android.os.Bundle;

import com.paic.crm.presenter.BasePresenter;


/**
 * Created by yueshaojun on 2017/6/25.
 */

public abstract class MVPBaseActivity<V extends IBaseView,
		T extends BasePresenter<V>>extends BaseActivity{
	public T mBasePresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBasePresenter = createPresenter();
	}

	@Override
	public void onAttachedToWindow() {
		mBasePresenter.attachView((V) this);
		super.onAttachedToWindow();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBasePresenter.detachView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public abstract T createPresenter();
}
