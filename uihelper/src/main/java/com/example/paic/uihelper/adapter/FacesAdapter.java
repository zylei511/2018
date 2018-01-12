package com.example.paic.uihelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.paic.R;
import com.example.paic.uihelper.entity.MsgFaceModle;

import java.util.List;


public class FacesAdapter extends BaseAdapter {

    private List<MsgFaceModle> data;

    private LayoutInflater inflater;

    private int size=0;

    public FacesAdapter(Context context, List<MsgFaceModle> list) {
        this.inflater=LayoutInflater.from(context);
        this.data=list;
        this.size=list.size();
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	MsgFaceModle emoji=data.get(position);
        ViewHolder viewHolder=null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.crm_sdk_face_item, null);
            viewHolder.iv_face=(ImageView)convertView.findViewById(R.id.face_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if(emoji.getId() == R.drawable.crm_sdk_face_delete_select) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageResource(emoji.getId());
        } else if(TextUtils.isEmpty(emoji.getCharacter())) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_face.setImageDrawable(null);
        } else {
            viewHolder.iv_face.setTag(emoji);
            viewHolder.iv_face.setImageResource(emoji.getId());
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView iv_face;
    }
}