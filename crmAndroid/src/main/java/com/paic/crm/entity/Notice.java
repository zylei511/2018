package com.paic.crm.entity;

import java.io.Serializable;

public class Notice implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = -584200363565733950L;
	/**
	 * 用户不带@的JID
	 */
	private String name;
	/**
	 * 消息条数
	 */
	private int count;
	/**
	 * 昵称或者备注名
	 */
	private String nickName;
	/**
	 * 群组 公众号 联系人
	 */
	private String type;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 消息类型 例如 图片 文本 音频
	 */
	private int msgtype;
	
	/**
	 * 标题
	 */
	private String title;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notice other = (Notice) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Notice() {
		super();
	}

	public Notice(String name) {
		super();
		this.name = name;
	}

}
