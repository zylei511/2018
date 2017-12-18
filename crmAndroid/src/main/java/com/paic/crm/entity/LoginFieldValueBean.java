package com.paic.crm.entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hanyh on 16/3/24.
 *
 */
@DatabaseTable(tableName = "tb_login_field")
public class LoginFieldValueBean  implements Serializable {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String clickUrl;
    @DatabaseField
    public String iconNormal;
    @DatabaseField
    public String iconSelect;
    @DatabaseField
    public String tabNameColor;
    @DatabaseField
    public String tabButtonName;
    @DatabaseField
    public String tabNameSelectColor;
    @DatabaseField
    public String umId;
    @DatabaseField
    public String typeMark;
    @DatabaseField
    public String isHome;
}
