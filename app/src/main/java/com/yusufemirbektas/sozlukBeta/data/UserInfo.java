package com.yusufemirbektas.sozlukBeta.data;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.NewContentResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfo extends AndroidViewModel {
    //hashmap keys for general purpose
    public static final String HTTP_STATUS ="HTTP_SUCCESS";
    public static final String SUBJECT_ID="SUBJECT_ID";
    public static final String SUBJECT_NAME="SUBJECT_NAME";
    public static final int HTTP_SUCCESS=0;
    public static final int HTTP_FAILED=-1;
    //login constants
    //0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata
    public static final int LOGIN_SUCCESS = 0;
    public static final int LOGIN_ACTIVATION_REQUIRED = 1;
    public static final int LOGIN_INVALID_INPUT = 2;
    public static final int LOGIN_WRONG_INPUT = 3;
    public static final int SYSTEM_ERROR = 404;

    //like constants
    public static final String LIKES="LIKES_KEY";
    public static final String COMMENT_ID="COMMENT_ID";

    //field holding login status
    private MutableLiveData<Integer> loginStatus = new MutableLiveData<>();

    //social and educational points which can change at runtime
    private MutableLiveData<Integer> socialPoints=new MutableLiveData<>();
    private MutableLiveData<Integer> eduPoints=new MutableLiveData<>();

    //fields holding data
    private String userCode;
    private String deviceToken;
    private String nickName;
    private String degree;

    //Gson object to parse data
    private Gson gson;

    //constructor
    public UserInfo(@NonNull Application application) {
        super(application);
        //initialising the sharedPrefs
        SharedPrefs.init(application);
        //getting the device token
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            deviceToken = task.getResult();
                        }
                    }
                });
        //assigning required values
        userCode = SharedPrefs.read(SharedPrefs.USER_CODE, "");
        deviceToken = SharedPrefs.read(SharedPrefs.DEVICE_TOKEN, "");
    }

    public synchronized void logIn(String email, String password, String deviceToken) {
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
                loginStatus.postValue(SYSTEM_ERROR);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        //parsing the response
                        LoginResult result = gson.fromJson(response.body().string(), LoginResult.class);
                        int status = result.getResult();
                        loginStatus.postValue(status);
                        if (status == 0) {
                            //if the user successfully logged in, saving data
                            userCode = result.getUserCode();
                            nickName = result.getNickName();
                            //saving to shared prefs for further use of autologin
                            SharedPrefs.write(SharedPrefs.USER_CODE, userCode);
                            SharedPrefs.write(SharedPrefs.DEVICE_TOKEN, deviceToken);
                        }

                    } catch (Exception e) {
                        //in case of system error
                        loginStatus.postValue(SYSTEM_ERROR);
                    }
                } else {
                    loginStatus.postValue(SYSTEM_ERROR);
                }
            }
        });
    }

    //automatic login
    public void autoLogin() {
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
                loginStatus.postValue(SYSTEM_ERROR);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "hoşgeldiniz", Toast.LENGTH_SHORT).show();
                } else {
                    loginStatus.postValue(SYSTEM_ERROR);
                }
            }
        });
    }

    //like an entry
    public LiveData<HashMap<String,Integer>> like(int subjectId, int commentId, int likes, String likedOne){
        //observable to inform ui
        MutableLiveData<HashMap<String,Integer>> likedData=new MutableLiveData<>();
        //hashmap to hold like event's ui related info
        HashMap<String,Integer> map=new HashMap<>();

        //request
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("subjectID", String.valueOf(subjectId))
                .add("commentID", String.valueOf(commentId))
                .add("userCode",userCode)
                .add("likedOne",likedOne)
                .add("like",String.valueOf(likes))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.LIKE_COMMENT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                map.put(HTTP_STATUS,SYSTEM_ERROR);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    map.put(HTTP_STATUS,HTTP_SUCCESS);
                    try{
                        String jsonResponse = response.body().string();
                        Gson gson=new Gson();
                        NewContentResponse serialisedResponse=gson.fromJson(jsonResponse, NewContentResponse.class);
                        if(serialisedResponse.getResult()==0){
                            map.put(LIKES,likes);
                            map.put(COMMENT_ID,commentId);
                            int totalSocials= socialPoints.getValue();
                            socialPoints.postValue(totalSocials-likes);
                        }
                    }catch (Exception e){
                        map.put(HTTP_STATUS,SYSTEM_ERROR);
                    }
                }else {
                    map.put(HTTP_STATUS,SYSTEM_ERROR);
                }
            }
        });
        likedData.postValue(map);
        return likedData;
    }

    private LiveData<HashMap<String,Integer>> publishSubject(String subjectName, String content, int points, String channel){
        //$userCode = $_POST["userCode"];
        //    $subjectName = $_POST["subjectName"];
        //    $comment = $_POST["comment"];
        //    $pointInvested = $_POST["invested"];
        //    $channel = $_POST["channel"];
        //observable to inform ui
        MutableLiveData<HashMap<String,Integer>> result=new MutableLiveData<>();
        //hashmap to hold like event's ui related info
        HashMap<String,Integer> map=new HashMap<>();

        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("userCode",String.valueOf(UserData.getUserCode()))
                .add("subjectName", subjectName)
                .add("comment",content)
                .add("invested",String.valueOf(points))
                .add("channel",channel)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.CREATE_SUBJECT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                map.put(HTTP_STATUS,SYSTEM_ERROR);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try{
                        String jsonResponse=response.body().string();
                        NewContentResponse serialisedResponse=gson.fromJson(jsonResponse, NewContentResponse.class);
                        String comment=serialisedResponse.getComment();
                        int i=1;
                        StringBuilder sb=new StringBuilder();
                        while(i>=0){
                            try {
                                int digit=Integer.parseInt(comment.substring(i-1,i));
                                sb.append(digit);
                                i++;
                            }catch (NumberFormatException e){
                                i=-1;
                            }
                        }
                        try {
                            map.put(SUBJECT_ID,Integer.parseInt(sb.toString()));

                        }catch (NumberFormatException e){
                            //subject ccreated before
                            int j=6;
                            StringBuilder sb2=new StringBuilder();
                            while(j>=0){
                                try {
                                    int digit=Integer.parseInt(comment.substring(j-1,j));
                                    sb2.append(digit);
                                    j++;
                                }catch (NumberFormatException e2){
                                    j=-1;
                                }
                            }
                            map.put(SUBJECT_ID,Integer.parseInt(sb2.toString()));
                        }
                        map.put(HTTP_STATUS,HTTP_SUCCESS);
                    }catch (Exception e){
                        map.put(HTTP_STATUS,SYSTEM_ERROR);
                    }
                }else {
                    map.put(HTTP_STATUS,SYSTEM_ERROR);
                }
            }
        });
        result.postValue(map);
        return result;
    }



}
