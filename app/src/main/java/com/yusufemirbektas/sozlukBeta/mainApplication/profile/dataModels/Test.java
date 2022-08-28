package com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels;

import com.google.gson.annotations.SerializedName;

public class Test {
    //member variables
    @SerializedName("date")
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
