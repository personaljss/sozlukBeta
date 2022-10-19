package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Test {
    //member variables
    @SerializedName("dateTS")
    private String date;

    @SerializedName("lessonNo")
    private int lessonNo;

    @SerializedName("testNo")
    private int testNo;

    @SerializedName("correct")
    private int correctAnswers;

    @SerializedName("wrong")
    private int wrongAnswers;


    public Test(String date, int lessonNo, int testNo, int correctAnswers, int wrongAnswers) {
        this.date = date;
        this.lessonNo = lessonNo;
        this.testNo = testNo;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
    }

    //date formatters
    public void formatDate(String sourcePattern, String targetPattern){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(sourcePattern);
            LocalDate localDate = LocalDate.parse(this.date, formatter);
            formatter = DateTimeFormatter.ofPattern(targetPattern);
            this.date = formatter.format(localDate);
        }else {
            //do something
        }
    }

    public void formatDate(String targetPattern){
        long epochTime= (long) 60 *Integer.parseInt(this.date);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(epochTime,0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(targetPattern);
            this.date = formatter.format(localDate);
        }else {
            //do something
        }
    }

    //setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    public void setTestNo(int testNo) {
        this.testNo = testNo;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    //getters
    public String getDate() {
        return date;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public int getTestNo() {
        return testNo;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }
}
