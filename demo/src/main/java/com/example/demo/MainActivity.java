package com.example.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zylei_library.uihelper.BindingHelper;
import com.example.zylei_library.uihelper.entity.QQFaceEntity;
import com.example.zylei_library.uihelper.fragment.AddMoreHelper;
import com.example.zylei_library.uihelper.fragment.FaceHelper;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelRelativeLayout;


public class MainActivity extends FragmentActivity implements TextWatcher {

    private EditText editText;
    private ImageButton sendBtn;
    private ImageButton addBtn;
    private KPSwitchPanelRelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.msg_et);
        editText.addTextChangedListener(this);
        ImageButton faceBtn = (ImageButton) findViewById(R.id.face_btn);
        sendBtn = (ImageButton) findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(layout);
            }
        });
        addBtn = (ImageButton) findViewById(R.id.btn_chat_add);

        ViewPager facePager = (ViewPager) findViewById(R.id.viewpager);
        LinearLayout pagerCursor = (LinearLayout) findViewById(R.id.msg_face_index_view);
        layout = (KPSwitchPanelRelativeLayout) findViewById(R.id.panel_root);
        GridView gridview = (GridView)findViewById(R.id.chat_add_grid);
        RelativeLayout faceLayout = (RelativeLayout)findViewById(R.id.left_layout);
        RelativeLayout addLayout = (RelativeLayout)findViewById(R.id.add_layout);
        new AddMoreHelper().init(this,gridview);

        FaceHelper.newInstance()
                .addFace(this, QQFaceEntity.class)
                .addViewPager(facePager)
                .addViewPagerCursor(pagerCursor)
                .create();

        BindingHelper.newInstance()
                .bindView(faceBtn,faceLayout,layout)
                .addStateResource(R.drawable.icon_expression_pressed,
                        R.drawable.icon_expression_unpressed)
                .bind();
        BindingHelper.newInstance()
                .bindView(addBtn,addLayout,layout)
                .addStateResource(R.drawable.icon_add_btn_pressed,
                        R.drawable.icon_add_btn_unpressed)
                .bind();

        KeyboardUtil.attach(this, layout,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d("TAG", String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
                    }
                });



        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    BindingHelper.newInstance().reset();
                }
                return false;
            }
        });
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (layout.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(layout);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


}
