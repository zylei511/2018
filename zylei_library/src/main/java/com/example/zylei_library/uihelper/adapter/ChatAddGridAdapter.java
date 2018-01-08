package com.example.zylei_library.uihelper.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.entity.ChatAddBean;

import java.util.List;

/**
 * Created by pingan001 on 16/2/18.
 */
public class ChatAddGridAdapter extends CommonAdapter<ChatAddBean>{

    public ChatAddGridAdapter(Context context, List<ChatAddBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }
    
    @Override
    public void convert(ViewHolder holder, ChatAddBean chatAddBean, int position) {
        ImageView imageView=(ImageView)holder.getView(R.id.iv_grid);
        TextView tvName=(TextView)holder.getView(R.id.tv_chat_add_name);
        imageView.setImageResource(chatAddBean.getIconRes());
        tvName.setText(chatAddBean.getIconName());
    }
}
