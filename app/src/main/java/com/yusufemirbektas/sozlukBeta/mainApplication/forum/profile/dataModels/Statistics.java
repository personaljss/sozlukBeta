package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels;

import com.google.gson.annotations.SerializedName;

public class Statistics {
    //members
    @SerializedName("totalTests")
    private int totalTests;

    @SerializedName("totalCorrect")
    private int totalCorrectAnswers;

    @SerializedName("totalWrong")
    private int totalWrongAnswers;

    //bedoya sor int mi
    @SerializedName("totalDuration")
    private int totalDuration;

    public int getTotalTests() {
        return totalTests;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public int getTotalWrongAnswers() {
        return totalWrongAnswers;
    }

    public int getTotalDuration() {
        return totalDuration;
    }
}
