package com.paic.crm.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueshaojun on 16/5/11.
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public static void finishAllActivity(){
        for(Activity activity :activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
    public static String getActivityName(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("activityTask:\n");
        for(Activity activity : activities){
            stringBuffer.append(activity.getClass().getName());
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
