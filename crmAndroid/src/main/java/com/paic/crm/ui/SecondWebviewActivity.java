package com.paic.crm.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.android.BuildConfig;
import com.paic.crm.android.R;
import com.paic.crm.callback.DialOutCallback;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.JsonCallback;
import com.paic.crm.callback.ShareCallback;
import com.paic.crm.callback.TouchIVRCallback;
import com.paic.crm.callback.WebViewCallback;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.H5InformationBean;
import com.paic.crm.entity.NativeForH5;
import com.paic.crm.net.VolleyHttpConnector;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Config;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.NetUtils;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.CustomWebView;
import com.paic.crm.widget.PullToRefreshLayout;
import com.paic.crmimlib.listener.OnNewMessageListener;
import com.paic.crmimlib.serivce.CrmChatBaseManager;
import com.pingan.paimkit.module.chat.chatsession.BaseChatSession;
import com.pingan.paimkit.module.chat.manager.PMChatBaseManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by hanyh on 16/3/15.
 */
public class SecondWebviewActivity extends BaseActivity implements View.OnClickListener,
        JsonCallback, WebViewCallback, View.OnKeyListener, ShareCallback, PullToRefreshLayout.OnRefreshListener
,DialOutCallback,TouchIVRCallback,OnNewMessageListener{

    private CustomWebView wb_second;
    private String wbUrl, title;
    private ImageView iv_back;
    private NativeForH5 nativeForH5;
    private LinearLayout iv_back_parent;
    private BaseChatSession baseChatSession;
    private TextView actionTitle;
    private TextView shareTxt;
    private String umId;
    private IWXAPI api;
    private PopupWindow popupWindow;
    private View share_layout;
    private TextView share_firend;
    private TextView share_weixin;
    private LinearLayout me_root_layout;
    private boolean isSceneSession;
    private PullToRefreshLayout pullToRefreshLayout;
    private String mShiroKey;
    private final String RESULT_OK = "200";
    private static final String TAG = "SecondWebviewActivity";
    private static final String BURIED_POINT_NEW_MESSAGE = "new_message";
    private static final String BURIED_POINT_TOUCH_IVR = "touch_ivr";
    private static final String BURIED_POINT_TOUCH_IVR_SUCCESS = "touch_ivr_success";
    private static final String BURIED_POINT_TOUCH_IVR_ERROR = "touch_ivr_error";
    private static final String BURIED_POINT_CALL_OUT = "call_out";
    private static final String BURIED_POINT_CALL_OUT_SUCCESS = "call_out_success";
    private static final String BURIED_POINT_CALL_OUT_ERROR = "call_out_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_wb_layout);
        api = WXAPIFactory.createWXAPI(this, Config.getConfig("APP_ID"));
        api.registerApp(Config.getConfig("APP_ID"));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wbUrl = bundle.getString("wbUrl");
            CRMLog.LogInfo(Constants.LOG_TAG, "second wbUrl" + wbUrl);
            baseChatSession = PMChatBaseManager.getInstace().createChatSession(Constants.PUBLICK_KEY);
            title = bundle.getString("h5Type");
        }
        initData();
        initViews();
        addListeners();
    }

    private void addListeners() {
        iv_back_parent.setOnClickListener(this);
        wb_second.setOnKeyListener(this);
        share_firend.setOnClickListener(this);
        share_weixin.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
        CrmChatBaseManager.getInstance().addOnNewMessageListener(TAG,this);
    }

    private void initData() {
        umId = (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
        mShiroKey = (String)SpfUtil.getValue(this,SpfUtil.SHIROKEY,"");
    }

    private void initViews() {
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.second_wb_refresh);
        pullToRefreshLayout.setRefreshEnable(false);
        pullToRefreshLayout.setLoadMoreEnable(false);
        shareTxt = (TextView) findViewById(R.id.iv_refresh);
        shareTxt.setOnClickListener(this);
        wb_second = (CustomWebView) findViewById(R.id.wb_second);
        me_root_layout = (LinearLayout) findViewById(R.id.me_root_layout);
        share_layout = getLayoutInflater().inflate(R.layout.share_layout, null);
        share_firend = (TextView) share_layout.findViewById(R.id.share_firend);
        share_weixin = (TextView) share_layout.findViewById(R.id.share_weixin);
        actionTitle = (TextView) findViewById(R.id.action_title);
        actionTitle.setText(title);
        iv_back_parent = (LinearLayout) findViewById(R.id.iv_back_parent);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        nativeForH5 = new NativeForH5();
        nativeForH5.setContext(this);
        nativeForH5.setJsonCallback(this);
        nativeForH5.setShareCallback(this);
        nativeForH5.setTouchIVRCallback(this);
        nativeForH5.setDialOutCallback(this);
        wb_second.setWebViewCallback(this);
        wb_second.addJavascriptInterface(nativeForH5, "android");
        reload();
    }


    public void reload() {
        //生成新的url
//        replaceUrl();
        CRMLog.LogInfo(Constants.LOG_TAG, "second wbUrl = " + wbUrl);
        wb_second.loadUrl(wbUrl);
    }

    private void replaceUrl() {
        //每次拿到url都要替换p参数
        int index = wbUrl.indexOf("?");
        String param = wbUrl.substring(index + 1);
        String[] params = param.split("&");
        HashMap<String, String> paramsMap = new HashMap<>();
        for (String str : params) {
            String[] strs = str.split("=");
            paramsMap.put(strs[0], strs[1]);
        }
        //每次拿到url都要替换p参数
        paramsMap.put("p", CommonUtils.getH5P(HttpUrls.INFORMATION_TEST_KEY, umId));

        //将map变成字符串
        StringBuffer stringBuffer = convertToString(paramsMap);
        wbUrl = wbUrl.substring(0, index + 1) + stringBuffer;
    }


    @NonNull
    private StringBuffer convertToString(HashMap<String, String> paramsMap) {
        Iterator<String> iterator = paramsMap.keySet().iterator();
        StringBuffer stringBuffer = new StringBuffer("");
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = paramsMap.get(key);
            stringBuffer.append("&").append(key).append("=").append(value);
        }
        return stringBuffer;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_parent:
                if (wb_second.canGoBack()) {
                    wb_second.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.iv_refresh:
                TCAgent.onEvent(this, PATDEnum.SHARE_BTN.getName(),PATDEnum.SHARE_BTN.getName());
                showPopUp(share_layout);
                break;
            case R.id.share_firend:
                TCAgent.onEvent(this,PATDEnum.SHARE_FRIEND_CIRCLE.getName(),PATDEnum.SHARE_FRIEND_CIRCLE.getName());
                shareWeixin(true);
                break;

            case R.id.share_weixin:
                TCAgent.onEvent(this,PATDEnum.SHARE_WEIXIN.getName(),PATDEnum.SHARE_WEIXIN.getName());
                shareWeixin(false);
                break;
            default:
                break;
        }

    }

    private void shareWeixin(boolean scene){
        if (api.isWXAppInstalled()){
            isSceneSession=scene;
            wb_second.loadUrl("javascript:sendOneselfWeiXin(\"android\")");
        }else {
            Toast.makeText(this,"您还没有安装微信客户端",Toast.LENGTH_LONG).show();
        }
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onH5JsonResult(String json) {
        Log.i("native","webView :"+json);
        CrmEnvValues.getInstance().setInforationSend(true);
        H5InformationBean h5InformationBean = CommonUtils.handleHttpResult(H5InformationBean.class, json);

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject content = (JSONObject) jsonObject.get("content");
            String jsonContent = content.toString();
            CustomMsgContent customMsgContent = new CustomMsgContent();
            customMsgContent.msgType = "h5";
            customMsgContent.customerName = h5InformationBean.to.name;
            customMsgContent.customerIcon = h5InformationBean.to.portrait;
            customMsgContent.customerId = h5InformationBean.to.customerId;
            customMsgContent.msg = jsonContent;
            customMsgContent.paImType = h5InformationBean.to.source;
            customMsgContent.umId = h5InformationBean.content.typeContent.umid;
            customMsgContent.createTime = System.currentTimeMillis() + "";
            String cus = new Gson().toJson(customMsgContent);
            boolean isSend = baseChatSession.sendTextMessage(cus, null);
            CRMLog.LogInfo(Constants.LOG_TAG, isSend + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("transOver".equals(json)) {
            finish();
        }
    }

    @Override
    public void webViewHomePageCalllback(String url) {
        wbUrl=url;
        wb_second.loadUrl(url);

    }

    @Override
    public void isPageFinished(boolean isFinish) {
        CrmEnvValues.getInstance().setWebViewLoadFinished(isFinish);
        if (wbUrl.contains("pad-info/resource/resourceDetail.do?resourceId=")){
            shareTxt.setVisibility(View.VISIBLE);
            shareTxt.setText("分享");
        }else {
            shareTxt.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && wb_second.canGoBack()) {
                wb_second.goBack();
                return true;
            }
        }
        return false;
    }

    private void showPopUp(View contentView) {
        popupWindow = new PopupWindow(contentView, screenWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,
                        getResources().getDisplayMetrics()));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(me_root_layout, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wb_second.destroy();
        CRMLog.LogInfo(Constants.LOG_TAG, "onDestroy");
    }

    @Override
    public void shareCallback(String json) {
        try {
            JSONObject jsonObject=new JSONObject(json);
            String title=jsonObject.getString("title");
            String description=jsonObject.getString("description");
            String url=jsonObject.getString("url");
            String picurl=jsonObject.getString("picurl");
            requsetImage(url, title, description, picurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CRMLog.LogInfo(Constants.LOG_TAG, "shareWeixin" + json);
    }

    public void shareWeiXinSession(String webpageUrl,String title,String description,Bitmap thumb) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title =title;
        msg.description =description;
        if (thumb!=null){
            Bitmap bitmap=    ThumbnailUtils.extractThumbnail(thumb,50,50);
            msg.thumbData = CommonUtils.bmpToByteArray(bitmap, true);

        }else {
            Bitmap thumb_ = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
            msg.thumbData = CommonUtils.bmpToByteArray(thumb_, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(System.currentTimeMillis()+"");
        req.message = msg;
        req.scene = isSceneSession?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
        CRMLog.LogInfo(Constants.LOG_TAG, "shareWeiXinSession" + "结束");
    }

    public void requsetImage(final String url,final String title,final String dec,String picUrl){
        VolleyHttpConnector.volleyGetImage(this,picUrl, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                Bitmap bitmap = (Bitmap) obj;
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                shareWeiXinSession(url,title,dec,bitmap);
            }

            @Override
            public void onError(VolleyError ve) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                shareWeiXinSession(url,title,dec,null);
            }

            @Override
            public void onLogOutside() {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                CommonUtils.exitWhenHasLogIn(SecondWebviewActivity.this);
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        reload();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void callOut(String jsonStr) {
        //its调用成功，埋点
        buryPoint(jsonStr,BURIED_POINT_CALL_OUT);

        try {//{umId:"",number:"",requestId:"",callOutCallBack:""}
            JSONObject jsonObject = new JSONObject(jsonStr);
            //下面几个参数是与后台约定好的，修改的时候记得需要和后台确认
            //{"umId":"FANCHANGFENG998","number":"15257582822","requestId":"718583362601522","callOutCallback":"mobilePhoneHandleCallBack"}
            String umId = jsonObject.optString("umId");
            String number = jsonObject.optString("number");
            String requestId = jsonObject.optString("requestId");
            String callOutCallBack = jsonObject.optString("callOutCallback");
            //调用外呼接口
            callOutAndJS(umId, number, requestId, callOutCallBack);
        } catch (JSONException e) {
            e.printStackTrace();
            CRMLog.LogInfo(TAG, "callOut e = "+e.toString());
        }


    }

    /**
     * 调用外呼接口，并根据返回结果，回调Js方法
     * @param umId
     * @param number
     * @param requestId
     * @param callbackMethod
     */
    private void callOutAndJS(String umId, String number, String requestId, final String callbackMethod) {
        VolleyRequest.dialOut(this, umId, number, requestId, mShiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                //ccod调用成功，埋点
                buryPoint(obj.toString(),BURIED_POINT_CALL_OUT_SUCCESS);

                CRMLog.LogInfo(TAG, "callOut onSuccess ve.getMessage : " + obj.toString());
                wb_second.loadUrl("javascript:" + callbackMethod + "(" + obj.toString() + ")");
            }

            @Override
            public void onError(VolleyError ve) {
                //ccod调用成功，埋点
                buryPoint(ve.toString(),BURIED_POINT_CALL_OUT_ERROR);

                CRMLog.LogInfo(TAG, "callOut onError ve.getMessage : " + ve.getMessage());
            }

            @Override
            public void onLogOutside() {

            }
        });
    }

    @Override
    public void touchIVR(String jsonStr) {
        //its调用成功，埋点
        buryPoint(jsonStr,BURIED_POINT_TOUCH_IVR);

        try {//{umId:"",requestId:"",touchIVRCallback:""}
            JSONObject jsonObject = new JSONObject(jsonStr);
            //// TODO: 2017/9/1 同样 这个地方的umId是不是要替换掉本地的
            String umId = jsonObject.optString("umId");
            String requestId = jsonObject.optString("requestId");
            String touchIVRCallback = jsonObject.optString("touchIVRCallback");
            //调用外呼接口
            touchIVRAndJs(umId, requestId, touchIVRCallback);
        } catch (JSONException e) {
            CRMLog.LogInfo(TAG, "touchIVR e = "+e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 调用touch接口，并回调
     * @param umId
     * @param requestId
     * @param touchIVRCallback
     */
    private void touchIVRAndJs(String umId, String requestId, final String touchIVRCallback) {
        VolleyRequest.touchIVR(this, umId, requestId, mShiroKey, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                //ccod调用成功，埋点
                buryPoint(obj.toString(),BURIED_POINT_TOUCH_IVR_SUCCESS);

                CRMLog.LogInfo(TAG, "touchIVR onSuccess ve.getMessage : " + obj.toString());
                //暂时定为这个，后续需要看后台传回的数据结构是否符合要求
                wb_second.loadUrl("javascript:" + touchIVRCallback + "(" + obj.toString() + ")");
            }

            @Override
            public void onError(VolleyError ve) {
                //ccod调用失败，埋点
                buryPoint(ve.toString(),BURIED_POINT_TOUCH_IVR_ERROR);

                CRMLog.LogInfo(TAG, "touchIVR onError ve.toString() : " + ve.toString());

            }

            @Override
            public void onLogOutside() {

            }
        });
    }


    @Override
    public void onNewMessage(final String s) {
        //// TODO: 2017/9/4 这个方法没有在分发MSG的时候把事件和消息两种类型分开，所以在这里要自己判断，后面可能会分开处理，在此标注 by zhangyuelei001

        CRMLog.LogInfo(TAG, "onNewMessage ve.toString() : " + s);
        try {
            JSONObject jsonObject = new JSONObject(s);
            String dealMsgType = jsonObject.optString(Constants.MSG_TYPE_KEY);
            String eventType = jsonObject.optString(Constants.MSG_EVENT_TYPE_KEY);
            //判断是事件还是消息，这里只处理事件，不处理消息
            if (Constants.MSG_TYPE_VALUE_EVENT.equals(dealMsgType) && Constants.MSG_EVENT_TYPE_VALUE_IVR.equals(eventType)) {
                //收到IVR回调时，埋点
                buryPoint(s,BURIED_POINT_NEW_MESSAGE);

                //调用H5页面的js方法
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wb_second.loadUrl("javascript:" + "onEvent" + "(" + s + ")");
                    }
                });

            }
        } catch (JSONException e) {
            CRMLog.LogInfo(TAG, "onNewMessage e"+e.toString());
            e.printStackTrace();
        }

    }

    /**
     * 埋点
     * @param jsonStr
     * @param point
     */
    private void buryPoint(String jsonStr,String point){
        CRMLog.LogInfo(TAG, "buryPoint");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type","ANDROID");
            jsonObject.put("sys_version", android.os.Build.VERSION.SDK_INT);
            jsonObject.put("net_operator", NetUtils.getCategorizedSubTypeName(this));
            jsonObject.put("app_version",BuildConfig.VERSION_NAME);
            jsonObject.put("param",jsonStr);
            jsonObject.put("position",point);
            VolleyRequest.httpBuriedPoint(this, umId, jsonObject.toString(), mShiroKey, new VolleyInterface() {
                @Override
                public void onSuccess(Object obj) {
                    CRMLog.LogInfo(TAG, "buryPoint onSuccess :"+obj.toString());
                }

                @Override
                public void onError(VolleyError ve) {
                    CRMLog.LogInfo(TAG, "buryPoint onError :"+ve.toString());
                }

                @Override
                public void onLogOutside() {

                }
            });
        } catch (JSONException e) {
            CRMLog.LogInfo(TAG, "buryPoint e"+e.toString());
            e.printStackTrace();
        }


    }
}
