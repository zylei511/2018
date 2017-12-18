package com.paic.crm.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理 json的util
 * @author yueshaojun
 */
public class JSONStringUtil {
	/**
	 * 这个变量用于存储重复的日期
	 * */
	public static String[] tempDateList;
	/**
	 *这个是对于h5的通用解析
	 * */
	public static List<JSONObject> getH5Json(String json){
		try {
			String type="";
			JSONObject jsonObject = new JSONObject();
			JSONObject h5Obj = new JSONObject(json);
			if(h5Obj.has("to")){
				type="news";
			}else {
				jsonObject = handleH5Json(json);
				type = jsonObject.getString("type");
			}

			List<JSONObject> jsonObjectList =new ArrayList<>();
			switch (type){
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
					jsonObjectList = handleNewsJson(json);
//					jsonObjectList.add(jsonObject);
					break;
				case "text":
					jsonObject.put("type","txt");
					jsonObjectList.add(jsonObject);
					break;
			}
			CRMLog.LogInfo(Constants.LOG_TAG, jsonObjectList.toString());
			return  jsonObjectList;
		}catch (JSONException e){
			CRMLog.LogError("jsonError", e.getMessage());
		}
		return null;
	}
	public static JSONObject handleH5Json(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			String type = (String)jsonObject.get("type");
			JSONObject content = jsonObject.getJSONObject("typeContent");
			JSONObject resultJson = new JSONObject();
			if(content.has("msg")){
				resultJson.put("msg",content.getString("msg"));
			}else{
				resultJson.put("msg", content.getString("msgList"));
			}
			if(content.has("templateInput")){
				resultJson.put("templateInput",content.getString("templateInput"));
			}
			if(content.has("param")){
				resultJson.put("param",content.getString("param"));
			}
			resultJson.put("type", type);

			return  resultJson;

		}catch (JSONException e){

			CRMLog.LogInfo(Constants.LOG_TAG, e.getMessage());
		}
		return null;

	}
	public static String[] getStringItems(String str){
		str = str.replaceAll("\\[|\\]", "");
		String[] arrayString = str.split(",");
		CRMLog.LogInfo("ddd", arrayString[0]);
		return arrayString;

	}
	public static JSONObject handleSMSJson(JSONObject jsonObject) throws JSONException{
		String templateInput = jsonObject.getString("templateInput");
		//拿到输入参数
		String[] inputParam = getStringItems(templateInput);
		CRMLog.LogInfo("input_param", inputParam[0]);
		String msg = jsonObject.getString("msg");
		JSONObject msgObject = new JSONObject(msg);
		String content = msgObject.getString("content");
		CRMLog.LogInfo("content", content);
		for(int i=0;i<inputParam.length;i++){
			content = content.replaceFirst("\\{[^}]*\\}\\}", inputParam[i]);
			CRMLog.LogInfo("cccc", content);
		}
		JSONObject smsJson = new JSONObject();
		smsJson.put("type","sms");
		smsJson.put("msg",content);
		smsJson.put("from","sms");
		CRMLog.LogInfo("sms", smsJson.toString());
		return smsJson;
	}
	public static JSONObject handleTemplateJson(JSONObject jsonObject)throws JSONException{
		String templateInput = jsonObject.getString("templateInput");
		//拿到输入参数
		String param = jsonObject.getString("param");
		JSONObject paramObj = new JSONObject(param);
		JSONArray paramArray = paramObj.names();
		String[] inputParam = getStringItems(templateInput);
		for(int i = 0;i<inputParam.length;i++){
			paramObj.put(paramArray.getString(i),inputParam[i]);
		}
		CRMLog.LogInfo("template111", paramObj.toString());
		String content = jsonObject.getString("msg");
		CRMLog.LogInfo("template_content", content);
		for(int i=0;i<paramArray.length();i++){
			content = content.replaceFirst("\\{[^}]*\\}\\}", paramObj.getString(paramArray.getString(i)));
			CRMLog.LogInfo("cccc", content);
		}
		JSONObject templateJson = new JSONObject();
		templateJson.put("type","template");
		templateJson.put("msg",content);
		templateJson.put("from","template");
		CRMLog.LogInfo("templateJson", templateJson.toString());
		return templateJson;
	}
	public static List<JSONObject> handlePicJson(JSONObject jsonObject)throws JSONException{
		String msg = jsonObject.getString("msg");
		String[] mediaIds = getStringItems(msg);
		List<JSONObject> jsonObjectList = new ArrayList<>();
		for(int i = 0 ;i<mediaIds.length;i++){
			JSONObject picJsonObject = new JSONObject();
			picJsonObject.put("type","image");
			picJsonObject.put("msg",mediaIds[i]);
			picJsonObject.put("from","pic");
			jsonObjectList.add(picJsonObject);
		}
		CRMLog.LogInfo("pic", jsonObjectList.toString());
		return jsonObjectList;
	}
    /**
     * 解析资讯
     * */
	public static List<JSONObject> handleNewsJson(String jsonString)throws JSONException{
		List<JSONObject> jsonObjectList = new ArrayList<>();
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONObject contentObject = jsonObject.getJSONObject("content");
		JSONObject typeContentObject = contentObject.getJSONObject("typeContent");
		String customerId = typeContentObject.getString("contactId");
		String msg = typeContentObject.getString("data");
		msg  = msg.replaceAll("\\[|\\]","");
        JSONObject msgObj = new JSONObject(msg);
        msg=msgObj.getString("description");
		CRMLog.LogInfo("news", msg);
		JSONObject newsObject = new JSONObject();
		newsObject.put("type","news");
		newsObject.put("msg",msg);
		newsObject.put("customerId",customerId);
		newsObject.put("from","news");
		jsonObjectList.add(newsObject);
		return  jsonObjectList;
	}
    /**
     * 会话历史的解析
	 * @param  jsonString
	 * @return
     * */
	 public static List<JSONObject> conversationJsonAnalyse(String jsonString) throws JSONException{
		 CRMLog.LogInfo("conversation", jsonString);
		 boolean isSameDate= false;
		 List<JSONObject> jsonObjectList = new ArrayList<>();
		 JSONObject conversationObj = new JSONObject(jsonString);
		 String resultCode = conversationObj.getString("resultCode");
		 String resultMsg = conversationObj.getString("resultMsg");
		 if(!"02".equals(resultCode)){
			CRMLog.LogInfo("conversation",resultMsg);
			return  null;
		 }
		 String data = conversationObj.getString("data");
		 JSONObject dataObj = new JSONObject(data);
		 String content = dataObj.getString("content");
		 JSONObject contentObj = new JSONObject(content);
		 String dataListStr = dataObj.getString("dataListStr");
		 String[] dataList = dataListStr.split(",");
		 if(null==tempDateList){
			tempDateList = dataList;
		 }else {
			 CRMLog.LogInfo("tempfsd", tempDateList[tempDateList.length - 1] + "lll" + dataList[0]);
			 if (tempDateList[tempDateList.length - 1].equals(dataList[0])) {
				 isSameDate = true;
				 tempDateList = null;
			 }
		 }
		 for(int i=0;i<dataList.length;i++){
			 String dataKey = dataList[i];
			 CRMLog.LogInfo("dataKey", dataKey);
			 String contentList = contentObj.getString(dataKey);
			 JSONArray contentArray = new JSONArray(contentList);
             CRMLog.LogInfo("hhssdf", contentArray.toString() + "|length=" + contentArray.length());

             for(int j=0;j<contentArray.length();j++) {
                 if(j==0&&!isSameDate) {
                     contentArray.getJSONObject(j).put("historyTime", dataKey);
					 isSameDate=false;
                 }else {
                     contentArray.getJSONObject(j).put("historyTime", "");
                 }
                 contentArray.getJSONObject(j).put("unReadCount",0);
             }
			 for(int j=0;j<contentArray.length();j++){
				 jsonObjectList.add(contentArray.getJSONObject(j));
			 }
			 CRMLog.LogInfo("conversationJsonAnalyse", jsonObjectList.toString());
		 }


		 return jsonObjectList;
	 }

	/**
	 * 判断是否被抢登
	 * @param obj
	 * @return
	 * */
	public static boolean checkIsLogOutside(Object obj){
		try {
			JSONObject checkObj = new JSONObject(obj.toString());
			String resultCode = checkObj.getString("resultCode");
			if("1001".equals(resultCode)){
				return true;
			}
		}catch(JSONException e){
			return false;
		}
		return false;
	}
}
