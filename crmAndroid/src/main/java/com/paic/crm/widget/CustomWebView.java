package com.paic.crm.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.android.R;
import com.paic.crm.app.AppContext;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.WebViewCallback;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.pingan.core.im.client.IMClientConfig;
import com.tendcloud.tenddata.TCAgent;

/**
 * Created by hanyh on 16/3/14.
 */
public class CustomWebView extends WebView implements Pullable {

    private ProgressBar progressbar;
    public Handler mHandler;
    private WebSettings settings;
    private boolean isShouldLoad = false;
    private WebViewCallback webViewCallback;
    private String pageStartUrl = null;
    private String overrideUrl = "";
    private static String IB_TASK_TAG = "IB_TASK_LIST";
    private Context mContext;

    public void setWebViewCallback(WebViewCallback webViewCallback) {
        this.webViewCallback = webViewCallback;
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initConfigs();
        initProgressBar(context);
        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);
    }

    private void initProgressBar(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mHandler = new Handler();
        progressbar.setIndeterminate(false);
        progressbar.setMax(100);
        progressbar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.web_progress));
        progressbar.setIndeterminateDrawable(ContextCompat.getDrawable(context, android.R.drawable.progress_indeterminate_horizontal));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, CommonUtils.dp2px(context, 3), 0, 0));
        addView(progressbar);
    }

    private void initConfigs() {
        settings = getSettings();
        settings.setSupportZoom(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setPluginState(WebSettings.PluginState.ON);
//        settings.setUserAgentString(settings.getUserAgentString() + mUserAgent);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }


    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (isShouldLoad) {
                updateProgress(newProgress, 0);
            }

            super.onProgressChanged(view, newProgress);
        }

    };

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            CRMLog.LogInfo(Constants.LOG_TAG, "override url" + url);
            if (url.contains(IB_TASK_TAG)) {
                TCAgent.onEvent(mContext, PATDEnum.IB_TASK.getName(), PATDEnum.IB_TASK.getName());
            }
           if (!overrideUrl.equals(url)) {

                overrideUrl = url;
           }
            if (webViewCallback != null && CrmEnvValues.getInstance().isWebViewLoadFinish()) {
                webViewCallback.webViewHomePageCalllback(overrideUrl);
                CrmEnvValues.getInstance().setWebViewLoadFinished(false);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            CRMLog.LogInfo(Constants.LOG_TAG, "onPageStarted :    " + url);
            isShouldLoad = true;
            pageStartUrl = url;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webViewCallback != null) {
                webViewCallback.isPageFinished(true);
            }
            isShouldLoad = false;
            CRMLog.LogInfo(Constants.LOG_TAG, "onPageFinished :    " + url);
        }
    };


    /**
     * 设置进度
     *
     * @param newProgress
     * @param time
     */
    public synchronized void updateProgress(final int newProgress, final long time) {

        if (progressbar.getVisibility() == View.GONE) {
            progressbar.setVisibility(VISIBLE);
        }
        if (newProgress < 100) {
            CRMLog.LogInfo(Constants.LOG_TAG, "加载中..." + newProgress);
            progressbar.setProgress(newProgress);
        } else {
            CRMLog.LogInfo(Constants.LOG_TAG, "加载完成..." + newProgress);
            progressbar.setProgress(newProgress);
            progressbar.setVisibility(View.GONE);
        }

    }

    /**
     * webview cookie 设置
     */
    public void initConfigCookie() {
        String sessionId = IMClientConfig.getInstance().getLoginSession().getSession();

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(".pingan.com.cn", "hm_sessionid=" + sessionId);
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        if (getScrollY() >= getContentHeight() * getScale()
                - getMeasuredHeight())
            return true;
        else
            return false;
    }
//    public void goBack() {
//        this.goBack();
//    }
//
//
//    public void reLoad() {
//        this.reload();
//    }
//
//    public void forWard() {
//
//        this.forWard();
//    }
}
