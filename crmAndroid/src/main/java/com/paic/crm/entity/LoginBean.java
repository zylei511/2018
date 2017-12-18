package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyh on 16/3/23.
 */
@DatabaseTable(tableName = "tb_login_bean")
public class LoginBean  implements Serializable {

    @DatabaseField(generatedId = true)
    public int id;


    @ForeignCollectionField
    public LoginData data;

    public String resultCode;

    public String resultMsg;

    public List<LoginFieldValueBean> loginFieldValueBeans=new ArrayList<>();//二次转换的json
    public List<SendMessageBean> sendMessageBeans=new ArrayList<>();
}
