package com.example.zylei_library.uihelper;

/**
 * Created by ex-zhangyuelei001 on 2017/12/11.
 */

public class FaceHelper extends BaseHelper {
    private static FaceHelper baseHelper;

    public static FaceHelper newInstance() {
        if (baseHelper == null) {
            synchronized (FaceHelper.class) {
                if (baseHelper == null) {
                    baseHelper = new FaceHelper();
                }
            }
        }
        return baseHelper;
    }
}
