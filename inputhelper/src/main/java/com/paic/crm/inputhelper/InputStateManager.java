package com.paic.crm.inputhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.paic.crm.inputhelper.entity.BaseFaceEntity;
import com.paic.crm.inputhelper.entity.ChatAddBean;
import com.paic.crm.inputhelper.fragment.ChatButtonFragment;
import com.paic.crm.inputhelper.fragment.InputFragment;

import java.util.ArrayList;

/**
 * 输入框状态的管理
 * <p>
 * 先调用{@link #getInstance()}创建一个对象
 * 然后调用{@link #setHasAudio(boolean)},
 * {@link #setFaceClasses(Class[])},
 * {@link #setAddMore(ArrayList)},
 * {@link #setInputState(InputState)},
 * {@link #setLayoutId(Context, int)},
 * {@link #setBottomBtnClickListener(View.OnClickListener)},
 * 没有顺序，最后再调用{@link #show()}，就可以显示出来。
 * 其中，{@link #getInstance()}和{@link #setLayoutId(Context, int)}是都会调用的;
 * <p>
 * 只有在{@link #setInputState(InputState)}的状态为InputState.STATE_BOTTOM_BTN时,
 * {@link #setBottomBtnClickListener(View.OnClickListener)}才有用,
 * 如果需要改变底部Button的提示和状态，调用{@link #setBottom(String, String, int)};
 * <p>
 * 而当{@link #setInputState(InputState)}的状态为InputState.STATE_INPUT时,
 * 才需设置{@link #setFaceClasses(Class[])},{@link #setAddMore(ArrayList)},
 * {@link #setHasAudio(boolean)};
 * <p>
 * 如果不设置{@link #setInputState(InputState)},则会抛出异常
 * <p>
 * 例如
 * 设置底部为输入框：
 * InputStateManager.getInstance()
 * .setLayoutId(this,R.id.panel_root)
 * .setInputState(InputState.STATE_INPUT)
 * .setAddMore(getChatAddBeanArrayList())
 * .setFaceClasses(QQFaceEntity.class)
 * .setHasAudio(false)
 * .show();
 * <p>
 * 设置底部为Button：
 * InputStateManager.getInstance()
 * .setLayoutId(this,R.id.panel_root)
 * .setBottomBtnClickListener(this)
 * .setInputState(InputState.STATE_BOTTOM_BTN)
 * .show();
 * <p>
 * 布局中的设置
 * 这个页面的根布局也应该是{@code KPSwitchRootFrameLayout}、
 * {@code KPSwitchRootLinearLayout}、{@code KPSwitchRootRelativeLayout}
 *
 * @author ex-zhangyuelei001
 * @date 2018/1/25
 */
public class InputStateManager {
	private InputState inputState = InputState.STATE_INPUT;
	private int layoutId;
	private Context context;
	boolean hasAudio;
	private ArrayList<ChatAddBean> chatAddBeanArrayList = new ArrayList<>();
	private ArrayList<Class<? extends BaseFaceEntity>> faceClasses = new ArrayList<>();
	private Fragment fragment;
	private String buttonText;
	private String tipText;
	private int buttonResId;

	public int getButtonResId() {
		return buttonResId;
	}

	public void setButtonResId(int buttonResId) {
		this.buttonResId = buttonResId;
	}

	public String getTipText() {
		return tipText;
	}

	public void setTipText(String tipText) {
		this.tipText = tipText;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	static class InputStateManagerHolder {
		private static InputStateManager instance = new InputStateManager();
	}

	public static InputStateManager getInstance() {

		return InputStateManager.InputStateManagerHolder.instance;
	}

	/**
	 * 设置InputState，有两种状态STATE_INPUT（输入状态）与STATE_BOTTOM_BTN（按钮状态)
	 * 使用InputStateManager.class的时候必须调用此方法，否则使用{@link #show()}时会调用失败
	 *
	 * @param inputState 需要设置的InputState
	 * @return
	 */
	public InputStateManager setInputState(InputState inputState) {
		this.inputState = inputState;
		return this;
	}

	/**
	 * 获取InputState
	 *
	 * @return 当前InputState
	 */
	public InputState getInputState() {
		return inputState;
	}

	/**
	 * 获取底部layout的Id
	 */
	private int getLayoutId() {
		return layoutId;
	}

	/**
	 * 设置底部layout
	 */
	public InputStateManager setLayoutId(Context context, int layoutId) {
		this.context = context;
		this.layoutId = layoutId;
		return this;
	}

	private Context getContext() {
		return context;
	}

	/**
	 * 是否有语音按钮{@link #setHasAudio(boolean)} false，没有语音按钮；true，有语音按钮
	 */
	public boolean isHasAudio() {
		return hasAudio;
	}

	/**
	 * 设置是否有语音按钮
	 *
	 * @param hasAudio false，没有语音按钮；true，有语音按钮
	 * @return
	 */
	public InputStateManager setHasAudio(boolean hasAudio) {
		this.hasAudio = hasAudio;
		return this;
	}

	private ArrayList<ChatAddBean> getChatAddBeanArrayList() {
		return chatAddBeanArrayList;
	}

	public void hidePanelAndKeybroad() {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof InputFragment) {
			((InputFragment) fragment).reset();
		}
	}


