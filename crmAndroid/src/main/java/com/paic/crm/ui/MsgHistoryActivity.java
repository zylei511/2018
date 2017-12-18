package com.paic.crm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.MsgHistoryAdapter;
import com.paic.crm.android.R;
import com.paic.crm.app.AppContext;
import com.paic.crm.entity.ChatDetail;
import com.paic.crm.entity.ConstantsBean;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.DigestUtil;
import com.paic.crm.utils.ExpressionUtils;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.utils.UCMComparator;
import com.paic.crm.widget.MsgListView;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by pingan001 on 16/1/21.
 */
public class MsgHistoryActivity extends BaseActivity implements View.OnClickListener, MsgListView.IXListViewListener {

    private String customerId;
    private TextView actionTitile;
    private ImageView iv_back;
    private LinearLayout iv_back_parent;
    private MsgListView msgHistoryListView;

    private String nowPage = "1";
    private String umId;
    private String shiroKey;
    private List<NewMessageBean> chatList = new ArrayList<>();
    private MsgHistoryAdapter chatAdapter;
    private ConstantsBean constantsBean;
    private ConversationBean conversationBean;
    private ImageView prePage;
    private ImageView nextPage;
    private TextView nowPageTxt;
    private boolean isLoadOver = false;
    private Dialog onLoadingDia;
    private String customerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CRMLog.LogInfo(Constants.LOG_TAG, "msgHistory main Thread--" +Thread.currentThread().getId());
        setContentView(R.layout.msg_history);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        customerId = bundle.getString("contactId");
        customerIcon = bundle.getString("icon");
        initView();
        addListeners();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TCAgent.onPageStart(this, PATDEnum.HIS_CHAT.getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, PATDEnum.HIS_CHAT.getName());
    }

    private void addListeners() {
        iv_back_parent.setOnClickListener(this);
        prePage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
    }

    public void initView(){
        actionTitile=(TextView)findViewById(R.id.action_title);
        msgHistoryListView=(MsgListView)findViewById(R.id.msg_history_listView);
        msgHistoryListView.setPullRefreshEnable(false);
        msgHistoryListView.setPullLoadEnable(false);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back_parent = (LinearLayout)findViewById(R.id.iv_back_parent);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        actionTitile.setText("历史消息");
        prePage = (ImageView)findViewById(R.id.pre_page);
        nextPage = (ImageView)findViewById(R.id.next_page);
        nowPageTxt = (TextView)findViewById(R.id.nowPage);
        nowPageTxt.setText("1");

        chatAdapter = new MsgHistoryAdapter(this,chatList);
        msgHistoryListView.setAdapter(chatAdapter);
        onLoadingDia = DialogFactory.getLoadingDialog(this,"正在加载中...");
    }
    public void initData(){
        umId = (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
        shiroKey =  SpfUtil.getValue(this, SpfUtil.SHIROKEY, "").toString();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            constantsBean = (ConstantsBean) bundle.getSerializable("constantsBean");
            conversationBean = (ConversationBean) bundle.getSerializable("chatConversation");
        }
        CRMLog.LogInfo("history params umId=", umId + "customerId=" + customerId+"token="+shiroKey);
        CRMLog.LogInfo("history params umId=",HttpUrls.HTTP_UCPMSG_HISTORY );
        queryMsgHistory(HttpUrls.HTTP_UCPMSG_HISTORY, umId, customerId, "1", shiroKey, volleyInterface);

    }
    VolleyInterface volleyInterface =new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            try{
                onLoadingDia.dismiss();
                CRMLog.LogInfo(Constants.LOG_TAG,"msgHistory currentThread"+Thread.currentThread().getId());
                CRMLog.LogInfo(Constants.LOG_TAG,"msgHistory   --"+obj.toString());
                List<NewMessageBean> beans = new ArrayList<>();
                JSONObject object = new JSONObject(obj.toString());
                String resultCode = object.getString("resultCode");

                if ("02".equals(resultCode)) {
                    JSONObject object1 = object.getJSONObject("data");
                    ChatDetail chatDetail = CommonUtils.handleHttpResult(ChatDetail.class, object1.toString());
                    CRMLog.LogInfo("chatdetail", null == chatDetail ? null : chatDetail.toString());
                    if (chatDetail != null) {
                        if (chatDetail.content != null && chatDetail.content.size() > 0) {
                            for (int i = 0; i < chatDetail.content.size(); i++) {
                                CustomMsgContent customMsgContent = chatDetail.content.get(i);
                                NewMessageBean newMessageBean = new NewMessageBean();
                                newMessageBean.customMsgContent = customMsgContent;
                                //处理客户的头像
                                if("2".equals(newMessageBean.customMsgContent.fromType)) {
                                    newMessageBean.customMsgContent.customerIcon = customerIcon;
                                }
                                String msgTmp = customMsgContent.msg;

                                //补到毫秒
                                newMessageBean.mutiMsg = ExpressionUtils.dispatchExpression(MsgHistoryActivity.this,msgTmp);
                                newMessageBean.customMsgContent.createTime +="000";
                                CRMLog.LogInfo("customMsgContent", customMsgContent.createTime);
                                newMessageBean.customMsgContent.msgState=Constants.MSG_STATE_SEND_SUCCESSFUL;
                                beans.add(newMessageBean);
                            }
                        }
                    }

                    if(beans.isEmpty()){
                        //如果没有更多了，就显示上一页的页数，并且不清除上一页数据
                        nowPage = String.valueOf(Integer.parseInt(nowPage) - 1);
                        nowPageTxt.setText(nowPage);
                        isLoadOver = true;
                        Toast.makeText(MsgHistoryActivity.this, "没有更多了！", Toast.LENGTH_SHORT).show();
                    }else{
                        isLoadOver = false;
                        chatList.clear();
                        chatList.addAll(beans);
                        nowPageTxt.setText(nowPage);
                        UCMComparator comparator = new UCMComparator();
                        Collections.sort(chatList, comparator);
                    }
                    CRMLog.LogInfo("msgHistory", chatList.toString() + "--" + beans.toString());
                    chatAdapter.changeData(chatList);
                }
            } catch (JSONException e) {
                CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        @Override
        public void onError(VolleyError ve) {
            onLoadingDia.dismiss();
            isLoadOver = true;
            Toast.makeText(MsgHistoryActivity.this,"网络异常！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLogOutside() {
            CRMLog.LogInfo("onLogOutside", "msgHistory");
            CommonUtils.exitWhenHasLogIn(MsgHistoryActivity.this);
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        chatList.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_parent:
                finish();
                break;
            case R.id.pre_page:
                if(Integer.parseInt(nowPage)>1) {
                    if(onLoadingDia.isShowing()){
                        onLoadingDia.dismiss();
                    }
                    onLoadingDia.show();
                    nowPage = String.valueOf(Integer.parseInt(nowPage) - 1);
                    CRMLog.LogInfo("hhhh", "prePage --" + nowPage);
                    queryMsgHistory(HttpUrls.HTTP_UCPMSG_HISTORY, umId, customerId, nowPage, shiroKey, volleyInterface);
                }else {
                    Toast.makeText(MsgHistoryActivity.this,"已到第一页！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.next_page:
                if(onLoadingDia.isShowing()){
                    onLoadingDia.dismiss();
                }
                onLoadingDia.show();
                if (!isLoadOver) {
                    nowPage = String.valueOf(Integer.parseInt(nowPage) + 1);
                    CRMLog.LogInfo("hhhh", "nextPage --" + nowPage+"umId="+umId+"customerId="+customerId);
                    queryMsgHistory(HttpUrls.HTTP_UCPMSG_HISTORY, umId, customerId, nowPage, shiroKey, volleyInterface);
                }else {
                    onLoadingDia.dismiss();
                    Toast.makeText(this,"已到达最后一页",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
    public void queryMsgHistory(final String url, final String umId, final String customerId, final String nowPage,String shiroKey, final VolleyInterface volleyInterface){
        Map params = new HashMap();
        params.put("nowPage",nowPage);
        params.put("customerId", customerId);
        final String shiroToken = DigestUtil.digest(shiroKey, params);
        CRMLog.LogInfo("yueshaojun umId=", umId + "\ncustomerId=" + customerId + "\ntoken=" + shiroToken + "\npage" + nowPage);
        VolleyRequest.httpMsgHistory(MsgHistoryActivity.this, url, umId, customerId, nowPage, shiroToken, volleyInterface);

    }

    @Override
    public void onRefresh() {
        CRMLog.LogInfo("test", "onRefresh");
    }

    @Override
    public void onLoadMore() {
        CRMLog.LogInfo("test", "onLoadMore");
    }
}
