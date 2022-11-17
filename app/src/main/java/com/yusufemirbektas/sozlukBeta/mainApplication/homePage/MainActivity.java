package com.yusufemirbektas.sozlukBeta.mainApplication.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.data.User;

import com.yusufemirbektas.sozlukBeta.databinding.ActivityMainBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.LoginActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.viewModel.MainViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private User user;
    private MainViewModel viewModel;
    @Override
    protected void onStart() {
        super.onStart();
        /*
        if(!user.isSignedIn()){
            //in case the system destroyed the app
            goToLoginActivity();
        }

         */
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(this).get(MainViewModel.class);

        user=User.getInstance();
        /*
        if(!user.isSignedIn()){
            //user directly coming to main activity
            viewModel.autoLogIn();
        }else {
            setUpUi();
        }

         */
        user.loginStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean!=null){
                    if(!aBoolean){
                        goToLoginActivity();
                    }else {
                        setUpUi();
                    }
                }
            }
        });

        viewModel.loginResult.observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if(loginResult!=null){
                    if(loginResult.getResult()!=0){
                        goToLoginActivity();
                    }else{
                        if(!user.doesProfileExist()){
                            goToSettingsActivity(true);
                        }else{
                            setUpUi();
                        }
                    }
                }
            }
        });
    }

    private void goToSettingsActivity(boolean closeThis) {
        Intent intent=new Intent(this, SettingsActivity.class);
        startActivity(intent);
        if(closeThis){
            finish();
        }
    }

    private void goToLoginActivity() {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setUpUi(){
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.goToForumButton.setVisibility(View.VISIBLE);
        binding.userInfoTextView.setVisibility(View.VISIBLE);
        binding.userInfoTextView.setText("user code: "+ user.getUserCode());
        binding.goToForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }

}