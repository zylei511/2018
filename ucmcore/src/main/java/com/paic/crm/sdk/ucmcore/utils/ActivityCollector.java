package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueshaojun on 16/5/11.
 */
public class ActivityCollector {
    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        CRMLog.LogInfo("ActivityCollector","add:"+activity.getClass().getSimpleName());
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
    public static Activity getTopActivity(){
        return activities.get(activities.size()-1);
    }
    public static void finishEscapeActivity(String escapeActivityName) {
        if(TextUtils.isEmpty(escapeActivityName)){
            return;
        }
        for (Activity activity : activities) {
            String activityName = activity.getClass().getSimpleName();
            if (!activity.isFinishing()&&!activityName.equals(escapeActivityName)) {
                activity.finish();
            }
        }
    }

    public static List<Activity> getActivities(){
        return activities;
    }
}
