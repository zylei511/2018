package com.paic.crm.entity;

import com.pingan.paimkit.module.conversation.bean.ChatConversation;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天的最后消息片段
 * 
 * @author hanyh
 *
 */
public class LastSnippet {
	 /**
     * 聊天内容类型，普通文本
     */
    public static final String CONTENT_TYPE_TEXT = "0";

    /**
     * 聊天内容类型，图片
     */
    public static final String CONTENT_TYPE_IMAGE = "1";

    /**
     * 聊天内容类型，音频
     */
    public static final String CONTENT_TYPE_AUDIO = "2";

    /**
     * 聊天内容类型，视频
     */
    public static final String CONTENT_TYPE_VIDEO = "3";

    /**
     * 聊天内容类型，地图
     */
    public static final String CONTENT_TYPE_LOCATION = "4";

    /**
     * 聊天内容类型，名片
     */
    public static final String CONTENT_TYPE_VCARD = "5";

    /**
     * 聊天内容类型，贴图
     */
    public static final String CONTENT_TYPE_TIETU = "6";

    /**
     * 聊天内容类型，链接消息
     */
    public static final String CONTENT_TYPE_LINK = "7";

    /**
     * 聊天内容类型，菜单事件消息
     */
    public static final String CONTENT_TYPE_MENU_EVENT = "8";

    /**
     * 聊天内容类型，文本超链接消息
     */
    public static final String CONTENT_TYPE_HYPERLINK = "9";

    /**
     * 聊天内容类型，Webview消息
     */
    public static final String CONTENT_TYPE_WEBVIEW = "10";

    /**
     * 聊天内容类型 ,gif消息
     */
    public static final String CONTENT_TYPE_GIF = "11";

    /**
     * 聊天内容类型，模板消息
     */
    public static final String CONTENT_TYPE_TEMPLATE = "12";

    /**
     * 聊天内容类型，单链接消息
     */
    public static final String CONTENT_TYPE_SLINK = "13";

    /**
     * 聊天内容类型，恋爱通关邀请消息
     */
    public static final String CONTENT_TYPE_LOVE_GAME = "14";

    /**
     * 聊天内容类型，图文链接消息
     */
    public static final String CONTENT_TYPE_FORWARDSLINK = "14.5";

    /**
     * 聊天内容类型，寿险出单请消息
     */
    public static final String CONTENT_TYPE_SINGLE = "15";

    /**
     * 聊天内容类型，寿险出单祝福消息
     */
    public static final String CONTENT_TYPE_CONGRATULATIONS = "16";

    /**
     * 聊天内容类型，寿险生日，入司
     */
    public static final String CONTENT_TYPE_WELCOME_BIRTHDAY = "17";

    private static final Map<String, String> lastMessageMaps = new HashMap<String, String>();

    static {
        lastMessageMaps.put(CONTENT_TYPE_IMAGE, "[图片]");
        lastMessageMaps.put(CONTENT_TYPE_AUDIO, "[语音]");
        lastMessageMaps.put(CONTENT_TYPE_VIDEO, "[视频]");
        lastMessageMaps.put(CONTENT_TYPE_LOCATION, "[位置]");
        lastMessageMaps.put(CONTENT_TYPE_VCARD, "[名片]");
        lastMessageMaps.put(CONTENT_TYPE_TIETU, "[图片]");
        lastMessageMaps.put(CONTENT_TYPE_GIF, "[图片]");
        lastMessageMaps.put(CONTENT_TYPE_WEBVIEW, "[链接]");
        lastMessageMaps.put(CONTENT_TYPE_LOVE_GAME, "[链接]");
        lastMessageMaps.put(CONTENT_TYPE_FORWARDSLINK, "[链接]");
    }

    /**
     * 获取消息摘要
     */
    public static String getSnippet(ChatConversation msg) {
      //  String type = msg.getContentType();
    	String type = CONTENT_TYPE_TEXT;
        // 如果是文本,文本超链接,生日祝福类消息[直接显示内容]
        if (CONTENT_TYPE_TEXT.equals(type) 
        		|| CONTENT_TYPE_HYPERLINK.equals(type)
                || CONTENT_TYPE_CONGRATULATIONS.equals(type)) {

            return msg.getmCmessage().getmLastMsgContent();
        } else if (CONTENT_TYPE_SINGLE.equals(type) 
        		|| CONTENT_TYPE_WELCOME_BIRTHDAY.equals(type)) {

          //  return CommonUtils.getLifeSingleContent(msg.getContent());
        	return "";
        }

        if (CONTENT_TYPE_FORWARDSLINK.equals(type)) {
            return"";
        	//return lastMessageMaps.get(msg.getContentType()) + CommonUtils.getLinkMesssageTitle(msg.getContent());
        }

        return lastMessageMaps.get(msg.getmCmessage().getmLastMsgContent());
    }

    public static Map<String, String> getLastMessageMaps() {
        return lastMessageMaps;
    }
}
