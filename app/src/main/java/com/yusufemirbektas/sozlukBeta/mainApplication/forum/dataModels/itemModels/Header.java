package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

public class Header {
    //member variables
    @SerializedName("nickname")
    private String nickName;

    @SerializedName("profilephoto")
    private String profilePhoto;

    @SerializedName("totalTests")
    private int totalTests;

    @SerializedName("totalChallenges")
    private int totalChallenges;

    @SerializedName("totalpoints")
    private int totalPoints;

    @SerializedName("socialearned")
    private int socialEarned;
    //will be check if boolean will  be convenient
    @SerializedName("following")
    private int following;

    @SerializedName("follower")
    private int follower;

    public Header(String nickName, String profilePhoto, int totalTests, int totalChallenges,
                  int totalPoints, int socialEarned, int following, int follower) {
        this.nickName = nickName;
        this.profilePhoto = profilePhoto;
        this.totalTests = totalTests;
        this.totalChallenges = totalChallenges;
        this.totalPoints = totalPoints;
        this.socialEarned = socialEarned;
        this.following = following;
        this.follower = follower;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public int getTotalChallenges() {
        return totalChallenges;
    }

    public void setTotalChallenges(int totalChallenges) {
        this.totalChallenges = totalChallenges;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getSocialEarned() {
        return socialEarned;
    }

    public void setSocialEarned(int socialEarned) {
        this.socialEarned = socialEarned;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
