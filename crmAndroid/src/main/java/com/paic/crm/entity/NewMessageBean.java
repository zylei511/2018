package com.paic.crm.entity;

import android.text.SpannableString;

import java.io.Serializable;

public class NewMessageBean implements Serializable,Cloneable{

	public SpannableString mutiMsg;

	public String msgContent;

	public String mHyperLinkText;

	public String privateJid;

	public int _id;

	public int isDisplay;

	public int isUpload;

	public int mContentType;

	public String msgFrom;

	public String msgPacketId;

	public int msgProto;

	public int msgState;

	public String msgTo;

	public boolean offLineMessage;

	public boolean timeFlag;

	private String type;//试图类型

	private int account;//消息数量

	private long msgCreateCST;//消息收发时间

	private String msgType;//文本类型（text:文本，image:图片，audio:语音，video:视频等)


	private String h5Url;

	public CustomMsgContent customMsgContent;

	public String getH5Url() {
		return h5Url;
	}

	public void setH5Url(String h5Url) {
		this.h5Url = h5Url;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public long getMsgCreateCST() {
		return msgCreateCST;
	}

	public void setMsgCreateCST(long msgCreateCST) {
		this.msgCreateCST = msgCreateCST;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public NewMessageBean() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public NewMessageBean clone() throws CloneNotSupportedException {
		NewMessageBean cloneBean = (NewMessageBean) super.clone();
		cloneBean.customMsgContent = (CustomMsgContent) cloneBean.customMsgContent.clone();
		return cloneBean;
	}
}
