package com.paic.crm.biz;

import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;

import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/11/7.
 */

public interface IDataServiceBiz {
    List<ConversationBean> getDataFromDb(List<ConversationBean> list);

    void getDataFromHttp(String url);

    void deleteData(ConversationBean conversationBean, int position ,String deleteUrl, String status, String bizSeries);

    void clearUnreadCount(ConversationBean chatConversation);

    List<ConversationBean> receiveNewMewMsg(CustomMsgContent newMsg, List<ConversationBean> conversationBeans);
}
