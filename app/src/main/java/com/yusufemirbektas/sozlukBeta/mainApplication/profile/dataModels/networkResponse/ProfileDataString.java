package com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse;

import com.google.gson.annotations.SerializedName;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;

import java.util.List;

public class ProfileDataString {
    @SerializedName("result")
    private int result;

    @SerializedName("comment")
    private String comment;

    @SerializedName("time")
    private String time;

    @SerializedName("header")
    private String header;

    @SerializedName("tests")
    private String tests;

    @SerializedName("entries")
    private String entries;

    /*
    @SerializedName("statistics")
    List<Statistics> statisticsList;

     */

    public ProfileDataString(int result, String comment, String time, String header, String tests, String entries) {
        this.result = result;
        this.comment = comment;
        this.time = time;
        this.header = header;
        this.tests = tests;
        this.entries = entries;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }
}
