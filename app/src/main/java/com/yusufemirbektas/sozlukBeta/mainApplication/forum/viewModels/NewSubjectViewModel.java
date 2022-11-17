package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewSubjectViewModel extends ViewModel {
    private static final String TAG = "NewSubjectViewModel";
    MutableLiveData<Response> liveResponse=new MutableLiveData<>();

    public void publishSubject(String subjectName, String content, String points, String channel){
        //$userCode = $_POST["userCode"];
        //    $subjectName = $_POST["subjectName"];
        //    $comment = $_POST["comment"];
        //    $pointInvested = $_POST["invested"];
        //    $channel = $_POST["channel"];
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", User.getInstance().getUserCode())
                .add("subjectName", subjectName)
                .add("comment",content)
                .add("invested",points)
                .add("channel",channel)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.CREATE_SUBJECT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: ");
                    liveResponse.postValue(response);
                }
            }
        });
    }

    public LiveData<Response> getLiveResponse() {
        return liveResponse;
    }

}
