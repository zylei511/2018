package com.paic.crm.utils;

import com.paic.crm.entity.ChatDetail;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.NewMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyh on 16/4/15.
 */
public class ChatActivityPresenter {

    public List<NewMessageBean> chatList=new ArrayList<>();
    private List<NewMessageBean> extractHttpChatMsg(List<NewMessageBean> beans, ChatDetail chatDetail,String portrait,boolean isRefresh) {
        if (chatDetail != null) {
            if (chatDetail.chatList != null && chatDetail.chatList.size() > 0) {
                for (int i = 0; i < chatDetail.chatList.size(); i++) {
                    CustomMsgContent customMsgContent = chatDetail.chatList.get(i);
                    NewMessageBean newMessageBean = new NewMessageBean();
                    if (customMsgContent.fromType.equals("1")) {//系统
                        //TODO 系统如何显示
                    } else if (customMsgContent.fromType.equals("2")) {//客户
                        newMessageBean.setType(Constants.MSG_NULL);
                        customMsgContent.customerIcon = portrait;
                    } else {//客户
                        newMessageBean.setType(Constants.MSG_MY);
                        customMsgContent.customerIcon = "";
                    }

                    newMessageBean.customMsgContent = customMsgContent;
                    if (isRefresh) {
                        beans.add(newMessageBean);
                    } else {
                        chatList.add(newMessageBean);
                    }
                }
            }
        }
        return chatList;
    }
}
