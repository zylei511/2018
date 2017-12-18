package com.paic.crm.utils;

/**
 * Created by hanyh on 16/3/23.
 */
public class HttpUrls {

    private static String HTTP_HEADER = Config.getConfig("HTTP_HEADER");
//       public static String HTTP_HEADER="https://umap-crm.pa18.com/umap-crm-mobile";
    /**
     * 登陆
     */
    public static String HTTP_LOGIN = HTTP_HEADER + "/login.do";
    /**
     * 会话
     */
    public static String HTTP_CONVERSATION = HTTP_HEADER + "/customerChats/getCustomerDialogue.do";
    /**
     * 移除会话
     */
    public static String HTTP_CONVERSATION_DELETE = HTTP_HEADER + "/customerChats/removeCustomerTask.do";
    /**
     * 聊天详细
     */
    public static String HTTP_CHAT = HTTP_HEADER + "/customerChats/getCustomerChats.do";
    /**
     * 历史聊天信息
     */
    public static String HTTP_CHAT_HISTORY = HTTP_HEADER + "/customerChats/getCustomerHistoryChats.do";
    /**
     * 聊天历史
     */
    public static String HTTP_GET_MSG_HISTORY = HTTP_HEADER + "/customerChats/getCustomerChats.do";
    /**
     * 版本更新
     */
    public static String HTTP_VERSION_UPDATA = HTTP_HEADER + "/getUcmVersionInfo.do";
    /**
     * 免登陆
     */
    public static String HTTP_CHECK_LOGIN = HTTP_HEADER + "/checkLandingTime.do";
    /**
     *  会话历史
     */
    public static String HTTP_CONVERSATION_HISTORY = HTTP_HEADER + "/customerChats/getCustomerHistoryDialogueList.do";
    /**
     * 意见反馈
     */
    public static String HTTP_FEED_BACK = HTTP_HEADER + "/tmrSuggest/addTmrSuggestion.do";
    /**
     * 登出
     */
    public static String HTTP_LOGOUT = HTTP_HEADER + "/logout.do";
    /**
     * 全渠道消息历史
     */
    public static String HTTP_UCPMSG_HISTORY = HTTP_HEADER + "/customerChats/getCustomerHistoryChats.do";
    /**
     *  提交修改电话号
     */
    public static String HTTP_PHONE_MODIFY = HTTP_HEADER +"/verifycode.do";
    /**
     *  获取验证码
     */
    public static String HTTP_GET_CODES = HTTP_HEADER +"/sendcode.do";
    /**
     * 签入
     */
    public static String HTTP_CHECKIN = HTTP_HEADER +"/communication/singIn.do";
    /**
     *  签出
     */
    public static String HTTP_CHECKOUT = HTTP_HEADER +"/communication/singOut.do";
    /**
     * 联系人
     * */
    public static final String HTTP_GETCONTACTS = HTTP_HEADER+"/ucpBusiness/getUcpClientContactList.do";
    /**
     * 系统点击量接口
     */
    public static final String CLICK_DATA=HTTP_HEADER+"/monitor/addServerHit.do";
    /**
     * 更改在线状态
     * */
     public static final String CHANG_ONLINE_STATUS=HTTP_HEADER+"/updateUserInfo.do";
    /**
     * 资讯测试和生产的加密key
     */
    public static final String INFORMATION_TEST_KEY=Config.getConfig("INFORMATION_TEST_KEY");
    /**
     * 各渠道的测试的h5界面地址
//     */
//    public static final String INFORMATION_URL = "http://test-pad-info.pa18.com:22080/pad-info/information.do?p=";
//    public static final String UCP_CONTACTS_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/contacts?p=";
//    public static final String UCP_SMS_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/sms?p=";
//    public static final String UCP_QUICK_REPLY_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/quickReplies?p=";
//    public static final String UCP_TEMPLATE_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/templates?p=";
//    public static final String UCP_PICTURE_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/pictures?p=";
//    public static final String UCP_PIC_PRIFEX = "http://test-eim-crm.pingan.com.cn:8145/showpicture?media_id=";
//    public static final String WORK_BEACH = "http://test-pad-crm.pa18.com:13080/pad-tmr-dmz/appIndex.html?p=";

    public static final String INFORMATION_URL = Config.getConfig("INFORMATION_URL");
    //public static final String UCP_CONTACTS_URL = "http://test-eim-crm.pingan.com.cn:8145/crm/contacts?p=";
    public static final String UCP_SMS_URL = Config.getConfig("UCP_SMS_URL");
    public static final String UCP_QUICK_REPLY_URL = Config.getConfig("UCP_QUICK_REPLY_URL");
    public static final String UCP_TEMPLATE_URL = Config.getConfig("UCP_TEMPLATE_URL");
    public static final String UCP_PICTURE_URL = Config.getConfig("UCP_PICTURE_URL");
    public static final String UCP_PIC_PRIFEX = Config.getConfig("UCP_PIC_PRIFEX");
    public static final String WORK_BEACH = Config.getConfig("WORK_BEACH");

    /**
     * 外呼接口
     */
    public static final String DIAL_OUT = Config.getConfig("DIAL_OUT");

    /**
     * IVR触发接口
     */
    public static final String TOUCH_IVR = Config.getConfig("TOUCH_IVR");
    /**
     * 空中客服转接接口
     */
    public static final String TRANSFER_KZKF = Config.getConfig("HTTP_HEADER") + "/transfer/transfer.do";

    /**
     * 空中客服签入
     */
    public static String CHECKIN_KZKF = Config.getConfig("HTTP_HEADER") + "/airCustomer/customerSign.do";

    /**
     * 空中客服会话列表
     */
    public static String HTTP_CONVERSATION_KZKF = Config.getConfig("HTTP_HEADER") + "/customerChats/querCustomerDialogue.do";

    /**
     * 删除空中客服会话列表
     */
    public static String HTTP_CONVERSATION_DELETE_KZKF = Config.getConfig("HTTP_HEADER") + "/customerChats/removeCustomerTask.do";

    public static String UCP_PIC_PRIFEX_KZKF = Config.getConfig("UCP_PIC_PRIFEX_KZKF");
    /**
     * 埋点
     */
    public static String UCP_BURIED_POINT = Config.getConfig("UCP_BURIED_POINT");

//    /**
//     * 资讯测试和生产的加密key
//     */
//    public static final String INFORMATION_TEST_KEY="20f045bd63610b6e";
//    /**
//     * 各渠道的正式的h5界面地址
//     */
//    public static final String INFORMATION_URL="http://pad-info.pa18.com/pad-info/information.do?p=";
//    public static final String UCP_CONTACTS_URL="http://eim-crm.pingan.com.cn/crm/contacts?p=";
//    public static final String UCP_SMS_URL="http://eim-crm.pingan.com.cn/crm/sms?p=";
//    public static final String UCP_QUICK_REPLY_URL="http://eim-crm.pingan.com.cn/crm/quickReplies?p=";
//    public static final String UCP_TEMPLATE_URL="http://eim-crm.pingan.com.cn/crm/templates?p=";
//    public static final String UCP_PICTURE_URL="http://eim-crm.pingan.com.cn/crm/pictures?p=";
//    public static final String UCP_PIC_PRIFEX="http://eim-crm.pingan.com.cn/crm/showpicture?media_id=";
//    public static final String WORK_BEACH="http://pad-crm.pa18.com/pad-tmr-dmz/appIndex.html?p=";
}
