package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

//{\"mID\":\"1\",\"sender\":\"0\",\"date\":\"27754237\",\"message\":\"koltuğun renginin dayanılmaz izmaritinin paramparça olması\"}
public class ChatMessage {
    @SerializedName("mID")
    private int messageId;
    @SerializedName("sender")
    private int sender;
    @SerializedName("date")
    private int date;
    @SerializedName("message")
    private String text;

    public ChatMessage(int messageId, int sender, int date, String text) {
        this.messageId = messageId;
        this.sender = sender;
        this.date = date;
        this.text = text;
    }

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

    public void setDate(int date) {
        this.date = date;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getSender() {
        return sender;
    }

    public int getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
