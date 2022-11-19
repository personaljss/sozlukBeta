package com.yusufemirbektas.sozlukBeta.mainApplication.homePage.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainViewModel extends ViewModel {
    private final User user=User.getInstance();
    public MutableLiveData<String> deviceToken=new MutableLiveData<>();
    public MutableLiveData<Boolean> httpSuccess=new MutableLiveData<>();
    public MutableLiveData<LoginResult> loginResult=new MutableLiveData<>();
    private final Gson gson=new Gson();

    //CallBack object to manage autologin http response
    private Callback callback=new Callback() {
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
                if(result.getResult()==0){
                    user.setNickname(result.getNickName());
                    loginResult.postValue(result);
                    //user.setSignedIn(true);
                }
            } else {
                httpSuccess.postValue(false);
            }
        }
    };

    public void updateDeviceToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            deviceToken.postValue(task.getResult());
                        }else if(task.getException()!=null){

                        }
                    }
                });
    }

}