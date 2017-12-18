package com.paic.crm.net;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.paic.crm.utils.JSONStringUtil;


/**
 * Created by hanyh on 16/1/5.
 */
public abstract class VolleyInterface {

    private static Response.Listener successListener;

    private static Response.ErrorListener errorListener;


    public abstract void onSuccess(Object obj);

    public abstract void onError(VolleyError ve);

    public abstract void onLogOutside();

    public  Response.Listener getSuccessListener() {

        successListener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                if(JSONStringUtil.checkIsLogOutside(o)){
                    onLogOutside();
                }else {
                    onSuccess(o);
                }
            }
        };
        return successListener;
    }


    public  Response.ErrorListener getErrorListener() {

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onError(volleyError);
            }
        };
        return errorListener;
    }


}
