package com.example.paic.uihelper.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by hanyh on 16/1/11.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> mDatas;
    protected LayoutInflater mLayoutInflater;
    protected int layoutId;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    public CommonAdapter(Context context, List<T> datas,int layoutId) {

        this.context = context;
        this.mDatas = datas;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.layoutId=layoutId;
    }
    public void changeData(List<T> datas){
        this.mDatas = datas;
        changeDataOnMainThread();
    }

    private void changeDataOnMainThread() {
        if(isOnMainThread()) {
            this.notifyDataSetChanged();
        }else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {

        return mDatas != null ? mDatas.get(position) : null;
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.get(context, convertView, parent, layoutId, position);
        convert(holder, getItem(position),position);

        return holder.getmConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);
    private boolean isOnMainThread(){
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }
}
