package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels;

import com.google.gson.annotations.SerializedName;

public class ForumSubject {
    //data: sonucu json array şeklinde return ediyor. bunun içinde de şu değerler var: subjectID, totalPoint, subjectName, channel, totalComments, comment24ID.
    //{"SubjectID":179,"totalPoint":401,"subjectName":"salağın hamlesinin yok olması","channel":2,"totalComments":18,"comment24ID":1}
    @SerializedName("subjectID")
    private int subjectID;
    @SerializedName("totalPoint")
    private int totalPoint;
    @SerializedName("subjectName")
    private String subjectName;
    @SerializedName("channel")
    private int channelID;
    @SerializedName("totalComments")
    private String totalComments;

    public ForumSubject(int subjectID, int totalPoint, String subjectName, int channelID, String totalComments) {
        this.subjectID = subjectID;
        this.totalPoint = totalPoint;
        this.subjectName = subjectName;
        this.channelID = channelID;
        this.totalComments = totalComments;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getChannelID() {
        return channelID;
    }

    public String getTotalComments() {
        return totalComments;
    }

}
