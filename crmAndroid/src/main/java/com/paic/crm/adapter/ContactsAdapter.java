package com.paic.crm.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.paic.crm.android.R;
import com.paic.crm.callback.AdapterCallBack;
import com.paic.crm.entity.GroupMemberBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.GlideCircleTransform;
import com.paic.crm.utils.SpfUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yueshaojun
 */
public class ContactsAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<GroupMemberBean> contacts;
    int resId = R.drawable.default_icon;
    private AdapterCallBack adapterCallBack;
    private int section;
    private final static String ALLOW = "Y";
    private final static String NOT_ALLOW = "N";
    private String AUTHORITY_WEIXIN = "";
    private String AUTHORITY_SMS = "";
    public ContactsAdapter(Context context, List<GroupMemberBean> datas) {
        mContext=context;
        this.contacts=datas;
        AUTHORITY_WEIXIN = (String)SpfUtil.getValue(context,SpfUtil.WEIXIN_FLAG,"");
        AUTHORITY_SMS = (String)SpfUtil.getValue(context,SpfUtil.MSG_FLAG,"");
    }

    public void setAdapterCallBack(AdapterCallBack adapterCallBack){
        this.adapterCallBack = adapterCallBack;
    }
    public void changeData(List<GroupMemberBean> datas){
        this.contacts=datas;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public GroupMemberBean getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view ;
        viewHolder = new ViewHolder();
        GroupMemberBean groupMemberBean = contacts.get(position);
        if(convertView==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contacts,null);
            viewHolder.picView = (ImageView)view.findViewById(R.id.contacts_icon);
            viewHolder.nameView = (TextView)view.findViewById(R.id.contacts_name);
            viewHolder.wechatView = (ImageView)view.findViewById(R.id.channel_weixin);
            viewHolder.smsView = (ImageView)view.findViewById(R.id.channel_sms);
            viewHolder.tvLetter = (TextView)view.findViewById(R.id.catalog);
            viewHolder.channels_bar = (RelativeLayout)view.findViewById(R.id.channels_bar);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        String nameTxt;
        if("".equals(groupMemberBean.getNickName())||null==groupMemberBean.getNickName()){
            nameTxt = "游客";
        }else if("".equals(groupMemberBean.getName())||null==groupMemberBean.getName()){
            nameTxt = groupMemberBean.getNickName();
        }else {
            nameTxt = groupMemberBean.getNickName()+"("+groupMemberBean.getName()+")";
        }
        viewHolder.nameView.setText(nameTxt);

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(groupMemberBean.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        if(groupMemberBean.isShowChannelsBar()){
            viewHolder.channels_bar.setVisibility(View.VISIBLE);
        }else{
            viewHolder.channels_bar.setVisibility(View.GONE);
        }
        String flag_WX = groupMemberBean.getFlag().weixin;
        String flag_MSG = groupMemberBean.getFlag().notMessage;
        CRMLog.LogInfo("flag",flag_WX+"|"+AUTHORITY_WEIXIN+"|"+groupMemberBean.getName());
        CRMLog.LogInfo("flag_msg",flag_MSG+"|"+AUTHORITY_SMS+"|"+groupMemberBean.getName());
        if(ALLOW.equals(flag_WX)&&ALLOW.equals(AUTHORITY_WEIXIN)){
            viewHolder.wechatView.setEnabled(true);
            viewHolder.wechatView.setImageResource(R.drawable.icon_c_wechat_02);
            viewHolder.wechatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallBack.handleAdapterCallBack(v, position);
                }
            });
        }else {
            Log.i("weixin", "N");
            viewHolder.wechatView.setImageResource(R.drawable.icon_c_wechat_01);
            viewHolder.wechatView.setEnabled(false);
        }
        if(ALLOW.equals(AUTHORITY_SMS)) {
            viewHolder.smsView.setEnabled(true);
            viewHolder.smsView.setImageResource(R.drawable.icon_c_message_02);
            viewHolder.smsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterCallBack.handleAdapterCallBack(v, position);
                }
            });
        }else {
            viewHolder.smsView.setImageResource(R.drawable.icon_c_message_01);
            viewHolder.smsView.setEnabled(false);
        }
        RequestManager glideRequest= Glide.with(mContext);
        glideRequest.load(groupMemberBean.getClientLogo()).transform(new GlideCircleTransform(mContext)).placeholder(resId).into(viewHolder.picView);
        return view;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return contacts.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = contacts.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }
    @Override
    public Object[] getSections() {
        return null;
    }
    private JSONObject getIsAllowed(String json) {
        JSONObject flag = new JSONObject();
        try {
            JSONObject allowObject = new JSONObject(json);
            flag = allowObject.getJSONObject("flag");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return flag;
    }

    class ViewHolder{
        ImageView picView;
        TextView nameView;
        ImageView wechatView;
        ImageView smsView;
        TextView tvLetter;
        RelativeLayout channels_bar;
    }

}
