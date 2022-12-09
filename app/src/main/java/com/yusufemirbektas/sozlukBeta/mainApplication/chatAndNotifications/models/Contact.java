package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

//{\"nickname\":\"IBRAHIM\",\"us1\":\"5\",\"totalMessages\":\"1\",
// \"us0LS\":\"0\",\"us1LS\":\"1\",\"lastSender\":\"1\",\"date\":\"27754237\",\"lastMessage\":\"koltuğun gücünün\"}
public class Contact {
    private String nickName;
    private int totalMessages;
    private int us0ls;
    private int us1Ls;
    private boolean lastSender;
    private int date;
    private String lastMessage;
    @SerializedName("us1")
    private int userCode;


    public String stringDate(String targetPattern){
        long epochTime= this.date*60;
        String res=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDate = LocalDateTime.ofEpochSecond(epochTime,0, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(targetPattern);
            res= formatter.format(localDate);
        }else {
            //do something
        }
        return res;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void setTotalMessages(int totalMessages) {
        this.totalMessages = totalMessages;
    }

    public int getUs0ls() {
        return us0ls;
    }

    public void setUs0ls(int us0ls) {
        this.us0ls = us0ls;
    }

    public int getUs1Ls() {
        return us1Ls;
    }

    public void setUs1Ls(int us1Ls) {
        this.us1Ls = us1Ls;
    }

    public boolean isLastSender() {
        return lastSender;
    }

    public void setLastSender(boolean lastSender) {
        this.lastSender = lastSender;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
