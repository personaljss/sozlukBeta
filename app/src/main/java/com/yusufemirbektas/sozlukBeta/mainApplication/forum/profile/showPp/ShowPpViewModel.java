package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.showPp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class ShowPpViewModel extends ViewModel {
    private MutableLiveData<String> imageStr=new MutableLiveData<>();

    public void loadProfilePhoto(int userCode){
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("images", String.valueOf(userCode))
                .add("folder", "profilePhotosFullRes")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.DISPLAY_PP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){

                }
            }
        });
    }

    public LiveData<String> getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr.setValue(imageStr);
    }
}
