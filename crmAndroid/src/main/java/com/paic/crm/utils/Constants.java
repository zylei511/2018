package com.paic.crm.utils;

/**
 * Created by hanyh on 16/2/19.
 */
public class Constants {

    public static final String FACE_KEY="image";
    public static final String LOG_TAG="crm_log_info";
    public static final String MSG_MY="myself";
    public static final String MSG_NULL="";
    public static final String CHARSET_NAME="utf-8";
    public static final String PUBLICK_KEY="10026@publicservice.pingan.com.cn";
    public static final int REQUEST_C0DE=0;
    public static final String OPEN_ID="2DD6788DE0570F3330DE72AF2FA772A8926F68EDA3F4EAFD";
    public static final String DB_NAME="crm.db";

    /**
     * 消息状态
     */

    public static final int MSG_STATE_FAILED=-1;
    public static final int MSG_STATE_SENDING=0;
    public static final int MSG_STATE_SEND_SUCCESSFUL=1;
    public static final int MSG_STATE_SEND_READ=2;
    public static final int MSG_STATE_SEND_UNREAD=3;
    public static final int MSG_STATE_SEND_UNDO=-1000;//只是个更改发送中的标识

    /**
     * 消息类型
     */
    public static final String MSG_TYPE_KEY="dealMsgType";
    public static final String MSG_TYPE_VALUE_EVENT="event";
    public static final String MSG_TYPE_VALUE_TEXT="text";

    /**
     * MSG_TYPE_EVENT细分事件类型
     */
    public static final String MSG_EVENT_TYPE_KEY="eventType";
    public static final String MSG_EVENT_TYPE_VALUE_IVR="ivrEvent";
    /**
     * http请求tag
    */

    public static final String TAG_LOGIN="01";
    public static final String TAG_CONVERSATION="02";
    public static final String TAG_CONVERSATION_DELETE="03";
    public static final String TAG_CHAT="04";
    public static final String TAG_CHANNEL_WEIXIN="01";
    public static final String TAG_CHANNEL_SMS="03";

    /**
     * 渠道编码
     */
    public static final String CHANNEL_WEIXIN="01";
    public static final String CHANNEL_WEB="02";
    public static final String CHANNEL_SMS="03";
    public static final String CHANNEL_APP_IM="09";
    public static final String CHANNEL_WORLD="06";

    /**
     * 业务线
     */
    public static final String BIZ_SERIES_BC_LIFE = "BC_LIFE";
    public static final String BIZ_SERIES_SDK_KZKF = "SDK_KZKF";

    /**
     * 空中客服签入状态
     */
    public static final String STATE_CHECK_IN_KZKF = "01";
    public static final String STATE_CHECK_OUT_KZKF = "00";

    /**
     * 空中客服中会话状态
     */
    public static final String STATE_CONVERSATION_VAILD = "1";
    public static final String STATE_CONVERSATION_INVAILD = "0";
    public static final String STATE_CONVERSATION_DELETE = "2";

    /**
     * 空中客服中会话绑定状态
     */
    public static final String STATE_CONVERSATION_STATUS_Y = "Y";
    public static final String STATE_CONVERSATION_STATUS_N = "N";
}
