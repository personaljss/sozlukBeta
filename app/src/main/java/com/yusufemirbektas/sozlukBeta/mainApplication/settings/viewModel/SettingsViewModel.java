package com.yusufemirbektas.sozlukBeta.mainApplication.settings.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.yusufemirbektas.sozlukBeta.data.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;


public class SettingsViewModel extends ViewModel {
    private final User user= User.getInstance();
    private final MutableLiveData<Boolean> httpSuccess=new MutableLiveData<>();
    private final Gson gson=new Gson();
    private final MutableLiveData<Response> mResponse=new MutableLiveData<>();

    //method
    public void updateProfile(String nickName, String degree, String ppImage){
        user.createProfile(nickName,null,null).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpSuccess.postValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    httpSuccess.postValue(true);
                    Response resp=gson.fromJson(response.body().string(),Response.class);
                    if(resp.result==0){
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

    public LiveData<Boolean> getHttpSuccess() {
        return httpSuccess;
    }

    public LiveData<Response> getResponse() {
        return mResponse;
    }

    public static class Response{
        @SerializedName("result")
        public int result;
    }

}
