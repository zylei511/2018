package com.paic.crm.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.LoginDesUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/3/25.
 */
public class ConversationBeanDao {

    private Dao<ConversationBean, Integer> conversationBeanDao;

    private DatabaseHelper helper;

    public ConversationBeanDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            conversationBeanDao = helper.getDao(ConversationBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int add(ConversationBean conversationBean) {
        int result = 10000;
        try {
            result = conversationBeanDao.create(conversationBean);
        } catch (SQLException e) {
            e.printStackTrace();
            CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public int delete(ConversationBean conversationBean) {
        int result = -1;

        try {
            DeleteBuilder<ConversationBean, Integer> deleteBuilder = conversationBeanDao.deleteBuilder();
            deleteBuilder.where().eq("customerId", conversationBean.customerId);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int deleteAll(String umId){
        int result = -1;

        try {
            DeleteBuilder<ConversationBean, Integer> deleteBuilder = conversationBeanDao.deleteBuilder();
            deleteBuilder.where().eq("umId", LoginDesUtil.encryptToURL(umId,LoginDesUtil.ZZGJSPWDKEY));
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<ConversationBean> queryAll() {
        List<ConversationBean> conversationBeans = null;
        try {
            conversationBeans = conversationBeanDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversationBeans;
    }

    public List<ConversationBean> queryForUmId(String umId) {
        List<ConversationBean> conversationBeans = null;
        try {
            conversationBeans = conversationBeanDao.queryBuilder().where().eq("umId", umId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversationBeans;
    }


    public ConversationBean queryConversation(String customId, String paImType,String umId) {


        ConversationBean conversationBean = null;
        QueryBuilder<ConversationBean, Integer> queryBuilder = conversationBeanDao.queryBuilder();
        try {
            conversationBean = queryBuilder.where().eq("customerId", customId).and().eq("paImType", paImType).and().eq("umId",umId).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversationBean;
    }

    public int update(ConversationBean conversationBean) {
        int result = -1;

        try {
            result = conversationBeanDao.update(conversationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int updateUnread(CustomMsgContent msgDb, int unReadCount, boolean isChat) {

        int result = -1;

        try {
            String messageId = msgDb.messageId;
            String customerId = msgDb.customerId;
            String paImType = msgDb.paImType;
            String createTime = msgDb.createTime;
            String msg = msgDb.msg;
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType);
            updateBuilder.updateColumnValue("messageId", messageId);
            updateBuilder.updateColumnValue("createTime", createTime);
            if (!isChat) {
                updateBuilder.updateColumnValue("unReadCount", unReadCount);
            }

            updateBuilder.updateColumnValue("msg", msg);
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG, "updateUnread result"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public int updateDcrptUnread(NewMessageBean msgDb, int unReadCount, boolean isChat) {

        int result = -1;

        try {
            String messageId = msgDb.customMsgContent.messageId;
            String customerId = msgDb.customMsgContent.customerId;
            String paImType = msgDb.customMsgContent.paImType;
            String createTime = msgDb.customMsgContent.createTime;
            String msg = msgDb.customMsgContent.msg;
            String umId = msgDb.customMsgContent.umId;
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType).and().eq("umId",umId);
            updateBuilder.updateColumnValue("messageId", messageId);
            updateBuilder.updateColumnValue("createTime", createTime);
            if (!isChat) {
                updateBuilder.updateColumnValue("unReadCount", unReadCount);
            }

            updateBuilder.updateColumnValue("msg",msg);
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG, "updateUnread result"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public int updateUnread(ConversationBean msgDb, int unReadCount) {

        int result = -1;

        try {
            String messageId = msgDb.messageId;
            String customerId = msgDb.customerId;
            String paImType = msgDb.paImType;
            String createTime = msgDb.createTime;
            String msg = msgDb.msg;
            String umId = msgDb.umId;
            String clientMobileNo = msgDb.clientMobileNo;
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType).and().eq("umId",umId);
            updateBuilder.updateColumnValue("messageId", messageId);
            updateBuilder.updateColumnValue("createTime", createTime);
            if (clientMobileNo!=null){
                updateBuilder.updateColumnValue("clientMobileNo", clientMobileNo);
            }
            updateBuilder.updateColumnValue("unReadCount", unReadCount);
            updateBuilder.updateColumnValue("msg",msg);
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    public int updateUnreadHttp(ConversationBean msgDb, int unReadCount, boolean isChat) {

        int result = -1;

        try {
            String messageId = msgDb.messageId;
            String customerId = msgDb.customerId;
            String paImType = msgDb.paImType;
            String createTime = msgDb.createTime;
            String msg = msgDb.msg;
            String umId = msgDb.umId;
            String clientMobileNo = msgDb.clientMobileNo;
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType).and().eq("umId",umId);
            updateBuilder.updateColumnValue("messageId", messageId);
            updateBuilder.updateColumnValue("createTime", createTime);
            if (clientMobileNo!=null){
                updateBuilder.updateColumnValue("clientMobileNo", clientMobileNo);
            }
            if (!isChat) {
                updateBuilder.updateColumnValue("unReadCount", unReadCount);
            }
            updateBuilder.updateColumnValue("msg", LoginDesUtil.encryptToURL(msg, LoginDesUtil.ZZGJSPWDKEY));
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public int updateUnreadDb(ConversationBean msgDb, int unReadCount, boolean isChat) {

        int result = -1;

        try {
            String messageId = msgDb.messageId;
            String customerId = msgDb.customerId;
            String paImType = msgDb.paImType;
            String createTime = msgDb.createTime;
            String msg = msgDb.msg;
            String umId = msgDb.umId;
            String imNicName = msgDb.imNickName;
            String customerIcon = msgDb.customerIcon;
            String clientMobileNo = msgDb.clientMobileNo;
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType).and().eq("umId",umId);
            updateBuilder.updateColumnValue("messageId", messageId);
            updateBuilder.updateColumnValue("createTime", createTime);
            updateBuilder.updateColumnValue("customerIcon",customerIcon);
            updateBuilder.updateColumnValue("imNickName",imNicName);
            if (clientMobileNo!=null){
                updateBuilder.updateColumnValue("clientMobileNo", clientMobileNo);
            }
            if (!isChat) {
                updateBuilder.updateColumnValue("unReadCount", unReadCount);
            }
            updateBuilder.updateColumnValue("msg", msg);
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 更新更新会话状态
     * @param umId
     * @param customerId
     * @param paImType
     * @param status
     */
    public void updateConversationStatus(String umId, String customerId, String paImType, String status) {

        try {
            UpdateBuilder<ConversationBean, Integer> updateBuilder = conversationBeanDao.updateBuilder();
            updateBuilder.where().eq("customerId", customerId).and().eq("paImType", paImType).and().eq("umId", umId);
            updateBuilder.updateColumnValue("status", status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
