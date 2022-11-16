package com.yusufemirbektas.sozlukBeta.mainApplication.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityMainBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.LoginActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.viewModel.MainViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.settings.SettingsActivity;
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


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private User user;
    private MainViewModel viewModel;
    @Override
    protected void onStart() {
        super.onStart();
        if(user==null || !user.isSignedIn()){
            //in case the system destroyed the app
            goToLoginActivity();
        }
        if(!user.doesProfileExist()){
            goToSettingsActivity(true);
        }
    }

    private void goToSettingsActivity(boolean closeThis) {
        Intent intent=new Intent(this, SettingsActivity.class);
        startActivity(intent);
        if(closeThis){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(this).get(MainViewModel.class);

        user=User.getInstance();
        if(!user.isSignedIn()){
            //user directly coming to main activity
            SharedPrefs.init(this);
            user.setUserCode(SharedPrefs.read(SharedPrefs.USER_CODE,""));
            user.setDeviceToken(SharedPrefs.read(SharedPrefs.DEVICE_TOKEN,""));
            viewModel.autoLogIn();
        }

        viewModel.loginResult.observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if(loginResult!=null){
                    if(loginResult.getResult()!=0){
                        goToLoginActivity();
                    }else{
                        setUpUi();
                    }
                }
            }
        });
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