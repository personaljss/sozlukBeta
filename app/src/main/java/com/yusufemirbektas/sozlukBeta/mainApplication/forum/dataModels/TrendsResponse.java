package com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels;

import com.google.gson.annotations.SerializedName;

public class TrendsResponse {
    //[06:00, 12.09.2022] Bedirhan Selim (Su): data: sonucu json array şeklinde return ediyor. bunun içinde de şu değerler var: subjectID, totalPoint, subjectName, channel, totalComments, comment24ID.
    //[06:01, 12.09.2022] Bedirhan Selim (Su): result: 0 ise başarılı, 404 ise sistemsel bi hata
    //[06:01, 12.09.2022] Bedirhan Selim (Su): comment: eğer hata meydana geldiyse türünü belirtiyor.
    //[06:01, 12.09.2022] Bedirhan Selim (Su): time: süre
    @SerializedName("data")
    String data;
    @SerializedName("result")
    int result;
    @SerializedName("comment")
    String comment;
    @SerializedName("time")
    String time;

    public TrendsResponse(String data, int result, String comment, String time) {
        this.data = data;
        this.result = result;
        this.comment = comment;
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public int getResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }
}
