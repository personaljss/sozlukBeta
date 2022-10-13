package com.yusufemirbektas.sozlukBeta.data;


public class UserData {
    private static int USER_CODE=-1;
    private static String DEVICE_TOKEN="";

    public static int getUserCode() {
        return USER_CODE;
    }

    public static void setUserCode(int userCode) {
        USER_CODE = userCode;
    }

    public static String getDeviceToken() {
        return DEVICE_TOKEN;
    }

    public static void setDeviceToken(String deviceToken) {
        DEVICE_TOKEN = deviceToken;
    }
}
