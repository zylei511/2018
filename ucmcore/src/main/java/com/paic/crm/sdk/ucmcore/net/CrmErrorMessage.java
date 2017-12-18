package com.paic.crm.sdk.ucmcore.net;

/**
 * Created by yueshaojun on 2017/7/7.
 */

public class CrmErrorMessage {
	public static String ERROR_CODE_NETWORK = "100000";//网络异常
	public static String ERROR_CODE_LOGIN_OUTSIDE = "1001";//在别处登陆
	public static String ERROR_CODE_DATA_FORMAT_DEFEATE = "100002";//解析失败
	public String errorCode;
	public String errorMsg;

	@Override
	public String toString() {
		return "CrmErrorMessage{" +
				"errorCode='" + errorCode + '\'' +
				", errorMsg='" + errorMsg + '\'' +
				'}';
	}
}
