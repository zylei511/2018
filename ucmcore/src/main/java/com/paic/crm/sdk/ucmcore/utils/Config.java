package com.paic.crm.sdk.ucmcore.utils;

import com.pingan.core.im.ConfigEnum;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yueshaojun on 2017/4/13.
 */
public class Config {
    private static String configSuffix ;
    private static String TAG = "Config" ;
    private static final Map<String,String> configMap = new HashMap<>();
    public static void setConfig(Class o ,ConfigEnum configEnum){
        if(configEnum==ConfigEnum.STG2){
            configSuffix = "STG";
        }else {
            configSuffix = "PRD";
        }
        getMem(o);
    }
    public static String getConfig(String key){
        return configMap.get(key+configSuffix);
    }

    private static void getMem(Class o){
        Field[] f = o.getDeclaredFields();
        for(int i = 0 ;i<f.length;i++){
            try {
                String name = f[i].getName();
                CRMLog.LogInfo(TAG,"Config"+ "name = = " + name);
                if(name.contains(configSuffix)) {

                    String value = (String) f[i].get(name);
                    CRMLog.LogInfo(TAG,"Config"+"value = = " + value);
                    configMap.put(name,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
