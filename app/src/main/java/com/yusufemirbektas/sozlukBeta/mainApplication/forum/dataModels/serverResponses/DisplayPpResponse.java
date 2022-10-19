package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses;

import com.google.gson.annotations.SerializedName;

public class DisplayPpResponse {
    @SerializedName("result")
    String imagesEncoded;
    @SerializedName("comment")
    String comment;
    @SerializedName("time")
    String time;

    public DisplayPpResponse(String imagesEncoded, String comment, String time) {
        this.imagesEncoded = imagesEncoded;
        this.comment = comment;
        this.time = time;
    }

    public String getImagesEncoded() {
        return imagesEncoded;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }
}
