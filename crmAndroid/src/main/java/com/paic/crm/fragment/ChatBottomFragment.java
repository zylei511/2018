package com.paic.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.paic.crm.android.R;
import com.paic.crm.uihelper.ChatAddHelper;
import com.paic.crm.uihelper.FaceStateHelper;
import com.paic.crm.uihelper.InputFrameHelper;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.SelectFaceHelper;
import com.paic.crm.utils.SpfUtil;

/**
 * Created by yueshaojun988 on 2017/9/6.
 */

public class ChatBottomFragment extends Fragment {
    private View fragmentView;
    private FaceStateHelper mFaceStateHelper;
    private ChatAddHelper mChatAddHelper;
    private InputFrameHelper mInputFrameHelper;
    private boolean faceShowFlag = false;
    private boolean chatAddShowFlag = false;
    private static final String TAG = "ChatBottomFragment";

    private int mBottomHeight = 0;
    private OnFragmentCreateListener onFragmentCreateListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomHeight = (int) SpfUtil.getValue(getActivity(),SpfUtil.KEYBORAD_HEIGHT,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.chat_bottom_layout,null);
        CRMLog.LogInfo(TAG,"onCreateView");
        initHelper();
        CRMLog.LogInfo(TAG,"initHelper");
        onFragmentCreateListener.onFragmentCreate(TAG);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CRMLog.LogInfo(TAG,"onActivityCreated");

    }

    private void initHelper() {
        mChatAddHelper =
                new ChatAddHelper(getContext(),fragmentView.findViewById(R.id.chat_add_grid),mBottomHeight);
        mFaceStateHelper =
                new FaceStateHelper(getContext(),fragmentView.findViewById(R.id.face_layout),mBottomHeight);
        mInputFrameHelper = new InputFrameHelper(getContext(),fragmentView.findViewById(R.id.ll_chatmain_input));
        mFaceStateHelper.setFaceOpreateListener(mOnFaceOprateListener);

    }

    public void showBottom(Context context,Fragment fragment){
        CRMLog.LogInfo(TAG,"showBottom");
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.chat_bottom,fragment,TAG);
        ft.commit();
    }
    public void hideBottom(Context context){
        FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(this);
        ft.commit();
    }
    public void showFace(){
        faceShowFlag = true;
        chatAddShowFlag = false;

        mChatAddHelper.inActive();
        mFaceStateHelper.active();

    }
    public void showChatAdd(){
        faceShowFlag = false;
        chatAddShowFlag = true;

        mChatAddHelper.active();
        mFaceStateHelper.inActive();

    }
    public void hideAll(){
        faceShowFlag = false;
        chatAddShowFlag = false;

        mChatAddHelper.inActive();
        mFaceStateHelper.inActive();
        mInputFrameHelper.reset();
    }

    public void hideFaceAndChatAdd(){
        faceShowFlag = false;
        chatAddShowFlag = false;

        mChatAddHelper.inActive();
        mFaceStateHelper.inActive();
        mInputFrameHelper.resetFace();
    }

    public void disableButSend(){
        mInputFrameHelper.showSendBtn();
        mInputFrameHelper.inActive();
    }

    public void hideButSend(){
        faceShowFlag = false;
        chatAddShowFlag = false;

        mChatAddHelper.inActive();
        mFaceStateHelper.inActive();
        mInputFrameHelper.resetButSend();
    }
    public boolean isFaceShow(){
        return faceShowFlag;
    }
    public boolean isChatAddShow(){
        return chatAddShowFlag;
    }

    public void enableFace(){
        mInputFrameHelper.enableFace();
    }
    public void disableFace(){
        mInputFrameHelper.disableFace();
    }
    public void disableEdit(String tip){
        mInputFrameHelper.disableEdit(tip);
    }
    public void enableEdit(){
        mInputFrameHelper.enableEdit();
    }
    public void setChatAddItemClickListener(AdapterView.OnItemClickListener itemClickListener){
        mChatAddHelper.setItemClickListener(itemClickListener);
    }
    public void showSendBtn(){
        mInputFrameHelper.showSendBtn();
    }

    public void disableSendBtn(){
        mInputFrameHelper.disableSendBtn();
    }

    public void enableSendBtn(){
        mInputFrameHelper.enableSendBtn();
    }

    public void setInputFrameBtnClickListener(InputFrameHelper.OnInputFrameBtnClickListener clickListener){
        mInputFrameHelper.setListener(clickListener);
    }
    public interface OnFragmentCreateListener{
        void onFragmentCreate(String tag);
    }
    public void setOnFragmentCreateListener(OnFragmentCreateListener createListener){
        onFragmentCreateListener = createListener;
    }
    SelectFaceHelper.OnFaceOprateListener mOnFaceOprateListener = new SelectFaceHelper.OnFaceOprateListener() {
        @Override
        public void onFaceSelected(SpannableString spanEmojiStr) {
            if (null != spanEmojiStr) {
                mInputFrameHelper.editAppend(spanEmojiStr);
            }
        }

        @Override
        public void onFaceDeleted() {
            int selection = mInputFrameHelper.getEditStart();
            String text = mInputFrameHelper.getEditText().toString();
            if (selection > 0) {
                String text2 = text.substring(selection - 1);
                if ("]".equals(text2)) {
                    int start = text.lastIndexOf("[");
                    int end = selection;
                    mInputFrameHelper.editDelete(start, end);
                    return;
                }
                mInputFrameHelper.editDelete(selection - 1, selection);
            }
        }

    };

    public boolean editHasContent(){
        return mInputFrameHelper.checkEditHasContent();
    }

    /**
     * 改变表情和表格的高。
     * @param newHeight
     */
    public void changeBottomHeight(int newHeight){
        CRMLog.LogDebg(TAG,"changeBottomHeight:"+newHeight);
        if(newHeight==mBottomHeight){
            return;
        }
        CRMLog.LogDebg(TAG,"changeBottomHeight changed:"+newHeight);
        mBottomHeight = newHeight;
        mChatAddHelper.setHeight(mBottomHeight);
        mFaceStateHelper.setHeight(mBottomHeight);
        SpfUtil.setValue(getContext(),SpfUtil.KEYBORAD_HEIGHT,mBottomHeight);
    }
    public void setInputFrameLocation(){
        int x = 0;
        int y = CommonUtils.getContentViewHeight(getActivity())-mBottomHeight;
        mInputFrameHelper.setInputFrameLocation(x,y);
    }
    public void setOnFocusChangedListener(View.OnFocusChangeListener l){
        mInputFrameHelper.setOnFocusChangedListener(l);
    }
    public void hideChatAdd(){
        mInputFrameHelper.hideChatAddBtn();
    }

}
