package com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserNameViewModel;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.ActivationActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.LoginActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientRetrofit;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiInterface;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginTabFragment extends Fragment {

    private EditText usernameET;
    private EditText passwordET;
    private Button loginButton;
    ImageView showHidePassword;
    ProgressBar progressBar;

    UserNameViewModel userNameViewModel;

    String deviceToken;

    public static final String SHARED_PREFS="sharedPrefs";
    public static final String USER_NAME="userName";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login,container,false);
        setViews(root);
        setShowHidePassword(passwordET,showHidePassword);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            deviceToken= task.getResult();
                            Toast.makeText(getContext(), deviceToken, Toast.LENGTH_SHORT).show();
                            return;
                        }else if(task.getException()!=null){
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        upDateEditTexts();

        userNameViewModel=new ViewModelProvider(getActivity()).get(UserNameViewModel.class);
        userNameViewModel.getUserName().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                usernameET.setText(s);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postLogin();
            }
        });
        return root;
    }

    private void postLogin(){
        //disabling button
        loginButton.setEnabled(false);
        //making progress bar visible on the button
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setText("");

        //posting login info to the server
        Retrofit retrofit= ApiClientRetrofit.getInstance();
        Call<LoginResult> call = retrofit.create(LoginApiInterface.class)
                .postLogin(deviceToken ,usernameET.getText().toString(),passwordET.getText().toString());

        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.isSuccessful()){
                    validateLogin(response.body());
                }else {
                    Toast.makeText(getActivity(), "serverla ilgili bir sorun yaşandı...", Toast.LENGTH_SHORT).show();
                    Log.d("Login post","response is not succesful!");
                }
                //enabling the button when the response comes
                loginButton.setEnabled(true);
                //making progress bar visible on the button
                progressBar.setVisibility(View.GONE);
                loginButton.setText("GİRİŞ YAP");
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.d("Login post","onFailure called");
                Toast.makeText(getContext(),"bir sorun yaşandı...",Toast.LENGTH_LONG).show();
                //enabling the button again if there is a problem
                loginButton.setEnabled(true);
                //making progress bar visible on the button
                progressBar.setVisibility(View.GONE);
                loginButton.setText("GİRİŞ YAP");
            }
        });
    }

    private void setViews(ViewGroup root){
        usernameET=root.findViewById(R.id.username_ET);
        passwordET=root.findViewById(R.id.password_ET);
        showHidePassword=root.findViewById(R.id.show_hide_password);
        loginButton=root.findViewById(R.id.login_btn);
        progressBar=root.findViewById(R.id.progress_bar);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void goToActivationActivity(int userCode){
        Intent intent = new Intent(getActivity(), ActivationActivity.class);
        intent.putExtra(BundleKeys.USERCODE,userCode);
        startActivity(intent);
    }

    private void setShowHidePassword(EditText passwordField, ImageView eyeIcon){
        //password hide-show
        passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eyeIcon.setImageResource(R.drawable.ic_hide_pwd);
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordField.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password is visible
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.ic_hide_pwd);
                }else{
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
    }

    private void validateLogin(LoginResult loginResult){
        //0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata
        try {
            Toast.makeText(getContext(),loginResult.getComment(),Toast.LENGTH_LONG).show();
            if(loginResult.getResult()==0){

                //save the user code
                UserData.setUserCode(loginResult.getUserCode());
                //save the data
                saveToSharedPrefs(loginResult.getUserCode());
                //if login is successful go to main activity;
                goToMainActivity();
            }else if(loginResult.getResult()==1){
                //if activation is required go to activation activity
                //UserData.setUserCode(loginResult.getUserCode());
                goToActivationActivity(loginResult.getUserCode());
                //when I solve the bugs, I will use it
                //openActivationDialog();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"bir şeyler ters gitti, lütfen internet bağlantınızı kontrol edin",Toast.LENGTH_LONG).show();
        }

    }

    /*
    private void saveLoginData(String userName, String password){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_NAME,userName);
        //editor.putString(PASSWORD,password);
        editor.apply();
    }

     */

    private void saveToSharedPrefs(int userCode){
        SharedPreferences sharedPrefs= getContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPrefs.edit();
        editor.putInt(LoginActivity.SP_USERCODE, userCode);
        editor.apply();
    }

    private void upDateEditTexts(){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        usernameET.setText(sharedPreferences.getString(USER_NAME,""));
        //passwordET.setText(sharedPreferences.getString(PASSWORD,""));
    }

}
