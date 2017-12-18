package test;

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
import com.paic.crm.ui.BaseActivity;
import com.paic.crm.utils.ActivityCollector;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.LoginDesUtil;
import com.paic.crm.utils.SpfUtil;
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

import test.p.Presenter;
import test.v.ILoginView;

/**
 * Created by hanyh on 16/1/21.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,
        XEditText.DrawableRightListener,
        ILoginView{

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

    private Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);
        init();
        addListeners();
        loginPresenter = new Presenter(this);
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
    }

    @Override
    public void onClick(View v) {

        umId = userName.getText().toString();
        pas = passWord.getText().toString();

        if (mDialog == null) {
            mDialog = DialogFactory.getLoadingDialog(this, "正在登陆...");
        }
        mDialog.show();
        loginPresenter.login(this,umId,pas);
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
    public void onLoginSuccess(Object successObj) {
        mDialog.dismiss();
        toActivity(this, HomeActivity.class, createBundle(loginBean));
        finish();
    }

    @Override
    public void onLoginError(final Object errorObj) {
        mDialog.dismiss();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "登陆失败,请重新登陆", Toast.LENGTH_LONG).show();
            }
        }, 1000);
        CRMLog.LogInfo(Constants.LOG_TAG, ((LoginErrorBean)errorObj).getErrorMassage());
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
