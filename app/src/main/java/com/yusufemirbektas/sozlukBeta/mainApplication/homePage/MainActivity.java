package com.yusufemirbektas.sozlukBeta.mainApplication.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityMainBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.LoginActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity;
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


    @Override
    protected void onStart() {
        super.onStart();
        //get userCode
        SharedPreferences sp=this.getSharedPreferences(LoginActivity.SHARED_PREFS,MODE_PRIVATE);
        int userCode=sp.getInt(LoginActivity.SP_USERCODE,-1);
        if(userCode==-1){
            goToLoginActivity();
        }
        UserData.setUserCode(userCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            // Get new FCM registration token
                            String token = task.getResult();
                            Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            sendDeviceToken(token);
                            setUpUi();
                            return;
                        }else if(task.getException()!=null){
                            Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onComplete: error ocurred");
                        }
                    }
                });

        //getSupportActionBar().setTitle("Main Activity");
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
        binding.userInfoTextView.setText("user code: "+ UserData.getUserCode());
        binding.goToForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }
    private void sendDeviceToken(String deviceToken){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("deviceToken", deviceToken)
                .add("userCode",String.valueOf(UserData.getUserCode()))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.AUTO_LOGIN)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "faiılure", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "başarılı", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}