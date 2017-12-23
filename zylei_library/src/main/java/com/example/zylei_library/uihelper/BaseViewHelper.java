package com.example.zylei_library.uihelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ex-zhangyuelei001 on 2017/12/21.
 */

public abstract class BaseViewHelper {

    private View childView;
    protected Context context;

    public BaseViewHelper(Context context, @NonNull ViewGroup parent) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        childView = onCreateView(inflater, parent);
        if (parent.getParent() != null){
            ((ViewGroup)parent.getParent()).removeAllViews();
        }
        parent.addView(childView);
    }

    protected abstract View onCreateView(LayoutInflater inflater, @NonNull ViewGroup parent);

    public View getContentView(){
        return childView;
    }
}
