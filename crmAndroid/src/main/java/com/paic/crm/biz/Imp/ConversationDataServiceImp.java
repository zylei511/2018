package com.paic.crm.biz.Imp;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.biz.IDataServiceBiz;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.ConversationResultBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.presenter.IDeletable;
import com.paic.crm.presenter.IHttpRefreshable;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.LoginDesUtil;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/11/7.
 */

public class ConversationDataServiceImp implements IDataServiceBiz {
    private final String umId;
    private final String shirokey;
    private Context context;
    private IHttpRefreshable iRefreshable;
    private IDeletable iDeletable;

    public ConversationDataServiceImp(Context context, IHttpRefreshable iRefreshable, IDeletable iDeletable) {
        this.context = context;
        this.iRefreshable = iRefreshable;
        this.iDeletable = iDeletable;
        umId = (String) SpfUtil.getValue(context, SpfUtil.UMID, "");
        shirokey = (String) SpfUtil.getValue(context, SpfUtil.SHIROKEY, "");
    }

    @Override
    public List<ConversationBean> getDataFromDb(List<ConversationBean> list) {

        String umIdDb = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        List<ConversationBean> dbConversationBeans = CrmDaoHolder.getInstance().getConversationBeanDao().queryForUmId(umIdDb);
        List<ConversationBean> showList = new ArrayList<>();
        showList.addAll(dbConversationBeans);
        if (showList.size() > 0) {
            Collections.sort(showList);
            for (int i = 0; i < showList.size(); i++) {
                CRMLog.LogInfo("updateUnread", "before||" + showList.get(0).msg + "||" + i);
                ConversationBean conversationBean = CommonUtils.decryptConversationData(showList.get(i));
                showList.set(i, conversationBean);
                CRMLog.LogInfo("updateUnread", "after||" + showList.get(i).msg + "||" + i);
            }
        }
        return showList;
    }

