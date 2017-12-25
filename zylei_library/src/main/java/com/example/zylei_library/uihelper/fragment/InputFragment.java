package com.example.zylei_library.uihelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.entity.QQFaceEntity;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelRelativeLayout;

public class InputFragment extends Fragment implements TextWatcher {
    private EditText editText;
    private ImageButton sendBtn;
    private ImageButton addBtn;
    private static final String BRACKET_PRE = "[";
    private static final String BRACKET_BACK = "]";

    public InputFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        final KPSwitchPanelRelativeLayout layout = (KPSwitchPanelRelativeLayout) view.findViewById(R.id.layout);
        editText = (EditText) view.findViewById(R.id.msg_et);
        editText.addTextChangedListener(this);
        ImageButton faceBtn = (ImageButton) view.findViewById(R.id.face_btn);
        sendBtn = (ImageButton) view.findViewById(R.id.send_btn);
        addBtn = (ImageButton) view.findViewById(R.id.btn_chat_add);

        ViewPager facePager = (ViewPager) view.findViewById(R.id.viewpager);
        LinearLayout pagerCursor = (LinearLayout) view.findViewById(R.id.msg_face_index_view);
        FaceHelper.newInstance()
                .addFace(getActivity(), QQFaceEntity.class)
                .addViewPager(facePager)
                .addViewPagerCursor(pagerCursor)
                .create();
//        BindingHelper.newInstance()
//                .bindView(faceBtn, layout)
//                .addStateResource(R.drawable.icon_expression_pressed, R.drawable.icon_expression_unpressed)
//                .bind();


        KeyboardUtil.attach(getActivity(), layout,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d("TAG", String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });
        KPSwitchConflictUtil.attach(layout, faceBtn, editText, new KPSwitchConflictUtil.SwitchClickListener() {
            @Override
            public void onClickSwitch(boolean switchToPanel) {
                if (switchToPanel) {
                    editText.clearFocus();
                } else {
                    editText.requestFocus();
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    layout.setVisibility(View.GONE);
                }
                return false;
            }
        });

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

}
