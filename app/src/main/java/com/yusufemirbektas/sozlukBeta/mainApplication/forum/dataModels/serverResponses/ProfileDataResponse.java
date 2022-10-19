package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses;

import com.google.gson.annotations.SerializedName;

public class ProfileDataResponse {
    @SerializedName("result")
    public int result;

    @SerializedName("comment")
    public String comment;

    @SerializedName("time")
    public String time;

    @SerializedName("header")
    public String header;

    @SerializedName("tests")
    public String tests;

    @SerializedName("entries")
    public String entries;

}
