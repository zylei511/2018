package com.paic.crm.uihelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.SelectFaceHelper;



/**
 * Created by yueshaojun on 2017/8/29.
 */

public class FaceStateHelper extends BaseHelper{
	private SelectFaceHelper mFaceHelper;
	private int mHeight = 0;

	private ViewGroup.LayoutParams lp;
	public FaceStateHelper(Context context, View container,int defaultHeight) {
		super(context, container);
		mFaceHelper = new SelectFaceHelper(context,container);
		mHeight = defaultHeight;
		lp = mContainer.getLayoutParams();
	}


	public void active(){
		mContainer.setVisibility(View.VISIBLE);
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = mHeight;
		mContainer.setLayoutParams(lp);
		CRMLog.LogDebg("face","helper active:"+mHeight);
	}
	public void inActive(){
		mContainer.setVisibility(View.GONE);
	}
	public void setFaceOpreateListener(SelectFaceHelper.OnFaceOprateListener opreateListener){
		mFaceHelper.setFaceOpreateListener(opreateListener);
	}
	public void setHeight(int height){
		mHeight = height;
	}
}
