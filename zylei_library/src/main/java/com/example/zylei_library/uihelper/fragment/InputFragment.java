package com.example.zylei_library.uihelper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.BindingHelper;
import com.example.zylei_library.uihelper.entity.ChatAddBean;
import com.example.zylei_library.uihelper.entity.QQFaceEntity;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelRelativeLayout;

/**
 * 带输入框的的Fragment，主要用在聊天页面底部
 *
 * @author ex-zhangyuelei001
 * @date 2017.12.26
 */
public class InputFragment extends Fragment implements TextWatcher, View.OnClickListener, View.OnTouchListener, AdapterView.OnItemClickListener, FaceHelper.OnFaceOprateListener, AddMoreHelper.OnAddMoreItemClickListener {
    private EditText editText;
    private ImageButton sendBtn;
    private ImageButton addBtn;
    private ImageButton faceBtn;
    private RelativeLayout faceLayout;
    private RelativeLayout addLayout;
    private ViewPager facePager;
    private LinearLayout pagerCursor;
    private GridView gridview;
    private KPSwitchPanelRelativeLayout panelLayout;
    private OnSendClickListener mOnSendClickListener;
    private OnAddMoreItemClickListener mOnAddMoreItemClickListener;

    private int[] icons = {R.drawable.icon_pictrue, R.drawable.icon_replay, R.drawable.icon_news,
            R.drawable.icon_send_messege};
    private int[] names = {R.string.text_pictrue, R.string.text_replay, R.string.text_news,
            R.string.text_send_messege};

    public InputFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSendClickListener) {
            mOnSendClickListener = (OnSendClickListener) context;
        }

        if (context instanceof OnAddMoreItemClickListener) {
            mOnAddMoreItemClickListener = (OnAddMoreItemClickListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        initViews(view);
        initFaceViewData();
        initAddViewData();
        //保存keyboard的高度
        KeyboardUtil.attach(getActivity(), panelLayout);

        return view;
    }

    /**
     * 初始化添加按钮下的数据
     */
    private void initAddViewData() {
        AddMoreHelper.newInstance()
                .setDatas(getAddBeanList())
                .addView(getActivity(), gridview)
                .setOnAddMoreItemClickListener(this);
        BindingHelper.newInstance()
                .bindView(addBtn, addLayout, panelLayout)
                .addStateResource(R.drawable.icon_add_btn_pressed,
                        R.drawable.icon_add_btn_unpressed)
                .bind();
    }

    @NonNull
    private List<ChatAddBean> getAddBeanList() {
        List<ChatAddBean> list = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            ChatAddBean chatAddBean = new ChatAddBean();
            chatAddBean.setIconName(getActivity().getString(names[i]));
            chatAddBean.setIconRes(icons[i]);
            list.add(chatAddBean);
        }
        return list;
    }

    /**
     * 初始化表情包数据
     */
    private void initFaceViewData() {
        //填充表情数据
        FaceHelper.newInstance()
                .addFace(getActivity(), QQFaceEntity.class)
                .addViewPager(facePager)
                .addViewPagerCursor(pagerCursor)
                .setOnFaceOprateListener(this)
                .create();

        //绑定View和layout
        BindingHelper.newInstance()
                .bindView(faceBtn, faceLayout, panelLayout)
                .addStateResource(R.drawable.icon_expression_pressed,
                        R.drawable.icon_expression_unpressed)
                .bind();
    }

    /**
     * 初始化View
     *
     * @param view rootView
     */
    private void initViews(View view) {
        panelLayout = (KPSwitchPanelRelativeLayout) view.findViewById(R.id.layout);
        editText = (EditText) view.findViewById(R.id.msg_et);
        editText.addTextChangedListener(this);
        editText.setOnTouchListener(this);
        faceBtn = (ImageButton) view.findViewById(R.id.face_btn);
        sendBtn = (ImageButton) view.findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);
        addBtn = (ImageButton) view.findViewById(R.id.btn_chat_add);
        faceLayout = (RelativeLayout) view.findViewById(R.id.left_layout);
        addLayout = (RelativeLayout) view.findViewById(R.id.add_layout);
        facePager = (ViewPager) view.findViewById(R.id.viewpager);
        pagerCursor = (LinearLayout) view.findViewById(R.id.msg_face_index_view);
        gridview = (GridView) view.findViewById(R.id.chat_add_grid);
        gridview.setOnItemClickListener(this);

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

    @Override
    public void onClick(View v) {
        String content = editText.getText().toString();
        if (mOnSendClickListener != null) {
            mOnSendClickListener.onSend(content);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.msg_et
                && event.getAction() == MotionEvent.ACTION_UP) {
            //重置Panel
            BindingHelper.newInstance().reset();
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnAddMoreItemClickListener != null) {
            mOnAddMoreItemClickListener.onAddMoreItemClick(parent, view, position, id);
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
            if ("]".equals(text2)) {
                int start = text.lastIndexOf("[");
                int end = selection;
                editText.getText().delete(start, end);
                return;
            }
            editText.getText().delete(selection - 1, selection);
        }
    }

    @Override
    public void onAddMoreItemClick(AdapterView<?> parent, View view, int position, long id) {
        //添加addMore布局下面的监听
    }

    /**
     * 发送按钮的回调接口
     */
    public interface OnSendClickListener {
        void onSend(String content);
    }

    public interface OnAddMoreItemClickListener {
        void onAddMoreItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
