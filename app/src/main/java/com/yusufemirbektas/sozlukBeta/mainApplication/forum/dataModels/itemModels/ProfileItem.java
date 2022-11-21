package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

public class ProfileItem {
    @SerializedName("nickname")
    private String nickName;

    @SerializedName("likestatu")
    private int likeStatus;

    @SerializedName("usercode")
    private String userCode;

    private String photo;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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
