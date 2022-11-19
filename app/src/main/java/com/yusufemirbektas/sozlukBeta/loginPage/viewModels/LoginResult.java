package com.yusufemirbektas.sozlukBeta.loginPage.viewModels;

import com.google.gson.annotations.SerializedName;
//0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata


public class LoginResult {
    @SerializedName("result")
    private int result;
    @SerializedName("comment")
    private String comment;
    @SerializedName("time")
    private String time;
    @SerializedName("usercode")
    private String userCode;
    @SerializedName("nickname")
    private String nickName;

    public LoginResult(Integer result,  String userCode) {
        this.result = result;

        this.userCode = userCode;
    }

    public Integer getResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public String getUserCode() {
        return userCode;
    }


    public String getNickName() {
        return nickName;
    }
}
