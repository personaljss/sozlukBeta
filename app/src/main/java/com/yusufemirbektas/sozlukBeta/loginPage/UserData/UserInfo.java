package com.yusufemirbektas.sozlukBeta.loginPage.UserData;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Parcelable{
    private Integer userCode;
    private String deviceToken="";

    //constructor
    public UserInfo(Integer userCode) {
        this.userCode = userCode;
    }

    protected UserInfo(Parcel in) {
        if (in.readByte() == 0) {
            userCode = null;
        } else {
            userCode = in.readInt();
        }
        deviceToken = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public Integer getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (userCode == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(userCode);
        }
        parcel.writeString(deviceToken);
    }
}
