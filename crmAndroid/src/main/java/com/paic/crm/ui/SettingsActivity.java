package com.paic.crm.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.paic.crm.android.BuildConfig;
import com.paic.crm.android.R;
import com.paic.crm.app.AppContext;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.module.DataManager;
import com.paic.crm.module.Update;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.ShockUtil;
import com.paic.crm.utils.SoundUtil;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.AlertView;
import com.paic.crm.widget.OnItemClickListener;


public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener,OnItemClickListener{
	private SwitchCompat swt_vib;
	private SwitchCompat swt_sound;
	private TextView tx_version;
	private LinearLayout back;
	private TextView title;
	private ImageView iv_back;
	private TableRow tb_version;
	private TableRow tb_cache;
	private TextView tx_cache;
	private Dialog dialog;
	private String umId;
	private Handler mHandler;
	private String cacheSize = null;
	private AlertView alert;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		initData();
		initView();
		addListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initData() {
		umId = (String) SpfUtil.getValue(this,SpfUtil.UMID,"");
		mHandler = new Handler();
//		／try {
//			cacheSize =DataManager.getTotalCacheSize(getApplicationContext());
//			Log.i("cache",cacheSize);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void initView() {
		swt_vib = (SwitchCompat) findViewById(R.id.switch_vibrate);

		swt_sound = (SwitchCompat) findViewById(R.id.switch_sound);

		tx_version = (TextView) findViewById(R.id.version);
		tx_version.setText(BuildConfig.VERSION_NAME);

		tb_version = (TableRow) findViewById(R.id.tb_version);
		tb_cache = (TableRow) findViewById(R.id.tb_cache);

		tx_cache = (TextView) findViewById(R.id.cache);

		tx_cache.setText(cacheSize);
		back = (LinearLayout) findViewById(R.id.iv_back_parent);
		iv_back = (ImageView) back.findViewById(R.id.iv_back);
		iv_back.setImageResource(R.drawable.icon_btn_back);
		title = (TextView) findViewById(R.id.action_title);
		title.setText(R.string.settings);

		swt_sound.setChecked((Boolean) SpfUtil.getValue(this, SpfUtil.SOUND_STATE, true));
		swt_vib.setChecked((Boolean) SpfUtil.getValue(this, SpfUtil.VIBRATE_STATE, true));
	}

	private void addListener() {
		swt_vib.setOnCheckedChangeListener(this);
		swt_sound.setOnCheckedChangeListener(this);
		back.setOnClickListener(this);
		tb_version.setOnClickListener(this);
		tb_cache.setOnClickListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
			case R.id.switch_vibrate:
				ShockUtil.getInstance(getApplication()).setIsAllowVib(isChecked);
				SpfUtil.setValue(this, SpfUtil.VIBRATE_STATE, isChecked);
				break;
			case R.id.switch_sound:
				SoundUtil.getInstance(getApplication()).setIsAllowPlay(isChecked);
				SpfUtil.setValue(this, SpfUtil.SOUND_STATE, isChecked);
				break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.iv_back_parent:
				finish();
				break;
			case R.id.tb_version:
				dialog = DialogFactory.getLoadingDialog(this,"正在检查更新");
				dialog.show();
				new Update(this).versionUpdate(true, new Update.UpdateOverListener() {
					@Override
					public void onOver() {
						dialog.dismiss();
					}
				});
				break;
			case R.id.tb_cache:
				alert = new AlertView("清除缓存","将清除缓存和聊天记录、会话记录？",null,new String[]{"取消","确定"},null,this,AlertView.Style.Alert,this);
				alert.show();
				break;
		}
	}
	private void deleteData(String umId){
		int  i= CrmDaoHolder.getInstance().getConversationBeanDao().deleteAll(umId);
		int j = CrmDaoHolder.getInstance().getCustomMsgContentDao().deleteAll(umId);
		DataManager.clearAllCache(this);
	}

	@Override
	public void onItemClick(Object o, int position) {
		alert.dismiss();
		switch (position){
			case 0:
				return;
			case 1:
				dialog = DialogFactory.getLoadingDialog(SettingsActivity.this,"正在清除...");
				dialog.show();
				deleteData(umId);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						dialog.dismiss();
//						try {
//							cacheSize = DataManager.getTotalCacheSize(getApplicationContext());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						tx_cache.setText(cacheSize);
					}
				},1000);
				break;
		}
	}
}