	/**
	 * 设置更多
	 */
	public InputStateManager setAddMore(@NonNull ArrayList<ChatAddBean> chatAddBeen) {
		chatAddBeanArrayList.clear();
		this.chatAddBeanArrayList.addAll(chatAddBeen);
		return this;
	}


	private ArrayList<Class<? extends BaseFaceEntity>> getFaceClasses() {
		return faceClasses;
	}

	/**
	 * 设置表情
	 *
	 * @param faceClasses 表情类BaseFaceEntity的子类
	 * @return
	 */
	public InputStateManager setFaceClasses(@NonNull Class<? extends BaseFaceEntity>... faceClasses) {
		this.faceClasses.clear();
		for (Class<? extends BaseFaceEntity> tClass : faceClasses) {
			this.faceClasses.add(tClass);
		}
		return this;
	}

	/**
	 * 设置底部的fragment,有两个Fragment:ChatButtonFragment（底部是一个Button）
	 * 和InputFragment（底部是一个输入框）
	 *
	 * @param fragment
	 */
	private void setBottomFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	private Fragment getBottomFragment() {
		return fragment;
	}

	/**
	 * 设置底部button的监听
	 *
	 * @param onClickListener
	 */
	public void setBottomBtnClickListener(View.OnClickListener onClickListener) {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof ChatButtonFragment) {
			((ChatButtonFragment) fragment).setBottomBtnClickListener(onClickListener);
		}
	}

	public void setOnSendClickListener(InputFragment.OnSendClickListener onSendClickListener) {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof InputFragment) {
			((InputFragment) fragment).addOnSendClickListener(onSendClickListener);
		}
	}

	public void setOnRecordFinishListener(InputFragment.OnRecordFinishListener onRecordFinishListener) {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof InputFragment) {
			((InputFragment) fragment).addOnRecordFinishListenr(onRecordFinishListener);
		}
	}

	public void setOnMoreItemClickListenr(InputFragment.OnMoreItemClickListenr onMoreItemClickListenr) {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof InputFragment) {
			((InputFragment) fragment).addOnMoreItemClickListenr(onMoreItemClickListenr);
		}
	}

	/**
	 * 设置底部Bottom的button的状态
	 *
	 * @param btnText
	 * @param tipText
	 * @param resId
	 */
	public InputStateManager setBottom(String btnText, String tipText, int resId) {
		Fragment fragment = getBottomFragment();
		if (fragment instanceof ChatButtonFragment) {
			((ChatButtonFragment) fragment).setBtnResource(resId);
			((ChatButtonFragment) fragment).setTip(tipText);
			((ChatButtonFragment) fragment).setBtnText(btnText);
			if (TextUtils.isEmpty(tipText)) {
				((ChatButtonFragment) fragment).setTipVisiable(View.GONE);
			}
			//第一次初始化时
		} else {
			setButtonText(btnText);
			setTipText(tipText);
			setButtonResId(resId);
		}
		return this;
	}

	public void show() {
		InputState state = getInputState();
		//底部是按钮的状态
		if (state == InputState.STATE_BOTTOM_BTN) {
			ChatButtonFragment chatButtonFragment = ChatButtonFragment
					.showBottom(getContext()
							, getLayoutId()
							, getButtonText()
							, getTipText()
							, getButtonResId());

			setBottomFragment(chatButtonFragment);
			//底部是输入框
		} else if (state == InputState.STATE_INPUT) {
			InputFragment inputFragment = InputFragment
					.showBottom(getContext()
							, getLayoutId()
							, isHasAudio()
							, getChatAddBeanArrayList()
							, getFaceClasses());

			setBottomFragment(inputFragment);
		} else {
			throw new IllegalArgumentException("must use setInputState() to set " +
					"InputState.STATE_INPUT or InputState.STATE_BOTTOM_BTN");
		}
	}
}
