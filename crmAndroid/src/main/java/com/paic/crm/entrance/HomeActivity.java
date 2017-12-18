package com.paic.crm.entrance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.paic.crm.PADsdk.Location;
import com.paic.crm.android.R;
import com.paic.crm.app.AppContext;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.entity.LoginBean;
import com.paic.crm.entity.LoginFieldValueBean;
import com.paic.crm.fragment.ContactsFragment;
import com.paic.crm.module.Update;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.ui.BaseActivity;
import com.paic.crm.fragment.ConversationFragment;
import com.paic.crm.fragment.MeFragment;
import com.paic.crm.fragment.WeixunFragment;
import com.paic.crm.fragment.WorkbenchFragment;
import com.paic.crm.ui.ConversationHistoryActivity;
import com.paic.crm.ui.SplashActivity;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.AlertView;
import com.paic.crm.widget.OnItemClickListener;
import com.paic.crm.widget.TabFragmentHost;
import com.pingan.core.data.DataCollectorConfig;
import com.pingan.core.data.PADataAgent;
import com.pingan.core.data.log.AppLog;
import com.pingan.paimkit.module.login.listener.OnKickListener;
import com.pingan.paimkit.module.login.manager.PMLoginManager;
import com.tendcloud.tenddata.TCAgent;


import java.util.List;