    @Override
    public void getDataFromHttp(String url) {

        VolleyRequest.httpConversationList(context, url, Constants.TAG_CONVERSATION,
                umId, shirokey, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object obj) {

                        CRMLog.LogInfo(Constants.LOG_TAG, "getDataFromHttp onSuccess   SuccessObject ---" + obj.toString());
                        try {
                            if (BizSeriesUtil.isKZKF(context)){
                                CrmDaoHolder.getInstance().getConversationBeanDao().deleteAll(umId);
                            }
                            handleHttpJson(obj);
                            iRefreshable.onHttpSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError ve) {
                        CRMLog.LogInfo(Constants.LOG_TAG, "getDataFromHttp onError   VolleyError  ---" + ve.toString());
                        iRefreshable.onHttpError();
                    }

                    @Override
                    public void onLogOutside() {
                    }
                });
    }

    // TODO: 2017/11/22 待处理
    @Override
    public void deleteData(final ConversationBean conversationBean, final int position , String url, final String status, String bizSeries) {
        VolleyRequest.httpConversationDelete(context, url, Constants.TAG_CONVERSATION_DELETE,
                umId, conversationBean.customerId, conversationBean.paImType, status, bizSeries,shirokey, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object obj) {

                        try {
                            JSONObject object = new JSONObject(obj.toString());
                            String resultCode = object.getString("resultCode");
                            if (resultCode.equals("02")) {
                                if(Constants.STATE_CONVERSATION_DELETE.equals(status)) {

                                    CrmDaoHolder.getInstance().getConversationBeanDao().
                                            delete(conversationBean);
                                    iDeletable.onDeleteSuccess(conversationBean,position);
                                }
                                if(Constants.STATE_CONVERSATION_INVAILD.equals(status)){
                                    CrmDaoHolder.getInstance().getConversationBeanDao().
                                            updateConversationStatus(conversationBean.umId,
                                                    conversationBean.customerId,
                                                    conversationBean.paImType,
                                                    status);
                                    conversationBean.status = status;
                                    iDeletable.onFinishSuccess(conversationBean,position);
                                }
                            }else {
                                if(Constants.STATE_CONVERSATION_DELETE.equals(status)) {
                                    iDeletable.onDeleteError();
                                }
                                if(Constants.STATE_CONVERSATION_INVAILD.equals(status)){
                                    iDeletable.onFinishError();
                                }
                            }
                        } catch (JSONException e) {
                            if(Constants.STATE_CONVERSATION_DELETE.equals(status)) {
                                iDeletable.onDeleteError();
                            }
                            if(Constants.STATE_CONVERSATION_INVAILD.equals(status)){
                                iDeletable.onFinishError();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError ve) {
                        if(Constants.STATE_CONVERSATION_DELETE.equals(status)) {
                            iDeletable.onDeleteError();
                        }
                        if(Constants.STATE_CONVERSATION_INVAILD.equals(status)){
                            iDeletable.onFinishError();
                        }
                    }

                    @Override
                    public void onLogOutside() {
                        CommonUtils.clearData(context);
                    }
                });

    }

    @Override
    public void clearUnreadCount(ConversationBean chatConversation) {
        chatConversation = CommonUtils.encryptConversationData(chatConversation);
        ConversationBean conversationBean = CrmDaoHolder.getInstance()
                .getConversationBeanDao().queryConversation(chatConversation.customerId, chatConversation.paImType, chatConversation.umId);
        String channel = conversationBean.paImType;
        CrmDaoHolder.getInstance()
                .getConversationBeanDao().updateUnreadDb(conversationBean, 0, false);
        CommonUtils.handleChannel(context, channel);
    }

    @Override
    public List<ConversationBean> receiveNewMewMsg(CustomMsgContent newMsg, List<ConversationBean> conversationBeans) {
        //如果没头像，从缓存中拿头像
        updateCustomerIconIfNull(newMsg);
        return judgeWhichConversation(newMsg, conversationBeans);
    }

    private void updateCustomerIconIfNull(CustomMsgContent newMsg) {
        if (StringUtil.isBlank(newMsg.customerIcon)) {
            CustomMsgContent tmpCusContent = null;
            try {
                tmpCusContent = (CustomMsgContent) newMsg.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            CommonUtils.encryptMsgData(tmpCusContent);
            ConversationBean tmpConver = CrmDaoHolder.getInstance().getConversationBeanDao().queryConversation(tmpCusContent.customerId, tmpCusContent.paImType, tmpCusContent.umId);
            if (tmpConver != null) {
                newMsg.customerIcon = tmpConver.customerIcon;
            }
        }
    }


    private synchronized ConversationBean handleConversationCome(CustomMsgContent newMsg) {
        ConversationBean conversationBean;
        conversationBean = new ConversationBean();
        conversationBean.createTime = newMsg.createTime;
        conversationBean.customerIcon = newMsg.customerIcon;
        conversationBean.customerId = newMsg.customerId;
        conversationBean.status = Constants.STATE_CONVERSATION_VAILD;
        //系统消息没有名字。
        if (!"1".equals(newMsg.fromType)) {
            conversationBean.imNickName = newMsg.customerName;
        }
        conversationBean.messageId = newMsg.messageId;
        conversationBean.msg = newMsg.msg;
        conversationBean.umId = newMsg.umId;
        conversationBean.msgType = newMsg.msgType;
        conversationBean.paImType = newMsg.paImType;
        int unReadCount = conversationBean.unReadCount;
        unReadCount++;
        conversationBean.unReadCount = unReadCount;
        return conversationBean;
    }

    /**
     * 来一条消息 替换掉原来此位置的消息
     */
    private List<ConversationBean> judgeWhichConversation(CustomMsgContent newMsg, List<ConversationBean> conversationBeans) {

        CustomMsgContent msgDb = null;
        try {
            msgDb = (CustomMsgContent) newMsg.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CommonUtils.encryptMsgData(msgDb);
        boolean isHas = false;
        for (ConversationBean item : conversationBeans) {
            boolean sameConver = newMsg.customerId.equals(item.customerId)
                    && newMsg.paImType.equals(item.paImType)
                    && newMsg.umId.equals(item.umId);
            if (sameConver) {
                //判断是否是空中客服
                if (BizSeriesUtil.isKZKF(context) && TextUtils.isEmpty(newMsg.isOverDialogue)){
                    CrmDaoHolder.getInstance().getConversationBeanDao().updateConversationStatus(umId,item.customerId,item.paImType,Constants.STATE_CONVERSATION_VAILD);
                    item.status = Constants.STATE_CONVERSATION_VAILD;
                }

                isHas = true;
                item.createTime = newMsg.createTime;
                item.customerIcon = newMsg.customerIcon;
                //系统消息没有名字传过来
                if (!"1".equals(newMsg.fromType)) {
                    item.imNickName = newMsg.customerName;
                }
                item.messageId = newMsg.messageId;
                item.msg = newMsg.msg;
                CRMLog.LogInfo(Constants.LOG_TAG, "no Conversation loop--->" + conversationBeans);
                item.msgType = newMsg.msgType;
                item.paImType = newMsg.paImType;
                //把更新的那条会话置顶
                conversationBeans.remove(conversationBeans.indexOf(item));
                conversationBeans.add(0, item);
                int unReadCount = item.unReadCount;
                unReadCount++;
                item.unReadCount = unReadCount;
                // 会话页面收到消息，把聊天表也增加了，以保证进入chat能第一时间看到内容。
                if (!CrmEnvValues.getInstance().isChatReceive()) {
                    int id = CrmDaoHolder.getInstance().getCustomMsgContentDao().queryForId(msgDb);
                    if (id == -1) {
                        CrmDaoHolder.getInstance().getCustomMsgContentDao().add(msgDb);
                        CRMLog.LogInfo(Constants.LOG_TAG, "conversation 1111111111" + "");
                    }
                }
                ConversationBean dbConBean = CrmDaoHolder.getInstance().getConversationBeanDao().
                        queryConversation(msgDb.customerId, msgDb.paImType, msgDb.umId);


                if (dbConBean != null) {
                    CRMLog.LogInfo(Constants.LOG_TAG, "updateUnread--->" + unReadCount);
                    CrmDaoHolder.getInstance().getConversationBeanDao().updateUnread(msgDb, unReadCount, false);
                }
                break;
            }

        }
        if (CrmEnvValues.getInstance().isChatReceive()) {
            //资讯发过来也要添加
            if (CrmEnvValues.getInstance().isInforationSend()) {
                int id = CrmDaoHolder.getInstance().getCustomMsgContentDao().queryForId(msgDb);
                if (id == -1) {
                    int result = CrmDaoHolder.getInstance().getCustomMsgContentDao().add(msgDb);
                    CRMLog.LogInfo(Constants.LOG_TAG, "conversation information" + result);
                }
            }
        }
        if (!isHas) {
            ConversationBean newConversationBean = handleConversationCome(newMsg);
            conversationBeans.add(0, newConversationBean);
            ConversationBean dbConversationBean = handleConversationCome(msgDb);
            addDb(dbConversationBean);
        }

        return conversationBeans;

    }

    /**
     * 解析数据
     *
     * @param obj
     * @throws JSONException
     */
    private void handleHttpJson(Object obj) throws JSONException {
        JSONObject object = new JSONObject(obj.toString());
        if (!object.getString("resultCode").equals("02")) {
            return;
        }
        List<ConversationBean> httpConBeans = null;
        ConversationResultBean conversationBean = CommonUtils.handleHttpResult(ConversationResultBean.class, obj.toString());
        if (conversationBean != null) {
            httpConBeans = conversationBean.data;
        }
        //遍历插入数据库
        for (int i = 0; i < httpConBeans.size(); i++) {
            ConversationBean conversationHttp = httpConBeans.get(i);
            //数据库里面没有数据直接添加
            String umIdDb = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
            List<ConversationBean> dbList =
                    CrmDaoHolder.getInstance().getConversationBeanDao().queryForUmId(umIdDb);
            if (dbList == null || dbList.size() == 0) {
                conversationHttp = CommonUtils.encryptConversationData(conversationHttp);
                addDb(conversationHttp);
            } else {
                //如果在db中有这条，更新
                boolean isContain = false;
                for (int j = 0; j < dbList.size(); j++) {
                    ConversationBean conversationBeanDb = dbList.get(j);
                    if (conversationBeanDb.customerId.equals(conversationHttp.customerId) &&
                            conversationBeanDb.paImType.equals(conversationHttp.paImType) && conversationBeanDb.umId.equals(umIdDb)) {
                        ConversationBean conversationBeanEncrypted = CommonUtils.encryptConversationData(conversationHttp);
                        int count = conversationBeanDb.unReadCount;
                        CRMLog.LogInfo(Constants.LOG_TAG, "unread count:   " + count);
                        conversationHttp.unReadCount = count;
                        CrmDaoHolder.getInstance().getConversationBeanDao().updateUnreadDb(conversationBeanEncrypted, count, false);
                        isContain = true;
                    }
                }
                //没有在插入
                if (!isContain) {
                    conversationHttp = CommonUtils.encryptConversationData(conversationHttp);
                    addDb(conversationHttp);
                }
            }
        }
    }


    /**
     * 将数据存入本地
     *
     * @param conversationBean
     */
    private void addDb(ConversationBean conversationBean) {
//        conversationBean = CommonUtils.encryptConversationData(conversationBean);
        ConversationBean tmp_conversationBean = CrmDaoHolder.getInstance().getConversationBeanDao().queryConversation(conversationBean.customerId, conversationBean.paImType, conversationBean.umId);
        if (tmp_conversationBean == null) {
            CrmDaoHolder.getInstance().getConversationBeanDao().add(conversationBean);
        } else {
            CrmDaoHolder.getInstance().getConversationBeanDao().update(conversationBean);
        }
        tmp_conversationBean = CrmDaoHolder.getInstance().getConversationBeanDao().queryConversation(conversationBean.customerId, conversationBean.paImType, conversationBean.umId);

    }
}
