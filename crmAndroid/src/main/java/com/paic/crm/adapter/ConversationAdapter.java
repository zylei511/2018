package com.paic.crm.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.paic.crm.android.R;
import com.paic.crm.common.CommonAdapter;
import com.paic.crm.common.ViewHolder;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DateFormatManager;
import com.paic.crm.utils.ExpressionUtils;
import com.paic.crm.utils.GlideCircleTransform;
import com.pingan.paimkit.module.conversation.bean.ChatConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hanyh on 16/1/11.
 */
public class ConversationAdapter extends CommonAdapter<ConversationBean> {

    private Context mContext;
    private List<ConversationBean> datas;
    private boolean isDb;
    public ConversationAdapter(Context context, List<ConversationBean> datas, int layoutId) {
        super(context, datas, layoutId);
        mContext=context;
        this.datas=datas;
    }
    private Handler handler = new Handler();


    public void changeDatas(List<ConversationBean> datas){

        this.datas=datas;
        super.changeData(this.datas);
    }

    public List<ChatConversation> filterList(List<ChatConversation> list) {
        List<ChatConversation> list1 = new ArrayList<ChatConversation>();
        List<ChatConversation> list2 = new ArrayList<ChatConversation>();
        List<ChatConversation> list3 = new ArrayList<ChatConversation>();
        for (ChatConversation conver : list) {
            if (conver.getStickTime() > 0) {
                list1.add(conver);
            } else {
                list2.add(conver);
            }
        }
        Collections.sort(list1);
        Collections.sort(list2);
        list3.addAll(list1);
        list3.addAll(list2);
        return list3;
    }
/**
     *  只需重写次方法
 * @param holder
 * @param conversationBean
 * @param position
 */

    @Override
    public void convert(ViewHolder holder, ConversationBean conversationBean, int position) {

        // 默认聊天图像
        int resId = R.drawable.default_icon;
        if (Constants.STATE_CONVERSATION_STATUS_Y.equals(conversationBean.dialogueStatus)
                && Constants.STATE_CONVERSATION_INVAILD.equals(conversationBean.status)){
            resId = R.mipmap.head_disable;
        }
        ImageView imageView=holder.getView(R.id.iv_conversation_icon);
        ImageView tv_channel=holder.getView(R.id.tv_channel);
        TextView redDot=holder.getView(R.id.iv_conversation_dot);
        TextView tvName=holder.getView(R.id.tv_name);
        final TextView tvMsgContent=holder.getView(R.id.tv_msg_content);
        TextView tvMsgTime=holder.getView(R.id.tv_msg_time);
        int unReadCount=conversationBean.unReadCount;
        if (unReadCount==0){
            redDot.setVisibility(View.GONE);
        }else {
            redDot.setVisibility(View.VISIBLE);
            redDot.setText(unReadCount + "");
        }
        if (conversationBean.paImType.equals(Constants.CHANNEL_SMS)){
            tv_channel.setImageResource(R.drawable.icon_channel_message);
        }else if (conversationBean.paImType.equals(Constants.CHANNEL_WEIXIN)){
            tv_channel.setImageResource(R.drawable.icon_channel_weixin);
        }else if (conversationBean.paImType.equals(Constants.CHANNEL_WEB)){
            tv_channel.setImageResource(R.drawable.icon_channel_pc);
        }else if (conversationBean.paImType.equals(Constants.CHANNEL_APP_IM)){
            tv_channel.setImageResource(R.drawable.icon_phone);
        }else if (conversationBean.paImType.equals(Constants.CHANNEL_WORLD)){
            tv_channel.setImageResource(R.drawable.icon_channel_world_tong);
        }
        RequestManager glideRequest=Glide.with(context);
        glideRequest.load(conversationBean.customerIcon).transform(new GlideCircleTransform(context)).placeholder(resId).into(imageView);
        CRMLog.LogInfo("cusName", "" + conversationBean.imNickName);
        tvName.setText(conversationBean.imNickName);
        final String content = conversationBean.msg;
        //子线程中处理耗时的表情处理，然后异步post到主线程中
        // TODO: 2017/11/22 待处理
        new Thread(new Runnable() {
            @Override
            public void run() {
                CRMLog.LogInfo("Thread", "in" + Thread.currentThread().getId() + "");
                final SpannableString spannableString = ExpressionUtils.dispatchExpression(mContext, content);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        CRMLog.LogInfo("Thread", Thread.currentThread().getId() + "");
                        tvMsgContent.setText(spannableString);
                    }
                });
            }
        }).start();

        long lastTime = Long.parseLong(conversationBean.createTime);
        if (lastTime != 0l) {
           String date= DateFormatManager.Factory.create(DateFormatManager.Model.ChatList).format(
                    lastTime);
            tvMsgTime.setText(date);
        } else {
            tvMsgTime.setText("");
        }

    }



}
