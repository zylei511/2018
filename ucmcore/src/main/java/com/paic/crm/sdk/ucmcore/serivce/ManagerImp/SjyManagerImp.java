package com.paic.crm.sdk.ucmcore.serivce.ManagerImp;

import com.paic.crm.sdk.ucmcore.serivce.CrmChatBaseManager;
import com.paic.crm.sdk.ucmcore.serivce.IManager.ISjyManager;
import com.pingan.paimkit.module.login.bean.LoginErrorBean;
import com.pingan.paimkit.module.login.listener.ILoginManagerListener;
import com.pingan.paimkit.module.login.manager.PMLoginManager;

/**
 * Created by yueshaojun on 2017/6/2.
 */

public class SjyManagerImp implements ISjyManager {
	@Override
	public void loginSjy(String token, String customId, String sourcesys, String nikeName, String timestamp, final ILoginManagerListener listener) {
		PMLoginManager.getInstance().loginFromThird(token, customId, sourcesys, nikeName, timestamp, new ILoginManagerListener() {
			@Override
			public void onLoginSuccess(String s) {
				CrmChatBaseManager.getInstance();
				listener.onLoginSuccess(s);
			}

			@Override
			public void onLoginError(LoginErrorBean loginErrorBean) {
				listener.onLoginError(loginErrorBean);
			}
		});
	}

	@Override
	public void logoutSjy() {
		PMLoginManager.getInstance().loginOut();
	}
}
