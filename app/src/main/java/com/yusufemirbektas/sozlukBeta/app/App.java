package com.yusufemirbektas.sozlukBeta.app;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class App extends Application{
    private static final String TAG = "App";
    private User user;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        User.init(this);
        user=User.getInstance();
        user.autoLogin().enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                try {
                    LoginResult result=new Gson().fromJson(response.body().string(), LoginResult.class);
                    if(result.getResult()==0){
                        user.setNickname(result.getNickName());
                        user.setSignedIn(true);
                        user.setLoginStatus(true);
                    }
                }catch (Exception e){
                    //when userCode and deviceToken in SharedPres is null, server returns http code 500
                    //which means there is something wrong with the server(probably a bug).
                    user.setLoginStatus(false);
                }
            }
        });
    }
}
