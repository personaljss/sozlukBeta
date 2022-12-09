

package com.yusufemirbektas.sozlukBeta.data;

import static com.yusufemirbektas.sozlukBeta.data.GenericResponse.CONNECTION_FAILED;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginViewModel;
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

/**
 * this singleton class is responsible for holding the current user's data.
 **/
public class User {
    private static final String TAG = "User";
    //login constants
    public static final int AUTO_FAILED=-1;
    public static final int LOGIN_SUCCESSFUL=0;
    public static final int ACTIVATION_REQUIRED=1;
    public static final int TOKEN_FETCHED=31;

    //instance
    private static User instance;

    //fields
    private final MutableLiveData<Integer> loginStatus = new MutableLiveData<>();
    private String degree;
    private String deviceToken;
    private String nickname;
    private String userCode;
    private final Gson gson=new Gson();

    //private constructor to make sure that there is only a single isntance of this class at runtime
    private User() {
    }

    //static method to get User instance
    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void init(Context context) {
        instance = getInstance();
        SharedPrefs.init(context);
        instance.deviceToken = SharedPrefs.read(SharedPrefs.DEVICE_TOKEN, "");
        instance.userCode = SharedPrefs.read(SharedPrefs.USER_CODE, "");
    }

    /**
     * These methods are created for checking the user's current status in the app
     */

    //checks whether required fields for a profile exits or not
    public boolean doesProfileExist() {
        //boolean res= degree != null && nickname != null;
        if(nickname==null){
            return false;
        }
        return (!nickname.equals("N"));
    }

    //Live data of issign in
    public LiveData<Integer> loginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int status) {
        loginStatus.postValue(status);
    }


    /**
     * These methods prepare a http request which are meant to be made in relevant ViewModels.
     * Returned call objects will be enqueued and their callbacks will be implemented.
     **/

    //login related methods:

    //regular login
    public Call logIn(String email, String password, String deviceToken) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken", deviceToken)
                .add("email_login", email)
                .add("ps_login", password)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.LOGIN)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    public LiveData<GenericResponse<LoginResult>> login(String email, String password) {
        MutableLiveData<GenericResponse<LoginResult>> liveResponse = new MutableLiveData<>();
        GenericResponse<LoginResult> genericResponse = new GenericResponse<>();

        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken", deviceToken)
                .add("email_login", email)
                .add("ps_login", password)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.LOGIN)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                genericResponse.httpStatus = CONNECTION_FAILED;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json=response.body().string();
                    //parsing the response
                    try {
                        LoginResult result = gson.fromJson(json, LoginResult.class);
                        genericResponse.response=result;
                        setLoginStatus(result.getResult());
                        if (result.getResult() == 0) {
                            //if the user successfully logged in, saving data
                            String userCode = result.getUserCode();
                            setUserCode(userCode);
                            setNickname(result.getNickName());
                            SharedPrefs.write(SharedPrefs.USER_CODE, userCode);
                            SharedPrefs.write(SharedPrefs.DEVICE_TOKEN, deviceToken);
                        }
                    } catch (Exception e) {
                        LoginViewModel.ActResp resp = gson.fromJson(json, LoginViewModel.ActResp.class);
                        genericResponse.response=new LoginResult(resp.result, resp.userCode);
                        setLoginStatus(resp.result);
                        if (resp.result == 1) {
                            setUserCode(resp.userCode);
                        }
                    }
                }else {
                    genericResponse.httpStatus=GenericResponse.SERVER_FAILED;
                }
                liveResponse.postValue(genericResponse);
            }
        });
        return liveResponse;
    }

    //automatic login
    public void autoLogin() {
        if(deviceToken.equals("")){
            //user installed the app for the first time or app data has been deleted
            setLoginStatus(AUTO_FAILED);
            fetchDeviceToken();
            return;
        }
        Log.i(TAG, "token: "+deviceToken);
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken", deviceToken)
                .add("userCode", userCode)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.AUTO_LOGIN)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                setLoginStatus(CONNECTION_FAILED);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    LoginResult result=gson.fromJson(response.body().string(), LoginResult.class);
                    if(result.getResult()==0){
                        setNickname(result.getNickName());
                        setLoginStatus(result.getResult());
                    }else if(result.getResult()==404){
                        setLoginStatus(User.AUTO_FAILED);
                        fetchDeviceToken();
                    }
                }catch (Exception e){
                    //which means there is something wrong with the server(probably a bug).
                }

            }
        });
    }
    //automatic login

    //sign-up
    public Call signUp(String email, String password1, String password2) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("email_reg", email)
                .add("ps_reg1", password1)
                .add("ps_reg2", password2)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.REGISTER)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    //activating the account
    public Call activate(String activationCode) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", userCode)
                .add("activationCode", activationCode)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.ACTIVATION)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    public Call createProfile(String nickName, String degree, String ppImage) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", userCode)
                .add("nickname", nickName)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.NEW_NICKNAME)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    public Call upLoadProfilePhoto(String imageStr) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", userCode)
                .add("image", imageStr)
                .add("imageType", "0")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.UPLOAD_PP)
                .post(requestBody)
                .build();
        return client.newCall(request);
    }

    private void fetchDeviceToken(){

        //getting the device token and saving it
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            setLoginStatus(TOKEN_FETCHED);
                            deviceToken = task.getResult();
                        }else {
                            setLoginStatus(CONNECTION_FAILED);
                        }

                    }
                });

    }

    //getter and setters
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getNickname() {
        return nickname;
    }

    public synchronized void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}