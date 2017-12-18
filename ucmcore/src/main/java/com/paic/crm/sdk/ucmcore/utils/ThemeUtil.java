package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

import com.paic.crm.sdk.ucmcore.R;

/**
 * Created by yueshaojun on 2017/5/31.
 */

public class ThemeUtil {
	private Context mContext;
	public ThemeUtil(Context context){
		mContext = context;
	}
	public void apply(@ColorRes int color){
		if(Build.VERSION.SDK_INT>=21) {
			((Activity) mContext)
					.getWindow()
					.setStatusBarColor(mContext.getResources().getColor(color));
		}
		if(Build.VERSION.SDK_INT>=23){
			((Activity) mContext)
					.getWindow()
					.setStatusBarColor(mContext.getResources().getColor(color,mContext.getTheme()));
		}
		mContext.getTheme().applyStyle(R.style.CRMAppTheme,true);
	}
}
