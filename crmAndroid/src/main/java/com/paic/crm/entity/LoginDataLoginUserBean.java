package com.paic.crm.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hanyh on 16/3/23.
 */
@DatabaseTable(tableName = "tb_login_user")
public class LoginDataLoginUserBean implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String bizSeries;
    @DatabaseField
    public String ctiCode;
    @DatabaseField
    public String iconUrl;
    @DatabaseField
    public String idEmployee;
    @DatabaseField
    public long lastLoginDate;
    @DatabaseField
    public String name;
    @DatabaseField
    public String personStatusCode;
    @DatabaseField
    public String phone;
    @DatabaseField
    public String platFormCode;
    @DatabaseField
    public String sex;
    @DatabaseField
    public String staffNo;
    @DatabaseField
    public String umId;
    @DatabaseField
    public String extensionNumber;
    @DatabaseField
    public String appStatus;

}
