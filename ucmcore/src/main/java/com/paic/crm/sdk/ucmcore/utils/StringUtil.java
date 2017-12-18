package com.paic.crm.sdk.ucmcore.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理 json的util
 *
 * @author yueshaojun
 */
public class StringUtil {
    private static final String TAG = "StringUtil";
    /**
     * 处理h5的统一入口
     */
    public static List<JSONObject> getH5Json(String json) {
        try {
            JSONObject jsonObject = handleH5Json(json);
            String type = jsonObject.getString("type");

            List<JSONObject> jsonObjectList = new ArrayList<>();
            switch (type) {
                case "sms":
                    jsonObject = handleSMSJson(jsonObject);
                    jsonObjectList.add(jsonObject);
                    break;
                case "pic":
                    jsonObjectList = handlePicJson(jsonObject);
                    break;
                case "template":
                    jsonObject = handleTemplateJson(jsonObject);
                    jsonObjectList.add(jsonObject);
                    break;
                case "news":
                    //TODO
//					JSONObject newObject = new JSONObject(json);
//					jsonObject = handleNewsJson(newObject);
//					jsonObjectList.add(jsonObject);
                    break;
                case "text":
                    jsonObjectList.add(jsonObject);
                    break;
            }
            return jsonObjectList;
        } catch (JSONException e) {
            Log.e("jsonError", e.getMessage());
        }
        return null;
    }

    /**
     * 处理h5json的一层过滤
     */
    public static JSONObject handleH5Json(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String type = (String) jsonObject.get("type");
            JSONObject content = jsonObject.getJSONObject("typeContent");
            JSONObject resultJson = new JSONObject();
            if (type.equals("news")) {
                if (content.has("data")) {
                    resultJson.put("news", content.getString("data"));
                    resultJson.put("type", type);
                }
            } else {

                if (content.has("msg")) {
                    resultJson.put("msg", content.getString("msg"));
                } else {
                    resultJson.put("msg", content.getString("msgList"));
                }
                if (content.has("templateInput")) {
                    resultJson.put("templateInput", content.getString("templateInput"));
                }
                if (content.has("param")) {
                    resultJson.put("param", content.getString("param"));
                }
                resultJson.put("type", type);
            }

            return resultJson;

        } catch (JSONException e) {

            CRMLog.LogInfo(TAG, "StringUtil" + e.getMessage());
        }
        return null;

    }

    /**
     * 获取字符串数组
     */
    public static String[] getStringItems(String str) {
        str = str.replaceAll("\\[|\\]", "");
        String[] arrayString = str.split(",");
        Log.i("ddd", arrayString[0]);
        return arrayString;

    }

    /**
     * 处理短信json
     */
    public static JSONObject handleSMSJson(JSONObject jsonObject) throws JSONException {
        String templateInput = jsonObject.getString("templateInput");
        //拿到输入参数
        String[] inputParam = getStringItems(templateInput);
        Log.i("param", inputParam[0]);
        String msg = jsonObject.getString("msg");
        JSONObject msgObject = new JSONObject(msg);
        String content = msgObject.getString("content");
        Log.i("content", content);
        for (int i = 0; i < inputParam.length; i++) {
            content = content.replaceFirst("\\{[^}]*\\}\\}", inputParam[i]);
            Log.i("cccc", content);
        }
        JSONObject smsJson = new JSONObject();
        smsJson.put("type", "sms");
        smsJson.put("msg", content);
        smsJson.put("from", "sms");
        Log.i("sms", smsJson.toString());
        return smsJson;
    }

    /**
     * 处理模版json
     */
    public static JSONObject handleTemplateJson(JSONObject jsonObject) throws JSONException {
        String templateInput = jsonObject.getString("templateInput");
        //拿到输入参数
        String[] inputParam = getStringItems(templateInput);
        Log.i("template", inputParam[0]);
        String content = jsonObject.getString("msg");
        Log.i("template_content", content);
        for (int i = 0; i < inputParam.length; i++) {
            content = content.replaceFirst("\\{[^}]*\\}\\}", inputParam[i]);
            Log.i("cccc", content);
        }
        JSONObject templateJson = new JSONObject();
        templateJson.put("type", "template");
        templateJson.put("msg", content);
        templateJson.put("from", "template");
        Log.i("templateJson", templateJson.toString());
        return templateJson;
    }

    /**
     * 处理图片json
     */
    public static List<JSONObject> handlePicJson(JSONObject jsonObject) throws JSONException {
        String msg = jsonObject.getString("msg");
        String[] mediaIds = getStringItems(msg);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (int i = 0; i < mediaIds.length; i++) {
            JSONObject picJsonObject = new JSONObject();
            picJsonObject.put("type", "pic");
            picJsonObject.put("msg", mediaIds[i]);
            picJsonObject.put("from", "pic");
            jsonObjectList.add(picJsonObject);
        }
        Log.i("pic", jsonObjectList.toString());
        return jsonObjectList;
    }

    /**
     * 处理资讯json
     */
    public static List<JSONObject> handleNewsJson(String jsonString) throws JSONException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject contentObject = jsonObject.getJSONObject("content");
        JSONObject typeContentObject = contentObject.getJSONObject("typeContent");
        String customerId = typeContentObject.getString("contactId");
        String msg = typeContentObject.getString("data");
        msg = msg.replaceAll("\\[|\\]", "");
        Log.i("news", msg);
        JSONObject newsObject = new JSONObject();
        newsObject.put("type", "news");
        newsObject.put("msg", msg);
        newsObject.put("customerId", customerId);
        newsObject.put("from", "news");
        jsonObjectList.add(newsObject);
        return jsonObjectList;
    }

    public static boolean isBlank(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

}
