package test.m;

import android.content.Context;

import com.android.volley.VolleyError;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.HttpUrls;

import test.listener.LoginListener;

/**
 * Created by yueshaojun988 on 2017/9/8.
 */

public class LoginImp implements ILoginBiz {
    @Override
    public void login(Context context, String umId, String password, final LoginListener loginListener) {
        VolleyRequest.httpLogin(context, HttpUrls.HTTP_LOGIN, "1", umId.toUpperCase(), password, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                //TODO 处理成功的逻辑
                loginListener.onSuccess(obj);
            }

            @Override
            public void onError(VolleyError ve) {
                //TODO 处理失败的逻辑
                loginListener.onError(ve);
            }

            @Override
            public void onLogOutside() {

            }
        });
    }
}
