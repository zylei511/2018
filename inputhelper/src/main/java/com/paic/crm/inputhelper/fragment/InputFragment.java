package com.paic.crm.inputhelper.fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.paic.crm.inputhelper.AddMoreHelper;
import com.paic.crm.inputhelper.BindingHelper;
import com.paic.crm.inputhelper.FaceHelper;
import com.paic.crm.inputhelper.R;
import com.paic.crm.inputhelper.RecordAudioView;
import com.paic.crm.inputhelper.entity.BaseFaceEntity;
import com.paic.crm.inputhelper.entity.ChatAddBean;
import com.paic.crm.inputhelper.entity.QQFaceEntity;
import com.paic.crm.inputhelper.kpswitch.widget.KPSwitchPanelRelativeLayout;
import com.paic.crm.inputhelper.util.MediaPlayerManager;
import com.paic.crm.inputhelper.util.MediaRecorderManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 带输入框的的Fragment，主要用在聊天页面底部
 *
 * @author ex-zhangyuelei001
 * @date 2017.12.26
 */
public class InputFragment extends Fragment implements TextWatcher, View.OnClickListener, View.OnTouchListener, FaceHelper.OnFaceOprateListener, AddMoreHelper.OnMoreItemClickListener, MediaRecorderManager.RecordListener {
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
    private OnSendCallback onSendCallback;
    private OnMoreItemCallback onMoreItemCallback;
    private OnRecordFinishCallback onRecordFinishCallback;
    private ImageButton audioView;
    private Button msgAudioView;
    private final static String HAS_AUDIO = "has_audio";
    private final static String ADD_MORE_NAME = "add_more_name";
    private final static String ADD_MORE_REID = "add_more_reid";
    private final static String FACE_CLASS = "face_class";

    private boolean hasAudio;
    private boolean hasFace;
    private boolean hasAddMore;

    private ArrayList<Integer> addMoreIcons = new ArrayList<>();
    //            {R.drawable.icon_pictrue, R.drawable.icon_replay, R.drawable.icon_news,
//            R.drawable.icon_send_messege};
    private ArrayList<String> AddMorenames = new ArrayList<>();
    //        {R.string.text_pictrue, R.string.text_replay, R.string.text_news,
//            R.string.text_send_messege};
    private MediaRecorderManager recorderUtil;
    private float startY;
    private ArrayList<Class<BaseFaceEntity>> faceNames = new ArrayList<>();
    private Class<BaseFaceEntity> faceName;

