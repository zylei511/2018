package com.paic.crm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.android.R;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.WebViewCallback;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.ui.SecondWebviewActivity;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.CustomWebView;
import com.paic.crm.widget.PullToRefreshLayout;
import com.tendcloud.tenddata.TCAgent;

/**
 * Created by hanyh on 16/1/21.
 */
public class WorkbenchFragment extends Fragment implements WebViewCallback,PullToRefreshLayout.OnRefreshListener{

    private CustomWebView wb_work_bench;
    private String umId;
    private PullToRefreshLayout pullToRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.work_bench_layout,null);
        initView(view);
        initPparams();
        addListener();
        return view;
    }

    private void initView(View view){
        wb_work_bench=(CustomWebView)view.findViewById(R.id.wb_work_bench);

        pullToRefreshLayout = (PullToRefreshLayout)view.findViewById(R.id.work_bench);
        pullToRefreshLayout.setRefreshEnable(true);
        pullToRefreshLayout.setLoadMoreEnable(false);
    }

    private void initPparams() {
        umId = (String) SpfUtil.getValue(getActivity(), SpfUtil.UMID, "");
        String url = HttpUrls.WORK_BEACH;
        String param = CommonUtils.getH5P(HttpUrls.INFORMATION_TEST_KEY, umId);
        String urlAll = url + param;
        CRMLog.isLogOpen = true;
        wb_work_bench.loadUrl(urlAll);
        CRMLog.LogInfo("webBench", urlAll);
    }

    private void addListener() {
        wb_work_bench.setWebViewCallback(this);
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void webViewHomePageCalllback(String url) {

        Bundle bundle = new Bundle();
        bundle.putString("wbUrl", url);
        bundle.putString("h5Type","工作台");
        ((HomeActivity) getActivity()).toActivity(getActivity(), SecondWebviewActivity.class, bundle);
    }

    @Override
    public void isPageFinished(boolean isFinish) {
        CrmEnvValues.getInstance().setWebViewLoadFinished(isFinish);
    }

    @Override
    public void onResume() {
        if(this.isVisible()) {
            TCAgent.onPageStart(getActivity(), PATDEnum.BRENCH.getName());
        }
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isVisible()){
            TCAgent.onPageEnd(getActivity(), PATDEnum.BRENCH.getName());
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        initPparams();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
