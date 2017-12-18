package com.paic.crm.entity;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.paic.crm.app.AppContext;
import com.paic.crm.callback.DialOutCallback;
import com.paic.crm.callback.JsonCallback;
import com.paic.crm.callback.ShareCallback;
import com.paic.crm.callback.TouchIVRCallback;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.ui.ChatActivity;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.JSONStringUtil;
import com.paic.crm.utils.SpfUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;


/**
 * Created by yueshaojun on 16/3/3.
 */
public class NativeForH5 implements Serializable {
    private List<JSONObject> h5JosnList;
    private JsonCallback jsonCallback;
    private Context context;
    private String isConstantOrNewsTag;
    private ShareCallback shareCallback;
    private DialOutCallback dialOutCallback;
    private TouchIVRCallback touchIVRCallback;

    public void setShareCallback(ShareCallback shareCallback) {
        this.shareCallback = shareCallback;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @JavascriptInterface
    public void messageSelectedCallback(String json) {
        try {
            json = URLDecoder.decode(json, "utf-8");
            if (jsonCallback != null)
                this.jsonCallback.onH5JsonResult(json);
            CRMLog.LogInfo(Constants.LOG_TAG, h5JosnList.toString());
        } catch (Exception e) {
            CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
        }


    }

    @JavascriptInterface
    public void conversationCreatedCallback(String json) throws JSONException {
        try {
            json = URLDecoder.decode(json, "utf-8");
            CRMLog.LogInfo(Constants.LOG_TAG,"converJson"+json);
//            JSONObject jsonObject = new JSONObject(json);
//            jsonObject = (JSONObject) jsonObject.get("to");
//            ConstantsBean constantsBean = CommonUtils.handleHttpResult(ConstantsBean.class, jsonObject.toString());
            ChatActivity.actionStart(context, json);
            if (jsonCallback != null) {
                this.jsonCallback.onH5JsonResult("transOver");
            }
        } catch (UnsupportedEncodingException e) {
            CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
        }

    }

    @JavascriptInterface
    public void infoShareCallback(String jsonString) throws JSONException {
        isConstantOrNewsTag = "news";
        try {
            jsonString = URLDecoder.decode(jsonString, "utf-8");
            CRMLog.LogInfo(Constants.LOG_TAG, "gg" + jsonString);
            if (jsonCallback != null)
                this.jsonCallback.onH5JsonResult(jsonString);
        } catch (UnsupportedEncodingException e) {
            Log.i("infoShareerror", e.getMessage());
        }
    }

    @JavascriptInterface
    public void shareWeixin(String jsonStr) {

        try {
            String jsonString = URLDecoder.decode(jsonStr, "utf-8");
            if (shareCallback!=null){
                shareCallback.shareCallback(jsonString);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 触发IVR接口
     */
    @JavascriptInterface
    public void touchIVR(String jsonString){
        CRMLog.LogInfo(Constants.LOG_TAG, "touchIVR jsonString : " + jsonString);

        try {
            String jsonStr = URLDecoder.decode(jsonString, "utf-8");
            if (touchIVRCallback!=null){
                touchIVRCallback.touchIVR(jsonStr);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 拨打移动电话
     */
    @JavascriptInterface
    public void callOut(String jsonString){
        CRMLog.LogInfo(Constants.LOG_TAG, "callOut jsonString : " + jsonString);

        try {
            String jsonStr = URLDecoder.decode(jsonString, "utf-8");
            if (dialOutCallback!=null){
                dialOutCallback.callOut(jsonStr);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> getH5JosnList() {
        return h5JosnList;
    }

    public void setH5JosnList(List<JSONObject> h5JosnList) {
        this.h5JosnList = h5JosnList;
    }

    public void setJsonCallback(JsonCallback jsonCallback) {
        this.jsonCallback = jsonCallback;
    }

    public void setDialOutCallback(DialOutCallback dialOutCallback) {
        this.dialOutCallback = dialOutCallback;
    }

    public void setTouchIVRCallback(TouchIVRCallback touchIVRCallback) {
        this.touchIVRCallback = touchIVRCallback;
    }
}
