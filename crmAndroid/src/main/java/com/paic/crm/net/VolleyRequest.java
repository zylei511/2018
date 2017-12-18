package com.paic.crm.net;

import android.content.Context;

import com.paic.crm.android.BuildConfig;
import com.paic.crm.utils.Config;
import com.paic.crm.utils.DigestUtil;
import com.paic.crm.utils.HttpUrls;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hanyh on 16/1/5.
 */
public class VolleyRequest {

    /**
     * 登陆
     *
     * @param url
     * @param tag
     * @param userName
     * @param passWord
     * @param volleyInterface
     */
    public static void httpLogin(Context context,String url, String tag, String userName, String passWord, VolleyInterface volleyInterface) {

        try {
            Map<String, String> params = new HashMap<>();
            params.put("userName", userName);
            params.put("passWord", passWord);
            VolleyHttpConnector.volleyPost(context,url, tag, params, volleyInterface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 会话列表
     *
     * @param url
     * @param tag
     * @param umId
     * @param volleyInterface
     */
    public static void httpConversationList(Context context,String url, String tag, String umId, String Shiro, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        Shiro = DigestUtil.digest(Shiro, params);
        params.put("umId", umId);
        params.put("shiroToken", Shiro);
        VolleyHttpConnector.volleyPost(context,url, tag, params, volleyInterface);
    }

    /**
     * 会话移除
     *
     * @param url
     * @param tag
     * @param umId
     * @param customerId
     * @param paImType
     * @param Shiro
     * @param volleyInterface
     */
    public static void httpConversationDelete(Context context,String url, String tag, String umId, String customerId, String paImType, String status, String bizSeries, String Shiro, VolleyInterface volleyInterface) {

        Map<String, String> params = new HashMap<>();

        params.put("customerId", customerId);
        params.put("paImType", paImType);
        params.put("status", status);
        params.put("bizSeries", bizSeries);
        String shiroToken = DigestUtil.digest(Shiro, params);
        params.put("shiroToken", shiroToken);
        params.put("umId", umId);
        VolleyHttpConnector.volleyPost(context,url, tag, params, volleyInterface);
    }


    /**
     * 聊天信息
     *
     * @param url
     * @param tag
     * @param umId
     * @param customerId
     * @param shiro
     * @param paImType
     * @param volleyInterface
     */
    public static void httpChat(Context context,String url, String tag, String umId, String customerId, String paImType, String messageId, String shiro, VolleyInterface volleyInterface) {

        Map<String, String> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("messageId", messageId);
        params.put("paImType", paImType);
        String shiroToken = DigestUtil.digest(shiro, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, url, tag, params, volleyInterface);
    }

    /**
     * 会话历史
     *
     * @param url,shiroToken ,umId,nowPage,volleyInterface
     */
    public static void httpConversationHistory(Context context,String url, String shiroToken, String umId, String nowPage, VolleyInterface volleyInterface) {
        Map params = new HashMap();
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        params.put("nowPage", nowPage);
        VolleyHttpConnector.volleyPost(context, url, "conversationHistory", params, volleyInterface);
    }

    /**
     * 消息历史
     *
     * @param url,umId ,customerId,nowPage,shiroToken,volleyInterface
     */
    public static void httpMsgHistory(Context context,String url, String umId, String customerId, String nowPage, String shiroToken, VolleyInterface volleyInterface) {
        Map params = new HashMap();
        params.put("umId", umId);
        params.put("customerId", customerId);
        params.put("nowPage", nowPage);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url, "msgHistory", params, volleyInterface);
    }

    /**
     * 版本更新
     *
     * @param url
     * @param umId
     * @param shiroToken
     * @param volleyInterface
     */
    public static void httpVersionUpdata(Context context,String url, String umId, String shiroToken, VolleyInterface volleyInterface) {
        Map params = new HashMap();
        shiroToken = DigestUtil.digest(shiroToken, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url, "msgHistory", params, volleyInterface);
    }

    public static void httpCheckLogin(Context context,String url, String umId, String shiroToken, VolleyInterface volleyInterface){
        Map params = new HashMap();
        shiroToken = DigestUtil.digest(shiroToken, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url, "httpCheckLogin", params, volleyInterface);

    }
    /**
     * 登出
     * @param url,umId，shiroToken,volleyInterface
     */
    public static void logOut(Context context,String url,String umId,String shiroToken,VolleyInterface volleyInterface){
        Map<String,String> param = new HashMap<>();
        param.put("umId",umId);
        param.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"logOut",param,volleyInterface);
    }
    /**
     * 反馈
     * @param url,umId ,content,shiroToken,volleyInterface
     */
    public static void submitFeedBack(Context context,String url,String umId,String content,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> param = new HashMap<>();
        param.put("suggestContent", content);
        String shiroToken= DigestUtil.digest(shiroKey,param);
        param.put("umId",umId);
        param.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"submitFeedBack",param,volleyInterface);
    }
    /**
     * 获取验证码
     * */
    public static void httpGetCodes(Context context,String url,String umId,String phoneNum,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> params = new HashMap<>();
        params.put("phone",phoneNum);
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("umId",umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"getCodes",params,volleyInterface);
    }

