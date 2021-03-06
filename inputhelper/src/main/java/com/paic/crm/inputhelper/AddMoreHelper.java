package com.paic.crm.inputhelper;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.paic.crm.inputhelper.adapter.ChatAddGridAdapter;
import com.paic.crm.inputhelper.entity.ChatAddBean;

import java.util.ArrayList;
import java.util.List;


public class AddMoreHelper implements AdapterView.OnItemClickListener {

    private OnMoreItemClickListener itemClickListener;
    List<ChatAddBean> addBeanList = new ArrayList<>();

    private AddMoreHelper() {
    }

    public static AddMoreHelper getInstance() {
        return AddMoreHelperHolder.addMoreHelper;
    }

    static class AddMoreHelperHolder{
        static AddMoreHelper addMoreHelper = new AddMoreHelper();
    }

    public AddMoreHelper addView(Context context, GridView gridview) {
        ChatAddGridAdapter addGridAdapter = new ChatAddGridAdapter(context,
                getDatas(), R.layout.item_chat_add_grid_layout);
        gridview.setAdapter(addGridAdapter);
        gridview.setOnItemClickListener(this);
        return this;
    }

    public void clearData(){
        getDatas().clear();
        itemClickListener = null;
    }

    public AddMoreHelper setDatas(List<ChatAddBean> addBeanList){
        this.addBeanList = addBeanList;
        return this;
    }

    private List<ChatAddBean> getDatas() {
        if (addBeanList == null){
            throw new IllegalArgumentException("user must setDatas() before use addView(context,gridview)");
        }
        return addBeanList;
    }

    public void setOnAddMoreItemClickListener(OnMoreItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        itemClickListener.onMoreItemClick(parent,view,position,id);
    }

    public interface OnMoreItemClickListener {
        void onMoreItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
