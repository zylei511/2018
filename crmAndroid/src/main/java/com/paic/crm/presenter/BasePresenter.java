package com.paic.crm.presenter;


import com.paic.crm.ui.IBaseView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by yueshaojun on 2017/6/25.
 */

public class BasePresenter<T extends IBaseView>{
	private Reference<T> mView;
	public void attachView(T view){
		mView = new WeakReference<T>(view);
	}
	public T getView(){
		return mView.get();
	}
	public void detachView(){
		if(mView!=null){
			mView.clear();
			mView = null;
		}
	}
	public boolean isViewAttatched(){
		if(mView!=null&&getView()!=null){
			return true;
		}
		return false;
	}

}
