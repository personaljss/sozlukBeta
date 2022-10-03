package com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.dataModel;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.internal.SerializedIr;

public class SubjectEntryModel {
    @SerializedName("commentID")
    private int commentId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("dateTS")
    private String dateTime;

    @SerializedName("usercode")
    private String userCode;

    @SerializedName("nickname")
    private String nickName;

    @SerializedName("likepoint")
    private int likePoint;

    @SerializedName("dislikepoint")
    private int dislikePoint;

    @SerializedName("likestatu")
    private int likeStatus;

    public int getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public String getNickName() {
        return nickName;
    }

    public int getLikePoint() {
        return likePoint;
    }

    public int getDislikePoint() {
        return dislikePoint;
    }

    public int isLikeStatus() {
        return likeStatus;
    }
}
