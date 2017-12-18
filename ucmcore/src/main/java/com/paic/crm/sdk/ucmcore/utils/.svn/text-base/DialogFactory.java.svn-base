package com.paic.crm.sdk.ucmcore.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.paic.crm.sdk.ucmcore.R;
import com.paic.crm.sdk.ucmcore.listener.OnItemClickListener;

/**
 * Created by yueshaojun on 2017/5/31.
 */

public class DialogFactory {
	/**
	 * 加载框
	 * @param context
	 * @param tip 提示内容
	 */
	public static Dialog getLoadingDialog(Context context, String tip) {
		Dialog loadingDialog = new Dialog(context, R.style.dialog);
		loadingDialog.setContentView(R.layout.crm_sdk_dialog_loading_layout);
		loadingDialog.setCancelable(true);
		Window window = loadingDialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int width = CommonUtils.getScreenWidth(context);
		lp.width = (int) (0.5 * width);
		TextView titleTxtv = (TextView) loadingDialog.findViewById(R.id.tvLoad);
		loadingDialog.setCanceledOnTouchOutside(false);
		if (tip == null || tip.length() == 0) {
			titleTxtv.setText(R.string.crm_sdk_empty_note_loading);
		} else {
			titleTxtv.setText(tip);
		}
		return loadingDialog;
	}
	public static PromptDialog getPromptDialog(Context context, final OnItemClickListener onItemClickListener){
		PromptDialog promptDialog = new PromptDialog(context);
		promptDialog.setOnItemClickListener(onItemClickListener);
		return promptDialog;
	}
}
