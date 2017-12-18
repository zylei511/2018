package com.paic.crm.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.CheckStateAdapter;
import com.paic.crm.android.R;
import com.paic.crm.entity.KZKFCheckStateBean;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.ui.ChatActivity;
import com.paic.crm.ui.LoginActivity;
import com.paic.crm.ui.SettingsActivity;
import com.paic.crm.ui.SplashActivity;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.DigestUtil;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.widget.ListviewForScrollView;
import com.paic.crm.widget.XEditText;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.CircleImageDrawable;
import com.pingan.paimkit.module.login.manager.PMLoginManager;
import com.tendcloud.tenddata.TCAgent;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST;

/**
 * Created by hanyh on 16/1/21.
 */
public class MeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText feedbackEdit, phone_codes_edit;
    private Button btn_exit, check_in, check_out, check_cancel, feedBack_sub, getCodes_btn, phone_modify_sub;
    private Dialog dialog, mDialog, checkDialog;
    private RelativeLayout tv_feed_back, tv_modify_phone,tv_settings;
    private PopupWindow popupWindow, phone_status_pop;
    private View feedBackLayout, phone_status_pop_layout, phone_modify_layout;
    private LinearLayout me_root_layout, feedback_back_parent, phone_modify_back_parent;
    private ImageView feedback_back, phone_modify_back;
    private TextView feedback_title, um_id, um_name, um_phone, um_sex, phone_modify_title;
    private ImageView myView;
    private String umId;
    private String shiroKey;
    private String umName;
    private String phoneNum;
    private String sex;
    private int statusBarHeight;
    private XEditText phone_modify_edit;
    private CountDownTimer timer;
    private Context appContext;
    private static final String STATUS_ONLINE="Y";
    private static final String STATUS_OFFLINE="N";
    private ListviewForScrollView checkStateListView;

    private String state;
    private String phoneState;
    private CheckStateAdapter checkStateAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_layout, null);
        initData();
        initViews(view, inflater);
        addListeners();
        return view;
    }

    @Override
    public void onResume() {
        if (this.isVisible()) {
            TCAgent.onPageStart(getActivity(), PATDEnum.ME.getName());
        }
        super.onResume();

        if (BizSeriesUtil.isKZKF(getActivity())){
            tv_modify_phone.setVisibility(View.GONE);
        }

        checkStateAdapter = new CheckStateAdapter(getActivity(),getCheckStateData(),R.layout.check_state_list_item);
        checkStateListView.setAdapter(checkStateAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if(isVisible()) {
            TCAgent.onPageEnd(getActivity(), PATDEnum.ME.getName());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        appContext = getActivity().getApplicationContext();
        umId = (String) SpfUtil.getValue(appContext, SpfUtil.UMID, "");
        shiroKey = SpfUtil.getValue(appContext, SpfUtil.SHIROKEY, "").toString();
        umName = (String) SpfUtil.getValue(appContext, SpfUtil.UM_NAME, "");
        phoneNum = (String) SpfUtil.getValue(appContext, SpfUtil.UM_EXTENSIONNUMBER, "");
        sex = (String) SpfUtil.getValue(appContext, SpfUtil.UM_SEX, "");
        if ("F".equals(sex)) {
            sex = "女";
        } else if ("M".equals(sex)) {
            sex = "男";
        }
        statusBarHeight = CommonUtils.getStatusHeigth(getActivity());
        CRMLog.LogInfo("statusBar", statusBarHeight + "");
    }

    private void initViews(View view, LayoutInflater inflater) {

//        online_status = (TextView) view.findViewById(R.id.tv_online_status);
//        String appStatus = (String) SpfUtil.getValue(getActivity(),SpfUtil.APP_STATUS,"");
//        if(STATUS_ONLINE.equals(appStatus)){
//            online_status.setText(R.string.online);
//        }else{
//            online_status.setText(R.string.offline);
//        }

        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        tv_feed_back = (RelativeLayout) view.findViewById(R.id.tv_feed_back);
        tv_modify_phone = (RelativeLayout) view.findViewById(R.id.me_phone);

        me_root_layout = (LinearLayout) view.findViewById(R.id.me_root_layout);
        feedBackLayout = inflater.inflate(R.layout.feedback_layout, null);
        feedback_back = (ImageView) feedBackLayout.findViewById(R.id.iv_back);
        feedback_title = (TextView) feedBackLayout.findViewById(R.id.action_title);
        feedback_title.setText("意见反馈");
        feedback_back_parent = (LinearLayout) feedBackLayout.findViewById(R.id.iv_back_parent);

//        tv_online_status = (RelativeLayout) view.findViewById(R.id.online_status);

        checkStateListView = (ListviewForScrollView) view.findViewById(R.id.check_state_list);
        checkStateListView.setDivider(null);

        feedback_back.setImageResource(R.drawable.icon_btn_back);
        feedBack_sub = (Button) feedBackLayout.findViewById(R.id.feedback_sub);
        feedbackEdit = (EditText) feedBackLayout.findViewById(R.id.et_feedback);

        phone_status_pop_layout = inflater.inflate(R.layout.phone_pop_layout, null);
        check_in = (Button) phone_status_pop_layout.findViewById(R.id.check_in);
        check_out = (Button) phone_status_pop_layout.findViewById(R.id.check_out);
        check_cancel = (Button) phone_status_pop_layout.findViewById(R.id.check_cancel);

        phone_modify_layout = inflater.inflate(R.layout.phone_modify_layout, null);
        phone_modify_title = (TextView) phone_modify_layout.findViewById(R.id.action_title);
        phone_modify_title.setText("修改手机号");
        phone_modify_back = (ImageView) phone_modify_layout.findViewById(R.id.iv_back);
        phone_modify_back.setImageResource(R.drawable.icon_btn_back);
        phone_modify_edit = (XEditText) phone_modify_layout.findViewById(R.id.modify_phone);
        phone_codes_edit = (EditText) phone_modify_layout.findViewById(R.id.codes);
        getCodes_btn = (Button) phone_modify_layout.findViewById(R.id.get_codes);
        phone_modify_sub = (Button) phone_modify_layout.findViewById(R.id.modify_sub_btn);
        phone_modify_back_parent = (LinearLayout) phone_modify_layout.findViewById(R.id.iv_back_parent);

        myView = (ImageView) view.findViewById(R.id.iv_me);
        myView.setImageDrawable(new CircleImageDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.kf)));

        um_id = (TextView) view.findViewById(R.id.um_id);
        um_id.setText(umId);
        um_name = (TextView) view.findViewById(R.id.um_name);
        um_name.setText(umName);
        um_sex = (TextView) view.findViewById(R.id.um_sex);
        um_sex.setText(sex);
        um_phone = (TextView) view.findViewById(R.id.um_phone);
        um_phone.setText(phoneNum);

        phone_status_pop = new PopupWindow();
        phone_status_pop.setAnimationStyle(R.style.popUpWindowAnim);
        phone_status_pop.setSoftInputMode(SOFT_INPUT_MASK_ADJUST);


        tv_settings = (RelativeLayout) view.findViewById(R.id.tv_settings);
    }

    private List<KZKFCheckStateBean> getCheckStateData() {
        List<KZKFCheckStateBean> list = new ArrayList<>();
        if (BizSeriesUtil.isKZKF(getActivity())) {
            KZKFCheckStateBean checkStateBean = new KZKFCheckStateBean();
            checkStateBean.tv_title = getString(R.string.check_state_kzkf);
            checkStateBean.tv_content = hasCheckedIn() ? getString(R.string.has_checked_in_kzkf):getString(R.string.has_checked_out_kzkf);
            list.add(checkStateBean);
        } else {

            KZKFCheckStateBean stateBean = new KZKFCheckStateBean();
            stateBean.tv_title = getString(R.string.txt_status);
            String appStatus = (String) SpfUtil.getValue(getActivity(), SpfUtil.APP_STATUS, "");
            if (STATUS_ONLINE.equals(appStatus)) {
                state = getString(R.string.online);

            } else {
                state = getString(R.string.offline);
            }
            stateBean.tv_content = state;
            list.add(stateBean);

            KZKFCheckStateBean checkInBean = new KZKFCheckStateBean();
            checkInBean.tv_title = getString(R.string.phone_status);
            checkInBean.tv_content = getString(R.string.check_status);
            list.add(checkInBean);
        }

        return list;
    }

    /**
     * 判断是否是签入状态
     * @return
     */
    private boolean hasCheckedIn(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String currentDate=sdf.format(System.currentTimeMillis());
        String loginDate = (String) SpfUtil.getValue(getActivity(),SpfUtil.CHECK_IN_TIME_KZKF,"");
        return currentDate.equals(loginDate);
    }

    private void addListeners() {

        tv_feed_back.setOnClickListener(this);
        tv_modify_phone.setOnClickListener(this);
        feedback_back_parent.setOnClickListener(this);
        phone_modify_back_parent.setOnClickListener(this);
        checkStateListView.setOnItemClickListener(this);

        tv_settings.setOnClickListener(this);

        feedBack_sub.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        check_in.setOnClickListener(this);
        check_out.setOnClickListener(this);
        check_cancel.setOnClickListener(this);

        phone_modify_edit.setClickDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                if (phone_modify_edit.getInputType() == InputType.TYPE_CLASS_NUMBER){
                    phone_modify_edit.setText("");
                }
            }
        });
        phone_modify_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    phone_modify_edit.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.icon_delete_02), null);
                    if (s.length() > 10) {
                        getCodes_btn.setBackgroundColor(Color.parseColor("#1AB8AA"));
                        getCodes_btn.setOnClickListener(MeFragment.this);
                    } else {
                        getCodes_btn.setBackgroundColor(Color.parseColor("#D1D9EA"));
                        getCodes_btn.setClickable(false);
                    }
                } else {
                    phone_modify_edit.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.icon_delete_01), null);
                    getCodes_btn.setBackgroundColor(Color.parseColor("#D1D9EA"));
                    getCodes_btn.setClickable(false);
                }
            }
        });
        phone_modify_sub.setOnClickListener(this);
    }

    private void clearData() {
        SpfUtil.remove(appContext, SpfUtil.UMID);
        SpfUtil.remove(appContext, SpfUtil.SHIROKEY);
        SpfUtil.remove(appContext, SpfUtil.UM_NAME);
        SpfUtil.remove(appContext, SpfUtil.UM_PHONE);
        SpfUtil.remove(appContext, SpfUtil.UM_SEX);
        SpfUtil.remove(appContext, SpfUtil.LOGIN_FLAG);
        Intent intent = new Intent();
        intent.setClass(getActivity(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_exit:
                //先判断是否是空中客服，如果是空中客服，则先签出，再退出
                if (BizSeriesUtil.isKZKF(getActivity())){
                    checkInOrOutKZKF(Constants.STATE_CHECK_OUT_KZKF);
                    //如果不是空中客服，则直接退出
                } else {
                    logOut(umId);
                }
                break;

            case R.id.tv_feed_back:
                showPopUp(feedBackLayout);
                break;

            case R.id.iv_back_parent:
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(popupWindow.getContentView().getWindowToken(), 0);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                break;

            case R.id.feedback_sub:
                submitFeedBack(umId, feedbackEdit.getText().toString());
                feedbackEdit.setHint("请输入您的意见或反馈");
                break;

            case R.id.me_phone:
                TCAgent.onEvent(getActivity(),PATDEnum.PHONE_MODIFY.getName(),PATDEnum.PHONE_MODIFY.getName());
                showPopUp(phone_modify_layout);
                break;

            case R.id.check_in:
                if(Source.PHONE_STATUS.equals(check_in.getTag())){
                     checkIn(umId);
                }else if(Source.ONLINE_STATUS.equals(check_in.getTag())){
                    CRMLog.LogInfo("online","online");
                    changeOnlineStatus(umId,STATUS_ONLINE);
                }
                phone_status_pop.dismiss();
                break;

            case R.id.check_out:
                if(Source.PHONE_STATUS.equals(check_out.getTag())){
                    checkOut(umId);
                }else if(Source.ONLINE_STATUS.equals(check_out.getTag())){
                    CRMLog.LogInfo("offline","offline");
                    changeOnlineStatus(umId,STATUS_OFFLINE);
                }
                phone_status_pop.dismiss();
                break;

            case R.id.check_cancel:
                if (phone_status_pop.isShowing()) {
                    phone_status_pop.dismiss();
                }
                break;

//            case R.id.phone_status:
//                TCAgent.onEvent(getActivity(),PATDEnum.PHONE_STATUS.getName(),PATDEnum.PHONE_STATUS.getName());
//                showPhonePop(Source.PHONE_STATUS);
//                break;

            case R.id.get_codes:
                getCodes(umId, phone_modify_edit.getText().toString().trim());
                break;

            case R.id.modify_sub_btn:
                String modifyPhoneNum = (String) SpfUtil.getValue(appContext, SpfUtil.UM_EXTENSIONNUMBER, "");
                CRMLog.LogInfo("modify", modifyPhoneNum);
                phoneModifySub(umId, modifyPhoneNum, phone_codes_edit.getText().toString().trim());
                break;

//            case R.id.online_status:
//                showPhonePop(Source.ONLINE_STATUS);
//                break;
            case R.id.tv_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    private void showPopUp(View contentView) {
        popupWindow = new PopupWindow(contentView, ((HomeActivity) getActivity()).screenWidth, ((HomeActivity) getActivity()).screenHeigh - statusBarHeight);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popUpWindowAnim);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(me_root_layout, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (timer != null) {
                    timer.cancel();
                }
                phone_modify_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                getCodes_btn.setBackgroundColor(Color.parseColor("#D1D9EA"));
                getCodes_btn.setText("获取验证码");
                getCodes_btn.setClickable(false);
                if (!"".equals(phone_modify_edit.getText().toString()) || !"".equals(phone_codes_edit.getText().toString())) {
                    CRMLog.LogInfo("DISMISS", "TRUE");
                    phone_modify_edit.setText("");
                    phone_codes_edit.setText("");
                }
            }
        });
    }

    private void showPhonePop(Source source) {

        switch (source){
            case ONLINE_STATUS:
                check_in.setText(R.string.online);
                check_out.setText(R.string.offline);
                break;
            case PHONE_STATUS:
                check_in.setText(R.string.check_in);
                check_out.setText(R.string.check_out);
                break;
            default:
                break;
        }
        check_in.setTag(source);
        check_out.setTag(source);


        CommonUtils.setViewBackGroundAlpha(getView(),0.5f);
        phone_status_pop.setContentView(phone_status_pop_layout);
        phone_status_pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        phone_status_pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        phone_status_pop.setFocusable(true);
        phone_status_pop.setOutsideTouchable(true);
        phone_status_pop.setBackgroundDrawable(new BitmapDrawable());
        phone_status_pop.showAtLocation(me_root_layout, Gravity.BOTTOM, 0, 0);
        phone_status_pop.update();
        phone_status_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setViewBackGroundAlpha(getView(), 1f);
            }
        });
    }


    private void logOut(String umId) {
        if (mDialog == null) {
            mDialog = DialogFactory.getLoadingDialog(getActivity(), "正在退出...");
        }
        mDialog.show();
        PMLoginManager.getInstance().loginOut();
        String shiroToken = DigestUtil.digest(shiroKey, new HashMap<String, Object>());
        VolleyRequest.logOut(getActivity(), HttpUrls.HTTP_LOGOUT, umId, shiroToken, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    JSONObject logOutObj = new JSONObject(obj.toString());
                    CRMLog.LogInfo("logOut", "logout success!" + obj.toString());
                    mDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    CommonUtils.clearData(getActivity());
                }
            }

            @Override
            public void onError(VolleyError ve) {
                //TODO 对于登出失败的处理还需改进
                CRMLog.LogInfo("sssss", "sggf");
                mDialog.dismiss();
                CommonUtils.clearData(getActivity());
            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFragmet_logout");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });
    }

    private void submitFeedBack(String umId, String content) {
        if ("".equals(content.trim())) {
            Toast.makeText(appContext, "请输入您的意见！", Toast.LENGTH_LONG).show();
            return;
        }
        if (dialog == null) {
            dialog = DialogFactory.getLoadingDialog(getActivity(), "正在提交...");
        }
        dialog.show();
        VolleyRequest.submitFeedBack(getActivity(), HttpUrls.HTTP_FEED_BACK, umId, content, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                feedbackEdit.setText("");
                dialog.dismiss();
                Toast.makeText(appContext, "提交成功！", Toast.LENGTH_LONG).show();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onError(VolleyError ve) {
                feedbackEdit.setText("");
                dialog.dismiss();
                Toast.makeText(appContext, "网络异常，请稍后重试！", Toast.LENGTH_LONG).show();
                ve.printStackTrace();
            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFrgement_sub");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });


    }

    private void getCodes(String umId, String newPhoneNum) {
        if (newPhoneNum.length() < 11) {
            Toast.makeText(getActivity(), "手机号不小于11位！", Toast.LENGTH_LONG).show();
            return;
        }
        phone_modify_edit.setInputType(InputType.TYPE_NULL);
        getCodes_btn.setClickable(false);
        getCodes_btn.setBackgroundColor(Color.parseColor("#D1D9EA"));
        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCodes_btn.setText(millisUntilFinished / 1000 + "s后重试");
            }

            @Override
            public void onFinish() {
                getCodes_btn.setClickable(true);
                getCodes_btn.setBackgroundColor(Color.parseColor("#1AB8AA"));
                getCodes_btn.setText("获取验证码");
                phone_modify_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        };
        timer.start();
        final String phoneNum = newPhoneNum;
        VolleyRequest.httpGetCodes(getActivity(), HttpUrls.HTTP_GET_CODES, umId, newPhoneNum, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                CRMLog.LogInfo("codes", obj.toString());
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    String resultCode = jsonObject.getString("resultCode");
                    if ("200".equals(resultCode)) {
                        SpfUtil.setValue(appContext, SpfUtil.UM_EXTENSIONNUMBER, phoneNum);
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(VolleyError ve) {

            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFragmet_getcodes");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });
    }

    private void phoneModifySub(String umId, final String phone, String codes) {
        if (codes.length() == 0) {
            Toast.makeText(getActivity(), "请输入手机号或验证码！", Toast.LENGTH_LONG).show();
            return;
        }
        dialog = DialogFactory.getLoadingDialog(getActivity(), "正在提交...");
        dialog.show();
        final String newPhoneNum = phone;
        VolleyRequest.httpPhoneMotify(getActivity(), HttpUrls.HTTP_PHONE_MODIFY, umId, phone, codes, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    CRMLog.LogInfo("modify", obj.toString());
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    if ("200".equals(jsonObject.getString("resultCode"))) {
                        //更新UI显示
                        um_phone.setText(newPhoneNum);
                        phone_modify_edit.setText("");
                        phone_codes_edit.setText("");
                        Toast.makeText(appContext, "修改成功！", Toast.LENGTH_LONG).show();
                        if (timer != null) {
                            timer.cancel();
                            timer.onFinish();
                        }
                        if (popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    } else {
                        Toast.makeText(appContext, "验证码错误，请重发！", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError ve) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(appContext, "验证失败，请重发！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFragment_modifynum");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });

    }

    private void checkIn(String umId) {
        checkDialog = DialogFactory.getLoadingDialog(getActivity(), getString(R.string.status_checking_in));
        checkDialog.show();
        checkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        VolleyRequest.httpCheckIn(getActivity(), HttpUrls.HTTP_CHECKIN, umId, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                checkDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    String resultMsg = jsonObject.getString("resultMsg");
                    Toast.makeText(appContext, resultMsg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(VolleyError ve) {
                checkDialog.dismiss();
                Toast.makeText(appContext, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFragment_checkIn");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });
    }

    private void checkOut(String umId) {
        checkDialog = DialogFactory.getLoadingDialog(getActivity(), "正在签出..");
        checkDialog.show();
        checkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        VolleyRequest.httpCheckOut(getActivity(), HttpUrls.HTTP_CHECKOUT, umId, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                checkDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(obj.toString());
                    String resultMsg = jsonObject.getString("resultMsg");
                    Toast.makeText(appContext, resultMsg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }
            }

            @Override
            public void onError(VolleyError ve) {
                checkDialog.dismiss();
                Toast.makeText(appContext, "网络异常，请检查网络！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogOutside() {
                CRMLog.LogInfo("onLogOutside", "meFragment_checkOut");
                dismissALl();
                CommonUtils.exitWhenHasLogIn(getActivity());
                dismissALl();
            }
        });
    }

    private void changeOnlineStatus(String umId, final String status){
        checkDialog = DialogFactory.getLoadingDialog(getActivity(), "正在切换状态...");
        checkDialog.show();
        checkDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
        VolleyRequest.changeOnlineStatus(getActivity(), HttpUrls.CHANG_ONLINE_STATUS, umId, status, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                CRMLog.LogInfo("change", obj.toString());
                try {
                    JSONObject changeResult = new JSONObject(obj.toString());
                    String resultCode = changeResult.getString("resultCode");
                    if("200".equals(resultCode)){
                        switch (status){
                            case STATUS_ONLINE:
                                state = getString(R.string.online);
                                SpfUtil.setValue(getActivity(),SpfUtil.APP_STATUS,STATUS_ONLINE);
                                break;
                            case STATUS_OFFLINE:
                                state = getString(R.string.offline);
                                SpfUtil.setValue(getActivity(), SpfUtil.APP_STATUS, STATUS_OFFLINE);
                                break;
                            default:
                                break;
                        }
                        Toast.makeText(getActivity(),"状态修改成功！",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Toast.makeText(getActivity(),"后台数据异常！",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    checkDialog.dismiss();
                }
            }

            @Override
            public void onError(VolleyError ve) {
                checkDialog.dismiss();
                CRMLog.LogInfo("change error", ve.toString());
                Toast.makeText(getActivity(),"网络异常，请稍后重试！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogOutside() {
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });
    }

    private void dismissALl() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (checkDialog != null && checkDialog.isShowing()) {
            checkDialog.dismiss();
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        if (phone_status_pop != null && phone_status_pop.isShowing()) {
            phone_status_pop.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                if (BizSeriesUtil.isKZKF(getActivity())){
                    checkDialog = DialogFactory.getLoadingDialog(getActivity(),getString(R.string.status_checking_in));
                    checkDialog.show();
                    checkInOrOutKZKF(Constants.STATE_CHECK_IN_KZKF);

                } else {
                    showPhonePop(Source.ONLINE_STATUS);
                }
                break;
            case 1:
                TCAgent.onEvent(getActivity(),PATDEnum.PHONE_STATUS.getName(),PATDEnum.PHONE_STATUS.getName());
                showPhonePop(Source.PHONE_STATUS);
                break;
            default:
                break;
        }

    }

    /**
     * 空中客服签入或签出
     * @param status
     */
    private void checkInOrOutKZKF(final String status){

        VolleyRequest.checkInKZKF(getActivity(), umId, status, shiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                if (checkDialog != null && checkDialog.isShowing()){
                    checkDialog.dismiss();
                }

                JSONObject object = null;
                try {
                    object = new JSONObject(obj.toString());
                    String resultCode = object.optString("resultCode");
                    String resultMsg = object.getString("resultMsg");

                    //如果是签出，直接设置默认值
                    if (Constants.STATE_CHECK_OUT_KZKF.equals(status)){
                        SpfUtil.setValue(getActivity(), SpfUtil.CHECK_IN_TIME_KZKF, String.valueOf(-1));
                        logOut(umId);
                        //如果是签到，则必须判断是否成功
                    }else if (Constants.STATE_CHECK_IN_KZKF.equals(status)){
                        if ("01".equals(resultCode)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sdf.format(System.currentTimeMillis());
                            SpfUtil.setValue(getActivity(), SpfUtil.CHECK_IN_TIME_KZKF, date);

                            //签到成功，更新UI
                            checkStateAdapter.getItem(0).tv_content = getString(R.string.has_checked_in_kzkf);
                            checkStateAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getActivity(), resultMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    //如果是签出，直接设置默认值
                    if (Constants.STATE_CHECK_OUT_KZKF.equals(status)){
                        SpfUtil.setValue(getActivity(), SpfUtil.CHECK_IN_TIME_KZKF, String.valueOf(-1));
                        logOut(umId);
                        //如果是签到
                    }else if (Constants.STATE_CHECK_IN_KZKF.equals(status)){
                        Toast.makeText(getActivity(), getString(R.string.status_check_in_fail), Toast.LENGTH_LONG).show();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError ve) {
                if (checkDialog != null && checkDialog.isShowing()){
                    checkDialog.dismiss();
                }
                //如果是签出，直接设置默认值
                if (Constants.STATE_CHECK_OUT_KZKF.equals(status)){
                    SpfUtil.setValue(getActivity(), SpfUtil.CHECK_IN_TIME_KZKF, String.valueOf(-1));
                    logOut(umId);
                    //如果是签到
                }else if (Constants.STATE_CHECK_IN_KZKF.equals(status)){
                    Toast.makeText(getActivity(), getString(R.string.program_call_failed), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLogOutside() {
                if (checkDialog != null && checkDialog.isShowing()){
                    checkDialog.dismiss();
                }
                CommonUtils.exitWhenHasLogIn(getActivity());
            }
        });
    }

    public enum Source{
        ONLINE_STATUS,PHONE_STATUS;
    }
}
