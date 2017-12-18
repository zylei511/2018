package com.paic.crm.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.paic.crm.biz.IDataServiceBiz;
import com.paic.crm.biz.Imp.ConversationDataServiceImp;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.fragment.IConversationFragmentView;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HttpUrls;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2017/11/7
 */

public class ConversationFragmentPresenter extends BasePresenter<IConversationFragmentView> implements IHttpRefreshable,
        IDeletable {
    private IDataServiceBiz dataService;
    private Context context;
    private IConversationFragmentView conversationFragmentView;

    public ConversationFragmentPresenter(Context context, IConversationFragmentView conversationFragmentView) {
        this.context = context;
        this.conversationFragmentView = conversationFragmentView;
        this.dataService = new ConversationDataServiceImp(context, this,this);

    }


    /**
     * 获取本地数据
     *
     * @return
     */
    public List<ConversationBean> getDataFromDb(List<ConversationBean> list) {
        if (!isViewAttatched()){
            return new ArrayList<>();
        }
        return dataService.getDataFromDb(list);


    }

    /**
     * 从网络获取数据
     *
     * @return
     */
    public void getDataFromHttp() {
        if (!isViewAttatched()){
            return ;
        }
        if (BizSeriesUtil.isKZKF(context)){
            dataService.getDataFromHttp(HttpUrls.HTTP_CONVERSATION_KZKF);
        } else {
            dataService.getDataFromHttp(HttpUrls.HTTP_CONVERSATION);
        }

    }

    /**
     * 将为读数清空
     *
     * @param chatConversation
     */
    public void clearUnreadCount(ConversationBean chatConversation) {
        if (!isViewAttatched()){
            return ;
        }
        dataService.clearUnreadCount(chatConversation);
    }

    /**
     * 删除某条会话
     *
     */
    public void deleteConversation(ConversationBean conversationBean,int position) {
        String status = Constants.STATE_CONVERSATION_VAILD.equals(conversationBean.status) ?
                Constants.STATE_CONVERSATION_INVAILD : Constants.STATE_CONVERSATION_DELETE;
        String bizSeries = BizSeriesUtil.isKZKF(context) ? Constants.BIZ_SERIES_SDK_KZKF : "";

        status = TextUtils.isEmpty(bizSeries)?"":status;

        dataService.deleteData(conversationBean, position ,HttpUrls.HTTP_CONVERSATION_DELETE, status, bizSeries);
    }

    @Override
    public void onHttpSuccess() {
        if (!isViewAttatched()){
            return ;
        }
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        String refreshTime = df.format(new Date());
        conversationFragmentView.onHttpSuccess(refreshTime);
    }

    @Override
    public void onHttpError() {
        if (!isViewAttatched()){
            return ;
        }
        conversationFragmentView.onHttpError();
    }

    @Override
    public void onDeleteSuccess(ConversationBean conversationBean,int position) {
        if (!isViewAttatched()){
            return ;
        }
        conversationFragmentView.onDeleteSuccess(conversationBean,position);

    }

    @Override
    public void onDeleteError() {
        if (!isViewAttatched()){
            return ;
        }
        conversationFragmentView.onDeleteError();
    }

    @Override
    public void onFinishSuccess(ConversationBean conversationBean,int position) {
        if (!isViewAttatched()){
            return ;
        }
        conversationFragmentView.onFinishSuccess(conversationBean,position);
    }

    @Override
    public void onFinishError() {
        if (!isViewAttatched()){
            return ;
        }
        conversationFragmentView.onFinishError();
    }

    public List<ConversationBean> receiveNewMsg(CustomMsgContent newMsg, List<ConversationBean> conversationBeans) {
        if (!isViewAttatched()){
            return new ArrayList<>();
        }
        return dataService.receiveNewMewMsg(newMsg,conversationBeans);
    }
}