public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, TabHost.OnTabChangeListener, View.OnClickListener,OnItemClickListener,Runnable {

    private Class<?>[] ClassTab = {ConversationFragment.class, ContactsFragment.class, WorkbenchFragment.class, WeixunFragment.class, MeFragment.class};
    private TabFragmentHost mTabHost;
    private RadioGroup tabs;
    private RadioButton rbConversion;
    private RadioButton rbWorkbench;
    private RadioButton rbWeixun;
    private RadioButton rbMe;
    private TextView actionTitle;
    private RadioButton rbContant;
    private ImageView iv_serch_btn;
    private LinearLayout historyBtn;
    private LoginBean loginBean;
    private List<LoginFieldValueBean> loginFieldValueBeans;
    private AlertView alertView;
    private Handler mHandler;
    ProgressDialog pd;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addClickData();
        mHandler=new Handler();
        Bundle bundle = getIntent().getExtras();
        handleBundle(bundle);
        init();
        addListeners();
        initPAD();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int tab = mTabHost.getCurrentTab();
//        new Update(this).versionUpdate(false,null);
        setActionTitle(tab);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int tab = mTabHost.getCurrentTab();
        onPauseFragment(tab);
    }

    @Override
    protected void onStart() {
        mLocation.GPSStart();
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    @Override
    protected void onStop() {
        mLocation.GPSStop();
        super.onStop();
    }

    private void addClickData(){
        String umId = (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
        String shiroKey = SpfUtil.getValue(this, SpfUtil.SHIROKEY, "").toString();
        if(shiroKey!=null&&!shiroKey.equals("")) {
            VolleyRequest.totalData(this, HttpUrls.CLICK_DATA, umId, shiroKey, totalDataInterface);
        }
    }

    private void handleBundle(Bundle bundle) {
        if (bundle != null) {
            loginBean = (LoginBean) bundle.getSerializable("loginBean");
            loginFieldValueBeans = loginBean.loginFieldValueBeans;
        } else {
            String umId= (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
            loginFieldValueBeans = CrmDaoHolder.getInstance().getLoginFieldValueDao().queryAll(umId);
            if (loginFieldValueBeans==null||loginFieldValueBeans.size()==0){
                CommonUtils.clearData(this);
                Intent intent = new Intent();
                intent.setClass(this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return;
            }
        }
        PMLoginManager.getInstance().setKickListener(new OnKickListener() {
            @Override
            public void onLogoutResult(boolean b, String s) {
                CRMLog.LogInfo(Constants.LOG_TAG, "onLogoutResult");
                mHandler.postDelayed(HomeActivity.this, 10);
            }
        });
    }


    private void addListeners() {

        tabs.setOnCheckedChangeListener(this);
        mTabHost.setOnTabChangedListener(this);
        historyBtn.setOnClickListener(this);
    }

    private void init() {
        mTabHost = (TabFragmentHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);
        InitTabView();
        tabs = (RadioGroup) findViewById(R.id.tabs);
        rbConversion = (RadioButton) findViewById(R.id.rb_conversion);
        rbWorkbench = (RadioButton) findViewById(R.id.rb_work_bench);
        rbContant = (RadioButton) findViewById(R.id.rb_contact);
        rbWeixun = (RadioButton) findViewById(R.id.rb_weixun);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        historyBtn = (LinearLayout) findViewById(R.id.iv_serch_parent);
        actionTitle = (TextView) findViewById(R.id.action_title);
        iv_serch_btn = (ImageView) findViewById(R.id.iv_serch_btn);
        handleBusinessLines();
    }

    private void handleBusinessLines() {
        if (loginFieldValueBeans != null) {
            for (int i = 0; i < loginFieldValueBeans.size(); i++) {
                LoginFieldValueBean loginFieldValueBean = loginFieldValueBeans.get(i);
                if (loginFieldValueBean.tabButtonName==null){
                    CommonUtils.clearData(this);
                    Intent intent = new Intent();
                    intent.setClass(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }
                if (loginFieldValueBean.typeMark.equals("Dialogue")) {

                } else if (loginFieldValueBean.typeMark.equals("Contacts")) {
                    rbContant.setVisibility(View.VISIBLE);
                    rbContant.setText(loginFieldValueBean.tabButtonName);
                } else if (loginFieldValueBean.typeMark.equals("Workbench")) {
                    rbWorkbench.setVisibility(View.VISIBLE);
                    rbWorkbench.setText(loginFieldValueBean.tabButtonName);
                } else if (loginFieldValueBean.typeMark.equals("News")) {
                    rbWeixun.setVisibility(View.VISIBLE);
                    rbWeixun.setText(loginFieldValueBean.tabButtonName);
                } else if (loginFieldValueBean.typeMark.equals("Mine")) {
                    rbMe.setVisibility(View.VISIBLE);
                    rbMe.setText(loginFieldValueBean.tabButtonName);
                }
            }
            rbMe.setVisibility(View.VISIBLE);
        }
    }

    private void setActionTitle(int tab) {
        switch (tab) {

            case 0:
                onFragment(tab);
                actionTitle.setText("会话");
                iv_serch_btn.setImageResource(R.drawable.icon_msg_history);
                historyBtn.setClickable(true);

                if (BizSeriesUtil.isKZKF(this)){
                    historyBtn.setVisibility(View.GONE);
                }
                break;


            case 1:
                onFragment(tab);
                actionTitle.setText("联系人");
                iv_serch_btn.setImageResource(0);
                historyBtn.setClickable(false);
                break;

            case 2:
                onFragment(tab);
                actionTitle.setText("工作台");
                iv_serch_btn.setImageResource(0);
                historyBtn.setClickable(false);
                break;
            case 3:
                onFragment(tab);
                actionTitle.setText("微讯");
                iv_serch_btn.setImageResource(0);
                iv_serch_btn.setClickable(false);
                break;

            case 4:
                onFragment(tab);
                actionTitle.setText("我");
                iv_serch_btn.setImageResource(0);
                historyBtn.setClickable(false);
                break;
        }
    }

    private void InitTabView() {

        Bundle b = new Bundle();

        for (int i = 0; i < ClassTab.length; i++) {

            mTabHost.addTab(mTabHost.newTabSpec(i + "").setIndicator(i + ""),
                    ClassTab[i], b);

        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        clearTextColor();
        clearDrawable();
        switch (checkedId) {

            case R.id.rb_conversion:
                mTabHost.setCurrentTab(0);
                rbConversion.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.icon_talk_seleted), null,
                        null);
                rbConversion.setTextColor(Color.parseColor("#48D1CC"));


                break;

            case R.id.rb_contact:
                mTabHost.setCurrentTab(1);
                rbContant.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.icon_contact_list_pressed), null,
                        null);
                rbContant.setTextColor(Color.parseColor("#48D1CC"));
                break;

            case R.id.rb_work_bench:
                mTabHost.setCurrentTab(2);
                rbWorkbench.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.icon_platform_seleted), null,
                        null);
                rbWorkbench.setTextColor(Color.parseColor("#48D1CC"));
                break;

            case R.id.rb_weixun:
                mTabHost.setCurrentTab(3);
                rbWeixun.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.icon_weixun_selete), null,
                        null);
                rbWeixun.setTextColor(Color.parseColor("#48D1CC"));
                break;

            case R.id.rb_me:
                mTabHost.setCurrentTab(4);
                rbMe.setCompoundDrawablesWithIntrinsicBounds(null,
                        getResources().getDrawable(R.drawable.icon_mine_selected), null,
                        null);
                rbMe.setTextColor(Color.parseColor("#48D1CC"));
                break;

        }


    }


    private void clearTextColor() {

        rbConversion.setTextColor(Color.parseColor("#999999"));
        rbWorkbench.setTextColor(Color.parseColor("#999999"));
        rbContant.setTextColor(Color.parseColor("#999999"));
        rbWeixun.setTextColor(Color.parseColor("#999999"));
        rbMe.setTextColor(Color.parseColor("#999999"));
    }


    private void clearDrawable() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rbConversion.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_talk_unselete, null), null,
                    null);
            rbWorkbench.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_platform_unselete, null), null,
                    null);
            rbContant.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_contact_list_unpressed, null), null,
                    null);
            rbWeixun.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_weixun_unselete, null), null,
                    null);
            rbMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_mine_unselete, null), null,
                    null);
        } else {
            rbConversion.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_talk_unselete), null,
                    null);
            rbWorkbench.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_platform_unselete), null,
                    null);
            rbContant.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_contact_list_unpressed), null,
                    null);
            rbWeixun.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_weixun_unselete), null,
                    null);
            rbMe.setCompoundDrawablesWithIntrinsicBounds(null,
                    getResources().getDrawable(R.drawable.icon_mine_unselete), null,
                    null);
        }

    }


    @Override
    public void onTabChanged(String tabId) {
        int tab = mTabHost.getCurrentTab();
        setActionTitle(tab);
    }

    @Override
    public void onClick(View v) {
        toActivity(this, ConversationHistoryActivity.class, null);
    }


    private void onFragment(int tab) {

        Fragment fm = getSupportFragmentManager().findFragmentByTag(tab + "");
        if (fm instanceof ConversationFragment) {
            fm.onResume();
        } else if (fm instanceof ContactsFragment) {
            fm.onResume();
        } else if (fm instanceof WorkbenchFragment) {
            fm.onResume();
        } else if (fm instanceof WeixunFragment) {
            fm.onResume();
        } else if (fm instanceof MeFragment) {
            fm.onResume();
        }
    }


    private void onPauseFragment(int tab) {

        Fragment fm = getSupportFragmentManager().findFragmentByTag(tab + "");
        if (fm instanceof ConversationFragment) {
            fm.onPause();
        } else if (fm instanceof ContactsFragment) {
            fm.onPause();
        } else if (fm instanceof WorkbenchFragment) {
            fm.onPause();
        } else if (fm instanceof WeixunFragment) {
            fm.onPause();
        } else if (fm instanceof MeFragment) {
            fm.onPause();
        }
    }

    VolleyInterface totalDataInterface=new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            CRMLog.LogInfo(Constants.LOG_TAG,"total data");
        }

        @Override
        public void onError(VolleyError ve) {
            CRMLog.LogInfo(Constants.LOG_TAG,"total data error");
        }

        @Override
        public void onLogOutside() {
            CRMLog.LogInfo("onLogOutside", "homeActivity");
            CommonUtils.exitWhenHasLogIn(HomeActivity.this);
        }
    };

    @Override
    public void onItemClick(Object o, int position) {

        if (alertView.isShowing()){
            CommonUtils.clearData(this);
            alertView.dismiss();
        }
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        CRMLog.LogInfo(Constants.LOG_TAG, "kicked !!!!!  " + "is kicked !!!!!");
    }


    @Override
    public void run() {
        CRMLog.LogInfo(Constants.LOG_TAG, "run");
        alertView=new AlertView("账号异常", "您的账号在别处登陆，请重新登陆!", null, new String[]{"确定"}, null, HomeActivity.this, AlertView.Style.Alert, HomeActivity.this).setCancelable(false);
        alertView.show();
    }

    private void initPAD(){
        DataCollectorConfig.mBatteryCollectFrequency = 5000;
        DataCollectorConfig.mBatteryCountEachRecord = 3;
        DataCollectorConfig.mDBUpNum = 10;
        DataCollectorConfig.mGPSCountEachRecord = 3;
        TCAgent.init(this);

        //初始化在 TCAgent.init(this); 之后
        PADataAgent.init(getApplicationContext());

        //TCAgent.setReportUncaughtExceptions(true);
        //安全级别日志，true:则不输出和保存任何日志，false:可选择输出或保存日志,默认为false
        AppLog.IS_SECURITY_LOG=true;
        //true:在控制台输出；false:不在控制台输出,默认为false
        AppLog.IS_DEBUG=false;
        //GPS初始化
        mLocation = new Location(getApplicationContext());
        TCAgent.setReportUncaughtExceptions(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
