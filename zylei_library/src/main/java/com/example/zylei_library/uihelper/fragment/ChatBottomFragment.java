package com.example.zylei_library.uihelper.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.BindingHelper;


/**
 * @author ex-zhangyuelei001
 * @date
 */
public class ChatBottomFragment extends Fragment implements
        FaceFragment.OnFaceOprateListener, TextWatcher {


    private EditText editText;
    private ImageButton sendBtn;
    private ImageButton addBtn;
    private FaceFragment leftFragment;
    private static final String BRACKET_PRE = "[";
    private static final String BRACKET_BACK = "]";

    public ChatBottomFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_view, container, false);
        editText = (EditText) view.findViewById(R.id.msg_et);
        editText.addTextChangedListener(this);
        ImageButton faceBtn = (ImageButton) view.findViewById(R.id.face_btn);
        sendBtn = (ImageButton) view.findViewById(R.id.send_btn);
        addBtn = (ImageButton) view.findViewById(R.id.btn_chat_add);
        leftFragment = FaceFragment.newInstance();
        AddMoreFragment rightFragment = new AddMoreFragment();
        BindingHelper.newInstance()
                .bindView(this, rightFragment, R.id.layout, addBtn)
                .bindActiveState(R.drawable.icon_add_btn_pressed)
                .bindInActiveState(R.drawable.icon_add_btn_unpressed)
                .create();
        BindingHelper.newInstance()
                .bindView(this, leftFragment, R.id.layout, faceBtn)
                .bindActiveState(R.drawable.icon_expression_pressed)
                .bindInActiveState(R.drawable.icon_expression_unpressed)
                .create();

        return view;
    }

    /**
     * 展示输入框
     */
    public void showBottom(Context context, int layoutId) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(layoutId, this);
        ft.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        leftFragment.setOnFaceOprateListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            sendBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
        }
    }


    @Override
    public void onFaceSelected(SpannableString spanEmojiStr) {
        editText.append(spanEmojiStr);
    }

    @Override
    public void onFaceDeleted() {
        int selection = editText.getSelectionStart();
        String text = editText.getText().toString();
        if (selection > 0) {
            String text2 = text.substring(selection - 1);
            if (BRACKET_BACK.equals(text2)) {
                int start = text.lastIndexOf(BRACKET_PRE);
                editText.getText().delete(start, selection);
                return;
            }
            editText.getText().delete(selection - 1, selection);
        }
    }

}
