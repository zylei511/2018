package com.paic.crm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.paic.crm.app.ChatAddHolder;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.app.ChatAddHolder;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.entity.ChatAddBean;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.H5InformationBean;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.entity.SendMessageBean;
import com.paic.crm.ui.SplashActivity;
import com.paic.crm.widget.AlertView;
import com.paic.crm.widget.OnItemClickListener;
import com.pingan.core.im.parser.JidManipulator;
import com.pingan.paimkit.common.userdata.PMDataManager;
import com.pingan.paimkit.module.chat.ChatConstant;
import com.pingan.paimkit.module.chat.bean.message.BaseChatMessage;
import com.pingan.paimkit.module.contact.bean.FriendsContact;
import com.pingan.paimkit.module.login.manager.PMLoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by hanyh on 16/1/22.
 */
public class CommonUtils {


    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }


    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                boolean hide = (double) displayHeight / height > 0.8;
                listener.onSoftKeyBoardChange(height - displayHeight, !hide);

            }
        });
    }


    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }


    /**
     * 将一个List按照固定的大小拆成很多个小的List
     *
     * @param listObj  需要拆分的List
     * @param groupNum 每个List的最大长度
     * @return 拆分后的List的集合
     */
    public static <T> List<List<T>> getSubList(List<T> listObj, int groupNum) {
        List<List<T>> resultList = new ArrayList<List<T>>();
        // 获取需要拆分的List个数
        int loopCount = (listObj.size() % groupNum == 0) ? (listObj.size() / groupNum)
                : ((listObj.size() / groupNum) + 1);
        // 开始拆分
        for (int i = 0; i < loopCount; i++) {
            // 子List的起始值
            int startNum = i * groupNum;
            // 子List的终止值
            int endNum = (i + 1) * groupNum;
            // 不能整除的时候最后一个List的终止值为原始List的最后一个
            if (i == loopCount - 1) {
                endNum = listObj.size();
            }
            // 拆分List
            List<T> listObjSub = listObj.subList(startNum, endNum);
            // 保存差分后的List
            resultList.add(listObjSub);
        }
        return resultList;

    }


    public static String getH5P(String key, String umId) {
//        String key = "83519aa6d30ecdc3";// "paic1234";
        String dec = null;
        String decrypt = null;
        long timestamp = new Date().getTime() + 1000 * 60 * 10;
        CRMLog.LogInfo(Constants.LOG_TAG, timestamp + "");
        long nonce = new Random().nextInt(100000);
        String toSign = timestamp + "" + nonce;
        System.out.println("signature：" + toSign + "2");
        String signature = HmacSHA1Utils.getSignature(toSign, key);
        System.out.println("加密signature：" + signature + "3");
        signature = signature.replaceAll("\r|\n", "");
        System.out.println("加密signature：" + signature + "1");
        String content = "timestamp=" + timestamp + "&nonce=" + nonce + "&signature=" + signature + "&umId=" + umId;//&customerId=170B3FA426736D29E0530F0A1F0AA100";
        // 加密字符串
        // String content="abc";
        System.out.println("加密前的：" + content + "5");
        System.out.println("加密密钥：" + key);
        // 加密方法
        String enc = Aes128CbcUtils.encrypt(content, key);
        System.out.println("加密前的：" + enc + "4");
        enc = enc.replaceAll("\r|\n", "");
        //URL encode
        try {
            enc = URLEncoder.encode(enc, Constants.CHARSET_NAME);

            System.out.println("URL encode：" + enc);
            //URL decode
            dec = URLDecoder.decode(enc, Constants.CHARSET_NAME);
            System.out.println("URL decode：" + dec);
            // 解密方法
            decrypt = Aes128CbcUtils.decrypt(dec, key);
            System.out.println("解密后的内容：" + dec);
            System.out.println("解密后的内容：" + decrypt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enc;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception ex) {

        }
        return 1.0f;
    }

    public static int calcOfProportionWidth(Context context, int realTotalWidth, int realWidth) {
        return realWidth * getScreenWidth(context) / realTotalWidth;
    }

    public static int calcOfProportionHeight(Context context, int realTotalHeight, int realHeight) {
        return realHeight * getScreenHeight(context) / realTotalHeight;
    }

    /**
     * 获取当前应用版本名 [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     */
    public static String getLocalVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pinfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = "V" + pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取顶部状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusHeigth(Activity activity) {
        Rect rectgle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int statusBarHeight = rectgle.top;
        return statusBarHeight;
    }

    /**
     * 获取显示内容（除去状态栏）的高度
     * @param activity
     * @return
     */
    public static int getContentViewHeight(Activity activity){
        Rect contentViewRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(contentViewRect);
        return contentViewRect.bottom-contentViewRect.top;
    }
    /**
     * 设置背景透明度
     *
     * @param activity
     * @param alpha
     */
    public static void setBackGroundAlpha(Activity activity,float alpha){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }
    /**
     * 设置视图背景透明度
     *
     * @param v
     * @param alpha
     */
    public static void setViewBackGroundAlpha(View v,float alpha){
        v.setAlpha(alpha);
    }
    /**
     * 当前是 单聊 群 公众号 等
     */
    public static String getContactTypeByJid(String jid) {
        if (TextUtils.isEmpty(jid)) {
            return null;
        }
        JidManipulator jidManipulator = JidManipulator.Factory.create();
        String serverName = jidManipulator.getServerName(jid);

        if (PMDataManager.getInstance().getConferenceHost().equals(serverName)) {
            // 讨论组
            return ChatConstant.ChatType.CAHT_CONTACT_GROUP;
        } else if (PMDataManager.getInstance().getPublicSpaceName()
                .equals(serverName)) {
            // 公共账号
            return ChatConstant.ChatType.CAHT_CONTACT_PUBLICACCOUNT;
        } else if (PMDataManager.getInstance().getServerName()
                .equals(serverName)) {
            // 好友聊天
            return ChatConstant.ChatType.CAHT_CONTACT_FRIENDS;
        }

        return null;
    }


    /**
     * 程序是否在前台运行 * * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;

    }

    public static <T> T handleHttpResult(Class<T> cls, Object obj) {

        try {
            String objs = (String) obj;
            T t = new Gson().fromJson(objs, cls);
            return t;
        } catch (Exception e) {
            return null;
        }

    }
    /**
     * 获得联系人的备注名【无就是昵称】
     *
     * @name hanyh
     */
    public static String getContactMarkOrNickName(FriendsContact contact) {
        if (contact == null) {
            return "";
        }
        String nick = contact.getRemarkName();
        if (TextUtils.isEmpty(nick)) {
            nick = contact.getNickname();
        }
        return nick;
    }

    // 隐藏软键盘
    public static void hideInputManager(Context ct) {
        try {
            ((InputMethodManager) ct.getSystemService(ct.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
                    .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("test", "hideInputManager Catch error,skip it!", e);
        }
    }


    public static void hideInputManager(Context ct,View v){
        if(null!=v) {
            ((InputMethodManager) ct.getSystemService(ct.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }else{
            CRMLog.LogInfo("hideInputManager","hide defeat");
        }
    }
    /**
     * 清除缓存数据
     * @param context
     * */
    public static void clearData(Context context){
        //退出社交云
        PMLoginManager.getInstance().loginOut();
        //清除内存
        SpfUtil.remove(context, SpfUtil.UMID);
        SpfUtil.remove(context, SpfUtil.SHIROKEY);
        SpfUtil.remove(context, SpfUtil.UM_NAME);
        SpfUtil.remove(context, SpfUtil.UM_PHONE);
        SpfUtil.remove(context, SpfUtil.UM_SEX);
        SpfUtil.remove(context, SpfUtil.LOGIN_FLAG);
        SpfUtil.remove(context, SpfUtil.IM_FLAG);
        SpfUtil.remove(context, SpfUtil.EMAIL_FLAG);
        SpfUtil.remove(context, SpfUtil.MSG_FLAG);
        SpfUtil.remove(context, SpfUtil.WEIXIN_FLAG);
        SpfUtil.remove(context, SpfUtil.MSO_FLAG);
        SpfUtil.remove(context, SpfUtil.BIZ_CHANNEL);
        SpfUtil.remove(context, SpfUtil.APP_STATUS);
        //清除缓存
        CrmDaoHolder.getInstance().getChatAddBeanDao().clearChatData();
        Intent intent = new Intent();
        intent.setClass(context, SplashActivity.class);
        //退出所有activity
        ActivityCollector.finishAllActivity();
        context.startActivity(intent);

    }

    /**
     * 抢登处理
     * */
    public static void exitWhenHasLogIn(final Context context){
        OnItemClickListener onItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                CommonUtils.clearData(context);
//                (Activity)context.finish();
            }
        };
        AlertView alertView = new AlertView("登陆异常", "您的账号已在别处登录！", null, new String[]{"确定"}, null, (Activity)context, AlertView.Style.Alert, onItemClickListener);
        alertView.show();
    }

    public static NewMessageBean handleInformationBean(H5InformationBean h5InformationBean) {


        CustomMsgContent customMsgContent = new CustomMsgContent();
        customMsgContent.msgType = "h5";
        customMsgContent.customerName = h5InformationBean.to.name;
        customMsgContent.customerIcon = h5InformationBean.to.portrait;
        customMsgContent.customerId = h5InformationBean.to.customerId;
        String msg = new Gson().toJson(h5InformationBean);
        customMsgContent.msg = msg;
        customMsgContent.paImType = h5InformationBean.to.source;
        customMsgContent.umId = h5InformationBean.content.typeContent.umid;
        customMsgContent.createTime = System.currentTimeMillis() + "";
        CrmDaoHolder.getInstance().getCustomMsgContentDao().add(customMsgContent);
        NewMessageBean bean = new NewMessageBean();
        bean.setType(Constants.MSG_MY);
        bean.customMsgContent = customMsgContent;
        return bean;
    }

    public static NewMessageBean processNewMsgData(BaseChatMessage baseChatMessage) {
        String json = new Gson().toJson(baseChatMessage);
        try {
            JSONObject object = new JSONObject(json);
            NewMessageBean msgBean = CommonUtils.handleHttpResult(NewMessageBean.class, object.toString());

//            String msgContent = msgBean.msgContent;
            String msgContent = msgBean.mHyperLinkText;
            msgBean.customMsgContent = CommonUtils.handleHttpResult(CustomMsgContent.class, msgContent);
            if (msgBean.msgProto == 0) {
                msgBean.setType(Constants.MSG_MY);
                msgBean.customMsgContent.messageId = msgBean.msgPacketId;
//                msgBean.customMsgContent.fromType = "3";
            } else {
                msgBean.setType(Constants.MSG_NULL);
//                msgBean.customMsgContent.fromType = "2";
            }
            return msgBean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static NewMessageBean processNewMsgData(CustomMsgContent newMsg) {
        NewMessageBean msgBean = new NewMessageBean();
        msgBean.customMsgContent = newMsg;
        return msgBean;
    }

    public static NewMessageBean processDataSend(BaseChatMessage baseChatMessage) {
        String json = new Gson().toJson(baseChatMessage);
        try {
            JSONObject object = new JSONObject(json);
            NewMessageBean msgBean = CommonUtils.handleHttpResult(NewMessageBean.class, object.toString());

            String msgContent = msgBean.msgContent;
//            String msgContent = msgBean.mHyperLinkText;
            msgBean.customMsgContent = CommonUtils.handleHttpResult(CustomMsgContent.class, msgContent);
            if (msgBean.msgProto == 0) {
                msgBean.setType(Constants.MSG_MY);
                msgBean.customMsgContent.messageId = msgBean.msgPacketId;
                msgBean.customMsgContent.fromType = "3";
            } else {
                msgBean.setType(Constants.MSG_NULL);
                msgBean.customMsgContent.fromType = "2";
            }
            return msgBean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int processMsgStatus(List<BaseChatMessage> lists) {
        BaseChatMessage baseChatMessage = lists.get(lists.size() - 1);
        String json = new Gson().toJson(baseChatMessage);
        int state = -1000;
        try {
            JSONObject object = new JSONObject(json);
            state = object.getInt("msgState");


            if (state == Constants.MSG_STATE_SENDING) {

                return Constants.MSG_STATE_SENDING;
            }
            if (state == Constants.MSG_STATE_FAILED) {

                return Constants.MSG_STATE_FAILED;
            }
            if (state == Constants.MSG_STATE_SEND_SUCCESSFUL) {

                return Constants.MSG_STATE_SEND_SUCCESSFUL;
            }
            if (state == Constants.MSG_STATE_SEND_READ) {

                return Constants.MSG_STATE_SEND_READ;
            }
            if (state == Constants.MSG_STATE_SEND_UNREAD) {

                return Constants.MSG_STATE_SEND_UNREAD;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -100;
        }
        return 0;
    }

    public static void encryptMsgData(CustomMsgContent customMsgContent) {
        String umId = LoginDesUtil.encryptToURL(customMsgContent.umId, LoginDesUtil.ZZGJSPWDKEY);
        String msg = LoginDesUtil.encryptToURL(customMsgContent.msg, LoginDesUtil.ZZGJSPWDKEY);
        String cusName = customMsgContent.customerName;
        if (cusName == null) {
            cusName = "";
        }
        String customerName = LoginDesUtil.encryptToURL(cusName, LoginDesUtil.ZZGJSPWDKEY);
        customMsgContent.umId = umId;
        customMsgContent.msg = msg;
        customMsgContent.customerName = customerName;
    }

    public static ConversationBean encryptConversationData(ConversationBean conversationBean){
        ConversationBean conversationBean_=null;
        if (conversationBean!=null){
            conversationBean_=new ConversationBean();
            conversationBean_.createTime=conversationBean.createTime;
            conversationBean_.paImType=conversationBean.paImType;
            conversationBean_.messageId=conversationBean.messageId;
            conversationBean_.msgType=conversationBean.msgType;
            conversationBean_.unReadCount=conversationBean.unReadCount;
            conversationBean_.customerIcon=conversationBean.customerIcon;
            conversationBean_.customerId=conversationBean.customerId;
            conversationBean_.dialogueStatus=conversationBean.dialogueStatus;
            conversationBean_.msg=conversationBean.msg;
            conversationBean_.umId=conversationBean.umId;
            conversationBean_.clientMobileNo=conversationBean.clientMobileNo;
            conversationBean_.imNickName=conversationBean.imNickName;
            conversationBean_.customerName=conversationBean.customerName;
            conversationBean_.status=conversationBean.status;
            if (conversationBean_.clientMobileNo!=null){
                conversationBean_.clientMobileNo=LoginDesUtil.encryptToURL(conversationBean_.clientMobileNo,LoginDesUtil.ZZGJSPWDKEY);
            }
            if (conversationBean_.imNickName!=null){

                conversationBean_.imNickName=LoginDesUtil.encryptToURL(conversationBean_.imNickName,LoginDesUtil.ZZGJSPWDKEY);
            }
            if (conversationBean.customerName!=null){
                conversationBean_.customerName=LoginDesUtil.encryptToURL(conversationBean_.customerName,LoginDesUtil.ZZGJSPWDKEY);
            }
            conversationBean_.msg=LoginDesUtil.encryptToURL(conversationBean_.msg,LoginDesUtil.ZZGJSPWDKEY);
            conversationBean_.umId=LoginDesUtil.encryptToURL(conversationBean_.umId,LoginDesUtil.ZZGJSPWDKEY);
        }


        return conversationBean_;
    }

    public static ConversationBean decryptConversationData(ConversationBean conversationBean){
        ConversationBean conversationBean_=null;
        if (conversationBean!=null){
            conversationBean_=new ConversationBean();
            conversationBean_.createTime=conversationBean.createTime;
            conversationBean_.paImType=conversationBean.paImType;
            conversationBean_.messageId=conversationBean.messageId;
            conversationBean_.msgType=conversationBean.msgType;
            conversationBean_.unReadCount=conversationBean.unReadCount;
            conversationBean_.customerIcon=conversationBean.customerIcon;
            conversationBean_.customerId=conversationBean.customerId;
            conversationBean_.dialogueStatus=conversationBean.dialogueStatus;
            conversationBean_.msg=conversationBean.msg;
            conversationBean_.umId=conversationBean.umId;
            conversationBean_.clientMobileNo=conversationBean.clientMobileNo;
            conversationBean_.imNickName=conversationBean.imNickName;
            conversationBean_.customerName=conversationBean.customerName;
            conversationBean_.status=conversationBean.status;
            if (conversationBean_.clientMobileNo!=null){
                conversationBean_.clientMobileNo=LoginDesUtil.decryptToURL(conversationBean_.clientMobileNo,LoginDesUtil.ZZGJSPWDKEY);
            }
            if (conversationBean_.imNickName!=null){

                conversationBean_.imNickName=LoginDesUtil.decryptToURL(conversationBean_.imNickName,LoginDesUtil.ZZGJSPWDKEY);
            }
            if (conversationBean.customerName!=null){
                conversationBean_.customerName=LoginDesUtil.decryptToURL(conversationBean_.customerName,LoginDesUtil.ZZGJSPWDKEY);
            }
            conversationBean_.msg=LoginDesUtil.decryptToURL(conversationBean_.msg,LoginDesUtil.ZZGJSPWDKEY);
            conversationBean_.umId=LoginDesUtil.decryptToURL(conversationBean_.umId,LoginDesUtil.ZZGJSPWDKEY);
        }


        return conversationBean_;
    }



    public ConversationBean createConversationBean(NewMessageBean newMessageBean){
        ConversationBean conversationBean=new ConversationBean();
        conversationBean.createTime = newMessageBean.customMsgContent.createTime;
        conversationBean.customerIcon = newMessageBean.customMsgContent.customerIcon;
        conversationBean.customerId = newMessageBean.customMsgContent.customerId;
        conversationBean.customerName = newMessageBean.customMsgContent.customerName;
        conversationBean.messageId = newMessageBean.customMsgContent.messageId;
        conversationBean.msg = newMessageBean.customMsgContent.msg;

        conversationBean.msgType = newMessageBean.customMsgContent.msgType;
        conversationBean.paImType = newMessageBean.customMsgContent.paImType;
        int unReadCount = conversationBean.unReadCount;


        return conversationBean;
    }


    public static void handleChannel(Context context,String channel){
        String bizChannel= (String) SpfUtil.getValue(context, SpfUtil.BIZ_CHANNEL, "");
        try {
            bizChannel(channel, bizChannel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void bizChannel(String channel, String bizChannel) throws JSONException {
        if (bizChannel!=null&&!bizChannel.equals("")){
            JSONArray array = new JSONArray(bizChannel);

            for (int i=0;i<array.length();i++){
                JSONObject object= (JSONObject) array.get(i);
                if (object.has(channel)){
                    JSONArray arr=object.getJSONArray(channel);
                    for (int j=0;j<arr.length();j++){
                        JSONObject obj= (JSONObject) arr.get(j);
                        SendMessageBean sendMessageBean = CommonUtils.handleHttpResult(SendMessageBean.class, obj.toString());
                        ChatAddBean chatAddBean=new ChatAddBean();
                        chatAddBean.iconName=sendMessageBean.btnValue;
                        chatAddBean.iconRes= ChatAddHolder.icons[j];
                        if (sendMessageBean.btnStatus.equals("Y")){
                            if (!ChatAddHolder.contains(chatAddBean)){

                                ChatAddHolder.addChatAddBean(chatAddBean);
                            }
                        }
                    }
                }
            }

        }
    }



    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {


        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0,i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }



    private static CommonUtils commonUtils;
    private CommonUtils(){

    }
    public static CommonUtils newInstance(){

        if (commonUtils==null){
            commonUtils=new CommonUtils();
            return commonUtils;
        }
        return commonUtils;
    }

    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

	/**
     * 生成运营商标识
     * @param context
     * @return
     */
    public static String getMso(Context context) {
        String mso = null;
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null)

        {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {

                mso = "00";//中国移动

            } else if (operator.equals("46001")) {

                mso = "01";//中国联通

            } else if (operator.equals("46003")) {

                mso = "03";//中国电信

            }else {
                mso="20";
            }
            return mso;
        }
        return "20";
    }
    /**
     * 生成消息ID
     *
     * @return
     */
    public static String generateMsgId() {

        String msgHeader = "appa";
        long timeStamp = System.currentTimeMillis();
        int radomNumber = randomNumber();
        String msgId = msgHeader + timeStamp + radomNumber;

        return msgId;
    }
    /**
     * 生成随机数
     *
     * @return
     */
    public static int randomNumber() {

        Random random = new Random();
        int max = 99999999;
        int min = 10000000;
        int s = random.nextInt(max) % (max - min + 1) + min;
        System.out.println(s);
        return s;
    }

}
