package com.paic.crm.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yueshaojun
 */
public class StringUtil {
    public static boolean isBlank(String str){
        if("".equals(str)||null==str){
            return true ;
        }
        return false ;
    }
}
