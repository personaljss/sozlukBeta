package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

public class ProfileItem {
    @SerializedName("nickname")
    private String nickName;

    @SerializedName("likestatu")
    private int likeStatus;

    @SerializedName("usercode")
    private String userCode;

    public String getNickName() {
        return nickName;
    }

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public String getUserCode() {
        return userCode;
    }
}
