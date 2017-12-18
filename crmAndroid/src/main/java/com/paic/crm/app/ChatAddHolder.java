package com.paic.crm.app;

import com.paic.crm.android.R;
import com.paic.crm.entity.ChatAddBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueshaojun on 2017/8/22.
 */

public class ChatAddHolder {
	public static int[] icons = {R.drawable.icon_pictrue, R.drawable.icon_replay, R.drawable.icon_news,
			R.drawable.icon_send_messege};
	private static List<ChatAddBean> chatAddBeans = new ArrayList<>();
	public static void clearChatAddBeans(){
		chatAddBeans.clear();
	}
	public static void addChatAddBean(ChatAddBean addBean){
		chatAddBeans.add(addBean);
	}
	public static boolean contains(ChatAddBean targetBean){
		return chatAddBeans.contains(targetBean);
	}

	public static List<ChatAddBean> getChatAddBeans() {
		return chatAddBeans;
	}
}
