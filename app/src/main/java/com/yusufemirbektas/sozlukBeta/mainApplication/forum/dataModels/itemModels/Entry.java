package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Entry {
    //member variables
    @SerializedName("subjectID")
    private int subjectID;

    @SerializedName("commentID")
    private int commentID;

    @SerializedName("comment")
    private String comment;

    @SerializedName("dateTS")
    private String date;

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

    @SerializedName("likestatu")
    private int likeStatus;


    //date formatters
    public void formatDate(String sourcePattern, String targetPattern){
        DateTimeFormatter formatter= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern(sourcePattern);
            LocalDate localDate = LocalDate.parse(this.date, formatter);
            formatter = DateTimeFormatter.ofPattern(targetPattern);
            this.date = formatter.format(localDate);
        }else {
            //do something
        }
    }

    public void formatDate(String targetPattern){
        long epochTime= Integer.parseInt(this.date);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(epochTime,0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(targetPattern);
            this.date = formatter.format(localDate);
        }else {
            //do something
        }
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

    public String getDate() {
        return date;
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

    public int getLikeStatus() {
        return likeStatus;
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

    public void setDate(String date) {
        this.date = date;
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

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }
}
