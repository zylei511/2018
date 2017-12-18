package com.paic.crm.sdk.ucmcore.serivce;

import android.text.TextUtils;

import com.pingan.paimkit.module.chat.bean.message.ChatMessageText;
import com.pingan.paimkit.module.chat.chatsession.PublicChatSession;
import com.pingan.paimkit.module.chat.manager.PMChatBaseManager;

/**
 * Created by yueshaojun on 2017/6/20.
 */

public class CrmChatSession extends PublicChatSession {
	public CrmChatSession(String jid_str, PMChatBaseManager chatBaseManager) {
		super(jid_str, chatBaseManager);
	}

	@Override
	public boolean sendTextMessage(String context_str, String messageId) {
		if(TextUtils.isEmpty(context_str)) {
			return false;
		} else {
			ChatMessageText text_message = new ChatMessageText();
			text_message.setMsgContent(context_str);
			text_message.setPrivateJid(null);
			text_message.setMsgPacketId(messageId);
			return this.sendChatMessage(text_message);
		}
	}
}
