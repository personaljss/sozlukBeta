package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses;

import com.google.gson.annotations.SerializedName;

public class UploadPpResponse {
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
