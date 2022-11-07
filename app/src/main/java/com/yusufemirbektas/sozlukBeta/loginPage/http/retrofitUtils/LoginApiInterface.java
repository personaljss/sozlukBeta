package com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApiInterface {
    String LOGIN_PHP="loginfiles/loginprocess/";
    String REGISTER_PHP="loginfiles/registerprocess/";
    String ACTIVATE_PHP="loginfiles/activationprocess/";
    @FormUrlEncoded
    @POST(LOGIN_PHP)
    Call<LoginResult> postLogin(@FieldMap Map<String,String> loginParams);

    @FormUrlEncoded
    @POST(LOGIN_PHP)
    Call<LoginResult> postLogin( @Field("deviceToken")String deviceToken ,@Field("email_login")String userName, @Field("ps_login")String password);

    @FormUrlEncoded
    @POST(LOGIN_PHP)
    Call<LoginResult> postLogin(@Field("email_login")String userName, @Field("ps_login")String password);

    @FormUrlEncoded
    @POST(REGISTER_PHP)
    Call<LoginResult> postSignup(@FieldMap Map<String,String> signupParams);

    @FormUrlEncoded
    @POST(REGISTER_PHP)
    Call<LoginResult> postSignup(@Field("email_reg")String email, @Field("ps_reg1")String password,
                                 @Field("ps_reg2") String passwordCheck);

    @FormUrlEncoded
    @POST(ACTIVATE_PHP)
    Call<LoginResult> postActivation(@Field("userCode") Integer userCode, @Field("activationCode")String activationCode);

}
