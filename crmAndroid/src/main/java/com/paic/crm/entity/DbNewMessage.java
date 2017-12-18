package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by hanyh on 16/3/30.
 */
@DatabaseTable(tableName = "tb_msg")
public class DbNewMessage {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String type;//试图类型
    @DatabaseField
    public CustomMsgContent customMsgContent;

}
