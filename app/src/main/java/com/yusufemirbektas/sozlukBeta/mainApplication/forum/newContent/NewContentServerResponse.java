package com.yusufemirbektas.sozlukBeta.mainApplication.forum.newContent;

import com.google.gson.annotations.SerializedName;

public class NewContentServerResponse {
    @SerializedName("result")
    private int result;
    @SerializedName("comment")
    private String comment;
    @SerializedName("time")
    private String time;

    public int getResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }
}
