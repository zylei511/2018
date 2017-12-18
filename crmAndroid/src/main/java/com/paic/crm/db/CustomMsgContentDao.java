package com.paic.crm.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.LoginDesUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/3/30.
 */
public class CustomMsgContentDao {

    private Dao<CustomMsgContent, Integer> msgContentDao;

    private DatabaseHelper helper;

    public CustomMsgContentDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            msgContentDao = helper.getDao(CustomMsgContent.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add(CustomMsgContent customMsgContent) {
        int result = 10000;
        try {
            result = msgContentDao.create(customMsgContent);
        } catch (SQLException e) {
            e.printStackTrace();
            CRMLog.LogInfo(Constants.LOG_TAG, " add error" + e.getMessage());
        }
        return result;
    }

    public int queryForId(CustomMsgContent customMsgContent) {
        int id = -1;
        try {
            String msgId = customMsgContent.messageId;
            QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
            queryBuilder.where().eq("messageId", msgId);
            CustomMsgContent content = queryBuilder.queryForFirst();
            if (content == null) {
                return id;
            } else {
                id = content.id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int deleteByMsgId(String msgId) {
        int result = -1;
        DeleteBuilder<CustomMsgContent, Integer> deleteBuilder = msgContentDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("messageId", msgId);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public long queryLastId(String umId, String customerId,String source) {
        long maxId = -1;
        QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
        try {
            queryBuilder.where().eq("umId", umId).and().eq("customerId", customerId).and().eq("paImType",source);
            maxId = queryBuilder.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    public CustomMsgContent queryForMesgId(String id) {
        CustomMsgContent customMsgContent = null;
        try {
            QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
            queryBuilder.where().eq("messageId", id);
            customMsgContent = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customMsgContent;
    }

    public List<CustomMsgContent> queryChat(String umId, String customerId,String source, long offset, long limite) {
        List<CustomMsgContent> customMsgContents = null;
        try {
            QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
            queryBuilder.orderBy("createTime",true).where().eq("umId", umId).and().eq("customerId", customerId).and().eq("paImType",source);
            customMsgContents = queryBuilder.query();
            queryBuilder.offset(customMsgContents.size() - 10);
            queryBuilder.limit(limite);
            customMsgContents = queryBuilder.query();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customMsgContents;
    }

    public List<CustomMsgContent> queryForCreateTime(String umId, String customerId, String createTime, String paImType,long offset, long limit) {
        List<CustomMsgContent> customMsgContents = null;
        try {
            QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
            queryBuilder.orderBy("createTime",true).where().eq("umId", umId).and().eq("customerId", customerId).and().lt("createTime", createTime).and().eq("paImType",paImType);
            customMsgContents = queryBuilder.query();
            int size = customMsgContents.size();
            queryBuilder.offset(size - 10);
            queryBuilder.limit(limit);
            customMsgContents = queryBuilder.query();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customMsgContents;
    }

    public List<CustomMsgContent> queryCusContents(String umId, String customerId, String source) {
        List<CustomMsgContent> customMsgContents = null;
        try {
            QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
            queryBuilder.where().eq("umId", umId).and().eq("customerId", customerId).and().eq("paImType",source);
//            queryBuilder.where().eq("customerId", customerId);
//            queryBuilder.offset(offset);
//            queryBuilder.limit(limite);
            customMsgContents = queryBuilder.query();
            CRMLog.LogInfo(Constants.LOG_TAG, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customMsgContents;
    }

    public int deleteAll(String umId) {
        int result = -1;

        try {
            DeleteBuilder<CustomMsgContent, Integer> deleteBuilder = msgContentDao.deleteBuilder();
            deleteBuilder.where().eq("umId", LoginDesUtil.encryptToURL(umId,LoginDesUtil.ZZGJSPWDKEY));
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    public int update(CustomMsgContent customMsgContent) {

        int result = -1;

        try {
            String messageId = customMsgContent.messageId;
            int msgStage = customMsgContent.msgState;
            UpdateBuilder<CustomMsgContent, Integer> updateBuilder = msgContentDao.updateBuilder();
            updateBuilder.where().eq("messageId", messageId);
            updateBuilder.updateColumnValue("msgState",msgStage);
            updateBuilder.updateColumnValue("customerName",customMsgContent.customerName);
            updateBuilder.updateColumnValue("customerIcon",customMsgContent.customerIcon);
            updateBuilder.updateColumnValue("customerId",customMsgContent.customerId);
            updateBuilder.updateColumnValue("msg",customMsgContent.msg);
            updateBuilder.updateColumnValue("msgType",customMsgContent.msgType);
            updateBuilder.updateColumnValue("paImType",customMsgContent.paImType);
            updateBuilder.updateColumnValue("umId",customMsgContent.umId);
            updateBuilder.updateColumnValue("msgId",customMsgContent.msgId);
            updateBuilder.updateColumnValue("createTime",customMsgContent.createTime);
            updateBuilder.updateColumnValue("fromType",customMsgContent.fromType);
            updateBuilder.updateColumnValue("toType",msgStage);
            result = updateBuilder.update();
            CRMLog.LogInfo(Constants.LOG_TAG,"");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public CustomMsgContent queryFirstItem(String umId, String customerId,String source) {
        CustomMsgContent customMsgContent = new CustomMsgContent();
        QueryBuilder<CustomMsgContent, Integer> queryBuilder = msgContentDao.queryBuilder();
        try {
            queryBuilder.where().eq("umId", umId).and().eq("customerId", customerId).and().eq("paImType",source);
            customMsgContent = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customMsgContent;
    }
    public void addOrUpdate(CustomMsgContent customMsgContent){
        CustomMsgContent tmp_customMsgContent =
                CrmDaoHolder
                        .getInstance()
                        .getCustomMsgContentDao()
                        .queryForMesgId(customMsgContent.messageId);
        if(tmp_customMsgContent==null) {
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .add(customMsgContent);
        }else {
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .update(customMsgContent);
        }
    }
}
