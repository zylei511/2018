package com.paic.crm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.paic.crm.android.R;
import com.paic.crm.callback.SendFailCallback;
import com.paic.crm.entity.H5InformationBean;
import com.paic.crm.entity.H5InformationContent;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DateFormatManager;
import com.paic.crm.utils.DateFormatUtil;
import com.paic.crm.utils.EmojiParser;
import com.paic.crm.utils.GlideCircleTransform;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.JSONStringUtil;
import com.paic.crm.utils.ParseEmojiMsgUtil;
import com.paic.crm.widget.CircleImageDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MsgHistoryAdapter extends BaseAdapter implements View.OnClickListener {
    private Context ccontext;
    private List<NewMessageBean> clist;
    private static final int MSG_FROM_MYSELF = 1;
    private static final int MSG_TO = 2;
    private boolean isJson = false;
    private SendFailCallback sendFailCallback;

    public SendFailCallback getSendFailCallback() {
        return sendFailCallback;
    }

    public void setSendFailCallback(SendFailCallback sendFailCallback) {
        this.sendFailCallback = sendFailCallback;
    }

    public MsgHistoryAdapter(Context context, List<NewMessageBean> list) {
        this.ccontext = context;
        this.clist = list;
    }

    public void changeData(List<NewMessageBean> list) {
        this.clist = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return clist != null ? clist.size() : 0;
    }

    @Override
    public int getViewTypeCount() {

        return 3;
    }

    @Override
    public int getItemViewType(int position) {

        String type = clist.get(position).customMsgContent.fromType;
        if ("2".equals(type)) {
            return MSG_TO;
        } else {
            return MSG_FROM_MYSELF;
        }
    }

    @Override
    public NewMessageBean getItem(int position) {
        return clist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * long lastTime = conversationBean.time;
     * if (lastTime != 0l) {
     * String date= DateFormatManager.Factory.create(DateFormatManager.Model.ChatList).format(
     * lastTime);
     * tvMsgTime.setText(date);
     * } else {
     * tvMsgTime.setText("");
     * }
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder = null;
        if (convertView == null) {
            hoder = new ViewHoder();
            int typeView = getItemViewType(position);
            switch (typeView) {

                case MSG_FROM_MYSELF:
                    convertView = LayoutInflater.from(ccontext).inflate(
                            R.layout.chat_room_item_right, null);
                    hoder.send_fail_btn = (ImageView) convertView.findViewById(R.id.send_fail_btn);
                    hoder.self_progress_bar = (ProgressBar) convertView.findViewById(R.id.self_progress_bar);
//                    convertView.setTag(hoder);
                    break;

                case MSG_TO:
                    convertView = LayoutInflater.from(ccontext).inflate(
                            R.layout.chat_room_item_left, null);
//                    convertView.setTag(hoder);
                    break;
            }

            hoder.dataTxt = (TextView) convertView
                    .findViewById(R.id.chat_room_date);

            hoder.headview = (ImageView) convertView
                    .findViewById(R.id.chat_room_headview);
            hoder.txtcontent = (TextView) convertView
                    .findViewById(R.id.chat_room_content);
            hoder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(hoder);
        }

        hoder = (ViewHoder) convertView.getTag();
        hoder.ivPic.setVisibility(View.GONE);
        hoder.txtcontent.setVisibility(View.GONE);
        NewMessageBean newMessageBean = clist.get(position);

        String msgType = newMessageBean.customMsgContent.msgType;
        String fromType = newMessageBean.customMsgContent.fromType;
        int state = newMessageBean.customMsgContent.msgState;
        if (state == Constants.MSG_STATE_SENDING) {
            if (hoder.self_progress_bar != null) {
                hoder.self_progress_bar.setVisibility(View.VISIBLE);
            }
            if (hoder.send_fail_btn != null) {
                hoder.send_fail_btn.setVisibility(View.GONE);
            }
        } else if (state == Constants.MSG_STATE_FAILED) {
            if (hoder.send_fail_btn != null && hoder.self_progress_bar != null) {
                hoder.self_progress_bar.setVisibility(View.GONE);
                hoder.send_fail_btn.setVisibility(View.VISIBLE);
                hoder.send_fail_btn.setTag(position);
                hoder.send_fail_btn.setOnClickListener(this);
            }
        } else if (state == Constants.MSG_STATE_SEND_SUCCESSFUL) {
            if (hoder.self_progress_bar != null) {
                hoder.self_progress_bar.setVisibility(View.GONE);
            }
        } else {
            if (hoder.self_progress_bar != null) {
                hoder.self_progress_bar.setVisibility(View.GONE);
            }
            if (hoder.send_fail_btn != null) {
                hoder.send_fail_btn.setVisibility(View.GONE);
            }
        }

        if (fromType.equals("3")) {
            Bitmap bitmap = BitmapFactory.decodeResource(ccontext.getResources(),R.drawable.kf);
            hoder.headview.setImageDrawable(new CircleImageDrawable(bitmap));
//            RequestManager glideRequest = Glide.with(ccontext);
//            glideRequest.load(newMessageBean.customMsgContent.customerIcon).transform(new GlideCircleTransform(ccontext)).placeholder(R.drawable.kf).into(hoder.headview);
        } else if (fromType.equals("2")) {
            RequestManager glideRequest = Glide.with(ccontext);
            glideRequest.load(newMessageBean.customMsgContent.customerIcon).transform(new GlideCircleTransform(ccontext)).placeholder(R.drawable.default_icon).into(hoder.headview);
        }
        long lastTime = Long.parseLong(newMessageBean.customMsgContent.createTime);
        if (lastTime != 0l) {
            String date = DateFormatManager.Factory.create(DateFormatManager.Model.ChatMessage).format(
                    lastTime, DateFormatManager.HoursFormat.H24);
//            String date = DateFormatUtil.format(lastTime, DateFormatUtil.HH_MM_24);
            hoder.dataTxt.setText(date);
            CRMLog.LogInfo("test", "lastTime   " + date + "lastTime   " + lastTime);
        } else {
//            hoder.dataTxt.setText("");
            CRMLog.LogInfo("test", "setText(\"\")   " + lastTime);
        }

        if ("image".equals(msgType) || "image/jpeg".equals(msgType)) {
            String msg = newMessageBean.customMsgContent.msg;
            String url ="";
            if (BizSeriesUtil.isKZKF(ccontext)){
                url = HttpUrls.UCP_PIC_PRIFEX_KZKF;
            } else {
                url = HttpUrls.UCP_PIC_PRIFEX;
            }
            String picUrl = url + msg;
            String picReplace = picUrl.replace("\"", "");
            hoder.ivPic.setVisibility(View.VISIBLE);

            Glide.with(ccontext)
                    .load(picReplace.trim())
                    .centerCrop()
                    .placeholder(R.drawable.pic_asyn)
                    .crossFade()
                    .into(hoder.ivPic);
            CRMLog.LogInfo(Constants.LOG_TAG, "picUrl   " + picReplace);
        } else if (msgType.equals("text") || "text/plain".equals(msgType)) {
            hoder.txtcontent.setVisibility(View.VISIBLE);
//            String unicode = EmojiParser.getInstance(ccontext).parseEmoji(newMessageBean.customMsgContent.msg);
//            SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(ccontext, unicode);
            hoder.txtcontent.setText(newMessageBean.mutiMsg);
        } else if (msgType.equals("h5")) {
            //TODO
            List<JSONObject> h5JosnList = new ArrayList<>();
            String json = newMessageBean.customMsgContent.msg;
            try {
                JSONObject wJson = new JSONObject(json);
                if (wJson.getString("type").equals("news")) {
                    hoder.txtcontent.setVisibility(View.VISIBLE);
                    hoder.txtcontent.setText("您刚刚发了一条资讯消息");
                } else {
                    h5JosnList = JSONStringUtil.getH5Json(json);
                    if (h5JosnList != null) {
                        String typeOf = h5JosnList.get(0).getString("type");
                        if (h5JosnList.get(0).getString("type").equals("image")) {
                            for (int i = 0; i < h5JosnList.size(); i++) {
                                JSONObject object = h5JosnList.get(i);
                                String msg = object.getString("msg");
                                String picUrl = HttpUrls.UCP_PIC_PRIFEX + msg;
                                String picReplace = picUrl.replace("\"", "");
                                String type = object.getString("type");
                                hoder.ivPic.setVisibility(View.VISIBLE);
                                Glide.with(ccontext)
                                        .load(picReplace.trim())
                                        .centerCrop()
                                        .placeholder(R.drawable.pic_asyn)
                                        .crossFade()
                                        .into(hoder.ivPic);
                                CRMLog.LogInfo(Constants.LOG_TAG, "picUrl   " + picReplace);
                            }
                        } else if (h5JosnList.get(0).getString("type").equals("sms")) {
                            hoder.txtcontent.setVisibility(View.VISIBLE);
                            String msg = h5JosnList.get(0).getString("msg");
                            String type = h5JosnList.get(0).getString("type");
                            hoder.txtcontent.setText(msg);
                            CRMLog.LogInfo(Constants.LOG_TAG, msg);

                        } else if (h5JosnList.get(0).getString("type").equals("template")) {
                            hoder.txtcontent.setVisibility(View.VISIBLE);
                            String msg = h5JosnList.get(0).getString("msg");
                            String type = h5JosnList.get(0).getString("type");
                            hoder.txtcontent.setText(msg);
                            CRMLog.LogInfo(Constants.LOG_TAG, msg);
                        } else if (typeOf.equals("txt")) {
                            hoder.txtcontent.setVisibility(View.VISIBLE);
                            String msg = h5JosnList.get(0).getString("msg");
                            String type = h5JosnList.get(0).getString("type");
                            hoder.txtcontent.setText(msg);
                            CRMLog.LogInfo(Constants.LOG_TAG, msg);
                        } else {
                            //TODO WEIXUN
                        }
                    }

                }
            } catch (Exception e) {
                CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
            }
        }


        return convertView;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        if (sendFailCallback != null) {
            sendFailCallback.sendFail(position);
        }
    }


    class ViewHoder {
        ImageView headview;
        ImageView ivPic;
        TextView txtcontent;
        TextView dataTxt;
        ImageView send_fail_btn;
        ProgressBar self_progress_bar;
    }

}
