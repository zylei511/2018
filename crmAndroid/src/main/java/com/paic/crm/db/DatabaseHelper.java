package com.paic.crm.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.paic.crm.entity.ChatAddBean;
import com.paic.crm.entity.ConstantsBean;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.LoginDataLoginUserBean;
import com.paic.crm.entity.LoginFieldValueBean;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.SpfUtil;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Map<String, Dao> daos = new HashMap<String, Dao>();
    private static DatabaseHelper instance;
    /**
     * 因为空中客服业务线
     * 在CustomMsgContent中添加了isOverDialogue字段，
     * 同时在ConversationBean中添加了status字段，
     * 所以将VERSION值设置为5
     */
    public static final int VERSION=5;
    private Context context;
    private DatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            CRMLog.LogInfo(Constants.LOG_TAG, "DB_VERSION   " + VERSION);
            TableUtils.createTable(connectionSource, LoginDataLoginUserBean.class);
            TableUtils.createTable(connectionSource, LoginFieldValueBean.class);
            TableUtils.createTable(connectionSource, CustomMsgContent.class);
            TableUtils.createTable(connectionSource, ConversationBean.class);
            TableUtils.createTable(connectionSource, ChatAddBean.class);
            TableUtils.createTable(connectionSource, ConstantsBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource arg1, int oldVer,
                          int newVer) {
        try {
            // TODO: 2017/11/22 分版本对待
            if (oldVer!=newVer){
                CRMLog.LogInfo(Constants.LOG_TAG, "oldVer   " + oldVer + "newVer   " + newVer);

                String STR= (String) SpfUtil.getValue(context, SpfUtil.LOGIN_FLAG, "");
                CRMLog.LogInfo(Constants.LOG_TAG, "STR---->   " + STR);
                TableUtils.dropTable(connectionSource, LoginDataLoginUserBean.class, true);
                TableUtils.dropTable(connectionSource, LoginFieldValueBean.class, true);
                TableUtils.dropTable(connectionSource, CustomMsgContent.class, true);
                TableUtils.dropTable(connectionSource, ConversationBean.class, true);
                TableUtils.dropTable(connectionSource, ChatAddBean.class, true);
                onCreate(database, connectionSource);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }


}
