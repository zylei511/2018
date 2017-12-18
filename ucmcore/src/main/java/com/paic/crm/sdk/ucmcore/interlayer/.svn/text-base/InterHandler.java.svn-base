package com.paic.crm.sdk.ucmcore.interlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yueshaojun on 2017/7/18.
 */

public class InterHandler {
	private static String protocol;
	private static HashMap<String ,Class<? extends Activity>> viewMap = new LinkedHashMap<>();
	private static HashMap<String ,IInterLayer> methodMap = new LinkedHashMap<>();
	private static HashMap<String ,String> methodNameMap = new LinkedHashMap<>();
	public InterHandler(){
	}

	/**
	 * 处理来自h5的url
	 * @param url
	 * @param context
	 * @return
	 */
	public static boolean handleMethodFromJs(String url,Context context){
		if(!checkLocal(url)){
			return false;
		}
		String firstOrder = getFirstOrder(url);
		String secondOrder = getSecondOrder(url);
		HashMap params = getParams(url);
		if(checkView(firstOrder)){
			Class dialog = viewMap.get(secondOrder);
			if(dialog==null){
				return false;
			}
			jumpToView(context,dialog,params);
		}
		if(checkBlock(firstOrder)){
//			invoke(secondOrder,context,params);
			return invokeByName(secondOrder,context,params);
		}
		return true;
	}

	/**
	 * 添加跳转页面
	 * @param key 关键字
	 * @param view 跳转的指定页面，是activity 的子类
	 */
	public static void addView(String key ,Class<? extends Activity> view){
		viewMap.put(key,view);
	}

	/**
	 * 添加动作
	 * @param key 关键字
	 * @param action 动作，是一个IInterLayer接口的实例
	 */
	public static void addAction(String key,IInterLayer action){
		methodMap.put(key,action);
	}

	public static void addActionName(String key ,String className){
		methodNameMap.put(key,className);
	}
	/**
	 * 加载js方法
	 * @param jsMethod js方法名
	 * @param params 参数
	 */
	public static String loadJavascriptMethod(String jsMethod,String params){
		String jsMethodStr = "javascript:"+jsMethod+"("+params+")";
		return jsMethodStr;
	}

	private void invoke(String secondOrder,Context context ,HashMap params) {
		methodMap.get(secondOrder).execute(context,params);
	}
	private static boolean invokeByName(String secondOrder ,Context context,HashMap params){
		String className = methodNameMap.get(secondOrder);
		try {
			Class c = Class.forName(className);
			Method[] m = c.getDeclaredMethods();
			for(int i =0 ;i<m.length;i++) {
				if(m[i].getName().equals("execute"))
					try {
						m[i].invoke(c.newInstance(),context,params);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return false;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						return false;
					} catch (InstantiationException e) {
						e.printStackTrace();
						return false;
					}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return true;
	}
	private boolean invokeReflect(String methodName,Context context ,HashMap params){
		Method[] m = IInterLayer.class.getDeclaredMethods();
		for(int i = 0;i< m.length;i++){
			if(m[i].getName().equals(methodName)){
				try {
					m[i].invoke(methodMap.get(methodName),context,params);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					return false;
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	private static boolean checkBlock(String firstOrder) {
		if("block".equals(firstOrder)){
			return true;
		}
		return false;
	}

	private static boolean checkView(String firstOrder) {
		if("view".equals(firstOrder)){
			return true;
		}
		return false;
	}

	private static String getFirstOrder(String s){
		return getOrder(s).split("/")[0];
	}
	private static String getSecondOrder(String s) {
		return getOrder(s).split("/")[1];
	}
	private static String getOrder(String s){
		String urlEscapeProtocol = s.replace(protocol+"://","");
		return urlEscapeProtocol.split("\\?")[0];
	}

	private static HashMap<String,Object> getParams(String s){
		String[] paramStr = s.split("\\?");
		HashMap<String,String> param = new HashMap<>();
		if(paramStr.length>1) {
			String[] params = paramStr[1].split("&");
			for (int i = 0; i < params.length; i++) {
				param.put(params[i].split("=")[0],params[i].split("=")[1]);
			}
		}
		return parseParams(param);
	}

	private static HashMap<String,Object> parseParams(HashMap<String,String> params) {
		if(params == null){
			return null;
		}
		HashMap<String,Object> pObj = new HashMap<>();
		for(String paramKey : params.keySet()){
			String param = params.get(paramKey);
			//int型参数
			if(isNumeric(param)){
				pObj.put(paramKey,Integer.parseInt(param));
			}
			//boolean型参数
			else if("true".equals(param)){
				pObj.put(paramKey,true);
			}
			else if("false".equals(param)){
				pObj.put(paramKey,false);
			}
			//字符串
			else {
				pObj.put(paramKey,param);
			}

		}


		return pObj;
	}

	private static boolean checkLocal(String m) {
		protocol = m.substring(0,m.indexOf("://"));
		Log.i("InterHandler::","InterHandler=="+protocol);
		if("local".equals(protocol)){
			return true ;
		}
		return false;
	}

	private static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]{1}");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	private static void jumpToView(Context context, Class<? extends Activity> targetActivity, HashMap<String,Object> param) {
		Intent intent = new Intent(context, targetActivity);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle paramBundle = new Bundle();
		paramBundle.putSerializable("FROM_VUE", param);
		intent.putExtras(paramBundle);
		context.startActivity(intent);
	}

}
