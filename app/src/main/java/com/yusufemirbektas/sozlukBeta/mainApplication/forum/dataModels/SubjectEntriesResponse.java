package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels;

import com.google.gson.annotations.SerializedName;

public class SubjectEntriesResponse {
    @SerializedName("result")
    private int result;
    @SerializedName("data")
    String data;
    @SerializedName("time")
    String time;

    public int getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }
}
