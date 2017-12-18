package com.paic.crm.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.paic.crm.android.R;
import com.paic.crm.common.CommonAdapter;
import com.paic.crm.common.ViewHolder;
import com.paic.crm.entity.KZKFChatMoreBean;

import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/11/8.
 */

public class ChatMoreAdapter extends CommonAdapter<KZKFChatMoreBean> {

    public ChatMoreAdapter(Context context, List<KZKFChatMoreBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, KZKFChatMoreBean chatMoreBean, int position) {
        ImageView imageView=(ImageView)holder.getView(R.id.iv_icon);
        TextView tvName=(TextView)holder.getView(R.id.tv_title);
        View dividerView=(View)holder.getView(R.id.divier_line);
        imageView.setImageDrawable(chatMoreBean.drawable);
        tvName.setText(chatMoreBean.title);
        dividerView.setVisibility(View.VISIBLE);
        if (mDatas.size()-1 == position){
            dividerView.setVisibility(View.GONE);
        }
    }
}
