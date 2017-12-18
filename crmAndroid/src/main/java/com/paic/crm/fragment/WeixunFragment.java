package com.paic.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.TextView;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.android.R;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.JsonCallback;
import com.paic.crm.app.AppContext;
import com.paic.crm.callback.WebViewCallback;
import com.paic.crm.entity.NativeForH5;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.ui.SecondWebviewActivity;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.CustomWebView;
import com.paic.crm.widget.PullToRefreshLayout;
import com.tendcloud.tenddata.TCAgent;

import java.lang.ref.WeakReference;


/**
 * Created by hanyh on 16/1/21.
 */
public class WeixunFragment extends Fragment implements WebViewCallback ,JsonCallback,PullToRefreshLayout.OnRefreshListener{

    private CustomWebView wb_weixun;
    private TextView share_btn;
    private String umId;
    private PullToRefreshLayout pullToRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CRMLog.LogInfo("onAttach", "onCreateView");
        View view = inflater.inflate(R.layout.weixun_layout, null);
        init(view);
        share_btn = (TextView)getActivity().findViewById(R.id.iv_refresh);
        share_btn.setText("");
        initPparams();
        addListeners();
        return view;
    }

    private void addListeners() {
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    private void init(View view) {
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
        pullToRefreshLayout.setRefreshEnable(true);
        pullToRefreshLayout.setLoadMoreEnable(false);
        wb_weixun = (CustomWebView) view.findViewById(R.id.wb_weixun);
        wb_weixun.setWebViewCallback(this);
    }

    @Override
    public void onAttach(Context context) {
        CRMLog.LogInfo("onAttach", "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        CRMLog.LogInfo("onAttach", "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    private void initPparams() {
        umId = (String) SpfUtil.getValue(getActivity(), SpfUtil.UMID, "");
        String url = HttpUrls.INFORMATION_URL;
        String param = CommonUtils.getH5P(HttpUrls.INFORMATION_TEST_KEY,umId);
        String urlAll = url + param;
        NativeForH5 nativeForH5 =new NativeForH5();
        nativeForH5.setContext(getContext());
        CRMLog.LogInfo(Constants.LOG_TAG, "weiXun   umId" + umId + "    " + "weiXun   param" + param);
        nativeForH5.setJsonCallback(this);
        wb_weixun.addJavascriptInterface(nativeForH5, "android");
        CRMLog.LogInfo("url", urlAll);
        wb_weixun.loadUrl(urlAll);
    }

    @Override
    public void onResume() {
        super.onResume();
        CRMLog.LogInfo("onAttach", "onResume");
        if(this.isVisible()) {
            share_btn.setVisibility(View.VISIBLE);
            TCAgent.onPageStart(getActivity(), PATDEnum.NEWS.getName());
        }

        CrmEnvValues.getInstance().setWebViewLoadFinished(true);
        CRMLog.LogInfo("test", "onResume()");

    }

    @Override
    public void onStop() {
        super.onStop();
        if(isVisible()) {
            TCAgent.onPageEnd(getActivity(), PATDEnum.NEWS.getName());
        }
    }

    @Override
    public void webViewHomePageCalllback(String url) {

        if (url.contains("404")){
            initPparams();
            CRMLog.LogInfo(Constants.LOG_TAG,"404 html");
        }else {

            Bundle bundle = new Bundle();
            bundle.putString("wbUrl", url);
            bundle.putString("h5Type","微讯");
            ((HomeActivity) getActivity()).toActivity(getActivity(), SecondWebviewActivity.class, bundle);
        }
    }

    @Override
    public void isPageFinished(boolean isFinish) {
        CRMLog.LogInfo("weixun","ispagefinished"+isFinish);
        CrmEnvValues.getInstance().setWebViewLoadFinished(isFinish);
    }


    @Override
    public void onH5JsonResult(String json) {

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        CookieManager.getInstance().removeSessionCookie();
        wb_weixun.stopLoading();
        wb_weixun.setEnabled(false);
        wb_weixun.destroy();
        CRMLog.LogInfo(Constants.LOG_TAG, "weixun  onDestroy ");
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        initPparams();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
