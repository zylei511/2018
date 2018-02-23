package com.paic.crm.inputhelper.entity;

/**
 * @author MartinLi 2018/2/1209:46
 *         用于底部按钮的点击状态
 *         {@link com.paic.crm.inputhelper.fragment.ChatButtonFragment}
 */

public class BottomStatusBean {
	public int bacegroundRes;
	public String showMsg;
	public String showTip;

	public BottomStatusBean() {
	}

	/**
	 * @param bacegroundRes
	 * @param showMsg
	 * @param showTip
	 */
	public BottomStatusBean(int bacegroundRes, String showMsg, String showTip) {
		this.bacegroundRes = bacegroundRes;
		this.showMsg = showMsg;
		this.showTip = showTip;
	}

	public int getBacegroundRes() {
		return bacegroundRes;
	}

	public void setBacegroundRes(int bacegroundRes) {
		this.bacegroundRes = bacegroundRes;
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	public String getShowTip() {
		return showTip;
	}

	public void setShowTip(String showTip) {
		this.showTip = showTip;
	}
}
