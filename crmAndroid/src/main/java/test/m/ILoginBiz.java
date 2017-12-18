package test.m;

import android.content.Context;


import test.listener.LoginListener;

/**
 * Created by yueshaojun988 on 2017/9/8.
 */

public interface ILoginBiz {
    void login(Context context, String umId, String password, LoginListener loginListener);
}
