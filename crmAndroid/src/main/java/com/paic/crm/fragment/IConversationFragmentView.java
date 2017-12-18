package com.paic.crm.fragment;

import com.paic.crm.entity.ConversationBean;
import com.paic.crm.ui.IBaseView;

/**
 * Created by ex-zhangyuelei001 on 2017/11/8.
 */

public interface IConversationFragmentView extends IBaseView {
    void onHttpSuccess(String str);

    void onHttpError();

    void onDeleteSuccess(ConversationBean successItem,int itemIndex);

    void onDeleteError();

    void onFinishSuccess(ConversationBean successItem,int itemIndex);

    void onFinishError();

}
