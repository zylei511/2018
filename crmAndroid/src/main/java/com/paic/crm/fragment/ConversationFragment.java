package com.paic.crm.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.ConversationAdapter;
import com.paic.crm.android.R;
import com.paic.crm.app.ChatAddHolder;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entrance.HomeActivity;
import com.paic.crm.presenter.ConversationFragmentPresenter;
import com.paic.crm.ui.BaseActivity;
import com.paic.crm.ui.ChatActivity;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.widget.IXListViewListener;
import com.paic.crm.widget.PullToRefreshSwipeMenuListView;
import com.paic.crm.widget.RefreshTime;
import com.paic.crm.widget.SwipeMenu;
import com.paic.crm.widget.SwipeMenuCreator;
import com.paic.crm.widget.SwipeMenuInteractAdapter;
import com.paic.crm.widget.SwipeMenuItem;
import com.paic.crm.widget.SwipeMenuListView;
import com.paic.crmimlib.listener.OnNewMessageListener;
import com.paic.crmimlib.serivce.CrmChatBaseManager;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by hanyh on 16/1/21.
 */
public class ConversationFragment extends MVPBaseFragment<IConversationFragmentView, ConversationFragmentPresenter> implements
        AdapterView.OnItemClickListener,
        Runnable,
        IXListViewListener, OnNewMessageListener,
        IConversationFragmentView {

    private PullToRefreshSwipeMenuListView mConversationListView;
    private BaseActivity mContext;
    private List<ConversationBean> conversationBeans = new ArrayList<>();
    private ConversationAdapter mConversationAdapter;
    private Dialog mDialog;
    private Handler mHandler;
    private static final String TAG = "ConversationFragment";

    private CrmChatBaseManager chatManager;

    private static final int SWIPE_ID_PUT_FIRST = 0;
    private static final int SWIPE_ID_DELETE = 1;
    private static final int SWIPE_ID_FINISH = 2;

    @Override
    public void onAttach(Context context) {
        mContext = (BaseActivity) context;
        super.onAttach(context);
        mHandler = new Handler();
        initIm();
    }

    @Override
    public ConversationFragmentPresenter createPresenter() {

        return new ConversationFragmentPresenter(getActivity().getApplicationContext(),this);
    }

    private void initIm() {
        chatManager = CrmChatBaseManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversion_layout, null);
        init(view);
        createCreater();
        addListeners();
        return view;
    }

    /**
     * 1 从chatActivity回来要查询最新的聊天记录的最后一条显示
     * 2 要重新刷新会话的条数
     */
    @Override
    public void onResume() {
        super.onResume();
        //在网络请求回来之前有内容显示，以免造成较长时间的空列表
        readDb(true);

        mHandler.postDelayed(this, 0);
        CrmEnvValues.getInstance().setChatReceived(false);
        ChatAddHolder.clearChatAddBeans();
        if (this.isVisible()) {
            TCAgent.onPageStart(getActivity(), PATDEnum.CONVERSATION.getName());
        }
        assemChatNameSpace();
        //readDb(false);

    }


    @Override
    public void onPause() {
        super.onPause();
        CrmEnvValues.getInstance().setChatReceived(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isVisible()) {
            TCAgent.onPageEnd(getActivity(), PATDEnum.CONVERSATION.getName());
        }
    }

    private void init(View view) {
        mConversationListView = (PullToRefreshSwipeMenuListView) view.findViewById(R.id.listView);
        mConversationListView.setPullRefreshEnable(true);
        mConversationListView.setPullLoadEnable(false);
        mConversationListView.setFooterDividersEnabled(false);
        String refTime = RefreshTime.getRefreshTime(getContext());
        mConversationListView.setRefreshTime(refTime);
    }


    private void assemChatNameSpace() {
        if (mDialog == null) {
            mDialog = DialogFactory.getLoadingDialog(getActivity(), "正在删除");
            mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    private void addListeners() {
        mConversationListView.setOnItemClickListener(this);
        mConversationListView.setXListViewListener(this);
        chatManager.addOnNewMessageListener(TAG, this);
    }

    private SwipeMenuCreator createDefaultCreater(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu, int position) {

                SwipeMenuItem openItem = new SwipeMenuItem(
                        mContext);
                openItem.setBackground(new ColorDrawable(Color.parseColor("#bbc6d1")));
                openItem.setWidth(CommonUtils.dp2px(mContext, 70));
                openItem.setTitle("置顶");
                openItem.setTitleSize(16);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setWidth(CommonUtils.dp2px(mContext, 70));
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ff6868")));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        return creator;
    }

    private void createCreater() {
        SwipeMenuCreator defaultCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu, int position) {

                SwipeMenuItem openItem = new SwipeMenuItem(
                        mContext);
                openItem.setBackground(new ColorDrawable(Color.parseColor("#bbc6d1")));
                openItem.setWidth(CommonUtils.dp2px(mContext, 70));
                openItem.setTitle("置顶");
                openItem.setId(SWIPE_ID_PUT_FIRST);
                openItem.setTitleSize(16);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#ff6868")));
                deleteItem.setWidth(CommonUtils.dp2px(mContext, 70));
                deleteItem.setIcon(R.drawable.ic_delete);
                deleteItem.setId(SWIPE_ID_DELETE);
                menu.addMenuItem(deleteItem);
            }
        };

        SwipeMenuCreator finishDeleteCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu, int position) {
                SwipeMenuItem item = new SwipeMenuItem(mContext);
                item.setWidth(CommonUtils.dp2px(mContext, 70));
                item.setBackground(new ColorDrawable(Color.parseColor("#ff6868")));
                item.setTitleSize(16);
                item.setTitle("结束");
                item.setTitleColor(Color.WHITE);
                menu.addMenuItem(item);
            }

        };

        SwipeMenuInteractAdapter swipeMenuInteractAdapter = new SwipeMenuInteractAdapter() {
            @Override
            public void interactWithUser(int position, SwipeMenu menu) {
                ConversationBean chatConversation = mConversationAdapter.getItem(position);
                String dialogueStatus = chatConversation.dialogueStatus;
                String status = chatConversation.status;
                SwipeMenuItem firstItem = menu.getMenuItem(0);
//                有效会话
                if (Constants.STATE_CONVERSATION_STATUS_Y.equals(dialogueStatus) && Constants.STATE_CONVERSATION_VAILD.equals(status)) {
                    firstItem.setTitle("结束");
                    firstItem.setId(SWIPE_ID_FINISH);
                    //无效会话
                } else if (Constants.STATE_CONVERSATION_STATUS_Y.equals(dialogueStatus) && Constants.STATE_CONVERSATION_INVAILD.equals(status)) {
                    firstItem.setTitle("删除");
                    firstItem.setId(SWIPE_ID_DELETE);
                }
            }
        };

        // set creator
        if(!BizSeriesUtil.isKZKF(getActivity())){
            mConversationListView.setMenuCreator(defaultCreator);
        }else {
            mConversationListView.setMenuCreator(finishDeleteCreator);
            mConversationListView.setMenuInteract(swipeMenuInteractAdapter);
        }
