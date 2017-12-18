package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.paic.crm.sdk.ucmcore.R;
import com.paic.crm.sdk.ucmcore.listener.OnItemClickListener;

/**
 * Created by yueshaojun on 2017/6/26.
 */

public class PromptDialog {
	private TextView titleTxtv;
	private TextView positive;
	private TextView negative;
	private View contentView;
	private WindowManager windowManager;
	private WindowManager.LayoutParams wlp;
	private OnItemClickListener itemClickListener;
	private Context mContext ;
	private boolean isShow = false;
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
			if(itemClickListener==null){
				return;
			}
			if(v.getId() == R.id.crm_sdk_btn_positive){
				itemClickListener.onItemClick(0);
			}else {
				itemClickListener.onItemClick(1);
			}
		}
	};

	public PromptDialog(Context context) {
		mContext = context;
		contentView =
				LayoutInflater.from(context).inflate(R.layout.crm_sdk_dialog_permission_layout,null);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wlp = new WindowManager.LayoutParams();
		wlp.height = CommonUtils.dp2px(context,100);
		wlp.width = (int) (CommonUtils.getScreenWidth(context)*0.8);
		wlp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		wlp.type = WindowManager.LayoutParams.TYPE_TOAST;
		titleTxtv = (TextView) contentView.findViewById(R.id.crm_sdk_prompt_title);
		positive = (TextView) contentView.findViewById(R.id.crm_sdk_btn_positive);
		negative = (TextView) contentView.findViewById(R.id.crm_sdk_btn_negative);
		positive.setOnClickListener(clickListener);
		negative.setOnClickListener(clickListener);
	}


	public void setTitle(String title){
		titleTxtv.setText(title);
	}

	public void setOnItemClickListener(OnItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}
	public void show(){
		if(isShow){
			return;
		}
		CommonUtils.setBackGroundAlpha((Activity) mContext,0.5f);
		windowManager.addView(contentView,wlp);
		isShow = true;
	}
	public void dismiss(){
		isShow = false;
		CommonUtils.setBackGroundAlpha((Activity) mContext,1f);
		if(contentView.getWindowToken()==null)
		{
			return;
		}
		windowManager.removeView(contentView);
	}
}
