package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hanyh on 16/2/18.
 */
@DatabaseTable(tableName = "tb_chat_add")
public class ChatAddBean implements Serializable{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public int iconRes;
    @DatabaseField
    public String iconName;
    @DatabaseField
    public String clickImageUrl;
}
