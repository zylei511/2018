package com.paic.crm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.paic.crm.android.R;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.entity.Notice;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.ui.SplashActivity;
import com.pingan.paimkit.module.chat.bean.message.BaseChatMessage;
import com.pingan.paimkit.module.chat.bean.message.ChatMessageForwardSlink;
import com.pingan.paimkit.module.chat.bean.message.ChatMessageLink;
import com.pingan.paimkit.module.chat.bean.message.ChatMessageSLink;
import com.pingan.paimkit.module.chat.bean.message.ChatMessageText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 消息Notification
 */
public class MessageNotification {

    private static MessageNotification instance;

    private Context mContext;
    private NotificationManager notificationManager;
    private Notification notification;
    public static boolean isOfflineMessage = false;
    private long time;
    // 考虑并发操作问题
    private List<Notice> noticeList = Collections.synchronizedList(new ArrayList<Notice>());
    // 在mainacitvity时 设置为true 离开设置为false
    private boolean noShow;
    // 当前和谁聊天
    private String noShowUserName;
    private AudioManager am;
    private ActivityManager manager;

    private MessageNotification(Context mContext) {
        super();
        this.mContext = mContext;
        am = (AudioManager) mContext.getSystemService(Service.AUDIO_SERVICE);
        manager = (ActivityManager) mContext.getSystemService(Service.ACTIVITY_SERVICE);
    }

    public static MessageNotification getInstance(Context context) {
        synchronized (MessageNotification.class) {
            if (instance == null) {
                instance = new MessageNotification(context.getApplicationContext());
            }
        }
        return instance;
    }

    /**
     * 验证某一时间是否在某一时间段
     * 
     * @param currTime 某一时间 例： "16:48"
     * @param timeSlot 某一时间段 例： {"16:00", "00:00"}
     * @return true/false
     */
    public static boolean isShift(final long currTime, String[] timeSlot) {
        String[] tmpArray = timeSlot[0].split(":");
        int beginHour = Integer.parseInt(tmpArray[0]);
        int beginMinute = Integer.parseInt(tmpArray[1]);
        tmpArray = timeSlot[1].split(":");
        int endHour = Integer.parseInt(tmpArray[0]);
        int endMinute = Integer.parseInt(tmpArray[1]);

        Calendar curCalendar = Calendar.getInstance();

        int curHour = curCalendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = curCalendar.get(Calendar.MINUTE);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear(Calendar.HOUR_OF_DAY);
        startCalendar.clear(Calendar.MINUTE);
        startCalendar.set(Calendar.HOUR_OF_DAY, beginHour);
        startCalendar.set(Calendar.MINUTE, beginMinute);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear(Calendar.HOUR_OF_DAY);
        endCalendar.clear(Calendar.MINUTE);
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);

        int contrast = contrastTime(beginHour, beginMinute, endHour, endMinute);
        boolean isJumpDay = false;
        // 23:00 1:00
        // 02:00 1:00
        // 如果是跨天 比如 晚上 2:00 到晚上1:00免打扰
        if (contrast == CONTRAST_BIG) {
            // 比如设置的是 晚上 2:00 到晚上1:00免打扰
            // 此时就是 1：00 到 2：00 才是打扰时间 则是 当前时间必须大于1：00 小于 2：00
            isJumpDay = true;
        }

