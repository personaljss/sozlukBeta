package com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Header {
    //member variables
    @SerializedName("nickname")
    private String nickName;

    @SerializedName("profilephoto")
    private int profilePhoto;

    @SerializedName("totalTests")
    private int totalTests;

    @SerializedName("totalChallenges")
    private int totalChallenges;

    @SerializedName("totalpoints")
    private int totalPoints;

    @SerializedName("socialearned")
    private int socialEarned;

    public Header(String nickName, int profilePhoto, int totalTests, int totalChallenges, int totalPoints, int socialEarned) {
        this.nickName = nickName;
        this.profilePhoto = profilePhoto;
        this.totalTests = totalTests;
        this.totalChallenges = totalChallenges;
        this.totalPoints = totalPoints;
        this.socialEarned = socialEarned;
    }

    public String getNickName() {
        return nickName;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public int getTotalChallenges() {
        return totalChallenges;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getSocialEarned() {
        return socialEarned;
    }
}
