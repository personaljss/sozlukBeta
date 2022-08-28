package com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments;

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
import androidx.lifecycle.ViewModelProvider;


import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserNameViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientRetrofit;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SignupTabFragment extends Fragment {

    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordCheckET;
    Button signupBtn;
    ImageView showHidePassword;
    ImageView showHidePassword2;
    ProgressBar progressBar;
    UserNameViewModel userNameViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);
        setViews(root);
        setShowHidePassword(passwordET,showHidePassword);
        setShowHidePassword(passwordCheckET,showHidePassword2);

        userNameViewModel=new ViewModelProvider(getActivity()).get(UserNameViewModel.class);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disabling the button until response comes
                signupBtn.setEnabled(false);
                //making progress bar visible on the button
                progressBar.setVisibility(View.VISIBLE);
                signupBtn.setText("");
                //posting the signup info
                Retrofit retrofit= ApiClientRetrofit.getInstance();
                Call<LoginResult> call = retrofit.create(LoginApiInterface.class)
                        .postSignup(emailET.getText().toString(),userNameET.getText().toString()
                                ,passwordET.getText().toString(), passwordCheckET.getText().toString());

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.isSuccessful()){
                            handleSignUpResult(response.body(),userNameViewModel);
                        }else{
                            Log.d("signup onResponse", "onResponse() is not succesful.");
                        }
                        //making progress bar gone and activating the button
                        signupBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        signupBtn.setText("KAYDOL");
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.d("signup onFailure", "onFailure() called.");
                        //making progress bar gone and activating the button
                        signupBtn.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        signupBtn.setText("KAYDOL");
                    }
                });

            }
        });

        return root;
    }

    private void setViews(ViewGroup root){
        userNameET=root.findViewById(R.id.username_ET);
        emailET=root.findViewById(R.id.email_ET);
        passwordET=root.findViewById(R.id.password_ET);
        passwordCheckET=root.findViewById(R.id.password_check_ET);
        signupBtn=root.findViewById(R.id.signup_btn);
        showHidePassword=root.findViewById(R.id.show_hide_password);
        showHidePassword2=root.findViewById(R.id.show_hide_password2);
        progressBar=root.findViewById(R.id.progress_bar);
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

    private void handleSignUpResult(LoginResult loginResult, UserNameViewModel userNameViewModel){
        //404: sistem hatası, 0: kayıt başarılı, 1: email v.d., 2: username v.d, 3: password v.d.,
        // 4: email exists, 5: username exists, 6: passwords do not match
        try{
            Toast.makeText(getContext(),loginResult.getComment(),Toast.LENGTH_LONG).show();
            if(loginResult.getResult()==0){
                //if signup is successful display the name in login fragment
                userNameViewModel.setUserName(userNameET.getText().toString());
            }
        }catch (NullPointerException e){
            Toast.makeText(getContext(),"bir sorun yaşandı, lütfen tekrar deneyin",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}

