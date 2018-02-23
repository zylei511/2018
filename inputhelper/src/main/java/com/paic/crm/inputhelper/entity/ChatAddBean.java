package com.paic.crm.inputhelper.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hanyh on 16/2/18.
 */
public class ChatAddBean implements Parcelable {

    private int iconRes;
    private String iconName;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.iconRes);
        dest.writeString(this.iconName);
    }

    public ChatAddBean() {
    }

    protected ChatAddBean(Parcel in) {
        this.iconRes = in.readInt();
        this.iconName = in.readString();
    }

    public static final Parcelable.Creator<ChatAddBean> CREATOR = new Parcelable.Creator<ChatAddBean>() {
        @Override
        public ChatAddBean createFromParcel(Parcel source) {
            return new ChatAddBean(source);
        }

        @Override
        public ChatAddBean[] newArray(int size) {
            return new ChatAddBean[size];
        }
    };
}
