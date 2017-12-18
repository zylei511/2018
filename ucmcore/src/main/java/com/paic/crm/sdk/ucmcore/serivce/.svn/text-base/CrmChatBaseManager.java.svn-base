package com.paic.crm.sdk.ucmcore.serivce;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.RawRes;
import android.text.TextUtils;

import com.paic.crm.sdk.ucmcore.listener.CrmBaseListener;
import com.paic.crm.sdk.ucmcore.listener.MsgFilter;
import com.paic.crm.sdk.ucmcore.listener.OnIMConnectionListener;
import com.paic.crm.sdk.ucmcore.listener.OnMsgReceipt;
import com.paic.crm.sdk.ucmcore.listener.OnNewMessageListener;
import com.paic.crm.sdk.ucmcore.listener.OverallMsgListener;
import com.paic.crm.sdk.ucmcore.utils.ActivityCollector;
import com.paic.crm.sdk.ucmcore.utils.CRMLog;
import com.paic.crm.sdk.ucmcore.utils.SoundUtil;
import com.paic.crm.sdk.ucmcore.utils.VibratorUtil;
import com.pingan.core.im.client.app.IMConnectStateListener;
import com.pingan.core.im.client.app.PAIMApi;
import com.pingan.core.im.client.app.PAIMStateListener;
import com.pingan.core.im.packets.model.IMConnectState;
import com.pingan.core.im.parser.JidManipulator;
import com.pingan.paimkit.common.userdata.PMDataManager;
import com.pingan.paimkit.core.BaseProcessor;
import com.pingan.paimkit.core.interfaces.IBaseProcessor;
import com.pingan.paimkit.module.chat.bean.message.BaseChatMessage;
import com.pingan.paimkit.module.chat.bean.message.ChatMessageHyperLink;
import com.pingan.paimkit.module.chat.chatsession.BaseChatSession;
import com.pingan.paimkit.module.chat.chatsession.FriendChatSession;
import com.pingan.paimkit.module.chat.chatsession.GroupChatSession;
import com.pingan.paimkit.module.chat.dao.chatdao.ChatMessgeDao;
import com.pingan.paimkit.module.chat.manager.PMChatBaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yueshaojun on 2017/4/28.
 */
public class CrmChatBaseManager extends PMChatBaseManager implements IMConnectStateListener{
    private static CrmChatBaseManager instance;
    private static final String TAG = "CrmChatBaseManager";
    private final Map<String, BaseChatSession> mChatMap = new HashMap(16);
    private Map<String,OnNewMessageListener> onNewMessageListeners = new HashMap<>();
    private Set<OnMsgReceipt> onMsgReceipts = new HashSet<>();
    private Set<OnIMConnectionListener> onIMConnectionListeners = new HashSet<>();
    private CrmChatSession mBaseChatSession;
    private OverallMsgListener mOverallMsgListener;
    private MsgFilter msgFilter;
    private static int mVibrateTime = 600;//默认震动时间
    private static int mResourceId = Integer.MIN_VALUE;

    private static final String PUBLICK_KEY="10026@publicservice.pingan.com.cn";

    private CrmChatBaseManager(IBaseProcessor processor) {
        super(processor);
        PAIMApi.getInstance().addIMServiceListener(this);
//        mBaseChatSession = this.createChatSession(PUBLICK_KEY);
    }

    public static CrmChatBaseManager getInstance() {
        if (instance == null) {
            synchronized (CrmChatBaseManager.class) {
                if (instance == null) {
                    CRMLog.LogInfo("CrmChatBaseManager","new a CrmChatBaseManager!");
                    instance = new CrmChatBaseManager(new BaseProcessor());
                }
            }
        }
        return instance;
    }


