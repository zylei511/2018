package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * 键盘监听器
 */
public class KeyboardChangeHelper implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = "ListenerHandler";
    private View mContentView;
    private int mPreHeight;
    private boolean isChanged = false;
    private List<KeyBoardListener> listeners = new ArrayList<>();
    private int mValve;

    public interface KeyBoardListener {
        void onKeyboardOpened();
        void onKeyboardClosed();
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        listeners.add(keyBoardListen);
    }

    public KeyboardChangeHelper(Activity contextObj,int valve) {
        mValve = valve;
        if (contextObj == null) {
            Log.i(TAG, "contextObj is null");
            return;
        }
        mContentView = findContentView(contextObj);
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
        Log.i(TAG, "onGlobalLayout");
        if (currHeight == 0) {
            Log.i(TAG, "currHeight is 0");
            return;
        }
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
        } else {
            int dert =mPreHeight - currHeight;
            Log.i(TAG, "value:"+mPreHeight+"||"+currHeight+"||"+rect.bottom+"||"+rect.top+"||"+mValve);
            if(!isChanged&&Math.abs(dert)>mValve&&dert>0){
                isChanged = true;
                notifyKeyboardOpened();
                Log.i(TAG, "ischanged"+isChanged);
            }else if(isChanged&&Math.abs(dert)>mValve&&dert<0){
                isChanged = false;
                notifyKeyboardCloseed();
                Log.i(TAG, "ischanged"+isChanged);
            }
            mPreHeight = currHeight;
        }
    }

    public void destroy() {
        if (mContentView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }
    private void notifyKeyboardOpened(){
        for(KeyBoardListener listener :listeners){
            listener.onKeyboardOpened();;
        }
    }
    private void notifyKeyboardCloseed(){
        for(KeyBoardListener listener :listeners){
            listener.onKeyboardClosed();
        }
    }
}
