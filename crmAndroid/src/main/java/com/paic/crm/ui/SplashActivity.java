package com.paic.crm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.paic.crm.adapter.SplashAdapter;
import com.paic.crm.android.R;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.pingan.core.im.client.app.PAIMApi;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity implements Runnable{

	private Handler mHandler=new Handler();
	private RelativeLayout sp_rootView;
	private ViewPager viewPager;
	private LinearLayout index;
	private List<ImageView> list;
	private Button clickToJump;

	private static final String TAG = "SplashActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		initView();
//		if("Y".equals(AppContext.firstUse)){
//			AppContext.firstUse = "N";
//			initPager();
//			return;
//		}

		//如果已经登录
		if(PAIMApi.getInstance().hasAccessToken()){
			CRMLog.LogDebg(TAG,"hasAccessToken");
			Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}else{
			CRMLog.LogDebg(TAG,"splashing");
			sp_rootView.setBackgroundResource(R.drawable.start);
			mHandler.postDelayed(this, 2000);
		}
	//	VolleyRequest.httpCheckLogin(HttpUrls.HTTP_CHECK_LOGIN,umId,shiroToken,volleyInterface);
	}

	private void initView(){
		sp_rootView=(RelativeLayout)findViewById(R.id.sp_rootView);
		viewPager = (ViewPager) findViewById(R.id.splash_vp);
		index = (LinearLayout) findViewById(R.id.index_view);
		clickToJump = (Button) findViewById(R.id.click_to_jump);
	}

	@Override
	public void run() {

		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(intent);
		CRMLog.LogInfo(Constants.LOG_TAG, "PAIMApi  " + "run");
		finish();
	}
	/**
	 * 绘制游标背景
	 */
	private void drawPoint(int index) {
		for (int i = 0; i < list.size(); i++) {
			if (index == i) {
				this.index.getChildAt(i).setBackgroundResource(R.drawable.dot_slide_01);
			} else {
				this.index.getChildAt(i).setBackgroundResource(R.drawable.dot_slide_02);
			}
		}
	}

	private void initPoint(){
		for(int i = 0;i<list.size();i++) {
			ImageView indexView = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 40;
			layoutParams.rightMargin = 40;
			index.addView(indexView, layoutParams);
			if(i==0) {
				index.getChildAt(i).setBackgroundResource(R.drawable.dot_slide_01);
			}else {
				index.getChildAt(i).setBackgroundResource(R.drawable.dot_slide_02);
			}
		}
	}

	private void initPager(){
		list = new ArrayList<>();
		int [] ids = {
				R.drawable.start,
				R.drawable.kf,
				R.drawable.app_icon
		};
		for(int i = 0;i<ids.length;i++){
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(ids[i]);
			list.add(imageView);
		}
		initPoint();
		SplashAdapter splashAdapter = new SplashAdapter(list);
		viewPager.setAdapter(splashAdapter);
		clickToJump.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				CRMLog.LogInfo(Constants.LOG_TAG, "PAIMApi  " + "run");
				finish();
			}
		});
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				drawPoint(position);
				if(position==list.size()-1){
					clickToJump.setVisibility(View.VISIBLE);
				}else {
					clickToJump.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

}
