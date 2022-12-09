package com.yusufemirbektas.sozlukBeta.mainApplication.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import android.app.NotificationManager;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import com.yusufemirbektas.sozlukBeta.data.User;

import com.yusufemirbektas.sozlukBeta.databinding.ActivityMainBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.LoginActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.ChatActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = User.getInstance();

        //observing the login status of the user to decide where to send him/her/they/them/trans birey
        user.loginStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=null){
                    handleLogInStatus(integer);
                }
            }

        });

        binding.chatActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChats();
            }
        });

        binding.mainActivitySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingsActivity(false);
            }
        });

    }

    private void goToChats() {
        Intent i=new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    private void handleLogInStatus(int status) {
        if (status == User.LOGIN_SUCCESSFUL) {
            //login successful
            if (!user.doesProfileExist()) {
                //if user has not required fields
                goToSettingsActivity(true);
            } else {
                setUpUi();
            }
        } else if(status==User.TOKEN_FETCHED){
            goToLoginActivity();
        }
    }

    private void goToSettingsActivity(boolean closeThis) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        if (closeThis) {
            finish();
        }
    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setUpUi() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.goToForumButton.setVisibility(View.VISIBLE);
        binding.userInfoTextView.setVisibility(View.VISIBLE);
        binding.userInfoTextView.setText("user code: " + user.getUserCode());
        binding.goToForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }

}