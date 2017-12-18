package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hanyh on 16/3/29.
 */
@DatabaseTable(tableName = "tb_constant")
public class ConstantsBean implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String customerId;
    @DatabaseField
    public String customerNo;
    @DatabaseField
    public String name;
    @DatabaseField
    public String portrait;
    @DatabaseField
    public String source;
    @DatabaseField
    public String dialogueStatus;
}
