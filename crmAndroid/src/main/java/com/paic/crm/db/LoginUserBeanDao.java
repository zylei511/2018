package com.paic.crm.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.paic.crm.entity.LoginDataLoginUserBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/3/25.
 */
public class LoginUserBeanDao {

    private Dao<LoginDataLoginUserBean, Integer> loginUserDao;

    private DatabaseHelper helper;
    public LoginUserBeanDao(Context context){
        try {
            helper = DatabaseHelper.getHelper(context);
            loginUserDao = helper.getDao(LoginDataLoginUserBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add(LoginDataLoginUserBean loginDataLoginUserBean) {
        int result=10000;
        try {
            result= loginUserDao.create(loginDataLoginUserBean);
        } catch (SQLException e) {
            e.printStackTrace();
            CRMLog.LogInfo(Constants.LOG_TAG,e.getMessage());
        }
        return result;
    }
    public List<LoginDataLoginUserBean> queryAll(){
        List<LoginDataLoginUserBean> banners=null;
        try {
            banners= loginUserDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return banners;
    }

}
