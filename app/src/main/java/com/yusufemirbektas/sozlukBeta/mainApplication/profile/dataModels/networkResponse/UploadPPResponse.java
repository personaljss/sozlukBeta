package com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse;

import com.google.gson.annotations.SerializedName;

public class UploadPPResponse {
    @SerializedName("time")
    private String time;
    @SerializedName("comment")
    private String comment;
    @SerializedName("result")
    private int result;

    public String getTime() {
        return time;
    }

    public String getComment() {
        return comment;
    }

    public int getResult() {
        return result;
    }
}