    public InputFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSendCallback) {
            onSendCallback = (OnSendCallback) context;
        }

        if (context instanceof OnMoreItemCallback) {
            onMoreItemCallback = (OnMoreItemCallback) context;
        }

        if (context instanceof OnRecordFinishCallback) {
            onRecordFinishCallback = (OnRecordFinishCallback) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        getData();
        initViews(view);
        initFaceViewData();
        initAddViewData();
        bindView();

        return view;
    }

    private void bindView() {
        BindingHelper.newInstance()
                .bindPanelLayout(getActivity(), panelLayout);

        if (hasAudio) {
            BindingHelper.newInstance()
                    .bindView(audioView, msgAudioView,
                            R.drawable.icon_keybroad,
                            R.drawable.icon_audio,
                            true);
        }

        if (hasFace) {
            BindingHelper.newInstance()
                    .bindView(faceBtn, faceLayout,
                            R.drawable.icon_expression_pressed,
                            R.drawable.icon_expression_unpressed);
        }
        if (hasAddMore) {
            BindingHelper.newInstance()
                    .bindView(addBtn, addLayout,
                            R.drawable.icon_add_btn_pressed,
                            R.drawable.icon_add_btn_unpressed);
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        Bundle bundle = getArguments();
        hasAudio = bundle.getBoolean(HAS_AUDIO);
        AddMorenames.addAll(bundle.getStringArrayList(ADD_MORE_NAME));
        addMoreIcons.addAll(bundle.getIntegerArrayList(ADD_MORE_REID));
        hasAddMore = !(AddMorenames.isEmpty() || AddMorenames.isEmpty());
        faceName = (Class<BaseFaceEntity>) bundle.getSerializable(FACE_CLASS);
        faceNames.add((Class<BaseFaceEntity>) bundle.getSerializable(FACE_CLASS));
        hasFace = !faceNames.isEmpty();
    }

    /**
     * 初始化添加按钮下的数据
     */
    private void initAddViewData() {
        AddMoreHelper.newInstance()
                .setDatas(getAddBeanList())
                .addView(getActivity(), gridview)
                .setOnAddMoreItemClickListener(this);
    }

    @NonNull
    private List<ChatAddBean> getAddBeanList() {
        List<ChatAddBean> list = new ArrayList<>();
        for (int i = 0; i < addMoreIcons.size(); i++) {
            ChatAddBean chatAddBean = new ChatAddBean();
            chatAddBean.setIconName(AddMorenames.get(i));
            chatAddBean.setIconRes(addMoreIcons.get(i));
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
//                .addFace(getActivity(), QQFaceEntity.class)
                .addFace(getActivity(), faceName)
                .addViewPager(facePager)
                .addViewPagerCursor(pagerCursor)
                .setOnFaceOprateListener(this)
                .init();
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
        audioView = (ImageButton) view.findViewById(R.id.btn_audio);
        msgAudioView = (Button) view.findViewById(R.id.msg_audio_btn);
        msgAudioView.setOnTouchListener(this);
        recorderUtil = new MediaRecorderManager();
        recorderUtil.setRecordListener(this);
        //判断语音、表情、更多模块是否显示
        audioView.setVisibility(hasAudio ? View.VISIBLE : View.GONE);
        faceBtn.setVisibility(hasFace ? View.VISIBLE : View.GONE);
        addBtn.setVisibility(hasAddMore ? View.VISIBLE : View.GONE);
        sendBtn.setVisibility(!hasFace && !hasAddMore ? View.VISIBLE : View.GONE);
    }

    /**
     * 展示输入框
     */
    public void showBottom(Context context, int layoutId, boolean hasAudio,
                           ArrayList<String> addMoreNames, ArrayList<Integer> addMoreRes,
                           Class<? extends BaseFaceEntity> faceClasses) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(HAS_AUDIO, hasAudio);
        bundle.putStringArrayList(ADD_MORE_NAME, addMoreNames);
        bundle.putIntegerArrayList(ADD_MORE_REID, addMoreRes);
        bundle.putSerializable(FACE_CLASS, faceClasses);
        setArguments(bundle);
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
        if (onSendCallback != null) {
            editText.setText("");
            onSendCallback.onSendBtnClick(content);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.msg_et
                && event.getAction() == MotionEvent.ACTION_UP) {
            //重置Panel
            BindingHelper.newInstance().reset();
        } else if (v.getId() == R.id.msg_audio_btn) {
            //点击录音按钮时，先关闭正在播放的语音
            MediaPlayerManager.newInstance().stop();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //默认是发送语音
                    recorderUtil.init(getActivity());
                    recorderUtil.startRecorder();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    recorderUtil.stopRecorder();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float endY = event.getY();
                    Log.e("TAG", "end - start = " + (endY - startY));
                    if (startY - endY > 50) {
                        recorderUtil.cancelRecorder();
                    } else if (endY - startY > 50) {
                        //默认是发送语音
                        recorderUtil.init(getActivity());
                        recorderUtil.startRecorder();
                    }
                    break;
                default:
                    recorderUtil.stopRecorder();
                    break;
            }
        }
        return false;
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
    public void onMoreItemClick(AdapterView<?> parent, View view, int position, long id) {
        //添加addMore布局下面的监听
        if (onMoreItemCallback != null) {
            onMoreItemCallback.onMoreItemClick(parent, view, position, id);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BindingHelper.newInstance().unBindView();
    }

    @Override
    public void onUpdate(double decible, float progress) {
        Log.e("onUpdate", "正在录制 " + "时间：" + progress + " ; 分贝：" + decible);
        RecordAudioView.newInstance()
                .initDialog(getActivity())
                .setRoundProgressBar(progress)
                .setImageLevel((int) decible)
                .show();
    }

    @Override
    public void onFinished(String path, float recordTime) {
        //用户取消发送语音
        if (TextUtils.isEmpty(path) && recordTime == 0) {
            RecordAudioView.newInstance().dismiss();
            return;
        }
        //用户录音时间太短
        if (recordTime < 1) {
            RecordAudioView.newInstance()
                    .initDialog(getActivity())
                    .setTips(getString(R.string.crm_sdk_audio_too_short))
                    .setImageResource(R.drawable.icon_audio_short_tips)
                    .delayDismiss();
            return;
        }
        RecordAudioView.newInstance().dismiss();
        Log.e("onFinished", "录制结束 " + "时间：" + recordTime);
        if (onRecordFinishCallback != null) {
            onRecordFinishCallback.onRecordFinish(path);
        }

    }

    @Override
    public boolean onCancel() {
        RecordAudioView.newInstance()
                .initDialog(getActivity())
                .setTips(getString(R.string.crm_sdk_audio_left_cancel_send))
                .setImageResource(R.drawable.icon_audio_cancel);
        return false;
    }

    /**
     * 发送按钮的回调接口
     */
    public interface OnSendCallback {
        void onSendBtnClick(String content);
    }

    public interface OnRecordFinishCallback {
        void onRecordFinish(String path);
    }

    public interface OnMoreItemCallback {
        void onMoreItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
