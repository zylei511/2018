package test.p;

import android.content.Context;
import android.content.res.ObbInfo;

import test.listener.LoginListener;
import test.m.ILoginBiz;
import test.m.LoginImp;
import test.v.ILoginView;

/**
 * Created by yueshaojun988 on 2017/9/8.
 */

public class Presenter implements LoginListener{
    private ILoginView mLoginView;
    private ILoginBiz mLoginBiz;
    public Presenter(ILoginView loginView){
        mLoginView = loginView;
        mLoginBiz = new LoginImp();
    }
    public void login(Context context,String umId,String password){
        mLoginBiz.login(context,umId,password,this);
    }

    @Override
    public void onSuccess(Object o) {
        mLoginView.onLoginSuccess(o);
    }

    @Override
    public void onError(Object o) {
        mLoginView.onLoginError(o);
    }
}
