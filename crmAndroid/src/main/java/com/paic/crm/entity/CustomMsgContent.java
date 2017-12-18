package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by hanyh on 16/3/23.
 *
 */
@DatabaseTable(tableName = "tb_msg_content")
public class CustomMsgContent implements Comparable<CustomMsgContent>,Cloneable,Serializable{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String customerName;//客户昵称
    @DatabaseField
    public String customerIcon;//客户头像地址
    @DatabaseField
    public String customerId;// 客户ID
    @DatabaseField
    public String msg;
    @DatabaseField
    public String msgType;//文本类型（text:文本，image:图片，audio:语音，h5,video:视频等)
    @DatabaseField
    public String paImType;//客户来源（客户im类型01微信，02-微博,03-android应用）
    @DatabaseField
    public String messageSeq;
    @DatabaseField
    public String umId;//坐席UMID
    @DatabaseField
    public String msgId;
    @DatabaseField
    public String createTime;
    @DatabaseField
    public String fromType;
    @DatabaseField
    public String messageId;
    @DatabaseField
    public String nowPage;
    @DatabaseField
    public String toType;
    @DatabaseField
    public int msgState;
    @DatabaseField
    public String isOverDialogue;

    @Override
    public int compareTo(CustomMsgContent another) {

        return this.createTime.compareTo(another.createTime);
    }

    @Override
    public String toString() {
        String jsonString = "{id:"+id+",customerName:"+customerName+",customerIcon:"+customerIcon+",customerId:"+customerId
                +",msg:"+msg+",msgType:"+msgType+",paImType:"+paImType+",umId:"+umId+",createTime:"+createTime+",fromType:"+fromType+",messageId:"
                +messageId+",msgState:"+msgState;
        return jsonString ;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
