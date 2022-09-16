package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels;

import com.google.gson.annotations.SerializedName;

public class ForumSubject {
    //data: sonucu json array şeklinde return ediyor. bunun içinde de şu değerler var: subjectID, totalPoint, subjectName, channel, totalComments, comment24ID.
    //{"SubjectID":179,"totalPoint":401,"subjectName":"salağın hamlesinin yok olması","channel":2,"totalComments":18,"comment24ID":1}
    @SerializedName("SubjectID")
    private int subjectID;
    @SerializedName("totalPoint")
    private int totalPoint;
    @SerializedName("subjectName")
    private String subjectName;
    @SerializedName("channel")
    private int channelID;
    @SerializedName("totalComments")
    private String totalComments;
    @SerializedName("comment24ID")
    private int comment24ID;

    public ForumSubject(int subjectID, int totalPoint, String subjectName, int channelID, String totalComments, int comment24ID) {
        this.subjectID = subjectID;
        this.totalPoint = totalPoint;
        this.subjectName = subjectName;
        this.channelID = channelID;
        this.totalComments = totalComments;
        this.comment24ID = comment24ID;
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

    public int getComment24ID() {
        return comment24ID;
    }
}
