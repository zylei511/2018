package com.paic.crm.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.paic.crm.android.R;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.entity.LoginBean;
import com.paic.crm.entity.LoginDataBizSeriesInfoListBean;
import com.paic.crm.entity.LoginFieldValueBean;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.ActivityCollector;
import com.paic.crm.utils.AndroidBug5497Workaround;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.JSONStringUtil;
import com.paic.crm.utils.LoginDesUtil;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.ScrollLayout;
import com.paic.crm.widget.XEditText;
import com.paic.crmimlib.serivce.CrmImManager;
import com.pingan.core.im.client.app.PAIMApi;
import com.pingan.paimkit.module.login.bean.LoginErrorBean;
import com.pingan.paimkit.module.login.listener.ILoginManagerListener;
import com.pingan.paimkit.module.login.manager.PMLoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;

/**
 * Created by hanyh on 16/1/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,
        XEditText.DrawableRightListener,
        ILoginManagerListener {

    private ScrollLayout scrollLayout;
    private Button loginBtn;
    private XEditText userName;
    private XEditText passWord;
    private boolean isShowPassWord;
    private Drawable iconShow, iconHidden, iconPassWord;
    private Dialog mDialog;
    private LoginBean loginBean;
    /**
     * 系统识别码，必输，由云平台统一分配给各个系统
     */
    private String sourcesys = "15001";
    private String umId;
    private String pas;
    private Long preTime = 0l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        init();
        addListeners();
    }


    private void addListeners() {
        loginBtn.setOnClickListener(this);
        passWord.setClickDrawableRightListener(this);
    }

    private void init() {

        loginBtn = (Button) findViewById(R.id.login_btn);
        userName = (XEditText) findViewById(R.id.userName);
        passWord = (XEditText) findViewById(R.id.passWord);
        iconShow = ContextCompat.getDrawable(this, R.drawable.icon_show);
        iconHidden = ContextCompat.getDrawable(this, R.drawable.icon_hide);
        iconPassWord = ContextCompat.getDrawable(this, R.drawable.icon_password);
        isShowPassWord = true;
        scrollLayout = (ScrollLayout) loginBtn.getParent();
        AndroidBug5497Workaround.assistActivity(this);
    }

    @Override
    public void onClick(View v) {

        umId = userName.getText().toString();
        pas = passWord.getText().toString();
        if (umId == null || umId.trim().equals("")) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (pas == null || pas.trim().equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (mDialog == null) {
            mDialog = DialogFactory.getLoadingDialog(this, "正在登陆...");
        }
        pas = LoginDesUtil.encryptToURL(pas, LoginDesUtil.ZZGJSPWDKEY);
        mDialog.show();
        SpfUtil.setValue(LoginActivity.this,SpfUtil.KEYBORAD_HEIGHT,scrollLayout.getKeyboardHeight());
        VolleyRequest.httpLogin(this, HttpUrls.HTTP_LOGIN, "1", umId.toUpperCase(), pas, httpResultListener);
    }


    private void cloudLogin(String customId, String pas, String secretKey, long timeStamp) {
        PMLoginManager.getInstance().loginFromThird(secretKey, customId, sourcesys, pas, String.valueOf(timeStamp), this);
        CRMLog.LogInfo(Constants.LOG_TAG, "secretKey="+secretKey + "customId=" + customId + "pas=" + pas + String.valueOf(timeStamp) + this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (CommonUtils.isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onDrawableRightClick(View view) {
        if (isShowPassWord) {
            passWord.setCompoundDrawablesWithIntrinsicBounds(iconPassWord, null, iconHidden, null);
            passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passWord.setCompoundDrawablesWithIntrinsicBounds(iconPassWord, null, iconShow, null);
            passWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        isShowPassWord = !isShowPassWord;
    }

    @Override
    public void onLoginSuccess(final String userName) {
        mDialog.dismiss();
        if (BizSeriesUtil.isKZKF(this)){
            checkInKZKF(Constants.STATE_CHECK_IN_KZKF);
        }
        toActivity(this, HomeActivity.class, createBundle(loginBean));
        finish();
    }


    /**
     * 空中客服签入或签出
     * @param status
     */
    private void checkInKZKF(String status){

        String shirokey = (String) SpfUtil.getValue(this, SpfUtil.SHIROKEY, "");
        VolleyRequest.checkInKZKF(this, umId, status, shirokey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {

                JSONObject object = null;
                try {
                    object = new JSONObject(obj.toString());
                    String resultCode = object.getString("resultCode");
                    String resultMsg = object.getString("resultMsg");
                    if (resultCode.equals("01")) {
                        recordCheckInTime();
                    }

                    Toast.makeText(LoginActivity.this, resultMsg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError ve) {
                Toast.makeText(LoginActivity.this, getString(R.string.program_call_failed), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogOutside() {
            }
        });
    }

    /**
     * 记录签入时间
     */
    private void recordCheckInTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(System.currentTimeMillis());
        SpfUtil.setValue(LoginActivity.this, SpfUtil.CHECK_IN_TIME_KZKF, date);
    }

    @Override
    public void onLoginError(final LoginErrorBean loginErrorBean) {
        mDialog.dismiss();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "登陆失败,请重新登陆", Toast.LENGTH_LONG).show();
            }
        }, 1000);
        CRMLog.LogInfo(Constants.LOG_TAG, loginErrorBean.getErrorMassage());
    }

    VolleyInterface httpResultListener = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {

            handleLoginSusscess(obj);
        }

        @Override
        public void onError(VolleyError ve) {
            mDialog.dismiss();
            Toast.makeText(LoginActivity.this, "用户登陆失败,请重新登陆", Toast.LENGTH_LONG).show();
            CRMLog.LogInfo(Constants.LOG_TAG, ve.toString());
        }

        @Override
        public void onLogOutside() {
            Log.i("onLogOutside", "LoginActivity");
        }
    };

    private void handleLoginSusscess(Object obj) {

        if (!isLoginSuccess(obj)) return;
        loginBean = CommonUtils.handleHttpResult(LoginBean.class, obj.toString());
        SpfUtil.setValue(LoginActivity.this, SpfUtil.SHIROKEY, loginBean.data.shiroKey);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.UMID, loginBean.data.loginUser.umId);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.UM_NAME, loginBean.data.loginUser.name);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.UM_SEX, loginBean.data.loginUser.sex);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.UM_PHONE, loginBean.data.loginUser.phone);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.APP_STATUS, loginBean.data.loginUser.appStatus);
        SpfUtil.setValue(LoginActivity.this, SpfUtil.UM_EXTENSIONNUMBER, loginBean.data.loginUser.extensionNumber);
        try {
            JSONObject object = new JSONObject(obj.toString());
            JSONObject data = object.getJSONObject("data");
            JSONObject ucpCode = data.getJSONObject("ucpCode");
            String CHANNEL_WEIXIN=ucpCode.getString(Constants.CHANNEL_WEIXIN);
            String CHANNEL_APP_IM=ucpCode.getString(Constants.CHANNEL_APP_IM);
            String CHANNEL_SMS=ucpCode.getString(Constants.CHANNEL_SMS);
            String CHANNEL_WEB=ucpCode.getString(Constants.CHANNEL_WEB);
            String CHANNEL_WORLD=ucpCode.getString(Constants.CHANNEL_WORLD);
            SpfUtil.setValue(LoginActivity.this, SpfUtil.IM_FLAG, CHANNEL_APP_IM);
            SpfUtil.setValue(LoginActivity.this, SpfUtil.MSG_FLAG, CHANNEL_SMS);
            SpfUtil.setValue(LoginActivity.this, SpfUtil.WEIXIN_FLAG, CHANNEL_WEIXIN);
            SpfUtil.setValue(LoginActivity.this, SpfUtil.WORLD_FLAG, CHANNEL_WORLD);
            SpfUtil.setValue(LoginActivity.this, SpfUtil.WEB_FLAG, CHANNEL_WEB);

            String PAD_BIZ_SERIES = data.optString("PAD_BIZ_SERIES");
            SpfUtil.setValue(LoginActivity.this, SpfUtil.PAD_BIZ_SERIES, PAD_BIZ_SERIES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SpfUtil.setValue(LoginActivity.this, SpfUtil.MSO_FLAG, CommonUtils.getMso(LoginActivity.this));
        SpfUtil.setValue(LoginActivity.this, SpfUtil.LOGIN_FLAG, "isLogin");
        SpfUtil.getValue(LoginActivity.this, SpfUtil.SHIROKEY, "");
        CRMLog.LogInfo(Constants.LOG_TAG, "loginBean   " + loginBean.data.shiroKey);
        try {
            if (loginBean.data.bizSeriesInfoList != null && loginBean.data.bizSeriesInfoList.size() > 0) {
                for (int i = 0; i < loginBean.data.bizSeriesInfoList.size(); i++) {
                    LoginDataBizSeriesInfoListBean loginDataBizSeriesInfoListBean = loginBean.data.bizSeriesInfoList.get(i);
                    if (loginDataBizSeriesInfoListBean.fieldName.equals("tabBarData")) {
                        String fieldValue = loginBean.data.bizSeriesInfoList.get(i).fieldValue;
                        JSONArray array = new JSONArray(fieldValue);
                        for (int j = 0; j < array.length(); j++) {
                            String js = array.get(j).toString();
                            LoginFieldValueBean loginFieldValueBean =
                                    CommonUtils.handleHttpResult(LoginFieldValueBean.class, js);
                            loginFieldValueBean.umId = loginBean.data.loginUser.umId;
                            int results =
                                    CrmDaoHolder
                                            .getInstance()
                                            .getLoginFieldValueDao()
                                            .add(loginFieldValueBean);
                            CRMLog.LogInfo(Constants.LOG_TAG, "results   " + results);
                            loginBean.loginFieldValueBeans.add(loginFieldValueBean);
                            CRMLog.LogInfo(Constants.LOG_TAG, "loginFieldValueBean   " + loginFieldValueBean.iconNormal);
                        }
                    }
                    if (loginDataBizSeriesInfoListBean.fieldName.equals("sendMessageBtn")) {
                        String fieldValue = loginBean.data.bizSeriesInfoList.get(i).fieldValue;
                        SpfUtil.setValue(this, SpfUtil.BIZ_CHANNEL, fieldValue);
                    }
                }
            }
            //云平台登陆
            if (!PAIMApi.getInstance().hasAccessToken()) {
                String token = URLDecoder.decode(loginBean.data.sjyToken, "utf-8");
                CRMLog.LogInfo(Constants.LOG_TAG, "sjyToken:    " + token);
//                cloudLogin(loginBean.data.loginUser.umId, loginBean.data.loginUser.name, token, loginBean.data.sjyTokenTime);
                CrmImManager
                        .getInstance()
                        .loginSjy(token,
                                loginBean.data.loginUser.umId,
                                sourcesys,
                                loginBean.data.loginUser.name,
                                loginBean.data.sjyTokenTime+"",
                                this);
            } else {
                toActivity(LoginActivity.this, HomeActivity.class, createBundle(loginBean));
                finish();
            }
        } catch (Exception e) {
            CRMLog.LogInfo(Constants.LOG_TAG, "Exception   " + e.getMessage());
        }
    }

    private boolean isLoginSuccess(Object obj) {
        if (obj != null) {
            try {
                JSONObject object = new JSONObject(obj.toString());
                if (!object.getString("resultCode").equals("200")) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this, object.getString("resultMsg"), Toast.LENGTH_LONG).show();
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return true;
    }

    private Bundle createBundle(LoginBean loginBean) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("loginBean", loginBean);
        return bundle;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - preTime > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_LONG).show();
                preTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
