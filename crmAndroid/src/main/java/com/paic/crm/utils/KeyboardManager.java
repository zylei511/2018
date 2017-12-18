package com.paic.crm.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by yueshaojun988 on 2017/9/6.
 */

public class KeyboardManager {
    private InputMethodManager inputManager;
    private Context mContext;
    public KeyboardManager(Context context){
        mContext = context;
        inputManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    public void showSoftInput(View view) {
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    public void hideSoftInput(){
        inputManager.hideSoftInputFromWindow(((Activity)mContext)
                .getCurrentFocus()
                .getWindowToken(),0);
    }
}
