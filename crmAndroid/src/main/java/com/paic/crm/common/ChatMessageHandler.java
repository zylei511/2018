package com.paic.crm.common;

import android.os.AsyncTask;

import com.pingan.core.im.log.PALog;
import com.pingan.core.im.parser.JidManipulator;
import com.pingan.paimkit.module.chat.manager.PMChatBaseManager;
import com.pingan.paimkit.module.conversation.manager.PMConversationManager;


/**
 * Created by hanyh on 16/3/3.
 * 以后处理未读消息等细节
 */
public class ChatMessageHandler {


    /**
     * 加载未读数和最后一条消息的消息ID
     */
    private class LoadUnreadMsgCountTask extends AsyncTask<Void, Void, int[]> {
        String username;

        public LoadUnreadMsgCountTask(String userName) {
            username = userName;
        }

        protected int[] doInBackground(Void... params) {

            int count = PMConversationManager.getInstance().getUnreadCountByUsername(
                    JidManipulator.Factory.create().getUsername(username));
            int index = PMChatBaseManager.getInstace().getLastMesssageId(
                    JidManipulator.Factory.create().getUsername(username));
            PALog.i("unread", "加载未读数 unreadcount：" + count + "最后一条消息的消息ID" + index);

            return new int[] { count, index };
        }

        protected void onPostExecute(int[] result) {
            // 无论结果怎样，加载聊天记录
//            loadMessages();
            if (result[0] < 0) {
                return;
            }

 //           showUnreadMsgLocateTips(result[0], result[1]);
        }
    }



}
