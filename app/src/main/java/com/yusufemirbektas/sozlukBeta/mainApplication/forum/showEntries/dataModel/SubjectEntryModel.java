package com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.dataModel;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class SubjectEntryModel {

    @SerializedName("commentID")
    private int commentId;

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
        long epochTime= 60L *Integer.parseInt(this.date);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(epochTime,0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(targetPattern);
            this.date = formatter.format(localDate);
        }else {
            //do something
        }
    }


    public int getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public String getDateTime() {
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

    public int getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public void setLikePoint(int likePoint) {
        this.likePoint = likePoint;
    }
}
