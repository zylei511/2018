package com.paic.crm.Enums;

/**
 * Created by yueshaojun on 16/5/19.
 * 使用PATD要监控的页面名
 */
public enum PATDEnum {

    CONVERSATION ("会话"),CHAT("聊天"),
    HIS_CON("历史会话"),HIS_CHAT("历史消息"),
    CONTACTS("联系人"),BRENCH("工作台"),NEWS("微讯"),
    ME("我"),HOME("HOME页"),

    PHONE_STATUS("签入签出"),PHONE_MODIFY("修改手机号"),IB_TASK("IB任务"),

    SHARE_BTN("分享按钮点击"),SHARE_WEIXIN("分享到微信好友"),SHARE_FRIEND_CIRCLE("分享到微信朋友圈");
    private String name;

    PATDEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
