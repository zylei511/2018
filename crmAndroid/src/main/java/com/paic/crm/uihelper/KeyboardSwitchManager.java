package com.paic.crm.uihelper;

import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import com.paic.crm.utils.KeyboardChangeHelper;

/**
 * Created by yueshaojun988 on 2017/9/19.
 */

public class KeyboardSwitchManager {
    private KeyboardChangeHelper mHelper;
    private Context mContext;
    private View mContentView;
    private KeyboardSwitchManager(Builder builder){
        mContext = builder.context;
        mContentView = builder.contentView;
        mHelper = new KeyboardChangeHelper(mContext);
    }
    public void lockContentHeight(){
        checkLegal();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        lp.height = mContentView.getHeight();
        lp.weight = 0.0f;
        mContentView.setLayoutParams(lp);
    }
    public void unLockContentHeight(){
        checkLegal();
        mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
                lp.weight = 1.0f;
            }
        },200L);
    }
    public static class Builder{
        private Context context;
        private View contentView;
        public Builder with(Context context){
            this.context = context;
            return this;
        }
        public Builder bindContentView(View contentView){
            this.contentView = contentView;
            return this;
        }
        public KeyboardSwitchManager build(){
            return new KeyboardSwitchManager(this);
        }
    }
    private void checkLegal(){
        boolean legal = mContentView.getParent() instanceof LinearLayout;
        if(!legal){
            throw new IllegalStateException("contentView must be instanceof LinearLayout!");
        }
        boolean isOnMainThread =
                Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
        if (!isOnMainThread){
            throw new IllegalThreadStateException();
        }
    }

    public void setKeyboardChangeListener(KeyboardChangeHelper.KeyBoardListener l){
        mHelper.setKeyboardListener(l);
    }
    public int getkeyboardHeight(){
        return mHelper.getKeyboardHeight();
    }
    public boolean isOpened(){
        return mHelper.isOpened();
    }
    public void destroy(){
        mHelper.destroy();
    }

}
