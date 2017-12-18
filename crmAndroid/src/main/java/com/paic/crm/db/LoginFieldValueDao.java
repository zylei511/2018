package com.paic.crm.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.paic.crm.entity.LoginFieldValueBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/3/25.
 */
public class LoginFieldValueDao {

    private Dao<LoginFieldValueBean, Integer> loginFieldValueBeanDao;

    private DatabaseHelper helper;

    public LoginFieldValueDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            loginFieldValueBeanDao = helper.getDao(LoginFieldValueBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add(LoginFieldValueBean loginFieldValueBean) {
        int result = 10000;
        try {
            result = loginFieldValueBeanDao.create(loginFieldValueBean);
        } catch (SQLException e) {
            e.printStackTrace();
            CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
        }
        return result;
    }

    public List<LoginFieldValueBean> queryAll(String umId) {
        List<LoginFieldValueBean> fieldValueBeans = null;
        try {
            QueryBuilder<LoginFieldValueBean,Integer> queryBuilder=loginFieldValueBeanDao.queryBuilder();
            queryBuilder.where().eq("umId",umId);
            fieldValueBeans = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fieldValueBeans;
    }

    public int deleteAll(List<LoginFieldValueBean> loginFieldValueBeans) {
        int result = -1;
        try {
            result = loginFieldValueBeanDao.delete(loginFieldValueBeans);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }
}
