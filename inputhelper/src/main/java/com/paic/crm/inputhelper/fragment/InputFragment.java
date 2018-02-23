package com.paic.crm.inputhelper.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.paic.crm.inputhelper.kpswitch.util.KPSwitchConflictUtil;
import com.paic.crm.inputhelper.kpswitch.util.KeyboardUtil;
import com.paic.crm.inputhelper.kpswitch.widget.KPSwitchPanelRelativeLayout;
import com.paic.crm.inputhelper.util.MediaPlayerManager;
import com.paic.crm.inputhelper.util.MediaRecorderManager;

import java.util.ArrayList;
import java.util.List;


/**
 * 带输入框的的Fragment，主要用在聊天页面底部
 *
 * @author ex-zhangyuelei001
 * @date 2017.12.26
 */
public class InputFragment extends Fragment
		implements TextWatcher
		, View.OnClickListener
		, View.OnTouchListener
		, FaceHelper.OnFaceOprateListener
		, AddMoreHelper.OnMoreItemClickListener
		, MediaRecorderManager.RecordListener {

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
	private OnSendClickListener onSendClickListener;
	private OnMoreItemClickListenr onMoreItemClickListenr;
	private OnRecordFinishListener onRecordFinishListener;
	private ImageButton audioView;
	private Button msgAudioView;
	private final static String HAS_AUDIO = "has_audio";
	private final static String ADD_MORE = "add_more";
	private final static String FACE_CLASS = "face_class";
	private final static Integer MAX_CANCEL_VALUE = 50;

	private boolean hasAudio;
	private boolean hasFace;
	private boolean hasAddMore;

	private ArrayList<ChatAddBean> chatAddBeanArrayList = new ArrayList<>();
	private MediaRecorderManager recorderUtil;
	private float startY;
	private ArrayList<Class<? extends BaseFaceEntity>> faceNames = new ArrayList<>();

	public InputFragment() {
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
		BindingHelper.getInstance()
				.bindPanelLayout(getActivity(), panelLayout);

		if (hasAudio) {
			BindingHelper.getInstance()
					.bindView(audioView, msgAudioView,
							R.drawable.icon_keybroad,
							R.drawable.icon_audio,
							true);
		}

		if (hasFace) {
			BindingHelper.getInstance()
					.bindView(faceBtn, faceLayout,
							R.drawable.icon_expression_pressed,
							R.drawable.icon_expression_unpressed);
		}
		if (hasAddMore) {
			BindingHelper.getInstance()
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
		ArrayList<ChatAddBean> arrayList = bundle.getParcelableArrayList(ADD_MORE);
		chatAddBeanArrayList.addAll(arrayList);
		hasAddMore = !chatAddBeanArrayList.isEmpty();
		Bundle cBundle = bundle.getBundle(FACE_CLASS);
		//如果cBundle == null ,则直接new一个，防止下面cBundle.keySet()出现空指针
		if (cBundle == null) {
			cBundle = new Bundle();
		}

		for (String key : cBundle.keySet()) {
			Class<? extends BaseFaceEntity> fClass
					= (Class<? extends BaseFaceEntity>) cBundle.getSerializable(key);
			faceNames.add(fClass);
		}
		hasFace = !faceNames.isEmpty();
	}

	/**
	 * 初始化添加按钮下的数据
	 */
	private void initAddViewData() {
		AddMoreHelper.getInstance()
				.setDatas(getAddBeanList())
				.addView(getActivity(), gridview)
				.setOnAddMoreItemClickListener(this);
	}

	@NonNull
	private List<ChatAddBean> getAddBeanList() {
		return chatAddBeanArrayList;
	}

	/**
	 * 初始化表情包数据
	 */
	private void initFaceViewData() {
		//填充表情数据
		FaceHelper.getInstance()
				.addFace(getActivity(), faceNames)
				.addViewPager(facePager)
				.setOnFaceOprateListener(this)
				.addViewPagerCursor(pagerCursor)
				.init();
	}

	/**
	 * 初始化View
	 *
	 * @param view rootView
	 */
	private void initViews(View view) {
		panelLayout = (KPSwitchPanelRelativeLayout) view.findViewById(R.id.layout);
		panelLayout.setVisibility(View.GONE);
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
		gridview.setSelector(R.drawable.transparent_background);
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
	 *
	 * @param context
	 * @param layoutId
	 * @param hasAudio     是否有语音功能。true,则显示；false，则隐藏
	 * @param chatAddBeens
	 * @param faceClasses  表情class,没有时，则不显示表情
	 */
	public static InputFragment showBottom(Context context, int layoutId, boolean hasAudio,
										   ArrayList<ChatAddBean> chatAddBeens,
										   ArrayList<Class<? extends BaseFaceEntity>> faceClasses) {

		InputFragment inputFragment = new InputFragment();

		Bundle bundle = new Bundle();
		bundle.putBoolean(HAS_AUDIO, hasAudio);
		bundle.putParcelableArrayList(ADD_MORE, chatAddBeens);
		Bundle tBundle = new Bundle();
		for (Class<? extends BaseFaceEntity> tclass : faceClasses) {
			tBundle.putSerializable(tclass.getName(), tclass);
		}
		bundle.putBundle(FACE_CLASS, tBundle);
		inputFragment.setArguments(bundle);
		FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		ft.replace(layoutId, inputFragment);
		ft.commit();


		return inputFragment;
	}

	public void addOnSendClickListener(OnSendClickListener onSendClickListener) {
		this.onSendClickListener = onSendClickListener;
	}

	public void removeOnSendClickListener() {
		this.onSendClickListener = null;
	}

	public void addOnMoreItemClickListenr(OnMoreItemClickListenr onMoreItemClickListenr) {
		this.onMoreItemClickListenr = onMoreItemClickListenr;
	}

	public void removeOnMoreItemClickListenr() {
		this.onMoreItemClickListenr = null;
	}

	public void addOnRecordFinishListenr(OnRecordFinishListener onRecordFinishListener) {
		this.onRecordFinishListener = onRecordFinishListener;
	}

	public void removeOnRecordFinishListenr() {
		this.onRecordFinishListener = null;
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
		if (v == sendBtn) {
			String content = editText.getText().toString().trim();
			sendMsg(content);
			KeyboardUtil.hideKeyboard(v);
		}

	}

	private void sendMsg(String content) {
		editText.setText("");
		if (onSendClickListener != null) {
			onSendClickListener.onSendBtnClick(content);
		}
	}

	public void reset() {
		//重置Panel
		BindingHelper.getInstance().reset();
		KPSwitchConflictUtil.hidePanelAndKeyboard(panelLayout);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.msg_et
				&& event.getAction() == MotionEvent.ACTION_UP) {
			//重置Panel
			BindingHelper.getInstance().reset();
		} else if (v.getId() == R.id.msg_audio_btn) {
			//点击录音按钮时，先关闭正在播放的语音
			MediaPlayerManager.getInstance().stop();
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
					if (startY - endY > MAX_CANCEL_VALUE) {
						recorderUtil.cancelRecorder();
					} else if (endY - startY > MAX_CANCEL_VALUE) {
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
		if (onMoreItemClickListenr != null) {
			onMoreItemClickListenr.onMoreItemClick(parent, view, position, id);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		BindingHelper.getInstance().unBindView();
		AddMoreHelper.getInstance().clearData();
		FaceHelper.getInstance().clearData();
		removeOnMoreItemClickListenr();
		removeOnRecordFinishListenr();
		removeOnSendClickListener();
		chatAddBeanArrayList.clear();
		faceNames.clear();
	}

	@Override
	public void onUpdate(double decible, float progress) {
		Log.e("onUpdate", "正在录制 " + "时间：" + progress + " ; 分贝：" + decible);
		RecordAudioView.getInstance()
				.initDialog(getActivity())
				.setRoundProgressBar(progress)
				.setImageLevel((int) decible)
				.show();
	}

	@Override
	public void onFinished(String path, float recordTime) {
		//用户取消发送语音
		if (TextUtils.isEmpty(path) && recordTime == 0) {
			RecordAudioView.getInstance().dismiss();
			return;
		}
		//用户录音时间太短
		if (recordTime < 1) {
			RecordAudioView.getInstance()
					.initDialog(getActivity())
					.setTips(getString(R.string.crm_sdk_audio_too_short))
					.setImageResource(R.drawable.icon_audio_short_tips)
					.delayDismiss();
			return;
		}
		RecordAudioView.getInstance().dismiss();
		Log.e("onFinished", "录制结束 " + "时间：" + recordTime);
		if (onRecordFinishListener != null) {
			onRecordFinishListener.onRecordFinish(path);
		}

	}

	@Override
	public boolean onCancel() {
		RecordAudioView.getInstance()
				.initDialog(getActivity())
				.setTips(getString(R.string.crm_sdk_audio_left_cancel_send))
				.setImageResource(R.drawable.icon_audio_cancel);
		return false;
	}

	/**
	 * 发送按钮点击回调接口
	 */
	public interface OnSendClickListener {
		/**
		 * 发送按钮被点击时，执行的方法
		 *
		 * @param content 从输入框中获取的发送内容
		 */
		void onSendBtnClick(String content);
	}

	/**
	 * 录音结束回调接口
	 */
	public interface OnRecordFinishListener {
		/**
		 * 录音结束后执行的回调
		 *
		 * @param path 录音文件存放的路径
		 */
		void onRecordFinish(String path);
	}

	/**
	 * 更多种item点击回调
	 */
	public interface OnMoreItemClickListenr {
		/**
		 * gridView点击事件
		 *
		 * @param parent
		 * @param view
		 * @param position
		 * @param id
		 */
		void onMoreItemClick(AdapterView<?> parent, View view, int position, long id);
	}
}
