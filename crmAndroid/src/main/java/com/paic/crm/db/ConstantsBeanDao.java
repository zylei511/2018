package com.paic.crm.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.paic.crm.entity.ConstantsBean;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by hanyh on 16/4/18.
 */
public class ConstantsBeanDao {

    private Dao<ConstantsBean, Integer> constantBeanDao;

    private DatabaseHelper helper;

    public ConstantsBeanDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            constantBeanDao = helper.getDao(ConstantsBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ConstantsBean> queryAll(){
        List<ConstantsBean> constantsBeans=null;
        try {
            constantsBeans= constantBeanDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return constantsBeans;
    }

    public int add(ConstantsBean constantsBean){

        int result=-1;
        try {
            result=constantBeanDao.create(constantsBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }
}
