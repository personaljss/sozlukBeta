package com.yusufemirbektas.sozlukBeta.loginPage.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.data.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * This class manages the login related process' in the background and communicate with ui
 **/
public class LoginViewModel extends AndroidViewModel {


    //required constructor matching super
    public LoginViewModel(@NonNull Application application) {
        super(application);
        //initialising the SharedPrefs to save data in case of a successful login
        SharedPrefs.init(getApplication());
    }

    //user instance
    private final User user = User.getInstance();

    //the result of the login process
    //0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata
    public MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    //this field checks the http process, in case of a failure the value will be false
    public MutableLiveData<Boolean> httpSuccess = new MutableLiveData<>();

    //Gson object to parse the response
    private Gson gson = new Gson();

    //CallBack object to manage http response
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            httpSuccess.postValue(false);
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if (response.isSuccessful()) {
                try {
                    //parsing the response
                    loginResult.postValue(gson.fromJson(response.body().string(), LoginResult.class));
                    httpSuccess.postValue(true);
                } catch (Exception e) {
                    httpSuccess.postValue(false);
                }
            } else {
                httpSuccess.postValue(false);
            }
        }
    };


    public static class ActResp {
        @SerializedName("result")
        public int result;
        @SerializedName("usercode")
        public String userCode;

    }


    public void signUp(String email, String password1, String password2) {
        user.signUp(email, password1, password2).enqueue(callback);
    }

    public void activate(String activationCode) {
        user.activate(activationCode).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    httpSuccess.postValue(true);
                    final String json = response.body().string();
                    //parsing the response
                    try {
                        LoginResult result = gson.fromJson(response.body().string(), LoginResult.class);
                        loginResult.postValue(result);
                        user.setLoginStatus(result.getResult());
                        if (result.getResult() == 0) {
                            //if the user successfully logged in, saving data
                            String userCode = result.getUserCode();
                            user.setUserCode(userCode);
                            SharedPrefs.write(SharedPrefs.USER_CODE, userCode);
                            SharedPrefs.write(SharedPrefs.DEVICE_TOKEN, user.getDeviceToken());
                            user.setNickname(result.getNickName());
                        }
                    } catch (Exception e) {
                        ActResp resp = gson.fromJson(json, ActResp.class);
                        loginResult.postValue(new LoginResult(resp.result, resp.userCode));
                        user.setLoginStatus(resp.result);
                        if (resp.result == 1) {
                            user.setUserCode(resp.userCode);
                        }
                    }

                } else {
                    httpSuccess.postValue(false);
                }
            }
        });
    }

}