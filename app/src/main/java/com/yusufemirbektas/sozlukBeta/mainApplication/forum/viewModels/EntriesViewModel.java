package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;


import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity.targetDatePattern;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.SubjectEntriesResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
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

public class EntriesViewModel extends ViewModel {
    private static final String TAG = "SubjectEntriesViewModel";
    private int subjectId = -1;
    private int commentId = -1;
    private MutableLiveData<List<Entry>> entries = new MutableLiveData<>();
    private MutableLiveData<Boolean> fail = new MutableLiveData<>(false);

    //subjectID, commentID, userCode
    public void loadSubjectEntries() {
        try {
            OkHttpClient client = ApiClientOkhttp.getInstance();

            RequestBody requestBody = new FormBody.Builder()
                    .add("subjectID", String.valueOf(subjectId))
                    .add("commentID", String.valueOf(commentId))
                    .add("userCode", String.valueOf(UserData.getUserCode()))
                    .build();

            Request request = new Request.Builder()
                    .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_SUBJECT_ENTRIES_PHP)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    fail.postValue(true);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try{
                            String jsonResponse = response.body().string();
                            addEntriesBg(jsonResponse);
                        }catch (Exception e){
                            fail.postValue(true);
                        }
                    }
                }
            });
        } catch (IllegalArgumentException exception) {
            exception.fillInStackTrace();
            Log.d(TAG, "!!!!!!!!!!!!!!!!" +
                    "loadSubjectEntries: muhtemelen usercode ve subject id bu" +
                    "metod çağrılmadan önce initialise edilmedi." + "\n"
                    + "metot çağrılmadan bunları fragmanın getArguments() ile " +
                    "aldığı args(bundle) objesindeki değerlerden" +
                    "başlat eğer sorun bu değilse doktoru ara da o uğraşsın" +
                    "!!!!!!!!!!!!!!!!");
        }
    }

    //subjectID, commentID, userCode
    public void loadSubjectEntries(int start) {
        try {
            OkHttpClient client = ApiClientOkhttp.getInstance();

            RequestBody requestBody = new FormBody.Builder()
                    .add("subjectID", String.valueOf(subjectId))
                    .add("commentID", String.valueOf(start))
                    .add("userCode", String.valueOf(UserData.getUserCode()))
                    .build();

            Request request = new Request.Builder()
                    .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_SUBJECT_ENTRIES_PHP)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        addEntriesBg(jsonResponse);
                    }
                }
            });
        } catch (IllegalArgumentException exception) {
            exception.fillInStackTrace();
            Log.d(TAG, "!!!!!!!!!!!!!!!!" +
                    "loadSubjectEntries: muhtemelen usercode ve subject id bu" +
                    "metod çağrılmadan önce initialise edilmedi." + "\n"
                    + "metot çağrılmadan bunları fragmanın getArguments() ile " +
                    "aldığı args(bundle) objesindeki değerlerden" +
                    "başlat eğer sorun bu değilse doktoru ara da o uğraşsın" +
                    "!!!!!!!!!!!!!!!!");
        }
    }

    //postlar: $usercode = $_POST["userCode"];
    //    $date = $_POST["date"];
    //    $limit = $_POST["limit"];
    public void loadMainFeed(int startDate) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("limit", "10")
                .add("date", String.valueOf(startDate))
                .add("userCode", String.valueOf(UserData.getUserCode()))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.MAIN_FEED)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    addEntriesBg(jsonResponse);
                }
            }
        });
    }

    //for epoch time
    private void addEntriesBg(String jsonResponse) {
        Gson gson = new Gson();
        SubjectEntriesResponse entriesResponse = gson.fromJson(jsonResponse, SubjectEntriesResponse.class);
        List<Entry> entryList = this.entries.getValue();
        if (entryList == null) {
            entryList = new ArrayList<>();
        } else if (entryList.size() > 0 && entryList.get(entryList.size() - 1) == null) {
            entryList.remove(entryList.size() - 1);
        }
        Type ListOfSubjectEntries = TypeToken.getParameterized(List.class, Entry.class).getType();
        List<Entry> entriesFromJson = gson.fromJson(entriesResponse.getData(), ListOfSubjectEntries);
        if (entriesFromJson.size() > 0) {
            for (Entry entry : entriesFromJson) {
                entry.formatDate(targetDatePattern);
                entryList.add(entry);
            }
        }
        this.entries.postValue(entryList);
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    private void setEntries(List<Entry> entries) {
        this.entries.setValue(entries);
    }

    private void postSubjectEntries(List<Entry> entries) {
        this.entries.postValue(entries);
    }
/*
    public LiveData<Integer> getPointsSpent() {
        return pointsSpent;
    }

    public void setPointsSpent(int pointsSpent) {
        this.pointsSpent.setValue(pointsSpent);
    }

    public void postPointsSpent(int pointsSpent){
        this.pointsSpent.postValue(pointsSpent);
    }

 */

    public int getSubjectId() {
        return subjectId;
    }

    public int getCommentId() {
        return commentId;
    }

    public LiveData<List<Entry>> getEntries() {
        return entries;
    }
}