    /**
     * 获取验证码
     * */
    public static void httpPhoneMotify(Context context,String url,String umId,String phone,String codes,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> params = new HashMap<>();
        params.put("phone",phone);
        params.put("code",codes);
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("umId",umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"httpPhoneMotify",params,volleyInterface);
    }
    /**
     * 签入
     * */
    public static void httpCheckIn(Context context,String url,String umId,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> params = new HashMap<>();
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("umId",umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"httpCheckIn",params,volleyInterface);
    }
    /**
     * 签出
     * */
    public static void httpCheckOut(Context context,String url,String umId,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> params = new HashMap<>();
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("umId",umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context,url,"httpCheckIn",params,volleyInterface);
    }

    /**
     * 获取联系人
     * @param url
     * @param shiroKey
     * @param volleyInterface
     * @param umId
     * */
    public static void httpGetContacts(Context context,String url,String umId,String shiroKey,VolleyInterface volleyInterface){
        Map<String ,String>params = new HashMap<>();
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("shiroToken",shiroToken);
        params.put("umId",umId);
        VolleyHttpConnector.volleyPost(context,url,"getContacts",params,volleyInterface);
    }
    /**
     * 更改在线状态
     * */
    public static void changeOnlineStatus(Context context,String url,String umId,String status,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String>params = new HashMap<>();
        params.put("appStatus",status);
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("shiroToken",shiroToken);
        params.put("umId",umId);
        VolleyHttpConnector.volleyPost(context,url,"changeOnlineStatus",params,volleyInterface);
    }

    /**
     *  app启动点击量
     * @param context
     * @param url
     * @param umId
     * @param shiroKey
     * @param volleyInterface
     */
    public static void totalData(Context context,String url,String umId,String shiroKey,VolleyInterface volleyInterface){
        Map<String,String> params = new HashMap<>();
        params.put("action","start");
        String shiroToken = DigestUtil.digest(shiroKey,params);
        params.put("umId",umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, url, "totalData", params, volleyInterface);
    }

    /**
     *触发IVR
     */
    public static void touchIVR(Context context, String umId, String requestId, String shiroKey, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("requestId", requestId);
        String shiroToken = DigestUtil.digest(shiroKey, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, HttpUrls.TOUCH_IVR, "touch_ivr", params, volleyInterface);
    }

    /**
     *外呼接口
     */
    public static void dialOut(Context context, String umId, String number, String requestId, String shiroKey, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("number", number);
        params.put("requestId", requestId);
        String shiroToken = DigestUtil.digest(shiroKey, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, HttpUrls.DIAL_OUT, "dial_out", params, volleyInterface);
    }

    /**
     * 转接
     */
    public static void transferKZKF(Context context, String customerId, String umId, String paImType, String reason, String shiroKey, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("paImType", paImType);
        params.put("reason", reason);
        String shiroToken = DigestUtil.digest(shiroKey, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, HttpUrls.TRANSFER_KZKF, "transfer_kzkf", params, volleyInterface);
    }

    /**
     * 空中客服签入
     */
    public static void checkInKZKF(Context context, String umId,String status, String shiroKey, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        String shiroToken = DigestUtil.digest(shiroKey, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, HttpUrls.CHECKIN_KZKF, "checkin_kzkf", params, volleyInterface);
    }

    /**
     * 埋点
     * @param context
     * @param umId
     * @param content
     * @param shiroKey
     * @param volleyInterface
     */
    public static void httpBuriedPoint(Context context, String umId, String content, String shiroKey, VolleyInterface volleyInterface) {
        Map<String, String> params = new HashMap<>();
        params.put("agentId", umId);
        params.put("sourceContent", content);
        params.put("action", "crmBuriedPoint");
        params.put("ip", "");
        params.put("device", android.os.Build.MODEL);
        params.put("systemVersion", BuildConfig.VERSION_NAME);
        params.put("networkOperators", "");
        String shiroToken = DigestUtil.digest(shiroKey, params);
        params.put("umId", umId);
        params.put("shiroToken", shiroToken);
        VolleyHttpConnector.volleyPost(context, HttpUrls.UCP_BURIED_POINT, "conversation_delete_kzkf", params, volleyInterface);
    }
}
