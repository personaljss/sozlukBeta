package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses;

import com.google.gson.annotations.SerializedName;

public class FollowResponse {
    @SerializedName("result")
    public int result;
    @SerializedName("comment")
    public String comment;
    @SerializedName("time")
    public String time;
}