    @Override
    protected void eventMessage(int pack_type
            , int event_id
            , int event_type
            , String to_jid
            , Object data
            , boolean isUpdate) {

        super.eventMessage(pack_type, event_id, event_type, to_jid, data, isUpdate);
        String substring = "";
        String packet_id;

        switch (event_type) {
            case 1:
                CRMLog.LogInfo("CrmChatBaseManager", "接收到消息");
                if (data instanceof ChatMessageHyperLink) {

                    String json = ((ChatMessageHyperLink) data)
                            .getmHyperLinkText();
                    CRMLog.LogInfo("CrmChatBaseManager", json);
                    //添加全局消息处理，在所有回调之前
                    if(mOverallMsgListener!=null){
                        mOverallMsgListener.onMsgComeIn(json);
                    }
                    List<String> filterList = new ArrayList<>();
                    if(msgFilter!=null){
                        filterList = msgFilter.getDisableTagList();
                    }
                    for(String tag : onNewMessageListeners.keySet()){
                        boolean disAllowDispatch = filterList.contains(tag);
                        if(!disAllowDispatch){
                            onNewMessageListeners.get(tag).onNewMessage(json);
                        }
                    }
                }
                break;
            case 2:
                CRMLog.LogInfo("CrmChatBaseManager", "2");
                if (to_jid.contains("@")) {
                    substring = to_jid.substring(0, to_jid.indexOf("@"));
                }
                if (data instanceof String) {
                    packet_id = (String) data;
                    ChatMessgeDao chatMessgeDao = new ChatMessgeDao(PMDataManager.defaultDbHelper(), substring);

                    BaseChatMessage baseChatMessage = chatMessgeDao.getChatMessageById(packet_id);
                    CRMLog.LogInfo("CrmChatBaseManager", "发送成功：" + baseChatMessage.getShowContent());

                    for(OnMsgReceipt l : onMsgReceipts){
                        l.onSuccess(baseChatMessage.getShowContent());
                    }
                }
                break;
            case 3:
                CRMLog.LogInfo("CrmChatBaseManager", "3");
            case 4:
                CRMLog.LogInfo("CrmChatBaseManager", "4");

                if (to_jid.contains("@")) {
                    substring = to_jid.substring(0, to_jid.indexOf("@"));
                }
                if (data instanceof String) {
                    packet_id = (String) data;
                    ChatMessgeDao chatMessgeDao = new ChatMessgeDao(PMDataManager.defaultDbHelper(), substring);
                    BaseChatMessage baseChatMessage = chatMessgeDao.getChatMessageById(packet_id);
                    CRMLog.LogInfo("CrmChatBaseManager", "发送失败：" + baseChatMessage.getShowContent());
                    for(OnMsgReceipt l : onMsgReceipts){
                        l.onDefeat(baseChatMessage.getShowContent());
                    }
                }
        }
    }

    public void addOnNewMessageListener(String tag, OnNewMessageListener listener){
        onNewMessageListeners.put(tag,listener);
    }
    public void removeOnNewMessageListener(String tag){
        onNewMessageListeners.remove(tag);
    }

    public void addOnMsgReceipt(OnMsgReceipt listener){
        onMsgReceipts.add(listener);
    }
    public void removeOnMsgReceipt(OnMsgReceipt listener){
        onMsgReceipts.remove(listener);
    }

    public void setMsgFilter(MsgFilter msgFilter){
        this.msgFilter = msgFilter;
    }
    public void removeMsgFilter(){
        this.msgFilter = null;
    }

    public void addListener(String tag ,CrmBaseListener l){
        if(l instanceof OnNewMessageListener){
            onNewMessageListeners.put(tag,(OnNewMessageListener) l);
        }else if(l instanceof  OnMsgReceipt){
            onMsgReceipts.add((OnMsgReceipt) l);
        }
    }

    public void removeListener(String tag ,CrmBaseListener l){
        if(l instanceof OnNewMessageListener){
            onNewMessageListeners.remove(tag);
        }else if(l instanceof  OnMsgReceipt){
            onMsgReceipts.remove(l);
        }
    }
    public BaseChatSession getBaseChatSession(){
        return mBaseChatSession;
    }

    public void setOverallMsgListener(OverallMsgListener listener){
        this.mOverallMsgListener = listener;
    }
    public void removeOverallMsgListener(){
        this.mOverallMsgListener = null;
    }

    public void sendMsg(String msg){
        if(mBaseChatSession == null) {
            mBaseChatSession = (CrmChatSession) this.createChatSession(PUBLICK_KEY);
        }
        mBaseChatSession.sendTextMessage(msg,null);
    }
    public void sendMsg(String msg,String messageId){
        if(mBaseChatSession == null) {
            mBaseChatSession = (CrmChatSession) this.createChatSession(PUBLICK_KEY);
        }
        mBaseChatSession.sendTextMessage(msg,messageId);
    }
    public BaseChatSession createChatSession(String jid_str) {
        JidManipulator jidManipulator = JidManipulator.Factory.create();
        String server_name = jidManipulator.getServerName(jid_str);
        if(TextUtils.isEmpty(server_name)) {
            return null;
        } else {
            BaseChatSession _session = null;
            Map pmDataManager = this.mChatMap;
            synchronized(this.mChatMap) {
                _session = (BaseChatSession)this.mChatMap.get(jid_str);
                if(_session != null) {
                    return (BaseChatSession)_session;
                }
            }

            PMDataManager pmDataManager1 = PMDataManager.getInstance();
            if(server_name.equals(pmDataManager1.getConferenceHost())) {
                _session = new GroupChatSession(jid_str, this);
            } else if(server_name.equals(pmDataManager1.getServerName())) {
                _session = new FriendChatSession(jid_str, this);
            } else if(server_name.equals(pmDataManager1.getPublicSpaceName())) {
                _session = new CrmChatSession(jid_str, this);
            }

            Map var6 = this.mChatMap;
            synchronized(this.mChatMap) {
                this.mChatMap.put(jid_str, _session);
                return (BaseChatSession)_session;
            }
        }
    }

    @Override
    public void onIMConnectState(IMConnectState imConnectState) {
        onIMServiceStateUpdateUI(imConnectState);
    }

