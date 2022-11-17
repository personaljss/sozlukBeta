package com.yusufemirbektas.sozlukBeta.data;

import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * this singleton class is responsible for holding the current user's data.
 **/
public class User {
    //constants
    public static final int DNE = -1;
    public static final int UNIVERSITY = 0;
    public static final int HIGH_SCHOOL = 1;
    public static final int ELEMENTARY = 2;
    //instance
    private static User instance;

    //fields
    private int socialPoints = DNE;
    private int eduPoints = DNE;
    private int degree = DNE;
    private String deviceToken;
    private String nickname;
    private String userCode;

    //private constructor to make sure that there is only a single isntance of this class at runtime
    private User() {
    }

    //static method to get User instance
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    //methods
    public boolean isSignedIn() {
        boolean res = true;
        if(userCode==null || deviceToken==null){
            res=false;
        }
        return res;
    }

    public boolean doesProfileExist(){
        boolean res=true;
        if(degree==DNE || nickname==null){
            res=false;
        }
        return res;
    }

    //login related methods
    public Call logIn(String email, String password,String deviceToken){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken",deviceToken)
                .add("email_login",email)
                .add("ps_login",password)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL+ServerAdress.LOGIN)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    public Call autoLogin(){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken", deviceToken)
                .add("userCode",userCode)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.AUTO_LOGIN)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    public Call signUp(String email, String password1, String password2){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("email_reg", email)
                .add("ps_reg1",password1)
                .add("ps_reg2",password2)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.REGISTER)
                .post(requestBody)
                .build();

        return client.newCall(request);
    }

    //getter and setters
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getSocialPoints() {
        return socialPoints;
    }

    public void setSocialPoints(int socialPoints) {
        this.socialPoints = socialPoints;
    }

    public int getEduPoints() {
        return eduPoints;
    }

    public void setEduPoints(int eduPoints) {
        this.eduPoints = eduPoints;
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

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}