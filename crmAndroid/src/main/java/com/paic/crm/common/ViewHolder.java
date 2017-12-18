package com.paic.crm.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by hanyh on 16/1/11.
 */
public class ViewHolder {

    private SparseArray<View> views;
    private int position;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {

        this.position = position;
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.views = new SparseArray<>();
        this.mConvertView.setTag(this);
    }


    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.position = position;
            return holder;
        }

    }

    public <T extends View> T getView(int viewId) {

        View view = null;

        view = this.views.get(viewId);

        if (view == null) {

            view = mConvertView.findViewById(viewId);
            this.views.put(viewId, view);
        }
        return (T) view;
    }


    //需要完善下类型转换的判断
    public ViewHolder setText(int viewId, String txt) {

        TextView tv = getView(viewId);
        tv.setText(txt);
        return this;
    }

    public View getmConvertView() {
        return mConvertView;
    }
}
