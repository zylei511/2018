package com.paic.crm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paic.crm.android.R;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.JsonCallback;
import com.paic.crm.callback.WebViewCallback;
import com.paic.crm.entity.NativeForH5;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.CustomWebView;
import com.paic.crm.widget.PullToRefreshLayout;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by pingan001 on 16/1/21.
 */
public class WebViewActivity extends BaseActivity implements JsonCallback,View.OnClickListener,WebViewCallback,PullToRefreshLayout.OnRefreshListener {

    private CustomWebView webView;
    private NativeForH5 nativeForH5;
    private ImageView iv_back;
    private String loadUrl,title,url;
    private LinearLayout iv_back_parent;
    private TextView action_title;
    private String umId;
    private String urlFrom;
    private PullToRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            url= bundle.getString("url");
            title= bundle.getString("h5Type");
            urlFrom = bundle.getString("urlFrom");
            CRMLog.LogInfo("this",url);
        }
        initData();
        initWebView();
        addListeners();
    }

    private void addListeners() {
        iv_back_parent.setOnClickListener(this);
    }

    private void initData(){
        umId = (String)SpfUtil.getValue(this, SpfUtil.UMID, "");
        iv_back_parent=(LinearLayout)findViewById(R.id.iv_back_parent);
        action_title = (TextView)findViewById(R.id.action_title);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.web_view);
        pullToRefreshLayout.setOnRefreshListener(this);
        pullToRefreshLayout.setRefreshEnable(true);
        pullToRefreshLayout.setLoadMoreEnable(false);
                ((TextView) findViewById(R.id.action_title)).setText(title);
        webView = (CustomWebView) findViewById(R.id.webView);
        webView.setWebViewCallback(this);
        if (title.equals("资讯")){
            String[] resUrls=url.split("&");
            for (String s:resUrls){
                if (s.startsWith("resourceUrl=")){
                    int index=s.indexOf("=");
                    String url=s.substring(index+1);
                    try {
                        loadUrl= URLDecoder.decode(url,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }else {
            loadUrl =url+CommonUtils.getH5P(HttpUrls.INFORMATION_TEST_KEY, umId);
        }
        nativeForH5  = new NativeForH5();
        nativeForH5.setJsonCallback(this);
    }

    private void initWebView(){
        webView.addJavascriptInterface(nativeForH5, "android");
        webView.loadUrl(loadUrl);
    }

    @Override
    public void onH5JsonResult(String json) {

        Intent intent=new Intent(this,ChatActivity.class);
        Bundle bundle=new Bundle();
//        String obj=objects.toString();
        bundle.putString("jsons",json);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        CRMLog.LogInfo(Constants.LOG_TAG, "objects.size()  ---" + json);
    }

    public void reload(){
        if(!"chat".equals(urlFrom)) {
            loadUrl = url + CommonUtils.getH5P(HttpUrls.INFORMATION_TEST_KEY, umId);
        }
        CRMLog.LogInfo("this,,",loadUrl);
        webView.loadUrl(loadUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_parent:
                finish();
                break;
        }

    }

    @Override
    public void webViewHomePageCalllback(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("wbUrl", url);
        toActivity(this, SecondWebviewActivity.class, bundle);
    }

    @Override
    public void isPageFinished(boolean isFinish) {
        CrmEnvValues.getInstance().setWebViewLoadFinished(isFinish);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        reload();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