//        mConversationListView.setMenuInteract(swipeMenuInteractAdapter);
        mConversationListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int itemId) {

                ConversationBean chatConversation = mConversationAdapter.getItem(position);
                switch (itemId) {
                    case SWIPE_ID_PUT_FIRST:
                        Collections.swap(conversationBeans, position, 0);
                        mConversationAdapter.changeData(conversationBeans);
                        break;

                    case SWIPE_ID_DELETE:
                        mDialog = DialogFactory.getLoadingDialog(mContext,"正在删除...");
                        mDialog.show();
                        mBasePresenter.deleteConversation(chatConversation,position);
                        break;
                    case SWIPE_ID_FINISH:
                        mDialog = DialogFactory.getLoadingDialog(mContext,"正在结束...");
                        mDialog.show();
                        mBasePresenter.deleteConversation(chatConversation,position);
                        break;
                    default:
                }
            }
        });
        mConversationAdapter = new ConversationAdapter(mContext, conversationBeans, R.layout.item_conversation_layout);
        mConversationListView.setAdapter(mConversationAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position > 0) {

            ConversationBean chatConversation = conversationBeans.get(position - 1);
            if (chatConversation != null) {
                chatConversation.unReadCount = 0;
                ConversationBean tmp_conver = new ConversationBean();
                try {
                    tmp_conver = (ConversationBean) chatConversation.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                mBasePresenter.clearUnreadCount(tmp_conver);
                Bundle bundle = new Bundle();
                bundle.putSerializable("chatConversation", chatConversation);
                ((HomeActivity) getActivity()).toActivity(getActivity(), ChatActivity.class, bundle);
            }
        }
    }

    @Override
    public void run() {
        CommonUtils.hideInputManager(getContext());
        if (getActivity() != null) {
            mBasePresenter.getDataFromHttp();
        }
    }

    @Override
    public void onRefresh() {
        String refTime = RefreshTime.getRefreshTime(getContext());
        mConversationListView.setRefreshTime(refTime);
        mHandler.postDelayed(this, 0);

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onNewMessage(String s) {
        //首先判断Msg类型：text / event，如果是event，则直接return，此处不处理
        if (isEventMsg(s)) {
            return;
        }
        CustomMsgContent newMsg = new Gson().fromJson(s, CustomMsgContent.class);

        conversationBeans = mBasePresenter.receiveNewMsg(newMsg,conversationBeans);
        mConversationAdapter.changeDatas(conversationBeans);
        //通知
        Intent intent = new Intent();
        intent.setAction("com.paic.crm.UnreadNumBroadcast");
        getActivity().sendBroadcast(intent);
    }

    private boolean isEventMsg(String s) {
        JSONObject newMsgObj = null;
        try {
            newMsgObj = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String dealMsgType = newMsgObj.optString(Constants.MSG_TYPE_KEY);
        if (Constants.MSG_TYPE_VALUE_EVENT.equals(dealMsgType)) {
            return true;
        }
        return false;
    }


    private void readDb(boolean isError) {
        if (isError) {
            conversationBeans.clear();
        }
        conversationBeans = mBasePresenter.getDataFromDb(conversationBeans);

        mConversationAdapter.changeDatas(conversationBeans);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHttpSuccess(String str) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }

        RefreshTime.setRefreshTime(getContext(), str);
        readDb(false);
        mConversationListView.stopRefresh();
    }

    @Override
    public void onHttpError() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        readDb(true);
        mConversationListView.stopRefresh();
    }

    @Override
    public void onDeleteSuccess(ConversationBean successItem, int itemIndex) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        conversationBeans.remove(itemIndex);
        mConversationAdapter.changeDatas(conversationBeans);
        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteError() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishSuccess(ConversationBean successItem, int itemIndex) {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        conversationBeans.set(itemIndex,successItem);
        mConversationAdapter.changeDatas(conversationBeans);
        Toast.makeText(getContext(),  "结束成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFinishError() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(getContext(), "结束失败", Toast.LENGTH_LONG).show();
    }
}
