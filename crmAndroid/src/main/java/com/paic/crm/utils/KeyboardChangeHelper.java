package com.paic.crm.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 键盘监听器
 */
public class KeyboardChangeHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "ListenerHandler";
    private View mContentView;
    private int mPreHeight = 0;
    private boolean opened = false;
    private KeyBoardListener mListener;
    private int keyboardHeight = 0;
    private int originVisibaleHeight = 0;
    public interface KeyBoardListener {
        void onKeyboardOpened();
        void onKeyboardClosed();
        void onKeyboardHeightChanged(int newHeight);
    }
    public void setKeyboardListener(KeyBoardListener keyBoardListener) {
        mListener = keyBoardListener;
    }

    public KeyboardChangeHelper(Context contextObj) {
        if (contextObj == null) {
            throw new NullPointerException("context is null!");
        }
        if (!(contextObj instanceof Activity)){
            throw new ClassCastException("context is must an Activity!");
        }
        mContentView = findContentView((Activity) contextObj);
        if (mContentView != null) {
            addContentTreeObserver();
        }
    }

    private View findContentView(Activity contextObj) {
        return contextObj.findViewById(android.R.id.content);
    }

    private void addContentTreeObserver() {
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        mContentView.getWindowVisibleDisplayFrame(rect);
        int currHeight = (rect.bottom - rect.top);
        Log.d(TAG, "onGlobalLayout currHeight->>" + currHeight + "\npreHeight->>" + mPreHeight);
        if (currHeight == 0) {
            return;
        }
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
            originVisibaleHeight = currHeight;
            return;
        }
        int dert =mPreHeight - currHeight;
        if(dert!=0){
            if(Math.abs(dert)!=keyboardHeight){
                notifyKeyboardChanged(Math.abs(dert));
            }
            keyboardHeight = Math.abs(dert);
        }
        Log.d(TAG,"onGlobalLayout"+keyboardHeight);
        if(!opened &&dert>0){
            opened = true;
            notifyKeyboardOpened();
        }else if(opened &&dert<0){
            opened = false;
            notifyKeyboardClosed();
        }
        mPreHeight = currHeight;
    }

    public void destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
        mListener = null;
    }
    private void notifyKeyboardOpened(){
        mListener.onKeyboardOpened();
    }
    private void notifyKeyboardClosed(){
        mListener.onKeyboardClosed();
    }
    private void notifyKeyboardChanged(int newHeight){
        mListener.onKeyboardHeightChanged(newHeight);
    }
    public boolean isOpened(){
        return opened;
    }
    public int getKeyboardHeight(){
        return keyboardHeight;
    }
    public int getContentViewHeight(){
        return originVisibaleHeight - keyboardHeight;
    }
}
