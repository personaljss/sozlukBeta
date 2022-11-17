package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses;

import com.google.gson.annotations.SerializedName;

public class SubjectEntriesResponse {
    @SerializedName("result")
    private int result;
    @SerializedName("data")
    String data;
    @SerializedName("time")
    String time;
    @SerializedName("comments")
    int totalEntries;

    public int getResult() {
        return result;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    public int getTotalEntries() {
        return totalEntries;
    }
}
