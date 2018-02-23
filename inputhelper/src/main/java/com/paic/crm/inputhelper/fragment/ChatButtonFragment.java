package com.paic.crm.inputhelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.paic.crm.inputhelper.R;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2018/1/18
 */

public class ChatButtonFragment extends Fragment {

    private TextView disableTxtNote;
    private Button btnContinueAndCancel;
    private static final String BUTTON_TEXT = "button_text";
    private static final String TIP_TEXT = "tip_text";
    private static final String RESOURCE_ID = "resource_id";
    private String buttonText;
    private String tipText;
    private int resId;

    public static ChatButtonFragment showBottom(Context context, int layoutId, String buttonText, String tipText, int resId){

        ChatButtonFragment fragment = new ChatButtonFragment();

        Bundle bundle = new Bundle();
        bundle.putString(BUTTON_TEXT,buttonText);
        bundle.putString(TIP_TEXT,tipText);
        bundle.putInt(RESOURCE_ID,resId);
        fragment.setArguments(bundle);

        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(layoutId, fragment);
        ft.commit();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_button,container,false);
        getData();
        initViews(view);
        initData(buttonText,tipText,resId);
        return view;
    }

    private void initData(String buttonText, String tipText, int resId) {
        setBtnText(buttonText);
        setTip(tipText);
        setBtnResource(resId);
        int visiable = TextUtils.isEmpty(tipText) ? View.GONE : View.VISIBLE;
        setTipVisiable(visiable);
    }

    private void initViews(View view) {
        disableTxtNote = (TextView)view.findViewById(R.id.ll_session_disable_txt_note);
        btnContinueAndCancel = (Button) view.findViewById(R.id.btn_cancle_and_continue);
    }

    private void getData() {
        Bundle bundle = getArguments();
        buttonText = bundle.getString(BUTTON_TEXT);
        tipText = bundle.getString(TIP_TEXT);
        resId = bundle.getInt(RESOURCE_ID);
    }

    public void setTipVisiable(int visiable){
        disableTxtNote.setVisibility(visiable);
    }

    public void setBtnText(String text){
        btnContinueAndCancel.setText(text);
    }

    public void setTip(String text){
        disableTxtNote.setVisibility(View.VISIBLE);
        disableTxtNote.setText(text);
    }

    public void setBtnResource(int resId){
        btnContinueAndCancel.setBackgroundResource(resId);
    }

    public void setBottomBtnClickListener(View.OnClickListener onClickListener){
        btnContinueAndCancel.setOnClickListener(onClickListener);
    }
}
