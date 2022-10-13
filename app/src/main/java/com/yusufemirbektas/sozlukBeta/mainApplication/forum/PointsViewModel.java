package com.yusufemirbektas.sozlukBeta.mainApplication.forum;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.newContent.NewContentServerResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PointsViewModel extends ViewModel {
    public static int DEFAULT_POINTS=0;
    public static final int DEFAULT_STATUS=0;
    public static int DEFAULT_POS=-1;
    MutableLiveData<Integer> pointsAvailable=new MutableLiveData<Integer>(DEFAULT_POINTS);
    MutableLiveData<Integer> entryItemPosition=new MutableLiveData<Integer>(DEFAULT_POS);
    MutableLiveData<Integer> entryItemLikeStatus=new MutableLiveData<Integer>(DEFAULT_STATUS);

    // getters and setters
    public LiveData<Integer> getPointsAvailable() {
        return pointsAvailable;
    }

    public void setPointsAvailable(int pointsAvailable) {
        this.pointsAvailable.setValue(pointsAvailable);
    }

    public LiveData<Integer> getEntryItemPosition() {
        return entryItemPosition;
    }

    public void setEntryItemPosition(int entryItemPosition) {
        this.entryItemPosition.setValue(entryItemPosition);
    }

    public LiveData<Integer> getEntryItemLikeStatus() {
        return entryItemLikeStatus;
    }

    public void setEntryItemLikeStatus(int entryItemLikeStatus) {
        this.entryItemLikeStatus.setValue(entryItemLikeStatus);
    }

    public void refresh(){
        entryItemLikeStatus.setValue(DEFAULT_STATUS);
        entryItemPosition.setValue(DEFAULT_POS);
    }

    public void likeEntry(int userCode, int likes, int subjectId, int commentId, int adapterPos){
        pointsAvailable.setValue(pointsAvailable.getValue()-Math.abs(likes));
        entryItemLikeStatus.setValue(likes);
        entryItemPosition.setValue(adapterPos);
        //postlar: $usercode = $_POST["userCode"];
        //    $subjectID = $_POST["subjectID"];
        //    $commentID = $_POST["commentID"];
        //    $likedOne = $_POST["likedOne"];
        //    $like = $_POST["like"]; //-128,+128
        try{
            OkHttpClient client = ApiClientOkhttp.getInstance();

            RequestBody requestBody = new FormBody.Builder()
                    .add("subjectID", String.valueOf(subjectId))
                    .add("commentID", String.valueOf(commentId))
                    .add("userCode",String.valueOf(UserData.getUserCode()))
                    .add("likedOne",String.valueOf(userCode))
                    .add("like",String.valueOf(likes))
                    .build();

            Request request = new Request.Builder()
                    .url(ServerAdress.SERVER_URL + ServerAdress.LIKE_COMMENT)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    pointsAvailable.postValue(pointsAvailable.getValue()-Math.abs(likes));
                    entryItemLikeStatus.postValue(entryItemLikeStatus.getValue()-likes);
                    entryItemPosition.postValue(DEFAULT_POS);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        Gson gson=new Gson();
                        NewContentServerResponse serialisedResponse=gson.fromJson(jsonResponse,NewContentServerResponse.class);
                        if(serialisedResponse.getResult()!=0){
                            pointsAvailable.postValue(pointsAvailable.getValue()+Math.abs(likes));
                            entryItemLikeStatus.postValue(entryItemLikeStatus.getValue()-likes);
                            entryItemPosition.postValue(DEFAULT_POS);
                        }
                    }
                }
            });
        }catch (Exception exception){
            exception.fillInStackTrace();
        }
    }

}
