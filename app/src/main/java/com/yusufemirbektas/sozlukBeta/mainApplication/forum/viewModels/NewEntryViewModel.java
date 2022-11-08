package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.NewContentServerResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewEntryViewModel extends ViewModel {
    public static final String SUBJECT_ID_KEY="SUBJECT_ID_KEY";
    public static final String COMMENT_ID_KEY="COMMENT_ID_KEY";
    MutableLiveData<HashMap<String,String>> newEntryLoc=new MutableLiveData<>(new HashMap<>(Map.ofEntries(
            Map.entry(SUBJECT_ID_KEY,""),
            Map.entry(COMMENT_ID_KEY,"")
    )));

    public void postNewEntry(int subjectId, String entry) {
        //{"result":0,"comment":"Yorum yapıldı.","time":"97.834825515747 ms"}
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("subjectID", String.valueOf(subjectId))
                .add("userCode", String.valueOf(UserData.getUserCode()))
                .add("comment", entry)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.NEW_COMMENT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    handleResponse(response.body().string());
                }
            }
        });
    }

    public LiveData<HashMap<String, String>> getNewEntryLoc() {
        return newEntryLoc;
    }

    private void handleResponse(String resp) {
        Gson gson=new Gson();
        NewContentServerResponse serialisedResponse=gson.fromJson(resp,NewContentServerResponse.class);
        String comment=serialisedResponse.getComment();

        String[] arrOfStr = comment.split(",", 2);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(SUBJECT_ID_KEY,arrOfStr[0]);
        hashMap.put(COMMENT_ID_KEY,arrOfStr[1]);
        newEntryLoc.postValue(hashMap);
    }
}
