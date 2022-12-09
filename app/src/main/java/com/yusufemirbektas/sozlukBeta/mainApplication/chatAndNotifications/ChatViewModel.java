package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.data.GenericResponse;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.ChatMessage;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//u0: userid küçük olan kullanıcının usercodeu
//u1: userid büyük olan kullanıcının usercodeu
//interval1: alınacak mesajların başlangıcının idsi
//interval2: alınacak son mesajın idsi
//unseen: eğer daha önce görmediği bi mesajı yükleyeceksek 1, eğer eski mesajları yükleyeceksek 0 olmalı
public class ChatViewModel extends ViewModel {
    public static final String CHAT_LS="CHAT_LS";
    private static final String TAG = "ChatViewModel";
    private MutableLiveData<List<ChatMessage>> chatMessages=new MutableLiveData<>();
    private User user=User.getInstance();
    private Gson gson=new Gson();
    private int chatBuddyCode;
    private int lastSeenMID;

    public void fetchChat(int startDate, int endDate){
        int u1=chatBuddyCode;
        int u0=Integer.parseInt(user.getUserCode());
        if(u0>u1){
            u1=u0;
            u0=chatBuddyCode;
        }

        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("u0",String.valueOf(u0))
                .add("u1",String.valueOf(u1))
                .add("interval1","0")
                .add("interval2",String.valueOf(endDate))
                .add("unseen","0")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.CHAT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    parseResponse(response.body().string());
                    seenReport();
                }
            }
        });
    }

    public synchronized void sendChatMessage(int getterCode,ChatMessage chatMessage){
        //https://hostingdenemesi.online/messaging/sendmessage/
        //sender, getter, message
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("sender", user.getUserCode())
                .add("getter",String.valueOf(getterCode))
                .add("message",chatMessage.getText())
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SEND_CHAT_MESSAGE)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        ChatResponse chatResponse=gson.fromJson(response.body().string(),ChatResponse.class);
                        if (chatResponse.result==0){
                            chatMessage.setDate(chatResponse.dateTime);
                            addMessage(chatMessage);
                            seenReport();
                        }
                    }catch (JsonSyntaxException e){

                    }
                }
            }
        });
    }

    public void seenReport(){
        //messaging/seemessage/
        //$u0 = $_POST["seeker"]; //gören kişi
        //$u1 = $_POST["seen"];  //görülen kişi
        //$number = $_POST["number"]; //görülen mesajın numarası
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("seeker", user.getUserCode())
                .add("seen",String.valueOf(chatBuddyCode))
                .add("number",String.valueOf(lastSeenMID))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SEEN_INFO)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }

    private void addMessage(ChatMessage chatMessage) {
        List<ChatMessage> messages=chatMessages.getValue();
        messages.add(chatMessage);
        chatMessages.postValue(messages);
    }

    private void parseResponse(String response){
        ChatResponse chatResponse=gson.fromJson(response,ChatResponse.class);
        Type ListOfMessages=new TypeToken<List<ChatMessage>>(){}.getType();
        List<ChatMessage> messageList=gson.fromJson(chatResponse.messages,ListOfMessages);
        int date=messageList.get(messageList.size()-1).getDate();

        int i=messageList.size()-1;
        ChatMessage chatMessage=messageList.get(i);
        while (chatMessage.getSender()!=chatBuddyCode && i>0){
            chatMessage=messageList.get(i);
            i+=-1;
        }
        lastSeenMID=i+1;

        this.chatMessages.postValue(messageList);
    }

    public LiveData<List<ChatMessage>> getMessages(){
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages){
        this.chatMessages.postValue(chatMessages);
    }

    public int getUserCode() {
        return chatBuddyCode;
    }

    public void setUserCode(int userCode) {
        this.chatBuddyCode = userCode;
    }

    public static class ChatResponse{
        @SerializedName("result")
        public int result;
        @SerializedName("comment")
        public String comment;
        @SerializedName("messages")
        public String messages;
        @SerializedName("date")
        public int dateTime;
    }

}
