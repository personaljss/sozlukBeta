package com.yusufemirbektas.sozlukBeta.loginPage.viewModels;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 * This class manages the login related process' in the background and communicate with ui
 * **/
public class LoginViewModel extends AndroidViewModel {

    private String deviceToken;
    //required constructor matching super
    public LoginViewModel(@NonNull Application application) {
        super(application);
        //initialising the SharedPrefs to save data in case of a successful login
        SharedPrefs.init(getApplication());
        //getting the device token and saving it
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            deviceToken = task.getResult();
                        }
                    }
                });
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
    private Callback callback=new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            httpSuccess.postValue(false);
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if (response.isSuccessful()) {
                try{
                    //parsing the response
                    loginResult.postValue(gson.fromJson(response.body().string(), LoginResult.class));
                    httpSuccess.postValue(true);
                }catch (Exception e){
                    httpSuccess.postValue(false);
                }
            } else {
                httpSuccess.postValue(false);
            }
        }
    };
    //methods
    public void logIn(String email, String password) {
        user.logIn(email, password, deviceToken).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                httpSuccess.postValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    httpSuccess.postValue(true);
                    //parsing the response
                    LoginResult result=gson.fromJson(response.body().string(), LoginResult.class);
                    loginResult.postValue(result);

                    if(result.getResult()==0){
                        //if the user successfully logged in, saving data
                        String userCode=result.getUserCode();
                        user.setUserCode(userCode);
                        SharedPrefs.write(SharedPrefs.USER_CODE,userCode);
                        user.setDeviceToken(deviceToken);
                        SharedPrefs.write(SharedPrefs.DEVICE_TOKEN,deviceToken);
                        user.setNickname(result.getNickName());
                        user.setSignedIn(true);
                        user.setLoginStatus(true);
                    }


                } else {
                    httpSuccess.postValue(false);
                }
            }
        });
    }


    public void signUp(String email, String password1, String password2) {
        user.signUp(email, password1, password2).enqueue(callback);
    }

    public void activate(String activationCode){
        user.activate(activationCode).enqueue(callback);
    }

}