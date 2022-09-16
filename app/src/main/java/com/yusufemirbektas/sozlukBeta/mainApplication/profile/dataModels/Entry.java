package com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels;

import com.google.gson.annotations.SerializedName;

public class Entry {
    //member variables
    @SerializedName("subjectID")
    private int subjectID;

    @SerializedName("commentID")
    private int commentID;

    @SerializedName("comment")
    private String comment;

    @SerializedName("dateTS")
    private String dateTime;

    @SerializedName("usercode")
    private int userCode;

    @SerializedName("nickname")
    private String nickName;

    @SerializedName("likepoint")
    private int likePoint;

    @SerializedName("dislikepoint")
    private int dislikePoint;

    @SerializedName("subjectName")
    private String subjectName;

    public Entry(int subjectID, int commentID, String comment, String dateTime, int userCode, String nickName, int likePoint, int dislikePoint, String subjectName) {
        this.subjectID = subjectID;
        this.commentID = commentID;
        this.comment = comment;
        this.dateTime = dateTime;
        this.userCode = userCode;
        this.nickName = nickName;
        this.likePoint = likePoint;
        this.dislikePoint = dislikePoint;
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public int getCommentID() {
        return commentID;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getUserCode() {
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

    public String getSubjectName() {
        return subjectName;
    }
}
