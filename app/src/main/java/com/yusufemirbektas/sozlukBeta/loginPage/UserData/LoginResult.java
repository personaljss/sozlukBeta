package com.yusufemirbektas.sozlukBeta.loginPage.UserData;

import com.google.gson.annotations.SerializedName;
//0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata


public class LoginResult {
    @SerializedName("result")
    private Integer result;
    @SerializedName("comment")
    private String comment;
    @SerializedName("time")
    private String time;
    @SerializedName("usercode")
    private Integer userCode;


    public Integer getResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public Integer getUserCode(){
        return userCode;
    }


}
