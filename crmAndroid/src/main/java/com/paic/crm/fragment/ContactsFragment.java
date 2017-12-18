package com.paic.crm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.paic.crm.Enums.PATDEnum;
import com.paic.crm.adapter.ContactsAdapter;
import com.paic.crm.android.R;
import com.paic.crm.callback.AdapterCallBack;
import com.paic.crm.entity.ConstantsBean;
import com.paic.crm.entity.GroupMemberBean;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.ui.ChatActivity;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CharacterParser;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.PinyinComparator;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.MsgListView;
import com.paic.crm.widget.SideBar;
import com.paic.crm.widget.XEditText;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by yueshaojun on 16/4/28.
 */
public class
        ContactsFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterCallBack,MsgListView.IXListViewListener{
    private List<GroupMemberBean> contacts = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private MsgListView contactsListView;
    private SideBar sidebar;
    private TextView dialog,no_searched_txt;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private XEditText contacts_search;
    private int tmpPosition =-1;
    private boolean isWhenSearch = false,isFirstClick=true;
    private List<GroupMemberBean> searchResult;
    private RelativeLayout contacts_list_parent;
    private String umId,shiroKey;
    private final static String SOURCE_WEIXIN = "01";
    private final static String SOURCE_SMS = "03";
    private boolean isRefreshOver = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_contacts,null);
        initView(view);
        addListener();
        initData();
        return view;
    }
    public void initData(){
        umId = (String)SpfUtil.getValue(getActivity(),SpfUtil.UMID,"");
        shiroKey = (String)SpfUtil.getValue(getActivity(),SpfUtil.SHIROKEY,"");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        CRMLog.LogInfo("keys", umId + "{}" + shiroKey);
        contactsListView.startRefresh();
    }
    public void initView(View view){
        contacts_list_parent = (RelativeLayout)view.findViewById(R.id.contact_list_parent);
        no_searched_txt = (TextView)view.findViewById(R.id.no_searched);
        contacts_search = (XEditText)view.findViewById(R.id.search_contacts);
        contactsListView = (MsgListView) view.findViewById(R.id.contacts_listView);
        contactsAdapter = new ContactsAdapter(getActivity(),contacts);
        contactsAdapter.setAdapterCallBack(this);
        contactsListView.setAdapter(contactsAdapter);
        contactsListView.setOnItemClickListener(this);
        sidebar = (SideBar) view.findViewById(R.id.sidebar);
        dialog = (TextView) view.findViewById(R.id.dialog);
        sidebar.setTextView(dialog);
        contactsListView.setPullLoadEnable(false);
        contactsListView.setPullRefreshEnable(true);

    }

    public void addListener(){
        contactsListView.setXListViewListener(this);
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                CRMLog.LogInfo("contacts", "|-|" + s.charAt(0));
                // 该字母首次出现的位置
                int position = contactsAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    CRMLog.LogInfo("contacts", "||" + position);
                    CRMLog.LogInfo("contacts", "- -" + contactsListView.getFirstVisiblePosition());
                    contactsListView.setSelection(position + 1);
                }
            }
        });
        contacts_search.setClickDrawableRightListener(new XEditText.DrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                CRMLog.LogInfo("doSearch", "do");
                contacts_search.clearFocus();
                CRMLog.LogInfo("doSearch", "do" + contacts_search.isFocused());
                contacts_search.setText("");
                isWhenSearch = false;
                contacts_search.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.search_bar_icon_normal), null, null, null);
                contactsAdapter.changeData(contacts);
            }
        });
        contacts_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchResult = doSearch(s.toString());
                CRMLog.LogInfo("position:", "S=" + s + "cc" + count);
                if (!"".equals(s.toString())) {
                    CommonUtils.setViewBackGroundAlpha(contacts_list_parent, 1.0f);
                    contacts_search.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.search_bar_icon_normal), null, ContextCompat.getDrawable(getActivity(), R.drawable.icon_delete_01), null);
                } else if ("".equals(s.toString()) && contacts_search.isFocused()) {
                    CRMLog.LogInfo("position:", "Sgggg");
                    CommonUtils.setViewBackGroundAlpha(contacts_list_parent, 0.7f);
                    contacts_search.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.search_bar_icon_normal), null, null, null);
                }
                contactsAdapter.changeData(searchResult);
                isWhenSearch = true;
                if (searchResult.size() == 0&&isRefreshOver) {
                    CRMLog.LogInfo("search", "no a");
                    contactsListView.setVisibility(View.GONE);
                    no_searched_txt.setVisibility(View.VISIBLE);
                } else {
                    contactsListView.setVisibility(View.VISIBLE);
                    no_searched_txt.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contacts_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    CRMLog.LogInfo("dd", "hh");
                    contacts_search.setGravity(Gravity.LEFT);
                    CommonUtils.setViewBackGroundAlpha(contacts_list_parent, 0.7f);
                    sidebar.setVisibility(View.INVISIBLE);
                    contacts_search.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(), R.drawable.search_bar_icon_normal), null, ContextCompat.getDrawable(getActivity(), R.drawable.icon_delete_01), null);
                } else {
                    CRMLog.LogInfo("dd", "sss");
                    sidebar.setVisibility(View.VISIBLE);
                    contacts_search.setGravity(Gravity.CENTER);
                    CommonUtils.setViewBackGroundAlpha(contacts_list_parent, 1.0f);
                    CommonUtils.hideInputManager(getActivity(), contacts_search);
                }
            }
        });

    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    @Override
    public void onResume() {
        CRMLog.LogInfo("ommm", "onResume");
        super.onResume();
        if(this.isVisible()) {
            TCAgent.onPageStart(getActivity(), PATDEnum.CONTACTS.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isVisible()) {
            TCAgent.onPageEnd(getActivity(), PATDEnum.CONTACTS.getName());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        contacts_search.clearFocus();
        if(isWhenSearch){
            doOnItemClick(position, searchResult);
        }else {
            doOnItemClick(position, contacts);
        }
    }
    VolleyInterface volleInterface = new VolleyInterface() {
        @Override
        public void onSuccess(Object obj) {
            CRMLog.LogInfo("getContacts", obj.toString());
            List<GroupMemberBean> tmpList = handleContactsJson(obj.toString());
            //contacts = handleContactsJson(getResources().getString(R.string.test));
            CRMLog.LogInfo("tmpList", tmpList.toString());
            if(!isRefreshOver){
                isRefreshOver = true;
            }
            contacts.clear();
            contacts = filledData(tmpList);
            Collections.sort(contacts, pinyinComparator);
            contactsAdapter.changeData(contacts);
            contactsListView.stopRefresh();
        }
        @Override
        public void onError(VolleyError ve) {
            CRMLog.LogInfo("getContacts", "sfsfffd" + ve.getMessage());
            contactsListView.stopRefresh();
        }

        @Override
        public void onLogOutside() {
            CommonUtils.exitWhenHasLogIn(getActivity());
        }
    };
    /**
     * 为ListView填充数据
     *
     * @param datas
     * @return
     */
    private List<GroupMemberBean> filledData(List<GroupMemberBean> datas) {
        List<GroupMemberBean> mSortList = new ArrayList<>();

//        for (int i = 0; i < data.size(); i++) {
//            GroupMemberBean sortModel = new GroupMemberBean();
//            sortModel.setName(data.get(i).getName());
//            //
        CRMLog.LogInfo("fddfdgf", datas.toString());
        if(datas.size()>0) {
            for (GroupMemberBean data : datas) {
                String pinyin = characterParser.getSelling(data.getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    data.setSortLetters(sortString.toUpperCase());
                } else {
                    data.setSortLetters("#");
                }
                mSortList.add(data);
//        }
            }
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = contacts;
//            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (GroupMemberBean sortModel : contacts) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        contactsAdapter.changeData(filterDateList);
        if (filterDateList.size() == 0) {
//            tvNofriends.setVisibility(View.VISIBLE);
            CRMLog.LogInfo("contacts", "filter");
        }
    }

    @Override
    public void handleAdapterCallBack(View view,int position) {
        CRMLog.LogInfo("contacts", "" + view.getId() + "," + position);
        GroupMemberBean contact = new GroupMemberBean();
        if(isWhenSearch){
            contact = searchResult.get(position);
        }else{
            contact = contacts.get(position);
        }
        ConstantsBean contactsBean = new ConstantsBean();
        contactsBean.customerId = contact.getClientId();
        contactsBean.customerNo = contact.getCustomerNo();
        contactsBean.name = contact.getName();
        contactsBean.portrait = contact.getClientLogo();
        switch (view.getId()){
            case R.id.channel_weixin:
                contactsBean.source = SOURCE_WEIXIN;
                contactsBean.dialogueStatus = contact.getFlag().weixin;
                CommonUtils.handleChannel(getContext(),SOURCE_WEIXIN);
                break;
            case R.id.channel_sms:
                contactsBean.source = SOURCE_SMS;
                CommonUtils.handleChannel(getContext(),SOURCE_SMS);
                contactsBean.dialogueStatus = "";
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("constantsBean",contactsBean);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public List<GroupMemberBean> doSearch(String s){
        List<GroupMemberBean> searchList = new ArrayList<>();
        String pinyin = characterParser.getSelling(s);
        CRMLog.LogInfo("pinyin", pinyin);
        for(int i=0;i<contacts.size();i++){
            String name = characterParser.getSelling(contacts.get(i).getName());
            if(pinyin!=null&&name.contains(pinyin)){
                searchList.add(contacts.get(i));
            }
        }
        return searchList;
    }

    private void doOnItemClick(int position,List<GroupMemberBean> itemList){
        //先把之前所有的点开的item还原。
        for (GroupMemberBean gmb:itemList){
            if(gmb.isShowChannelsBar()){
                gmb.setIsShowChannelsBar(false);
            }
        }
        if(position>0&&position<=itemList.size()){
            if(tmpPosition!=-1&&tmpPosition==position) {
                isFirstClick=!isFirstClick;
            }else{
                isFirstClick =true;
            }
            tmpPosition = position;
            contactsAdapter.getItem(position-1).setIsShowChannelsBar(isFirstClick);
        }
        contactsAdapter.changeData(itemList);
        if(position==itemList.size()){
            contactsListView.setSelection(itemList.size()-1);
        }
    }

    public List<GroupMemberBean> handleContactsJson(String json){
        CRMLog.LogInfo("data", "json=" + json);
        List<GroupMemberBean> contacts =new ArrayList<>();
        try{
            JSONObject contactsObj = new JSONObject(json);
            JSONArray contactArray =new JSONArray();
            String resultCode = contactsObj.getString("resultCode");
            String dataArray = "";
            if("200".equals(resultCode)){
                dataArray=contactsObj.getString("data");
                if(!"".equals(dataArray)) {
                    contactArray = new JSONArray(dataArray);
                }
                CRMLog.LogInfo("data", contactArray.toString());
            }
            for(int i=0;i<contactArray.length();i++) {
                GroupMemberBean coniitact = CommonUtils.handleHttpResult(GroupMemberBean.class, contactArray.get(i).toString());
                CRMLog.LogInfo("coniitant", coniitact.getFlag().weixin);
                contacts.add(coniitact);
            }
        }catch (JSONException e){
            CRMLog.LogInfo("eeerrorr", e.getMessage());
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void onRefresh() {
        CRMLog.LogInfo("contacts", "onRefresh");
        VolleyRequest.httpGetContacts(getActivity(),HttpUrls.HTTP_GETCONTACTS, umId, shiroKey, volleInterface);
    }

    @Override
    public void onLoadMore() {

    }
}
