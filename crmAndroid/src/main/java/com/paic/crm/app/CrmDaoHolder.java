package com.paic.crm.app;

import android.content.Context;

import com.paic.crm.db.ChatAddBeanDao;
import com.paic.crm.db.ConstantsBeanDao;
import com.paic.crm.db.ConversationBeanDao;
import com.paic.crm.db.CustomMsgContentDao;
import com.paic.crm.db.LoginFieldValueDao;
import com.paic.crm.db.LoginUserBeanDao;

/**
 * Created by yueshaojun on 2017/8/22.
 */

public class CrmDaoHolder {
	private CrmDaoHolder(){}
	private LoginUserBeanDao loginUserBeanDao;
	private LoginFieldValueDao loginFieldValueDao;
	private CustomMsgContentDao customMsgContentDao;
	private ConversationBeanDao conversationBeanDao;
	private ConstantsBeanDao constantsBeanDao;
	private ChatAddBeanDao chatAddBeanDao;
	private static class Holder{
		private static CrmDaoHolder instance = new CrmDaoHolder();
	}
	public static CrmDaoHolder getInstance(){
		return Holder.instance;
	}
	public void initDb(Context context){
		loginFieldValueDao = new LoginFieldValueDao(context);
		loginUserBeanDao = new LoginUserBeanDao(context);
		customMsgContentDao = new CustomMsgContentDao(context);
		conversationBeanDao = new ConversationBeanDao(context);
		chatAddBeanDao = new ChatAddBeanDao(context);
		constantsBeanDao=new ConstantsBeanDao(context);
	}
	public LoginUserBeanDao getLoginUserBeanDao(){
		return loginUserBeanDao;
	}
	public LoginFieldValueDao getLoginFieldValueDao(){
		return loginFieldValueDao;
	}
	public CustomMsgContentDao getCustomMsgContentDao(){
		return customMsgContentDao;
	}
	public ConversationBeanDao getConversationBeanDao(){
		return conversationBeanDao;
	}
	public ChatAddBeanDao getChatAddBeanDao(){
		return chatAddBeanDao;
	}
	public ConstantsBeanDao getConstantsBeanDao(){
		return constantsBeanDao;
	}


}
