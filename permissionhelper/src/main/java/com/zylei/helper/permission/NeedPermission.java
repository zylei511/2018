package com.zylei.helper.permission;

/**
 * Created by ex-zhangyuelei001 on 2018/1/26.
 */
public class NeedPermission {
    private static NeedPermission ourInstance = new NeedPermission();

    public static NeedPermission getInstance() {
        return ourInstance;
    }

    private NeedPermission() {
    }
}
