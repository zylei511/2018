package com.example.zylei_library.uihelper;

import java.io.Serializable;

/**
 * Created by hanyh on 16/2/18.
 */
public class ChatAddBean implements Serializable{

    private int iconRes;
    private String iconName;
    private String clickImageUrl;

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getClickImageUrl() {
        return clickImageUrl;
    }

    public void setClickImageUrl(String clickImageUrl) {
        this.clickImageUrl = clickImageUrl;
    }
}
