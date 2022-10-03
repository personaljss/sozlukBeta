package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels;

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
}
