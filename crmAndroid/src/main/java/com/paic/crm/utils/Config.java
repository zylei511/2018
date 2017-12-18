//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.paic.crm.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.pingan.core.im.ConfigEnum;
import com.pingan.core.im.log.PALog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {
    protected static final String TAG = Config.class.getSimpleName();
    public static String configName = "config_ucm.xml";
    private static boolean IS_INIT = false;
    private static HashMap<String, String> configMap = new HashMap();
    private static HashMap<String, ArrayList<HashMap<String, String>>> listMap = new HashMap();
    private static String configTAG = null;

    public Config() {
    }

    @SuppressLint({"NewApi"})
    public static void init(Context context, ConfigEnum configEnum) {
        if(context == null) {
            PALog.e(TAG, "初始化config\tcontext为null无法初始化");
        } else if(IS_INIT) {
            PALog.w(TAG, "初始化config\t已经初始化完毕，无需再次初始化");
        } else {
            PALog.i(TAG, "初始化config\t开始");
            IS_INIT = true;
            String configTag = configEnum.toString().toLowerCase();
            boolean flagTag = false;
            boolean isConfigInTag = false;
            boolean isListTag = false;
            boolean isListItemTag = false;
            String currentListTag = "";
            ArrayList currentList = null;
            HashMap currentItem = null;
            XmlPullParser xml = null;

            try {
                xml = XmlPullParserFactory.newInstance().newPullParser();
                xml.setInput(context.getAssets().open(configName), "utf-8");
                if(xml == null || xml.next() == -1) {
                    PALog.e(TAG, "初始化config   没有在项目中找到" + configName + "文件");
                    return;
                }
            } catch (IOException var17) {
                var17.printStackTrace();
            } catch (XmlPullParserException var18) {
                var18.printStackTrace();
            }

            int eventType = -1;

            while(eventType != 1) {
                String e;
                if(eventType == 2) {
                    e = xml.getName();
                    String value;
                    if(e.equalsIgnoreCase("TAG")) {
                        isConfigInTag = true;
                        value = xml.getAttributeValue((String)null, "name");
                        if(value != null && configTag.equals(value.trim())) {
                            flagTag = true;
                        }
                    } else if(flagTag || !isConfigInTag) {
                        value = xml.getAttributeValue((String)null, "value");
                        String type = xml.getAttributeValue((String)null, "type");
                        if(type != null && "list".equalsIgnoreCase(type.trim())) {
                            isListTag = true;
                            currentListTag = e;
                            currentList = new ArrayList();
                            listMap.put(e, currentList);
                        }

                        if(isListTag) {
                            if(e.equalsIgnoreCase("ITEM")) {
                                isListItemTag = true;
                                currentItem = new HashMap();
                                currentList.add(currentItem);
                            } else if(isListItemTag && value != null && !"".equals(value.trim())) {
                                currentItem.put(e, value);
                            }
                        } else if(value != null && !"".equals(value.trim())) {
                            configMap.put(e, value);
                        }
                    }
                } else if(eventType == 3) {
                    e = xml.getName();
                    if(e.equalsIgnoreCase("TAG")) {
                        isConfigInTag = false;
                        flagTag = false;
                    } else if(e.equalsIgnoreCase(currentListTag)) {
                        isListTag = false;
                    } else if(isListTag && e.equalsIgnoreCase("ITEM")) {
                        isListItemTag = false;
                    }
                }

                try {
                    eventType = xml.next();
                } catch (XmlPullParserException var15) {
                    var15.printStackTrace();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

            PALog.i(TAG, "初始化config\t结束");
        }
    }

    public static HashMap<String, String> getConfigMap() {
        return configMap;
    }

    public static String getConfig(String key) {
        return (String)configMap.get(key);
    }

    public static ArrayList<HashMap<String, String>> getList(String key) {
        return (ArrayList)listMap.get(key);
    }

    public static String getConfigTAG() {
        return configTAG;
    }
}
