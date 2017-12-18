package com.paic.crm.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.ChatAdapter;
import com.paic.crm.adapter.ChatMoreAdapter;
import com.paic.crm.android.R;
import com.paic.crm.app.ChatAddHolder;
import com.paic.crm.app.CrmDaoHolder;
import com.paic.crm.app.CrmEnvValues;
import com.paic.crm.callback.SendFailCallback;
import com.paic.crm.db.DatabaseHelper;
import com.paic.crm.entity.ChatAddBean;
import com.paic.crm.entity.ChatDetail;
import com.paic.crm.entity.KZKFChatMoreBean;
import com.paic.crm.entity.ConstantsBean;
import com.paic.crm.entity.ConversationBean;
import com.paic.crm.entity.CustomMsgContent;
import com.paic.crm.entity.H5InformationBean;
import com.paic.crm.entity.H5InformationContent;
import com.paic.crm.entity.NewMessageBean;
import com.paic.crm.fragment.ChatBottomFragment;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.uihelper.InputFrameHelper;
import com.paic.crm.uihelper.KeyboardSwitchManager;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DialogFactory;
import com.paic.crm.utils.ExpressionUtils;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.KeyboardChangeHelper;
import com.paic.crm.utils.LoginDesUtil;
import com.paic.crm.utils.ParseEmojiMsgUtil;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.AlertView;
import com.paic.crm.widget.MsgListView;
import com.paic.crm.widget.OnItemClickListener;
import com.paic.crmimlib.listener.OnMsgReceipt;
import com.paic.crmimlib.listener.OnNewMessageListener;
import com.paic.crmimlib.serivce.CrmChatBaseManager;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class ChatActivity extends BaseActivity implements OnClickListener,
        MsgListView.IXListViewListener,
        SendFailCallback ,
        OnNewMessageListener,
        OnMsgReceipt,
        InputFrameHelper.OnInputFrameBtnClickListener,
        AdapterView.OnItemClickListener,
        Runnable,
        ChatBottomFragment.OnFragmentCreateListener{
    private MsgListView msgListView;
    private List<NewMessageBean> chatList = new ArrayList<>();
    private ChatAdapter chatRoomAdapter;
    private PopupWindow popupWindow;
    private ImageView iv_serch_btn;
    private View conversation_pop_layout;
    private List<Integer> dataPops = new ArrayList<>();
    public static final int DATA_CUSTOMER = 0;
    public static final int DATA_MSG_HISTORY = 1;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private String h5Url;
    private ImageView iv_back;
    private LinearLayout iv_back_parent;
    private ListView chatMoreList;
    private ConstantsBean constantsBean;
    private TextView action_title;
    private TextView unreadNum;

    private ConversationBean chatConversation;
    private String customerId;
    private String customerNo;
    private String name;
    private String portrait;
    private String source;
    private String umId;
    private H5InformationBean h5InformationBean;
    private List<CustomMsgContent> dbCustomMsgContents;
    private String dialogueStatus;
    public static volatile NewMessageBean faiLMessageBean;
    private int failIndex;
    private Button copyBtn;
    private PopupWindow pp;
    private View ppview;
    private LinearLayout msgHistoryBtn;
    private Handler mHandler = new MyHandler();
    private UnreadNumBroadcast broadcast;

    private CrmChatBaseManager chatManager ;
    private static final String TAG = "ChatActivity";

    private KeyboardSwitchManager mKeyboardSwitchManager;
    private ChatBottomFragment mChatBottomFragment;
    private String shirokey;
    private Dialog checkDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        initIm();
        initView();
        InitData();
        addListeners();
    }

    private void handleSource() {
        //接受发来的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.paic.crm.UnreadNumBroadcast");
        broadcast = new UnreadNumBroadcast(this);
        registerReceiver(broadcast,intentFilter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            constantsBean = (ConstantsBean) bundle.getSerializable("constantsBean");
            chatConversation = (ConversationBean) bundle.getSerializable("chatConversation");
            h5InformationBean = (H5InformationBean) bundle.getSerializable("h5InformationBean");
            umId = (String) SpfUtil.getValue(this, SpfUtil.UMID, "");
        }


        //如果是空中客服
        changeBottomEdit();

        /**
         * 联系人过来
         * 1 查会话信息
         *   1-1 如果有会话信息 调用聊天信息接口 刷新界面
         *   1-2 如果没有会话信息正常操作
         */
        if (constantsBean != null) {
            customerId = constantsBean.customerId;
            customerNo = constantsBean.customerNo;
            name = constantsBean.name;
            dialogueStatus = constantsBean.dialogueStatus;
            portrait = constantsBean.portrait;
            source = constantsBean.source;
            action_title.setText(constantsBean.name);
        }
        /**
         * 会话过来
         * 1 用msgId来查询最新的聊天信息
         * 2 数据库缓存这个聊天信息 遍历的时候存在不缓存，不存在缓存
         */
        if (chatConversation != null) {
            customerId = chatConversation.customerId;
            name = chatConversation.imNickName;
            portrait = chatConversation.customerIcon;
            dialogueStatus = chatConversation.dialogueStatus;
            source = chatConversation.paImType;
            action_title.setText(name);
            //clearConverUnread(customerId,source,umId);
        }
        handleChatChannel();
        //先读缓存显示已经有的,避免长时间显示为空
        handleDbData();
        //计算未读数
        int count = countAllUnread();
        if(count!=0) {
            unreadNum.setText("("+count+")");
        }
        shirokey = (String) SpfUtil.getValue(this, SpfUtil.SHIROKEY, "");
        VolleyRequest.httpChat(this, HttpUrls.HTTP_CHAT, Constants.TAG_CHAT, umId, customerId, source, "last", shirokey, volleyInterface);
    }

    /**
     * 判断是否显示+号按钮
     */
    private void changeBottomEdit() {
        //禁止输入和发送
        if (BizSeriesUtil.isKZKF(this) && Constants.STATE_CONVERSATION_INVAILD.equals(chatConversation.status)) {
            disableEdit();
            //启用输入和发送
        } else if (BizSeriesUtil.isKZKF(this) && Constants.STATE_CONVERSATION_VAILD.equals(chatConversation.status)) {
            enableEdit();
        }
    }

    private void enableEdit() {
        mChatBottomFragment.enableEdit();
        mChatBottomFragment.showSendBtn();
        mChatBottomFragment.hideChatAdd();
        mChatBottomFragment.enableFace();
        mChatBottomFragment.enableSendBtn();
    }

    private void disableEdit() {
        mChatBottomFragment.disableEdit(getString(R.string.chat_has_ended));
        mChatBottomFragment.showSendBtn();
        mChatBottomFragment.hideChatAdd();
        mChatBottomFragment.disableFace();
        mChatBottomFragment.disableSendBtn();
    }

    private void handleChatChannel() {
        Iterator<ChatAddBean> chatAddBeanIterator = ChatAddHolder.getChatAddBeans().iterator();
        if (Constants.TAG_CHANNEL_SMS.equals(source)) {
            mChatBottomFragment.disableEdit("当前会话是短息渠道只能发短息模板");
            mChatBottomFragment.disableFace();
            while (chatAddBeanIterator.hasNext()) {
                if (!chatAddBeanIterator.next().iconName.equals("发短信")) {
                    chatAddBeanIterator.remove();
                }
            }
        } else {
            while (chatAddBeanIterator.hasNext()) {
                if (chatAddBeanIterator.next().iconName.equals("发短信")) {
                    chatAddBeanIterator.remove();
                }
            }
        }
        if (dialogueStatus != null && dialogueStatus.equals("N")) {
            mChatBottomFragment.disableEdit("当前客户微信渠道已解绑，不能发消息");
            mChatBottomFragment.disableFace();
        }
    }


    // 拼装完整namespace
    private void initIm() {
        chatManager = CrmChatBaseManager.getInstance();
    }

    public static void actionStart(Context context, String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            jsonObject = (JSONObject) jsonObject.get("to");
            ConstantsBean constantsBean = new ConstantsBean();
            constantsBean.source = jsonObject.getString("source");
            constantsBean.portrait = jsonObject.getString("portrait");
            constantsBean.name = jsonObject.getString("name");
            constantsBean.customerId = jsonObject.getString("customerId");
            constantsBean.customerNo = jsonObject.getString("customerNo");
            Intent intent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("constantsBean", constantsBean);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    VolleyInterface volleyDeleteKZKF = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {

            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }

            JSONObject object = null;
            try {
                object = new JSONObject(obj.toString());
                String resultCode = object.optString("resultCode");
                String resultMsg = object.optString("resultMsg");
                if ("02".equals(resultCode)) {
                    finish();
                }
                Toast.makeText(ChatActivity.this, resultMsg, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(VolleyError ve) {
            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }
            Toast.makeText(ChatActivity.this, getString(R.string.program_call_failed), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLogOutside() {
            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }
            CommonUtils.exitWhenHasLogIn(ChatActivity.this);
        }
    };

    VolleyInterface volleyTranslateKZKF = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }

            JSONObject object = null;
            try {
                object = new JSONObject(obj.toString());
                String resultCode = object.optString("resultCode");
                String resultMsg = object.optString("resultMsg");
                //转接成功
                if ("200".equals(resultCode)){
                    finish();
                }
                Toast.makeText(ChatActivity.this, resultMsg, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(VolleyError ve) {
            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }
            Toast.makeText(ChatActivity.this, getString(R.string.program_call_failed), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLogOutside() {
            if (checkDialog != null && checkDialog.isShowing()){
                checkDialog.dismiss();
            }
            CommonUtils.exitWhenHasLogIn(ChatActivity.this);
        }
    };


    VolleyInterface volleyInterface = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            try {
                CRMLog.LogInfo(Constants.LOG_TAG, "jjjjj" + obj.toString());
                JSONObject object = new JSONObject(obj.toString());
                String resultCode = object.optString("resultCode");
                if ("02".equals(resultCode)) {
                    JSONObject object1 = (JSONObject) object.get("data");
                    ChatDetail chatDetail = CommonUtils.handleHttpResult(ChatDetail.class, object1.toString());
                    ChatDetail chatDetailforDb = CommonUtils.handleHttpResult(ChatDetail.class, object1.toString());
                    //生成聊天并保存
                    handleDbChatData(chatDetail, chatDetailforDb);
                    //通过读db更新视图
                    handleDbData();
                }
            } catch (JSONException e) {
                msgListView.stopRefresh();
                CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onError(VolleyError ve) {
            CRMLog.LogInfo(Constants.LOG_TAG, ve.getMessage());
        }

        @Override
        public void onLogOutside() {
            CommonUtils.exitWhenHasLogIn(ChatActivity.this);
        }
    };

    private void handleDbData() {
        queryForDb();
        //这里需要清一下listView，不然会多次add；另外，这种写法需要改，更新UI最好不要用全局变量，
        // 不然会容易导致其它地方如果修改了chatList，显示就出错。
        chatList.clear();
        ChatDetail chatDetail = new ChatDetail();
        if (dbCustomMsgContents != null) {
            chatDetail.chatList = dbCustomMsgContents;
            generateChatList(chatDetail, false);
            CRMLog.LogInfo("sfsfsssss", "1" + dbCustomMsgContents.toString());
            chatRoomAdapter.changeData(chatList);
            msgListView.setSelection(chatList.size() - 1);
        }
    }

    private void queryForDb() {
        String umIden = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        long maxId = CrmDaoHolder.getInstance().getCustomMsgContentDao().queryLastId(umIden, customerId,source);
        CRMLog.LogInfo(Constants.LOG_TAG, "queryForDb maxId=" + maxId);
        if (maxId != 0) {
            List<CustomMsgContent> list =
                    CrmDaoHolder
                            .getInstance()
                            .getCustomMsgContentDao()
                            .queryChat(umIden, customerId,source, maxId - 10, 10);
            dbCustomMsgContents = list;
            for (CustomMsgContent customMsgContent : dbCustomMsgContents) {
                CRMLog.LogInfo(Constants.LOG_TAG, "queryForDb " + customMsgContent.msg);
            }
        }
    }

    private void handleDbChatData(ChatDetail chatDetail, ChatDetail chatDetailforDb) {
        String umIden = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        dbCustomMsgContents =
                CrmDaoHolder
                        .getInstance()
                        .getCustomMsgContentDao()
                        .queryCusContents(umIden, customerId, source);
        if (dbCustomMsgContents == null || dbCustomMsgContents.size() == 0) {
            generateChatList(chatDetail, true);
            Collections.reverse(chatList);
            chatRoomAdapter.changeData(chatList);
            msgListView.setSelection(chatList.size() - 1);
        }
        //进行数据库操作
        List<CustomMsgContent> customMsgContents = chatDetailforDb.chatList;
        Collections.reverse(customMsgContents);
        for (int i = 0; i < customMsgContents.size(); i++) {
            CustomMsgContent customMsgContent = customMsgContents.get(i);
            if("2".equals(customMsgContent.fromType)) {
                customMsgContent.customerIcon = portrait;
            }
            //进行数据库操作 没有数据添加数据
            if (dbCustomMsgContents == null || dbCustomMsgContents.size() == 0) {
                //数据库为空时添加的，icon为空
                addDb(customMsgContent);
            } else {
                //有这条数据覆盖更新
                boolean isContain = false;
                for (int j = 0; j < dbCustomMsgContents.size(); j++) {
                    CustomMsgContent msgContent = dbCustomMsgContents.get(j);
                    if (customMsgContent.messageId.equals(msgContent.messageId)) {
                        CommonUtils.encryptMsgData(customMsgContent);
                        int result = CrmDaoHolder
                                .getInstance()
                                .getCustomMsgContentDao()
                                .update(customMsgContent);
                        CRMLog.LogDebg(Constants.LOG_TAG, "更新数据  :" + result);
                        isContain = true;
                    }
                }
                //没有这条数据添加
                if (!isContain) {
                    addDb(customMsgContent);
                }
            }
        }
        msgListView.stopRefresh();
    }

    private void generateChatList(ChatDetail chatDetail, boolean isHttpData) {
        if (chatDetail.chatList != null && chatDetail.chatList.size() > 0) {
            for (int i = 0; i < chatDetail.chatList.size(); i++) {
                CustomMsgContent customMsgContent = chatDetail.chatList.get(i);
                NewMessageBean newMessageBean = new NewMessageBean();
                if (customMsgContent.fromType.equals("1")) {//系统
                    //TODO 系统如何显示
                } else if (customMsgContent.fromType.equals("2")) {//客户
                    newMessageBean.setType(Constants.MSG_NULL);
                    customMsgContent.customerIcon = portrait;
                } else {// 坐席
                    newMessageBean.setType(Constants.MSG_MY);
                    customMsgContent.customerIcon = "";
                }
                if (isHttpData) {
                    customMsgContent.msgState = Constants.MSG_STATE_SEND_UNDO;
                } else {
                    String msg = customMsgContent.msg;
                    msg = LoginDesUtil.decryptToURL(msg, LoginDesUtil.ZZGJSPWDKEY);
                    customMsgContent.msg = msg;
                    int msgStage = customMsgContent.msgState;
                    if (msgStage == Constants.MSG_STATE_SENDING) {
                        customMsgContent.msgState = Constants.MSG_STATE_SEND_UNDO;
                    }
                }
                newMessageBean.customMsgContent = customMsgContent;
                newMessageBean.mutiMsg = ExpressionUtils.dispatchExpression(ChatActivity.this,newMessageBean.customMsgContent.msg);
                chatList.add(newMessageBean);
            }

        }
    }

    private void readChatdb(List<CustomMsgContent> customMsgContents) {
        if (customMsgContents != null && customMsgContents.size() > 0) {
            for (int i = 0; i < customMsgContents.size(); i++) {
                final CustomMsgContent content = customMsgContents.get(i);
                final NewMessageBean newMessageBean = new NewMessageBean();
                if (content.fromType.equals("1")) {//系统
                    //TODO 系统如何显示
                } else if (content.fromType.equals("3")) {//坐席
                    newMessageBean.setType(Constants.MSG_MY);
                } else {//客户
                    newMessageBean.setType(Constants.MSG_NULL);

                }
                if (content.msgState == Constants.MSG_STATE_SENDING) {
                    content.msgState = Constants.MSG_STATE_SEND_UNDO;
                }
                String msg = content.msg;
                msg = LoginDesUtil.decryptToURL(msg, LoginDesUtil.ZZGJSPWDKEY);
                content.msg = msg;
                CRMLog.LogInfo("STATE", "readChatdb  :" + msg+"||"+content.id);
                newMessageBean.customMsgContent = content;
                newMessageBean.mutiMsg = ExpressionUtils.dispatchExpression(ChatActivity.this,content.msg);
                CRMLog.LogInfo(Constants.LOG_TAG, "readChatdb analyMsg:" + newMessageBean.customMsgContent.customerIcon);
                chatList.add(0, newMessageBean);
            }
            chatRoomAdapter.changeData(chatList);
            msgListView.setSelection(customMsgContents.size() - 1);
        }
        msgListView.stopRefresh();

    }


    private void addListeners() {
        chatManager.addOnNewMessageListener(TAG,this);
        chatManager.addOnMsgReceipt(this);

        mKeyboardSwitchManager.setKeyboardChangeListener(new KeyboardChangeHelper.KeyBoardListener() {
            @Override
            public void onKeyboardOpened() {
                CRMLog.LogDebg(TAG,"onGlobalLayout opened ");
                if(mChatBottomFragment.editHasContent()){
                    mChatBottomFragment.showSendBtn();
                    mChatBottomFragment.hideChatAdd();
                }
                CRMLog.LogInfo(TAG,"current Thread "+Thread.currentThread().getId());
                delaySlideToEnd(100);
            }

            @Override
            public void onKeyboardClosed() {
                CRMLog.LogDebg(TAG,"onGlobalLayout closed ");
                if(mChatBottomFragment.editHasContent()){
                    mChatBottomFragment.showSendBtn();
                    mChatBottomFragment.hideChatAdd();
                }
            }

            @Override
            public void onKeyboardHeightChanged(int newHeight) {
                mChatBottomFragment.changeBottomHeight(newHeight);
            }
        });

        mChatBottomFragment.setOnFragmentCreateListener(this);
        msgListView.setXListViewListener(this);
        msgHistoryBtn.setOnClickListener(this);
        iv_back_parent.setOnClickListener(this);
        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position<1){
                    return;
                }
                String json = chatList.get(position - 1).customMsgContent.msg;
                String msgType = chatList.get(position - 1).customMsgContent.msgType;
                informationOrPic(json, msgType);
                CRMLog.LogInfo("test", json);
            }
        });
        msgListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CRMLog.LogInfo("ChatBottomFragment","onTouch");
                mChatBottomFragment.hideAll();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }
                changeBottomEdit();
                return false;
            }
        });

        msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 1) {
                    return false;
                }
                String msgType = chatList.get(position - 1).customMsgContent.msgType;
                String fromType = chatList.get(position - 1).customMsgContent.fromType;
                CRMLog.LogInfo("fromType", fromType);
                //1 是系统消息
                if (fromType.equals("1")) {
                    return false;
                }
                if ("text".equals(msgType) || "text/plain".equals(msgType)) {
                    showCpyPop(view);
                }

                return false;
            }
        });

        copyBtn.setOnClickListener(this);

        chatMoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                switch (position){
                    //结束会话
                    case 0:
                        if (BizSeriesUtil.isKZKF(ChatActivity.this)){

                            if (Constants.STATE_CONVERSATION_INVAILD.equals(chatConversation.status)){
                                Toast.makeText(ChatActivity.this, getString(R.string.invaild_conversation_cannot_ended), Toast.LENGTH_LONG).show();
                                return;
                            }

                            checkDialog = DialogFactory.getLoadingDialog(ChatActivity.this, getString(R.string.status_sending));
                            checkDialog.show();

                            String bizSeries = BizSeriesUtil.isKZKF(ChatActivity.this) ? Constants.BIZ_SERIES_SDK_KZKF : "";
                            VolleyRequest.httpConversationDelete(ChatActivity.this, HttpUrls.HTTP_CONVERSATION_DELETE
                                    , Constants.TAG_CONVERSATION_DELETE, umId, customerId, chatConversation.paImType,
                                    Constants.STATE_CONVERSATION_INVAILD, bizSeries, shirokey, volleyDeleteKZKF);

                        } else {
                            showMsgHistory();
                        }
                        break;
                    //转接
                    case 1:
                        //先判断是不是有效会话，如果是无效会话，直接提示：会话无效，无法转接
                        if (BizSeriesUtil.isKZKF(ChatActivity.this) &&
                                Constants.STATE_CONVERSATION_INVAILD.equals(chatConversation.status)){
                            Toast.makeText(ChatActivity.this, getString(R.string.invaild_conversation_cannot_transfer), Toast.LENGTH_LONG).show();
                            return;
                        }

                        checkDialog = DialogFactory.getLoadingDialog(ChatActivity.this, getString(R.string.status_transfering));
                        checkDialog.show();
                        VolleyRequest.transferKZKF(ChatActivity.this, customerId, umId, chatConversation.paImType, "reason", shirokey, volleyTranslateKZKF);
                        break;
                    //历史消息
                    case 2:
                        showMsgHistory();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void informationOrPic(String json, String msgType) {
        H5InformationContent h5InformationContent = CommonUtils.handleHttpResult(H5InformationContent.class, json);
        if (msgType != null && (msgType.equals("image/jpeg") || msgType.equals("image"))) {

            Bundle bundle = new Bundle();
            bundle.putString("picUrl", json);
            toActivity(this, ImageScaleActivity.class, bundle);
            return;
        } else if (h5InformationContent != null && h5InformationContent.type.equals("pic")) {
            try {
                JSONObject object = new JSONObject(json).getJSONObject("typeContent");
                String msgPic = (String) object.getJSONArray("msgList").get(0);
                CRMLog.LogInfo(Constants.LOG_TAG, msgPic + "");
                Bundle bundle = new Bundle();
                bundle.putString("picUrl", msgPic);
                toActivity(this, ImageScaleActivity.class, bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (h5InformationContent != null && h5InformationContent.type.equals("news")) {
            Bundle bundle = new Bundle();
            bundle.putString("url", h5InformationContent.typeContent.data.get(0).url);
            bundle.putString("h5Type", "资讯");
            bundle.putString("urlFrom", "chat");
            toActivity(this, WebViewActivity.class, bundle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String json = null;
        CRMLog.LogInfo(Constants.LOG_TAG, "activityResult"+resultCode);
        if (resultCode == RESULT_CANCELED) {

            return;
        }
        if (requestCode == Constants.REQUEST_C0DE) {
            if (data != null) {
                json = data.getExtras().getString("jsons");
            }
            CRMLog.LogInfo(Constants.LOG_TAG, "json=" + json);
            extractH5Json(json);
        }
    }

    private void extractH5Json(String json) {
        CustomMsgContent customMsgContent = new CustomMsgContent();
        customMsgContent.msgType = "h5";
        customMsgContent.customerName = name;
        customMsgContent.customerIcon = "123";
        customMsgContent.fromType = "3";
        customMsgContent.customerId = customerId;
        customMsgContent.msg = json;
        customMsgContent.toType = "send";
        customMsgContent.paImType = source;
        customMsgContent.umId = umId;
        customMsgContent.msgId = customMsgContent.messageId = CommonUtils.generateMsgId();
        customMsgContent.createTime = System.currentTimeMillis() + "";
        String str = new Gson().toJson(customMsgContent);
        chatManager.sendMsg(str,customMsgContent.msgId);
        addSendingItem(customMsgContent);
    }


    private void initView() {
        ppview = LayoutInflater.from(this).inflate(R.layout.copy_layout, null);
        ppview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        copyBtn = (Button) ppview.findViewById(R.id.copy);

        msgListView = (MsgListView) findViewById(R.id.msg_listView);
        action_title = (TextView) findViewById(R.id.action_title);
        action_title.setText(R.string.txt_name);
        iv_serch_btn = (ImageView) findViewById(R.id.iv_serch_btn);
        iv_back_parent = (LinearLayout) findViewById(R.id.iv_back_parent);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        iv_serch_btn.setImageResource(R.drawable.icon_show_more);
        msgHistoryBtn = (LinearLayout) findViewById(R.id.iv_serch_parent);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        unreadNum = (TextView) findViewById(R.id.tv_unread);
        conversation_pop_layout = getLayoutInflater().inflate(R.layout.conversation_pop_layout, null);
        conversation_pop_layout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        chatMoreList = (ListView) conversation_pop_layout.findViewById(R.id.pop_list);
        ChatMoreAdapter chatMoreAdapter = new ChatMoreAdapter(this,initPopList(),R.layout.pop_list_item);
        chatMoreList.setAdapter(chatMoreAdapter);
        handleSourceData();
        msgListView.setPullLoadEnable(false);
        msgListView.setPullRefreshEnable(true);
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeigh / 3;
    }

    private List<KZKFChatMoreBean> initPopList(){
        List<KZKFChatMoreBean> list = new ArrayList<>();



        if (BizSeriesUtil.isKZKF(this)){
            //结束会话
            KZKFChatMoreBean finishBean = new KZKFChatMoreBean();
            finishBean.title = getString(R.string.txt_msg_finish_chat);
            finishBean.drawable = getResources().getDrawable(R.drawable.end_dialog);
            list.add(finishBean);

            //转接
            KZKFChatMoreBean translateBean = new KZKFChatMoreBean();
            translateBean.title = getString(R.string.txt_msg_translate_chat);
            translateBean.drawable = getResources().getDrawable(R.drawable.trans_dialog);
            list.add(translateBean);
        }

        //历史
        KZKFChatMoreBean historyBean = new KZKFChatMoreBean();
        historyBean.title = getString(R.string.txt_msg_history);
        historyBean.drawable = getResources().getDrawable(R.drawable.icon_msg_history);
        list.add(historyBean);

        return list;
    }

    private void handleSourceData() {
        if (Constants.TAG_CHANNEL_SMS.equals(name)) {
            for (ChatAddBean chatAddBean : ChatAddHolder.getChatAddBeans()) {
                if (!chatAddBean.iconName.equals("发短信")) {
                    ChatAddHolder.getChatAddBeans().remove(chatAddBean);
                }
            }
        }
    }

    private void InitData() {
        chatRoomAdapter = new ChatAdapter(this, chatList);
        chatRoomAdapter.setSendFailCallback(this);
        mChatBottomFragment = new ChatBottomFragment();
        CRMLog.LogInfo("ChatBottomFragment","InitData");
        mChatBottomFragment.showBottom(this,mChatBottomFragment);
        msgListView.setAdapter(chatRoomAdapter);
        msgListView.setSelection(chatRoomAdapter.getCount() - 1);
        dataPops.add(DATA_CUSTOMER);
        dataPops.add(DATA_MSG_HISTORY);
        mKeyboardSwitchManager = new KeyboardSwitchManager
                .Builder()
                .bindContentView(msgListView)
                .with(this)
                .build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_serch_parent:
                CommonUtils.hideInputManager(this);
                showPopUp(conversation_pop_layout, iv_serch_btn);
                break;
            case R.id.iv_back_parent:
                CommonUtils.hideInputManager(this);
                finish();
                break;
            case R.id.copy:
                doClip();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CRMLog.LogInfo(Constants.LOG_TAG, "onBackPressed");
    }

    public void send(CharSequence editContent) {
        String content = editContent.toString();
        if (!TextUtils.isEmpty(content.trim())) {
            NewMessageBean bean = new NewMessageBean();
            String msgStr = ParseEmojiMsgUtil.convertToMsg(editContent, this);// 这里不要直接用mEditM
            CustomMsgContent customMsgContent = new CustomMsgContent();
            customMsgContent.customerIcon = "";
            customMsgContent.customerId = customerId;
            customMsgContent.customerName = name;
            customMsgContent.msgType = "text";
            customMsgContent.toType = "send";
            customMsgContent.msg = msgStr;
            customMsgContent.paImType = source;
            customMsgContent.umId = umId;
            customMsgContent.fromType = "3";
            customMsgContent.msgId = customMsgContent.messageId = CommonUtils.generateMsgId();
            customMsgContent.createTime = System.currentTimeMillis() + "";
            bean.customMsgContent = customMsgContent;
            bean.setType(Constants.MSG_MY);
            String jsonData = new Gson().toJson(customMsgContent);
            chatManager.sendMsg(jsonData,customMsgContent.msgId);
            addSendingItem(customMsgContent);

        }
    }

    private void showPopUp(View layout, View showLocationView) {

        popupWindow = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        showLocationView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        showLocationView.getLocationOnScreen(location);
        popupWindow.showAtLocation(showLocationView, Gravity.NO_GRAVITY, location[0] - popupWindow.getContentView().getMeasuredWidth() + showLocationView.getMeasuredWidth(),location[1]+showLocationView.getMeasuredHeight());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
        TCAgent.onPageStart(this, PATDEnum.CHAT.getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TCAgent.onPageEnd(this, PATDEnum.CHAT.getName());

    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onRefresh() {
        if (chatList != null && chatList.size() > 0) {

            refreshDb();
        }
    }

    private void refreshDb() {
        List<CustomMsgContent> customMsgContents = null;
        String umIdDb = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        List<CustomMsgContent> customMsg =
                CrmDaoHolder
                        .getInstance()
                        .getCustomMsgContentDao()
                        .queryCusContents(umIdDb, customerId, source);
        CRMLog.LogInfo(Constants.LOG_TAG, "customMsg.size()    ............." + customMsg.size() + "customerId ---" + customerId);
        String createTime = chatList.get(0).customMsgContent.createTime;
        customMsgContents = CrmDaoHolder
                .getInstance()
                .getCustomMsgContentDao()
                .queryForCreateTime(umIdDb, customerId, createTime, source,0, 10);
        if (customMsgContents == null || customMsgContents.size() == 0) {
            msgListView.stopRefresh();
        } else {
            Collections.reverse(customMsgContents);
            readChatdb(customMsgContents);
        }

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void sendFail(int position) {
        failIndex = position;
        AlertView alertView = new AlertView("发送失败", "消息是否重发", "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, onSendFailClickListener);
        alertView.show();
    }

    OnItemClickListener onSendFailClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(Object o, int position) {
            CRMLog.LogInfo(Constants.LOG_TAG, "position" + position);
            if (position == 0) {
                faiLMessageBean = chatList.get(failIndex);
                faiLMessageBean.customMsgContent.toType = "123";
                String umId = LoginDesUtil.decryptToURL(faiLMessageBean.customMsgContent.umId, LoginDesUtil.ZZGJSPWDKEY);
                if (umId != null) {
                    faiLMessageBean.customMsgContent.umId = umId;
                }
                String str = new Gson().toJson(faiLMessageBean.customMsgContent);
                CRMLog.LogInfo(Constants.LOG_TAG, "reSend" + str);
                chatManager.sendMsg(str);
            }
        }
    };

    @Override
    public void onSuccess(String s) {
        if (!CrmEnvValues.getInstance().isInforationSend()) {
            NewMessageBean receipt = new NewMessageBean();
            receipt.customMsgContent = new Gson().fromJson(s,CustomMsgContent.class);
            receipt.mutiMsg = ExpressionUtils.dispatchExpression(ChatActivity.this, receipt.customMsgContent.msg);
            CRMLog.LogInfo("loadTask :","onPostExecute msg-->"+receipt.customMsgContent.msg);
            NewMessageBean dbMessageBean = null;
            try {
                dbMessageBean = receipt.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            CommonUtils.encryptMsgData(dbMessageBean.customMsgContent);
            dbMessageBean.customMsgContent.msgState = Constants.MSG_STATE_SEND_SUCCESSFUL;
            for (NewMessageBean item : chatList) {
                if (item.customMsgContent.messageId.equals(receipt.customMsgContent.messageId)) {
                    item.customMsgContent.msgState = Constants.MSG_STATE_SEND_SUCCESSFUL;
                }
            }
            addOrUpdate(dbMessageBean);
            chatRoomAdapter.changeData(chatList);
        }
    }

    @Override
    public void onDefeat(String s) {
        NewMessageBean defeatBean = new NewMessageBean();
        defeatBean.customMsgContent = new Gson().fromJson(s,CustomMsgContent.class);
        defeatBean.mutiMsg = ExpressionUtils.dispatchExpression(ChatActivity.this, defeatBean.customMsgContent.msg);
        NewMessageBean dbMessageBean = null;
        try {
            dbMessageBean = defeatBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CommonUtils.encryptMsgData(dbMessageBean.customMsgContent);
        dbMessageBean.customMsgContent.msgState = Constants.MSG_STATE_FAILED;
        for (NewMessageBean newMessageBean1 : chatList) {
            if (newMessageBean1.customMsgContent.messageId.equals(defeatBean.customMsgContent.messageId)) {
                newMessageBean1.customMsgContent.msgState = Constants.MSG_STATE_FAILED;
                CRMLog.LogInfo(Constants.LOG_TAG, "MSG_STATE_FAILED  ：" + newMessageBean1.customMsgContent.msgState);
            }
            CRMLog.LogInfo(Constants.LOG_TAG, "MSG_STATE_FAILED meizhaodao ：");

        }
        addOrUpdate(dbMessageBean);
        chatRoomAdapter.changeData(chatList);
    }

    @Override
    public void onNewMessage(String s) {
        //首先判断Msg类型：text / event，如果是event，则直接return，此处不处理
        if(isEventMsg(s)){
            return;
        }
        //如果Msg类型为text，则继续执行
        final CustomMsgContent newMsg = new Gson().fromJson(s,CustomMsgContent.class);

        //other chat return directly
        CRMLog.LogInfo("loadTask :","onPostExecute customerID-->"+customerId+"||"+newMsg.customerId);
        if(!customerId.equals(newMsg.customerId)||!source.equals(newMsg.paImType)){
            return ;
        }
        NewMessageBean newMessageBean = new NewMessageBean();
        newMessageBean.customMsgContent = newMsg;
        newMessageBean.mutiMsg = ExpressionUtils.dispatchExpression(ChatActivity.this, newMessageBean.customMsgContent.msg);
        CRMLog.LogInfo("loadTask :","onPostExecute msg-->"+newMessageBean.customMsgContent.msg);
        NewMessageBean newMessageBeanFordb = null;
        try {
            newMessageBeanFordb = newMessageBean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CommonUtils.encryptMsgData(newMessageBeanFordb.customMsgContent);
        newMessageBean.customMsgContent.msgState = Constants.MSG_STATE_SEND_UNREAD;
        //如果list中已经有该条消息，则不添加。场景：当回执有多条status相同的消息，只添加一次。
        for(NewMessageBean showBean : chatList){
            if(showBean.customMsgContent.messageId.equals(newMessageBean.customMsgContent.messageId)){
                return;
            }
        }
        //清除当前会话未读数
        clearConverUnread(newMessageBean.customMsgContent.customerId, newMessageBean.customMsgContent.paImType,
                newMessageBean.customMsgContent.umId);
        chatList.add(newMessageBean);
        chatRoomAdapter.changeData(chatList);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(msgListView.getCount() - 1);
            }
        });

        //判断是否是空中客服,更新数据库中的status
        if (BizSeriesUtil.isKZKF(this)){
            //判断是否是空中客服，根据数据库中的status判断是否让用户输入,isOverDialogue不为null的时候，表示会话结束，需要禁止用户输入
            chatConversation.status = TextUtils.isEmpty(newMsg.isOverDialogue) ? Constants.STATE_CONVERSATION_VAILD : Constants.STATE_CONVERSATION_INVAILD;
            CrmDaoHolder.getInstance().getConversationBeanDao().updateConversationStatus(umId, customerId, chatConversation.paImType, chatConversation.status);
            mHandler.postDelayed(this,0);
        }


        //客户消息添加
        addDb(newMessageBeanFordb.customMsgContent);
    }
    private synchronized void addOrUpdate(NewMessageBean newMessageBean) {
        CommonUtils.encryptMsgData(newMessageBean.customMsgContent);
        int id = CrmDaoHolder
                .getInstance()
                .getCustomMsgContentDao()
                .queryForId(newMessageBean.customMsgContent);
        if (id != -1) {
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .update(newMessageBean.customMsgContent);
        } else {
            //坐席发消息添加
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .add(newMessageBean.customMsgContent);
        }
    }

    /**
     * 复制粘贴
     */
    private void doClip() {
        ClipboardManager cp = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String copyStr = copyBtn.getTag(R.id.id_cpystr_tag).toString();
        ClipData cpd = ClipData.newPlainText("simple_text",copyStr);
        cp.setPrimaryClip(cpd);
        pp.dismiss();
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
    /**
     * 显示复制粘贴视图
     */
    private void showCpyPop(View view) {
        TextView root = (TextView) view.findViewById(R.id.chat_room_content);
        String copyStr = root.getText().toString();
//        Pattern pattern = Pattern.compile("\\[e\\](.*?)\\[/e\\]");
//        Matcher matcher = pattern.matcher(copyStr);
//
//        if(matcher.matches()){
//            return;
//        }
        if(copyStr.contains("[e]")||copyStr.contains("[\\e]")){
            return;
        }
        copyBtn.setTag(R.id.id_cpystr_tag,copyStr);
        copyBtn.setTag(R.id.id_root_tag,root);
        //可以考虑windowmanager
        pp = new PopupWindow();
        pp.setContentView(ppview);
        pp.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pp.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pp.setFocusable(false);
        pp.setOutsideTouchable(true);
        pp.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        root.getLocationOnScreen(location);
        CRMLog.LogInfo("location", "" + location);
        pp.showAtLocation(root,
                Gravity.NO_GRAVITY,
                location[0] + root.getMeasuredWidth() / 2 - pp.getContentView().getMeasuredWidth() / 2,
                location[1] - pp.getContentView().getMeasuredHeight());
        pp.setOnDismissListener(new MyOnDismissListener());
    }

    @Override
    public void onFaceBtnClick() {
        if(!mKeyboardSwitchManager.isOpened()){
            mChatBottomFragment.showFace();
            delaySlideToEnd(100);
            return;
        }
        mKeyboardSwitchManager.lockContentHeight();
        mChatBottomFragment.showFace();
        CommonUtils.hideInputManager(this);
        mKeyboardSwitchManager.unLockContentHeight();
    }

    @Override
    public void onChatAddClick() {
        if(!mKeyboardSwitchManager.isOpened()){
            mChatBottomFragment.showChatAdd();
            delaySlideToEnd(100);
            return;
        }
        mKeyboardSwitchManager.lockContentHeight();
        mChatBottomFragment.showChatAdd();
        CommonUtils.hideInputManager(this);
        mKeyboardSwitchManager.unLockContentHeight();
    }

    @Override
    public void onSendBtnClick(CharSequence editContent) {
        send(editContent);
    }

    @Override
    public void onEditClick() {
        CRMLog.LogDebg(TAG,""+mChatBottomFragment.isChatAddShow()+
                "||"+mChatBottomFragment.isChatAddShow());
        if(mChatBottomFragment.isFaceShow()||mChatBottomFragment.isChatAddShow()){
            mKeyboardSwitchManager.lockContentHeight();
            if(mChatBottomFragment.editHasContent()){
                mChatBottomFragment.hideButSend();
            } else {
                if (!BizSeriesUtil.isKZKF(this)){
                    mChatBottomFragment.hideAll();
                } else {
                    mChatBottomFragment.hideFaceAndChatAdd();
                }
            }
            mKeyboardSwitchManager.unLockContentHeight();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String h5Type = null;
        CommonUtils.hideInputManager(ChatActivity.this);
        ChatAddBean chatAddBean = ChatAddHolder.getChatAddBeans().get(position);
        if (chatAddBean.iconName.equals("模板")) {
            h5Url = HttpUrls.UCP_TEMPLATE_URL;
            h5Type = "模板";
        } else if (chatAddBean.iconName.equals("发短信")) {
            h5Url = HttpUrls.UCP_SMS_URL;
            h5Type = "发短信";
        } else if (chatAddBean.iconName.equals("快捷回复")) {
            h5Url = HttpUrls.UCP_QUICK_REPLY_URL;
            h5Type = "快捷回复";
        } else if (chatAddBean.iconName.equals("图片")) {
            h5Url = HttpUrls.UCP_PICTURE_URL;
            h5Type = "图片";
        }
        Bundle bundle = new Bundle();
        bundle.putString("url", h5Url);
        bundle.putString("h5Type", h5Type);
        Intent intent = new Intent(ChatActivity.this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_C0DE);
    }

    @Override
    public void onFragmentCreate(String tag) {

        handleSource();
        mChatBottomFragment.setInputFrameBtnClickListener(this);
        mChatBottomFragment.setChatAddItemClickListener(this);
    }

    @Override
    public void run() {
        //关闭键盘
        CommonUtils.hideInputManager(ChatActivity.this);
        //判断是否是空中客服，根据数据库中的status判断是否让用户输入
        changeBottomEdit();
    }

    private class MyOnDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            View root = (View) copyBtn.getTag(R.id.id_root_tag);
            if((int)root.getTag()==ChatAdapter.MSG_FROM_MYSELF){
                root.setBackgroundResource(R.drawable.chatfrom_bg_out);
            }else if((int)root.getTag()==ChatAdapter.MSG_TO){
                root.setBackgroundResource(R.drawable.chatfrom_bg_in);
            }
        }
    }

    public void showMsgHistory() {
        Bundle bundle = new Bundle();
        bundle.putString("contactId", customerId);
        bundle.putString("icon",portrait);
        toActivity(this, MsgHistoryActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(broadcast);
        chatManager.removeOnNewMessageListener(TAG);
        chatManager.removeOnMsgReceipt(this);
        mKeyboardSwitchManager.destroy();
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            chatRoomAdapter.notifyDataSetChanged();
        }
    }

    private void addDb(CustomMsgContent customMsgContent){
        CRMLog.LogInfo("addDb","messageId="+customMsgContent.messageId);
        CommonUtils.encryptMsgData(customMsgContent);
        CustomMsgContent tmp_customMsgContent =
                CrmDaoHolder
                        .getInstance()
                        .getCustomMsgContentDao()
                        .queryForMesgId(customMsgContent.messageId);
        if(tmp_customMsgContent==null) {
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .add(customMsgContent);
        }else {
            CrmDaoHolder
                    .getInstance()
                    .getCustomMsgContentDao()
                    .update(customMsgContent);
        }
    }

    private void clearConverUnread(String customerId,String paImType,String umId){
        umId = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        ConversationBean conversationBean =
                CrmDaoHolder
                        .getInstance()
                        .getConversationBeanDao()
                        .queryConversation(customerId, paImType, umId);
        if(conversationBean!=null) {
            CrmDaoHolder
                    .getInstance()
                    .getConversationBeanDao()
                    .updateUnread(conversationBean, 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //如果表情框弹出，不响应手势操作。
        if(mChatBottomFragment.isFaceShow()&&ev.getAction()!=MotionEvent.ACTION_DOWN){
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
            return super.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private int countAllUnread(){
        int count=0;
        String encryptUm = LoginDesUtil.encryptToURL(umId, LoginDesUtil.ZZGJSPWDKEY);
        List<ConversationBean> allConver = CrmDaoHolder
                .getInstance()
                .getConversationBeanDao()
                .queryForUmId(encryptUm);
        for(ConversationBean conversationBean : allConver){
            boolean isCurrentConver = TextUtils.equals(conversationBean.customerId,customerId)
                    &&TextUtils.equals(conversationBean.paImType,source)
                    &&TextUtils.equals(conversationBean.umId,umId);
            if(!isCurrentConver) continue;
            count += conversationBean.unReadCount;
        }
        return count;
    }

    public static class UnreadNumBroadcast extends BroadcastReceiver {
        private ChatActivity mActivity;
        private WeakReference<ChatActivity> weakReference;
        UnreadNumBroadcast(ChatActivity activity){
            weakReference = new WeakReference<>(activity);
            mActivity = weakReference.get();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = mActivity.countAllUnread();
             CRMLog.LogInfo("countAllUnread", "receive" + count);
            if(count!=0) {
                mActivity.unreadNum.setText("(" + count + ")");
            }
        }

    }
    private void addSendingItem(CustomMsgContent sendingMsg){
        NewMessageBean sendingNewMessage = new NewMessageBean();
        sendingNewMessage.customMsgContent = sendingMsg;
        sendingNewMessage.mutiMsg = ExpressionUtils.dispatchExpression(this,sendingMsg.msg);
        sendingNewMessage.customMsgContent.msgState = Constants.MSG_STATE_SENDING;
        sendingNewMessage.msgState = Constants.MSG_STATE_SENDING;
        chatList.add(sendingNewMessage);
        chatRoomAdapter.changeData(chatList);
        slideToEnd();
        CustomMsgContent dbMsg = null;
        try {
            dbMsg = (CustomMsgContent) sendingNewMessage.customMsgContent.clone();
            CommonUtils.encryptMsgData(dbMsg);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        CommonUtils.encryptMsgData(dbMsg);
        CrmDaoHolder
                .getInstance()
                .getCustomMsgContentDao()
                .addOrUpdate(dbMsg);
    }
    private void slideToEnd(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(chatList.size()-1);
            }
        });
    }
    private void delaySlideToEnd(int delayTime){
        msgListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                msgListView.setSelection(chatList.size()-1);
            }
        },delayTime);
    }
}


