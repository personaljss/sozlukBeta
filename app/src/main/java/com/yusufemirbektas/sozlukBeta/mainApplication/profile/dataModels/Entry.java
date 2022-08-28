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

    public Entry(int subjectID, int commentID, String comment, String dateTime, int userCode, String nickName, int likePoint, int dislikePoint) {
        this.subjectID = subjectID;
        this.commentID = commentID;
        this.comment = comment;
        this.dateTime = dateTime;
        this.userCode = userCode;
        this.nickName = nickName;
        this.likePoint = likePoint;
        this.dislikePoint = dislikePoint;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLikePoint(int likePoint) {
        this.likePoint = likePoint;
    }

    public void setDislikePoint(int dislikePoint) {
        this.dislikePoint = dislikePoint;
    }

    //getters
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
}
