package com.paic.crm.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.paic.crm.android.R;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.utils.DateFormatUtil;
import com.paic.crm.utils.ExpressionUtils;
import com.paic.crm.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by hanyh on 16/1/11.
 */
public class ConversationHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<ConversationBean> datas;
    int resId = R.drawable.default_icon;
    public ConversationHistoryAdapter(Context context, List<ConversationBean> datas) {
        mContext=context;
        this.datas=datas;
    }


    public void changeData(List<ConversationBean> datas){
        this.datas=datas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ConversationBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view ;
        ConversationBean conversationBean = datas.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation_history_layout,null);
            viewHolder.timeView = (TextView)view.findViewById(R.id.date_time);
            viewHolder.picView = (ImageView)view.findViewById(R.id.conversation_history_icon);
            viewHolder.contentView = (TextView)view.findViewById(R.id.msg_history_content);
            viewHolder.nameView = (TextView)view.findViewById(R.id.name_history);
            viewHolder.sourceView = (ImageView)view.findViewById(R.id.channel_history);
            viewHolder.createTimeView = (TextView)view.findViewById(R.id.msg_time_history);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        if(!"".equals(conversationBean.historyTime)&&null!=conversationBean.historyTime) {
            viewHolder.timeView.setVisibility(View.VISIBLE);
            viewHolder.timeView.setText(conversationBean.historyTime);
            Log.i("dataTime",viewHolder.timeView.getText().toString());
        }else {
            viewHolder.timeView.setVisibility(View.GONE);
            Log.i("dataTimeGone", viewHolder.timeView.getText().toString());
        }
        viewHolder.nameView.setText(conversationBean.imNickName);
        SpannableString spannableString = ExpressionUtils.dispatchExpression(mContext,conversationBean.msg);
        viewHolder.contentView.setText(spannableString);
        String sendTime = DateFormatUtil.format(conversationBean.sendTime,DateFormatUtil.HH_MM_24);
        viewHolder.createTimeView.setText(sendTime);
        String paImType = conversationBean.paImType;
        Log.i("paimtype", "sss" + paImType);
        switch (paImType) {
            case "01":
                viewHolder.sourceView.setImageResource(R.drawable.icon_channel_weixin);
                break;
            case "02":
                viewHolder.sourceView.setImageResource(R.drawable.icon_channel_pc);
                break;
            case "03":
                viewHolder.sourceView.setImageResource(R.drawable.icon_channel_message);
                break;
            case "09":
                viewHolder.sourceView.setImageResource(R.drawable.icon_phone);
                break;
            case "06":
                viewHolder.sourceView.setImageResource(R.drawable.icon_channel_world_tong);
                break;
            default:
                //默认用微信图标
                viewHolder.sourceView.setImageResource(R.drawable.icon_channel_weixin);
        }

        RequestManager glideRequest=Glide.with(mContext);
        glideRequest.load(conversationBean.customerIcon).transform(new GlideCircleTransform(mContext)).placeholder(resId).into(viewHolder.picView);

        return view;
    }
    class ViewHolder{
        TextView timeView;
        ImageView picView;
        TextView contentView;
        TextView nameView;
        ImageView sourceView;
        TextView createTimeView;
    }
}
