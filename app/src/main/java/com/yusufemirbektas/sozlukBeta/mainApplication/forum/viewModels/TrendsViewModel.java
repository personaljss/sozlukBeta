package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.ForumSubject;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.TrendsResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrendsViewModel extends ViewModel {
    MutableLiveData<List<ForumSubject>> forumSubjects=new MutableLiveData<>();

    public void setForumSubjects(List<ForumSubject> forumSubjects) {
        this.forumSubjects.setValue(forumSubjects);
    }

    public LiveData<List<ForumSubject>> getForumSubjects() {
        return forumSubjects;
    }

    public void loadTrends(String channel){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("channels", channel)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_CHANNEL_SUBJECTS_PHP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseString=response.body().string();
                    Gson gson=new Gson();
                    TrendsResponse trendsResponse=gson.fromJson(responseString,TrendsResponse.class);
                    Type forumSubjectListType= TypeToken.getParameterized(ArrayList.class,ForumSubject.class).getType();
                    forumSubjects.postValue(gson.fromJson(trendsResponse.getData(),forumSubjectListType));
                }
            }
        });
    }
}
