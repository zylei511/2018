package com.paic.crm.presenter;

import com.paic.crm.entity.ConversationBean;

/**
 * Created by ex-zhangyuelei001 on 2017/11/8.
 */

public interface IDeletable {
    void onDeleteSuccess(ConversationBean conversationBean,int position);
    void onDeleteError();
    void onFinishSuccess(ConversationBean conversationBean,int position);
    void onFinishError();
}
