package com.paic.crm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.ConversationHistoryAdapter;
import com.paic.crm.android.R;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DateFormatUtil;
import com.paic.crm.utils.DigestUtil;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.JSONStringUtil;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.MsgListView;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hanyh on 16/3/14.
 */
public class ConversationHistoryActivity extends BaseActivity implements View.OnClickListener,MsgListView.IXListViewListener,AbsListView.OnScrollListener,MsgListView.OnItemClickListener{

    private List<ConversationBean> conversationBeans=new ArrayList<>();
    private MsgListView mConversationListview;
    private ConversationHistoryAdapter mConversationAdapter;
    private ImageView iv_back;
    private LinearLayout iv_back_parent;
    private String umId;
    private String shiroKey;
    private String nowPage = "";
    private String totalPage = "";
    private boolean isLastRow = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_history_layout);
        initViews();
        addListeners();
    }

    private void addListeners() {
        iv_back_parent.setOnClickListener(this);
    }

    private void initViews() {
        mConversationListview = (MsgListView)findViewById(R.id.conversation_history_listView);
        iv_back_parent = (LinearLayout)findViewById(R.id.iv_back_parent);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        ((TextView)findViewById(R.id.action_title)).setText("历史会话");
        mConversationAdapter = new ConversationHistoryAdapter(this, conversationBeans);
        mConversationListview.setAdapter(mConversationAdapter);
        mConversationListview.setOnScrollListener(this);
        mConversationListview.setPullLoadEnable(false);
        mConversationListview.setPullRefreshEnable(true);
        mConversationListview.setXListViewListener(this);
        mConversationListview.setOnItemClickListener(this);
    }
    private void initDatas(){
        umId = (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
        shiroKey =  SpfUtil.getValue(this, SpfUtil.SHIROKEY, "").toString();
        nowPage = "1";
        queryConversationHistory(HttpUrls.HTTP_CONVERSATION_HISTORY, umId, nowPage, freshVolleyInterface, shiroKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TCAgent.onResume(this);
        initDatas();
        TCAgent.onPageStart(this, PATDEnum.HIS_CON.getName());
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TCAgent.onPause(this);
        TCAgent.onPageEnd(this, PATDEnum.HIS_CON.getName());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_back_parent:
                conversationBeans.clear();
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        CRMLog.LogInfo("conversation", "onRefresh...");
        mConversationListview.stopLoadMore();
        mConversationListview.setPullLoadEnable(false);
        nowPage = "1";
        queryConversationHistory(HttpUrls.HTTP_CONVERSATION_HISTORY, umId, nowPage, freshVolleyInterface, shiroKey);
        mConversationListview.stopRefresh();
    }


    @Override
    public void onLoadMore() {
        CRMLog.LogInfo("conversation", "onLoadMore...");
        nowPage = String.valueOf(Integer.parseInt(nowPage) + 1);
        queryConversationHistory(HttpUrls.HTTP_CONVERSATION_HISTORY, umId, nowPage, loadMoreInterface, shiroKey);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount){
        CRMLog.LogInfo("onScroll", visibleItemCount + "--" + totalItemCount+"||"+mConversationListview.getLastVisiblePosition());
        if(firstVisibleItem+visibleItemCount==totalItemCount&&totalItemCount>0){
            isLastRow = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isLastRow&&scrollState==SCROLL_STATE_IDLE){
            mConversationListview.setPullLoadEnable(true);
            isLastRow = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CRMLog.LogInfo("onItemClick", position + "||" + conversationBeans.size());
        if(position>conversationBeans.size()||position<1){
            return;
        }
        ConversationBean onClickBean;
        onClickBean = conversationBeans.get(position-1);
        CRMLog.LogInfo("onItemClick", onClickBean.msg);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chatConversation", onClickBean);
        String paImType = onClickBean.paImType;
        CommonUtils.handleChannel(this,paImType);
        Intent intent = new Intent(ConversationHistoryActivity.this,ChatActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    VolleyInterface freshVolleyInterface =new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            List<JSONObject> conversationHistory = new ArrayList<>();
            try {
                conversationHistory = JSONStringUtil.conversationJsonAnalyse(obj.toString());
            }catch(JSONException e){
                CRMLog.LogInfo(Constants.LOG_TAG,"conversationHistoryException   ---"+e.getMessage());
            }
            if(null==conversationHistory){
                Toast.makeText(ConversationHistoryActivity.this, "没有记录！", Toast.LENGTH_SHORT).show();
            }else {
                JSONStringUtil.tempDateList=null;
                //隐藏刷新显示
                mConversationListview.stopRefresh();
                //没次请求刷新，要清空临时数组；
                //JSONStringUtil.tempDateList= null;
                conversationBeans.clear();
                mConversationAdapter.changeData(conversationBeans);
                List<ConversationBean> historyList=new ArrayList<>();
                CRMLog.LogInfo(Constants.LOG_TAG, "conversationHistory==   ---" + conversationHistory.toString());

                for(int i =0;i<conversationHistory.size();i++){
                    ConversationBean historyBean;

                    CRMLog.LogInfo(Constants.LOG_TAG, "conversationHistoryitem   ---" + conversationHistory.get(i).toString());
                    historyBean = CommonUtils.handleHttpResult(ConversationBean.class,conversationHistory.get(i).toString());
                    CRMLog.LogInfo(Constants.LOG_TAG, "historyDataTime   ---" + i + "==" +historyBean.historyTime+"\n"+DateFormatUtil.format(historyBean.sendTime,DateFormatUtil.HH_MM_12));
                    historyList.add(historyBean);
                }
//                totalPage = historyList.get(historyList.size()-1).totalPage ;
                CRMLog.LogInfo(Constants.LOG_TAG, "nowPage   ---" + nowPage+"tatolPage  ---"+totalPage);
                CRMLog.LogInfo(Constants.LOG_TAG, "historyList   ---" + historyList.size());
                //把查出来的list添加到要显示的list中
                conversationBeans.addAll(historyList);
                //清空临时列表，避免重复显示
                historyList.clear();
                CRMLog.LogInfo(Constants.LOG_TAG, "historyListAFATER   ---" + historyList.size());
                mConversationAdapter.changeData(conversationBeans);
                //刷新和刚进来，定位在第一行；上拉定位在最后一行。
                mConversationListview.setSelection(0);
            }
        }

        @Override
        public void onError(VolleyError ve) {
            CRMLog.LogInfo(Constants.LOG_TAG, "conversationHistoryError   ---" + ve.getMessage());
            mConversationListview.stopRefresh();
            Toast.makeText(ConversationHistoryActivity.this, "网络异常！稍后重试", Toast.LENGTH_LONG);
        }

        @Override
        public void onLogOutside() {
            CRMLog.LogInfo("onLogOutside", "conversationHistory");
            CommonUtils.exitWhenHasLogIn(ConversationHistoryActivity.this);
        }
    };

    VolleyInterface loadMoreInterface = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            List<JSONObject> conversationHistory = new ArrayList<>();
            try {
                conversationHistory = JSONStringUtil.conversationJsonAnalyse(obj.toString());
            }catch(JSONException e){
                CRMLog.LogInfo(Constants.LOG_TAG,"loadmore conversationHistoryException   ---"+e.getMessage());
            }
            if(null==conversationHistory){
                Toast.makeText(ConversationHistoryActivity.this, "没有更多了！", Toast.LENGTH_SHORT).show();
            }else {
                List<ConversationBean> historyList=new ArrayList<>();
                CRMLog.LogInfo(Constants.LOG_TAG, "conversationHistory==   ---" + conversationHistory.toString());

                for(int i =0;i<conversationHistory.size();i++){
                    ConversationBean historyBean;

                    CRMLog.LogInfo(Constants.LOG_TAG, "conversationHistoryitem   ---" + conversationHistory.get(i).toString());
                    historyBean = CommonUtils.handleHttpResult(ConversationBean.class,conversationHistory.get(i).toString());
                    CRMLog.LogInfo(Constants.LOG_TAG, "historyDataTime   ---" + i + "==" +historyBean.historyTime+"\n"+DateFormatUtil.format(historyBean.sendTime,DateFormatUtil.HH_MM_12));
                    historyList.add(historyBean);
                }
                totalPage = historyList.get(historyList.size()-1).totalPage ;
                CRMLog.LogInfo(Constants.LOG_TAG, "nowPage   ---" + nowPage+"tatolPage  ---"+totalPage);
                CRMLog.LogInfo(Constants.LOG_TAG, "historyList   ---" + historyList.size());
                //把查出来的list添加到要显示的list中
                conversationBeans.addAll(historyList);
                //清空临时列表，避免重复显示
                historyList.clear();
                CRMLog.LogInfo(Constants.LOG_TAG, "historyListAFATER   ---" + historyList.size());
                mConversationAdapter.changeData(conversationBeans);
                //刷新和刚进来，定位在第一行；上拉定位在最后一行。
                mConversationListview.setSelection(conversationBeans.size() - 1);
            }
            mConversationListview.stopLoadMore();
            mConversationListview.setPullLoadEnable(false);
        }

        @Override
        public void onError(VolleyError ve) {
            mConversationListview.stopLoadMore();
            mConversationListview.setPullLoadEnable(false);
            Toast.makeText(ConversationHistoryActivity.this, "网络异常！稍后重试", Toast.LENGTH_LONG);
        }

        @Override
        public void onLogOutside() {
            CommonUtils.exitWhenHasLogIn(ConversationHistoryActivity.this);
        }
    };
    public void queryConversationHistory(String url, String umId ,String nowPage,VolleyInterface volleyInterface, String shiroKey) {
        Map map = new HashMap();
        map.put("nowPage", nowPage);
        String shiroToken = DigestUtil.digest(shiroKey, map);
        VolleyRequest.httpConversationHistory(this,url, shiroToken, umId, nowPage, volleyInterface);
    }

}
