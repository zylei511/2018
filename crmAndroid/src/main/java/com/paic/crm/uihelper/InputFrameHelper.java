package com.paic.crm.uihelper;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.paic.crm.android.R;
import com.paic.crm.utils.BizSeriesUtil;


/**
 * Created by yueshaojun on 2017/8/9.
 */

public class InputFrameHelper extends BaseHelper implements View.OnClickListener
		,TextWatcher{
	private ImageButton chatAddImgBtn;
	private ImageButton sendImgBtn;
	private ImageButton faceImgBtn;
	private EditText editText;
	private OnInputFrameBtnClickListener listener;
	public InputFrameHelper(Context context, View container) {
		super(context, container);
		initView();
		addListener();

	}

	private void addListener() {
		chatAddImgBtn.setOnClickListener(this);
		sendImgBtn.setOnClickListener(this);
		faceImgBtn.setOnClickListener(this);
		editText.addTextChangedListener(this);
		editText.setOnClickListener(this);
	}

	private void initView() {
		chatAddImgBtn = (ImageButton) mContainer.findViewById(R.id.btn_chat_add);
		sendImgBtn = (ImageButton) mContainer.findViewById(R.id.send_btn);
		faceImgBtn = (ImageButton) mContainer.findViewById(R.id.face_btn);
		editText = (EditText) mContainer.findViewById(R.id.msg_et);

	}

	public void active(){
		mContainer.setVisibility(View.VISIBLE);
	}
	public void inActive(){
		mContainer.setVisibility(View.GONE);
	}
	private void pressChatAddBtn(){
		chatAddImgBtn.setImageResource(R.drawable.icon_add_btn_pressed);
	}
	private void unPressChatAddBtn(){
		chatAddImgBtn.setImageResource(R.drawable.icon_add_btn_unpressespng);
	}
	private void pressFaceBtn(){
		faceImgBtn.setImageResource(R.drawable.icon_expression_pressed);
	}
	private void unPressFaceBtn(){
		faceImgBtn.setImageResource(R.drawable.icon_expression_unpressed);
	}
	private void hideSendBtn(){
		sendImgBtn.setVisibility(View.GONE);
	}
	public void hideFaceBtn(){
		faceImgBtn.setVisibility(View.GONE);
	}
	public void showSendBtn(){
		sendImgBtn.setVisibility(View.VISIBLE);
	}
	public void showFaceBtn(){
		faceImgBtn.setVisibility(View.VISIBLE);
	}
	private void showChatAddBtn(){
		chatAddImgBtn.setVisibility(View.VISIBLE);
	}
	public void hideChatAddBtn(){
		chatAddImgBtn.setVisibility(View.GONE);
	}
	public void reset(){
		chatAddImgBtn.setVisibility(View.VISIBLE);
		faceImgBtn.setVisibility(View.VISIBLE);
		sendImgBtn.setVisibility(View.GONE);
		unPressChatAddBtn();
		unPressFaceBtn();
	}
	public void resetButSend(){
		chatAddImgBtn.setVisibility(View.GONE);
		faceImgBtn.setVisibility(View.VISIBLE);
		unPressChatAddBtn();
		unPressFaceBtn();
	}

	public void resetFace(){
		chatAddImgBtn.setVisibility(View.GONE);
		faceImgBtn.setVisibility(View.VISIBLE);
		sendImgBtn.setVisibility(View.VISIBLE);
		unPressFaceBtn();
	}

	public void disableSendBtn(){
		sendImgBtn.setEnabled(false);
		sendImgBtn.setClickable(false);
	}

	public void enableSendBtn(){
		sendImgBtn.setEnabled(true);
		sendImgBtn.setClickable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.face_btn:
				lightFaceBtn();
				if(listener==null){
					break;
				}
				listener.onFaceBtnClick();
				break;
			case R.id.btn_chat_add:
				lightChatAdd();
				if(listener==null){
					break;
				}
				listener.onChatAddClick();
				break;
			case R.id.send_btn:
				if (!BizSeriesUtil.isKZKF(mContext)){
					reset();
				}
				if(listener==null){
					break;
				}
				listener.onSendBtnClick(editText.getText());
				editText.setText("");
				break;
			case R.id.msg_et:
				listener.onEditClick();
				break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (!BizSeriesUtil.isKZKF(mContext)&&s.toString().trim().equals("")) {
			hideSendBtn();
			showChatAddBtn();
		} else {
			showSendBtn();
			hideChatAddBtn();
		}
	}

	public interface OnInputFrameBtnClickListener{
		void onFaceBtnClick();
		void onChatAddClick();
		void onSendBtnClick(CharSequence editContent);
		void onEditClick();
	}
	public void setListener(OnInputFrameBtnClickListener clickListener){
		listener = clickListener;
	}

	public void enableFace(){
		faceImgBtn.setEnabled(true);
	}
	public void disableFace(){
		faceImgBtn.setEnabled(false);
	}
	public void disableEdit(String tip){
		editText.setHint(tip);
		editText.setClickable(false);
		editText.setText("");
		editText.setEnabled(false);
	}
	public void enableEdit(){
		editText.setEnabled(true);
		editText.setText("");
		editText.setClickable(true);
		editText.setHint("");
	}
	private void lightChatAdd(){
		pressChatAddBtn();
		unPressFaceBtn();
	}
	private void lightFaceBtn(){
		unPressChatAddBtn();
		pressFaceBtn();
	}
	public int getEditStart(){
		return editText.getSelectionStart();
	}
	public void editAppend(CharSequence charSequence){
		editText.append(charSequence);
	}
	public void editDelete(int start ,int end){
		editText.getText().delete(start, end);
	}
	public Editable getEditText(){
		return editText.getText();
	}
	public boolean checkEditHasContent(){
		return !TextUtils.isEmpty(getEditText().toString().trim());
	}
	public void setInputFrameLocation(int x , int y){
		mContainer.scrollTo(x,y-mContainer.getHeight());
	}
	public void setOnFocusChangedListener(View.OnFocusChangeListener l){
		editText.setOnFocusChangeListener(l);
	}
}
