package com.paic.crm.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 *
 * 会话实体类
 * Created by hanyh on 16/1/22.
 * {
 "data":
 [{"customerId":"xiaoxiong123",
 " customerIcon":"img",
 "clientMobileNo":"18616611120",
 " customerName":"张三",
 "imNickName":"三儿",
 " msg ":"你好啊",
 "createTime ":1458098603000,
 "paImType":"01",
 "umId ":"CUIJUN629",
 “msgType”:”txt”,
 “messageId”:”dafdsfasdfasdfads”}],
 "resultCode":"02",
 "resultMsg":"程序调用成功"
 }
 */
@DatabaseTable(tableName = "tb_conversation")
public class ConversationBean implements Serializable,Comparable,Cloneable{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String customerId;
    @DatabaseField
    public String customerIcon;
    @DatabaseField
    public String clientMobileNo;
    @DatabaseField
    public String customerName;
    @DatabaseField
    public String imNickName;
    @DatabaseField
    public String msg;
    @DatabaseField
    public String createTime;
    @DatabaseField
    public String paImType;
    @DatabaseField
    public String umId;
    @DatabaseField
    public String msgType;
    @DatabaseField
    public String messageId;
    @DatabaseField
    public String dialogueStatus;
    @DatabaseField
    public String status;

    public String totalPage;


    public String nowPage;
    @DatabaseField
    public int unReadCount;

    public String historyTime;

    public long sendTime;




    @Override
    public int compareTo(Object another) {
        String lhsTime=this.createTime;
        String rhsTime=((ConversationBean) another).createTime;
        Double v1 = Double.valueOf(lhsTime.toString());
        Double v2 = Double.valueOf(rhsTime.toString());
        if (v1 == v2){
            return 0;
        }else if (v1>v2){

            return -1;
        }else {
            return 1;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    @Override
    public String toString() {
        return "ConversationBean{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", customerIcon='" + customerIcon + '\'' +
                ", clientMobileNo='" + clientMobileNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", imNickName='" + imNickName + '\'' +
                ", msg='" + msg + '\'' +
                ", createTime='" + createTime + '\'' +
                ", paImType='" + paImType + '\'' +
                ", umId='" + umId + '\'' +
                ", msgType='" + msgType + '\'' +
                ", messageId='" + messageId + '\'' +
                ", dialogueStatus='" + dialogueStatus + '\'' +
                ", totalPage='" + totalPage + '\'' +
                ", nowPage='" + nowPage + '\'' +
                ", unReadCount=" + unReadCount +
                ", historyTime='" + historyTime + '\'' +
                ", sendTime=" + sendTime +
                ", status=" + status +
                '}';
    }
}