    @Override
    public void onIMServiceStateChange(int i, int i1) {

    }
    public void addOnIMConnectionListener(OnIMConnectionListener listener){
        onIMConnectionListeners.add(listener);
    }
    public void removeOnIMConnectionListener(OnIMConnectionListener listener){
        onIMConnectionListeners.remove(listener);
    }
    /**
     * 长连接建立状态
     *
     * @param statePacket 报文
     */
    private void onIMServiceStateUpdateUI(IMConnectState statePacket) {
        int type = statePacket.getType();
        int code = statePacket.getCode();
        CRMLog.LogInfo(TAG, "onIMServiceStateChange type=" + type + " code=" + code);
        for(OnIMConnectionListener onIMConnectionListener : onIMConnectionListeners){
            switch (type) {
                case PAIMStateListener.StateType.CONNECTION_OPEN:
                    CRMLog.LogInfo(TAG, "正在连接");
                    onIMConnectionListener.onConnecting();
                    break;
                case PAIMStateListener.StateType.SOCKET_CREATE:
                    CRMLog.LogInfo(TAG, "正在Socket创建...");
                    onIMConnectionListener.onConnecting();
                    break;
                case PAIMStateListener.StateType.SOCKET_CREATE_SUCCESSFUL:
                    CRMLog.LogInfo(TAG, "Socket   创建成功 耗时：" + statePacket.getCreateSocketTime() + " 毫秒");
                    onIMConnectionListener.onConnected();
                    break;
                case PAIMStateListener.StateType.SOCKET_CREATE_FAIL:
                    CRMLog.LogInfo(TAG, "Socket   创建失败 耗时：" + statePacket.getCreateSocketTime() + " 毫秒");
                    onIMConnectionListener.onConnectError(type,code,"Socket创建失败");
                    break;
                case PAIMStateListener.StateType.CONNECTION_FAIL:
                    if (code == PAIMStateListener.StateCode.FAIL_NETWORK_UNAVAILABLE) {
                        CRMLog.LogInfo(TAG, "连接失败：网络不可用");
                        onIMConnectionListener.onConnectError(type,code,"连接失败：网络不可用");
                    } else if (code == PAIMStateListener.StateCode.FAIL_CONNECTION_CLOSE) {
                        CRMLog.LogInfo(TAG, "连接被断开");
                        onIMConnectionListener.onDisConnected();
                    } else if (code == PAIMStateListener.StateCode.FAIL_RECONNECTING_IN) {
                        CRMLog.LogInfo(TAG, "稍后进行尝试重新连接 ...");
                        onIMConnectionListener.onConnecting();
                    } else {
                        CRMLog.LogInfo(TAG, "连接失败：与服务器建立连接失败");
                        onIMConnectionListener.onConnectError(type,code,"连接失败：与服务器建立连接失败");
                    }
                    break;
                case PAIMStateListener.StateType.LOGINSESSION_LOGIN:
                    CRMLog.LogInfo(TAG, "正在LoginSesion登录...");
                    break;
                case PAIMStateListener.StateType.LOGINSESSION_SUCCESSFUL:
                    CRMLog.LogInfo(TAG, "LoginSession耗时：" + statePacket.getLoginSessionTime() + " 毫秒");
                    break;
                case PAIMStateListener.StateType.LOGINSESSION_FAIL:
                    CRMLog.LogInfo(TAG, "LoginSesion登录失败");
                    if (code == PAIMStateListener.StateCode.FAIL_LOGINSESSION_UNAVAILABLE) {
                        CRMLog.LogInfo(TAG, "LoginSession不可用...");
                    }
                    break;
                case PAIMStateListener.StateType.ACCESSTOKEN_LOGIN:
                    CRMLog.LogInfo(TAG, "accesstoken登录...");
                    break;
                case PAIMStateListener.StateType.ACCESSTOKEN_SUCCESSFUL:
                    CRMLog.LogInfo(TAG, "LoginAccesstoken 成功耗时：" + statePacket.getLoginAccesstokenTime() + " 毫秒");
                    break;
                case PAIMStateListener.StateType.ACCESSTOKEN_FAIL:
                    CRMLog.LogInfo(TAG, "accesstoken登陆失败");
                    if (code == PAIMStateListener.StateCode.FAIL_ACCESSTOKEN_UNAVAILABLE) {
                        CRMLog.LogInfo(TAG, "accesstoken登陆失效");
                    } else if (code == PAIMStateListener.StateCode.FAIL_ACCESSTOKEN_NOTEXIST) {
                        CRMLog.LogInfo(TAG, "accesstoken 不存在");
                    } else if (code == PAIMStateListener.StateCode.FAIL_ACCESSTOKEN_REQUEST) {
                        CRMLog.LogInfo(TAG, "accesstoken 请求失败");
                    }
                    break;
                case PAIMStateListener.StateType.CONNECTION_SUCCESSFUL:
                    CRMLog.LogInfo(TAG, "连接成功");
                    onIMConnectionListener.onConnected();
                    break;
                default:
                    break;
            }
        }

    }
}
