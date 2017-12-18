package com.paic.crm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.paic.crm.android.R;
import com.paic.crm.callback.SendFailCallback;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.H5InformationContent;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DateFormatManager;
import com.paic.crm.utils.GlideCircleTransform;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.JSONStringUtil;
import com.paic.crm.widget.CircleImageDrawable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends BaseAdapter implements View.OnClickListener,View.OnLongClickListener{
    private Context ccontext;
    private List<NewMessageBean> clist;
    public static final int MSG_FROM_MYSELF = 1;
    public static final int MSG_TO = 2;
    public static final int MSG_SYS = 3;
    private boolean isJson = false;
    private SendFailCallback sendFailCallback;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    public SendFailCallback getSendFailCallback() {
        return sendFailCallback;
    }
    public void setSendFailCallback(SendFailCallback sendFailCallback) {
        this.sendFailCallback = sendFailCallback;
    }

    public ChatAdapter(Context context, List<NewMessageBean> list) {
        this.ccontext = context;
        this.clist = list;
    }

    public void changeData(List<NewMessageBean> list) {
        this.clist = list;
        if(Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()){
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
        return clist != null ? clist.size() : 0;
    }

    @Override
    public int getViewTypeCount() {

        return 4;
    }

    @Override
    public int getItemViewType(int position) {

        String type = clist.get(position).customMsgContent.fromType;
        if ("3".equals(type)) {
            return MSG_FROM_MYSELF;
        } else if ("2".equals(type)) {
            return MSG_TO;
        }
        return MSG_SYS;
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
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder = null;
        SysHoder sysHoder = null;
        if (convertView == null) {
            hoder = new ViewHoder();
            int typeView = getItemViewType(position);
            switch (typeView) {

                case MSG_FROM_MYSELF:
                    convertView = LayoutInflater.from(ccontext).inflate(
                            R.layout.chat_room_item_right, null);
                    hoder.send_fail_btn = (ImageView) convertView.findViewById(R.id.send_fail_btn);
                    hoder.mix_pic = (ImageView) convertView.findViewById(R.id.mix_pic);
                    hoder.mix_title = (TextView) convertView.findViewById(R.id.mix_title);
                    hoder.mix_content = (TextView) convertView.findViewById(R.id.mix_content);
                    hoder.self_progress_bar = (ProgressBar) convertView.findViewById(R.id.self_progress_bar);
                    hoder.mix_view = (RelativeLayout) convertView.findViewById(R.id.mix_view);
                    hoder.dataTxt = (TextView) convertView
                            .findViewById(R.id.chat_room_date);

                    hoder.headview = (ImageView) convertView
                            .findViewById(R.id.chat_room_headview);
                    hoder.txtcontent = (TextView) convertView
                            .findViewById(R.id.chat_room_content);
                    hoder.txtcontent.setOnLongClickListener(this);
                    hoder.txtcontent.setTag(MSG_FROM_MYSELF);
                    hoder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                    convertView.setTag(hoder);
                    break;

                case MSG_TO:
                    convertView = LayoutInflater.from(ccontext).inflate(
                            R.layout.chat_room_item_left, null);
                    hoder.dataTxt = (TextView) convertView
                            .findViewById(R.id.chat_room_date);

                    hoder.headview = (ImageView) convertView
                            .findViewById(R.id.chat_room_headview);
                    hoder.txtcontent = (TextView) convertView
                            .findViewById(R.id.chat_room_content);
                    hoder.txtcontent.setOnLongClickListener(this);
                    hoder.txtcontent.setTag(MSG_TO);
                    hoder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                    convertView.setTag(hoder);
                    break;
                case MSG_SYS:
                    sysHoder = new SysHoder();
                    convertView = LayoutInflater.from(ccontext).inflate(
                            R.layout.sys_note, null);
                    TextView txt_sys_note = (TextView) convertView.findViewById(R.id.txt_sys_note);
                    TextView chat_room_date = (TextView) convertView.findViewById(R.id.chat_room_date);
                    sysHoder.txt_sys_note = txt_sys_note;
                    sysHoder.chat_room_date = chat_room_date;
                    convertView.setTag(sysHoder);

            }

        }


        if (convertView.getTag() != null && convertView.getTag() instanceof SysHoder) {
            sysHoder = (SysHoder) convertView.getTag();
            NewMessageBean newMessageBean = clist.get(position);
            long lastTime = Long.parseLong(newMessageBean.customMsgContent.createTime);
            if (position == 0) {
                if (lastTime != 0l) {
                    String date = DateFormatManager.Factory.create(DateFormatManager.Model.ChatMessage).format(
                            lastTime, DateFormatManager.HoursFormat.H24);
                    sysHoder.chat_room_date.setText(date);
                }
            }else {
                CustomMsgContent preCustomMsg = clist.get(position - 1).customMsgContent;
                long preTime = Long.parseLong(preCustomMsg.createTime);
                long intervalTime = lastTime - preTime;
                CRMLog.LogDebg(Constants.LOG_TAG, "intervalTime   " + intervalTime);
                if (intervalTime >= 180000) {
                    String date = DateFormatManager.Factory.create(DateFormatManager.Model.ChatMessage).format(
                            lastTime, DateFormatManager.HoursFormat.H24);
                    sysHoder.chat_room_date.setText(date);
                } else {
                    CRMLog.LogDebg(Constants.LOG_TAG, "else  intervalTime   " + intervalTime);
                    sysHoder.chat_room_date.setVisibility(View.GONE);
                }
            }

            sysHoder.txt_sys_note.setText(newMessageBean.customMsgContent.msg);
            return convertView;
        } else if (convertView.getTag() != null && convertView.getTag() instanceof ViewHoder) {
            hoder = (ViewHoder) convertView.getTag();
            if (hoder.mix_view != null) {
                hoder.mix_view.setVisibility(View.GONE);
            }
            hoder.ivPic.setVisibility(View.GONE);
            hoder.txtcontent.setVisibility(View.GONE);

            NewMessageBean newMessageBean = clist.get(position);
            String msgType = newMessageBean.customMsgContent.msgType;
            String fromType = newMessageBean.customMsgContent.fromType;
            int state = newMessageBean.customMsgContent.msgState;
            CRMLog.LogInfo("STATE","MSG:"+newMessageBean.customMsgContent.msg+"||"+state+"||"+newMessageBean.customMsgContent.messageId+"||"+newMessageBean.customMsgContent.id);
            switch (state){
                case Constants.MSG_STATE_SENDING:
                    if (hoder.self_progress_bar != null) {
                        hoder.self_progress_bar.setVisibility(View.VISIBLE);
                    }
                    if (hoder.send_fail_btn != null) {
                        hoder.send_fail_btn.setVisibility(View.GONE);
                    }
                    break;

                case Constants.MSG_STATE_FAILED:
                    if (hoder.send_fail_btn != null && hoder.self_progress_bar != null) {
                        hoder.self_progress_bar.setVisibility(View.GONE);
                        hoder.send_fail_btn.setVisibility(View.VISIBLE);
                        hoder.send_fail_btn.setTag(position);
                        hoder.send_fail_btn.setOnClickListener(this);
                    }
                    break;

                case Constants.MSG_STATE_SEND_SUCCESSFUL:
                    if (hoder.self_progress_bar != null) {
                        hoder.self_progress_bar.setVisibility(View.GONE);
                    }

                    if (hoder.send_fail_btn != null) {
                        hoder.send_fail_btn.setVisibility(View.GONE);
                    }
                    break;

                default:
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
            } else if (fromType.equals("2")) {
                RequestManager glideRequest = Glide.with(ccontext);
                glideRequest.load(newMessageBean.customMsgContent.customerIcon).transform(new GlideCircleTransform(ccontext)).placeholder(R.drawable.default_icon).into(hoder.headview);
            }
            long lastTime = Long.parseLong(newMessageBean.customMsgContent.createTime);
            if (position == 0) {
                if (lastTime != 0l) {
                    String date = DateFormatManager.Factory.create(DateFormatManager.Model.ChatMessage).format(
                            lastTime, DateFormatManager.HoursFormat.H24);
                    hoder.dataTxt.setText(date);
                }
            } else {
                CustomMsgContent preCustomMsg = clist.get(position - 1).customMsgContent;
                long preTime = Long.parseLong(preCustomMsg.createTime);
                long intervalTime = lastTime - preTime;
                CRMLog.LogDebg(Constants.LOG_TAG, "intervalTime   " + intervalTime);
                if (intervalTime > 180000) {
                    String date = DateFormatManager.Factory.create(DateFormatManager.Model.ChatMessage).format(
                            lastTime, DateFormatManager.HoursFormat.H24);
                    hoder.dataTxt.setVisibility(View.VISIBLE);
                    hoder.dataTxt.setText(date);
                } else {
                    hoder.dataTxt.setVisibility(View.GONE);
                }
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
                CRMLog.LogInfo("picUrl",picReplace);
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
                CharSequence showText =
                        TextUtils.isEmpty(newMessageBean.mutiMsg)?
                                newMessageBean.customMsgContent.msg:newMessageBean.mutiMsg;
                hoder.txtcontent.setText(showText);
            } else if (msgType.equals("h5")) {
                List<JSONObject> h5JosnList = new ArrayList<>();
                String json = newMessageBean.customMsgContent.msg;
                int msgState = newMessageBean.customMsgContent.msgState;
                CRMLog.LogInfo(Constants.LOG_TAG, "news--->" + json);
                CRMLog.LogInfo(Constants.LOG_TAG, "msgState--->" + msgState);
                try {
                    JSONObject wJson = new JSONObject(json);
                    if (wJson.getString("type").equals("news")) {
                        H5InformationContent h5InformationContent = CommonUtils.handleHttpResult(H5InformationContent.class, json);
                        if (hoder.mix_view != null && hoder.mix_title != null && hoder.mix_pic != null && hoder.mix_content != null) {
                            hoder.mix_view.setVisibility(View.VISIBLE);
                            hoder.mix_title.setText(h5InformationContent.description);
                            hoder.mix_content.setText(h5InformationContent.typeContent.data.get(0).description);
                            Glide.with(ccontext)
                                    .load(h5InformationContent.typeContent.data.get(0).picurl)
                                    .centerCrop()
                                    .placeholder(R.drawable.pic_asyn)
                                    .crossFade()
                                    .into(hoder.mix_pic);
                        }
                        hoder.txtcontent.setVisibility(View.GONE);
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
                                CRMLog.LogInfo("sfsfsssss","char->>>"+msg);
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
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        if (sendFailCallback != null) {
            sendFailCallback.sendFail(position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if((int)v.getTag()==MSG_FROM_MYSELF){
            v.setBackgroundResource(R.drawable.bg_chat_box_r);
        }else if((int)v.getTag()==MSG_TO){
            v.setBackgroundResource(R.drawable.bg_chat_box_l);
        }
        return false;
    }

    class ViewHoder {
        ImageView headview;
        ImageView ivPic;
        ImageView mix_pic;
        TextView txtcontent;
        TextView dataTxt;
        TextView mix_title;
        TextView mix_content;
        ImageView send_fail_btn;
        ProgressBar self_progress_bar;
        RelativeLayout mix_view;
    }

    class SysHoder {
        TextView txt_sys_note;
        TextView chat_room_date;
    }
}
