package com.paic.crm.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanyh on 16/3/31.
 */
public class ConversationResultBean implements Serializable {

    public List<ConversationBean> data;

    public String resultCode;

    public String resultMsg;

}
