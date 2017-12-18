package com.paic.crm.app;

/**
 * Created by yueshaojun on 2017/8/22.
 */

public class CrmEnvValues {
	private boolean isFinish = false;//webView是否加载完成

	private CrmEnvValues(){}
	public static CrmEnvValues getInstance(){
		return Holder.instance;
	}
	private static class Holder{
		private static CrmEnvValues instance = new CrmEnvValues();
	}
	private boolean isInforationSend = false;
	private boolean isChatReceive = false;
	public void setChatReceived(boolean received){
		isChatReceive = received;
	}

	public boolean isChatReceive() {
		return isChatReceive;
	}

	public boolean isInforationSend() {
		return isInforationSend;
	}

	public void setInforationSend(boolean inforationSend) {
		isInforationSend = inforationSend;
	}
	public void setWebViewLoadFinished(boolean finished){
		isFinish = finished;
	}
	public boolean isWebViewLoadFinish(){
		return isFinish;
	}
}
