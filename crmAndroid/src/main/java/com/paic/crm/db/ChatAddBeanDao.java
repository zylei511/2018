package com.paic.crm.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.paic.crm.entity.ChatAddBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/4/18.
 */
public class ChatAddBeanDao {

    private Dao<ChatAddBean, Integer> chatAddBeanDao;

    private DatabaseHelper helper;

    public ChatAddBeanDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            chatAddBeanDao = helper.getDao(ChatAddBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ChatAddBean> queryAll(){
        List<ChatAddBean> chatAddBeans=null;
        try {
            chatAddBeans= chatAddBeanDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return chatAddBeans;
    }

    public int add(ChatAddBean chatAddBean){
        int result=-1;
        try {
            result=chatAddBeanDao.create(chatAddBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int clearChatData(){
        int result=-1;

        try {
            List<ChatAddBean> chatAddBeans=chatAddBeanDao.queryForAll();
            for (ChatAddBean chatAddBean:chatAddBeans){
//                result=chatAddBeanDao.delete(chatAddBean);
                DeleteBuilder<ChatAddBean,Integer> deleteBuilder=chatAddBeanDao.deleteBuilder();
                deleteBuilder.where().eq("iconName",chatAddBean.iconName);
                result=deleteBuilder.delete();
                CRMLog.LogInfo(Constants.LOG_TAG,"clearChatData   result"+result);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