        if (isJumpDay) {
            if (curCalendar.after(startCalendar)) {
                return true;
            } else if (contrastTime(curHour, curMinute, endHour, endMinute) == CONTRAST_SMALL) {
                return true;
            } else {
                return false;
            }
        } else {
            // 如果开始时间和结束时间一样
            if (contrast == CONTRAST_SAME) {
                if (contrastTime(curHour, curMinute, endHour, endMinute) == CONTRAST_SAME) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (curCalendar.after(startCalendar) && curCalendar.before(endCalendar)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    }

    private static int CONTRAST_BIG = 1;

    private static int CONTRAST_SMALL = 2;

    private static int CONTRAST_SAME = -1;

    private static int contrastTime(int temp1Hour, int temp1Minute, int temp2Hour, int temp2Minute) {
        if (temp1Hour > temp2Hour) {
            return CONTRAST_BIG;
        } else if (temp2Hour > temp1Hour) {
            return CONTRAST_SMALL;
        } else {
            if (temp1Minute > temp2Minute) {
                return CONTRAST_BIG;
            } else if (temp2Minute > temp1Minute) {
                return CONTRAST_SMALL;
            } else {
                return CONTRAST_SAME;
            }
        }
    }

    /**
     * @param jid 当前报文发送者
     * @param msg 当前报文类型 例如 文本 图片 视频
     *
     */
    public synchronized void notification(String jid, BaseChatMessage msg) {
        for(Activity activity : ActivityCollector.activities){
            if(isExist()&&isTopActivy(activity.getLocalClassName())){
                return;
            }
        }

        NewMessageBean db = CommonUtils.processNewMsgData(msg);
        if (db!=null){
            CommonUtils.encryptMsgData(db.customMsgContent);
            int id = CrmDaoHolder.getInstance().getCustomMsgContentDao().queryForId(db.customMsgContent);
            if (id != -1) {
                CrmDaoHolder.getInstance().getCustomMsgContentDao().update(db.customMsgContent);
            } else {
                CrmDaoHolder.getInstance().getCustomMsgContentDao().add(db.customMsgContent);
            }
            ConversationBean conversationBean=
                    CrmDaoHolder
                            .getInstance()
                            .getConversationBeanDao()
                            .queryConversation(db.customMsgContent.customerId,
                                    db.customMsgContent.paImType,
                                    db.customMsgContent.umId);

            if (conversationBean!=null){
                int unReadCount=conversationBean.unReadCount;
                unReadCount++;
                CrmDaoHolder
                        .getInstance()
                        .getConversationBeanDao()
                        .updateUnreadDb(conversationBean, unReadCount, false);
            }else {
                NewMessageBean dbConversation = CommonUtils.processNewMsgData(msg);
                ConversationBean conversationBean1=handleConversationCome(dbConversation);
                ConversationBean conversationBean2=CommonUtils.encryptConversationData(conversationBean1);
                CrmDaoHolder.getInstance().getConversationBeanDao().add(conversationBean2);
            }
        }

        sendNotification(mContext.getResources().getString(R.string.notification_title), "收到一条消息",
                R.mipmap.app_icon);
    }



    private synchronized void sendNotification(String title, String message, int icon) {
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis();
        CRMLog.LogInfo(Constants.LOG_TAG,"sendNotification");
        notification = new Notification(icon, message, when);
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.notification_title, title);
        contentView.setTextViewText(R.id.notification_content, message);
        notification.contentView = contentView;

        Intent notificationIntent = null;

//        if (noticeList.size() > 1) {
//            if (isExist()) {
//                notificationIntent = new Intent(mContext, HomeActivity.class); // 点击该通知后要跳转的Activity
//           //     notificationIntent.putExtra(MainActivity.Param.MSGFLAG, true);
//            } else {
                notificationIntent = new Intent(mContext, SplashActivity.class); // 点击该通知后要跳转的Activity,先跳转到splash再跳转
           //     notificationIntent.putExtra(MainActivity.Param.MSGFLAG, true);
 //           }

//        } else if (noticeList.size() == 1) {
//            if (isExist()) {
//
//                notificationIntent = ChatActivity.getNotificationExtras(mContext, noticeList.get(0).getName(),
//                        noticeList.get(0).getType(), noticeList.get(0).getTitle()); // 点击该通知后要跳转的Activity
//            } else {
//               // notificationIntent = new Intent(mContext, SplashActivity.class);// 点击该通知后要跳转的Activity,先跳转到splash再跳转
//                notificationIntent = new Intent(mContext, HomeActivity.class);// 点击该通知后要跳转的Activity,先跳转到splash再跳转
//
//                notificationIntent.putExtra(ChatActivity.EXTRA_USERNAME, noticeList.get(0).getName());
////                notificationIntent.putExtra(ChatActivity.EXTRA_TYPE, noticeList.get(0).getType());
//            }
//
//        } else {
//            return;
//        }

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notification.contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        Random random=new Random();
        int notiId=random.nextInt();
        notificationManager.notify(notiId, notification);
    }

    public synchronized void hideNotification() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
            noticeList.clear();
        }

    }

    public boolean isNoShow() {
        return noShow;
    }

    public void setNoShow(boolean noShow) {
        this.noShow = noShow;
    }

    public String getNoShowUserName() {
        return noShowUserName;
    }

    public void setNoShowUserName(String noShowUserName) {
        this.noShowUserName = noShowUserName;
    }

    public boolean isTopActivy(String cmdName) {
        String cmpNameTemp = null;
        try {
            List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

            if (null != runningTaskInfos && runningTaskInfos.size() >= 1) {
                cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
            }
            if (null == cmpNameTemp)
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TTTT", "崩溃：。。。。。" + e.getMessage());
        }
        return cmpNameTemp.contains(cmdName);
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className
     *            某个界面名称
     */
    private boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }
    private boolean isExist() {
        //TODO 这里要弄一个SplashActivity
       // Intent intent = new Intent(mContext, SplashActivity.class);
        Intent intent = new Intent(mContext, HomeActivity.class);
        ComponentName cmpName = intent.resolveActivity(mContext.getPackageManager());
        boolean bIsExist = false;
        ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
        for (RunningTaskInfo taskInfo : taskInfoList) {
            if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                bIsExist = true;
                break;
            }
        }
        return bIsExist;
    }

    /**
     * 获取最后消息的文本展示
     * 
     *
     */
    private String getLastContent(BaseChatMessage baseChatMessage) {
        String lastContent = "";
        int msgType = baseChatMessage.getMsgContentType();
        switch (msgType) {
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_TEXT:
                lastContent = ((ChatMessageText) baseChatMessage).getMsgContent().toString();
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_AUDIO:
                lastContent = "[语音]";
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_IMAGE:
                lastContent = "[图片]";
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_VIDEO:
                lastContent = "[视频]";
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_LOCATION:
                lastContent = "[位置]";
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_LINK://单图文
                lastContent = ((ChatMessageLink) baseChatMessage).getShowContent();
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_SLINK://多图文
                lastContent = ((ChatMessageSLink) baseChatMessage).getShowContent();
                break;
            case BaseChatMessage.MsgContentType.CONTENT_TYPE_FORWARDSLINK://图文转发连接
                lastContent = ((ChatMessageForwardSlink) baseChatMessage).getShowContent();
                break;
        }

        return lastContent;
    }
    private synchronized ConversationBean handleConversationCome(NewMessageBean newMessageBean) {
        ConversationBean conversationBean;
        conversationBean = new ConversationBean();
        conversationBean.createTime = newMessageBean.customMsgContent.createTime;
        conversationBean.customerIcon = newMessageBean.customMsgContent.customerIcon;
        conversationBean.customerId = newMessageBean.customMsgContent.customerId;
        conversationBean.customerName = newMessageBean.customMsgContent.customerName;
        conversationBean.messageId = newMessageBean.customMsgContent.messageId;
        conversationBean.msg = newMessageBean.customMsgContent.msg;
        conversationBean.umId = newMessageBean.customMsgContent.umId;
        conversationBean.msgType = newMessageBean.customMsgContent.msgType;
        conversationBean.paImType = newMessageBean.customMsgContent.paImType;
        int unReadCount = conversationBean.unReadCount;
        unReadCount++;
        conversationBean.unReadCount = unReadCount;
        return conversationBean;
    }
}
