package com.yusufemirbektas.sozlukBeta.mainApplication.settings.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
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


public class SettingsViewModel extends ViewModel {
    private final User user = User.getInstance();
    private final MutableLiveData<Boolean> httpSuccess = new MutableLiveData<>();
    private final Gson gson = new Gson();
    private final MutableLiveData<Response> mResponse = new MutableLiveData<>();

    //method
    public void updateProfile(String nickName, String degree, String ppImage) {
        user.createProfile(nickName, null, null).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpSuccess.postValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    httpSuccess.postValue(true);
                    Response resp = gson.fromJson(response.body().string(), Response.class);
                    if (resp.result == 0) {
                        //if succesfully changed, assign the fields to the user object
                        user.setNickname(nickName);
                        user.setDegree(degree);
                    }
                    mResponse.postValue(resp);
                }
                httpSuccess.postValue(false);
            }
        });
    }

    public LiveData<Integer> updatePp(String imageStr) {
        MutableLiveData<Integer> result = new MutableLiveData<>();
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", User.getInstance().getUserCode())
                .add("image", imageStr)
                .add("imageType", "0")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.UPLOAD_PP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result.postValue(404);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    //do something
                    try{
                        ResponseRes res = gson.fromJson(response.body().string(), ResponseRes.class);
                        result.postValue(res.result);
                    }catch (Exception e){
                        result.postValue(404);
                    }
                }else {
                    result.postValue(404);
                }
            }
        });
        return result;
    }

    public static class ResponseRes {
        @SerializedName("result")
        public int result;
        @SerializedName("comment")
        public String comment;
    }

    public LiveData<Boolean> getHttpSuccess() {
        return httpSuccess;
    }

    public LiveData<Response> getResponse() {
        return mResponse;
    }

    public static class Response {
        @SerializedName("result")
        public int result;
    }

}