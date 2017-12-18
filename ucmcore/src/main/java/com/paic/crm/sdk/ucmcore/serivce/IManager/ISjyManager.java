package com.paic.crm.sdk.ucmcore.serivce.IManager;

import com.pingan.paimkit.module.login.listener.ILoginManagerListener;

/**
 * Created by yueshaojun on 2017/6/2.
 */

public interface ISjyManager {
	void loginSjy(String token,
	              String customId,
	              String sourcesys,
	              String nikeName,
	              String timestamp,
	              ILoginManagerListener listener);
	void logoutSjy();
}
