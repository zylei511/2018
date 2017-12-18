package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by yueshaojun on 2017/6/2.
 */

public class ToastUtil {
	public static void showToast(Activity targetActivity,String tip,boolean isLong){
		if(isLong) {
			Toast.makeText(targetActivity.getApplicationContext(), tip, Toast.LENGTH_LONG).show();
		}else Toast.makeText(targetActivity.getApplicationContext(),tip,Toast.LENGTH_SHORT).show();
	}
	public static void showLongToast(Activity targetActivity ,String tip){
		showToast(targetActivity,tip,true);
	}
	public static void showShortToast(Activity targetActivity,String tip){
		showToast(targetActivity,tip,false);
	}
}
