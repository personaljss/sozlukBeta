package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.data.GenericResponse;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.Contact;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContactsViewModel extends ViewModel {
    private MutableLiveData<List<Contact>> contacts=new MutableLiveData<>();
    private User user=User.getInstance();
    private Gson gson=new Gson();
    private boolean isSpInitialised=false;


    public void fetchContacts(int date){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("date", String.valueOf(date))
                .add("userCode", user.getUserCode())
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.CONTACTS)
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
                }
            }
        });
    }

    private void parseResponse(String response) {
        ContactsResponse responseModel=gson.fromJson(response,ContactsResponse.class);
        Type ListOfContact=new TypeToken<List<Contact>>(){}.getType();
        contacts.postValue(gson.fromJson(responseModel.contacts,ListOfContact));
    }

    public void setContacts(List<Contact> contacts){
        this.contacts.postValue(contacts);
    }

    public MutableLiveData<List<Contact>> getContacts(){
        return contacts;
    }

    public static class ContactsResponse {
        @SerializedName("result")
        public int result;
        @SerializedName("comment")
        public String comment;
        @SerializedName("messages")
        public String contacts;
    }
}
