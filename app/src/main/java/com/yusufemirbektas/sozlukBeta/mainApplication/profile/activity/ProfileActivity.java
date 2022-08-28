package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.ProfileDataString;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;


import java.io.IOException;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    ProfileDataViewModel profileDataViewModel;
    TextView textView;
    ProgressBar progressBar;
    private volatile boolean dataLoaded=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileDataViewModel=new ViewModelProvider(this).get(ProfileDataViewModel.class);

        textView=findViewById(R.id.textViewProfile);
        textView.setVisibility(View.INVISIBLE);
        progressBar=findViewById(R.id.progressBarProfile);

        OkHttpClient client= ApiClientOkhttp.getInstance();

        RequestBody requestBody=new FormBody.Builder()
                .add("usercode",String.valueOf(UserData.getUserCode()))
                .build();

        Request request=new Request.Builder()
                .url(ApiClientOkhttp.SERVER_URL+ApiClientOkhttp.PROFILE_PHP)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonResponse=response.body().string();
                    Log.d(TAG, "onResponse: response body: "+jsonResponse);

                    Gson gson=new Gson();
                    ProfileDataString profileDataString=gson.fromJson(jsonResponse,ProfileDataString.class);
                    Log.d(TAG, "onResponse: profileData comment: "+profileDataString.getComment());
                    String headerString=profileDataString.getHeader();
                    Header header=gson.fromJson(headerString,Header.class);
                    Log.d(TAG, "onResponse: header: "+header.getNickName());
                    String testString=profileDataString.getTests();
                    Test[] tests=gson.fromJson(testString,Test[].class);
                    String entriesString=profileDataString.getEntries();
                    Entry[] entries=gson.fromJson(entriesString,Entry[].class);

                    dataLoaded=true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileDataViewModel.setHeader(header);
                            profileDataViewModel.setEntries(Arrays.asList(entries));
                            profileDataViewModel.setTests(Arrays.asList(tests));

                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText("Header: " +
                                    "\nnickname: "+profileDataViewModel.getHeader().getValue().getNickName()+
                                    "\ntotal tests: "+profileDataViewModel.getHeader().getValue().getTotalTests()+
                                    "\ntotal challenges: "+profileDataViewModel.getHeader().getValue().getTotalChallenges()+
                                    "\ntotal points: " +profileDataViewModel.getHeader().getValue().getSocialEarned()+
                                    "\nsocial earned: "+profileDataViewModel.getHeader().getValue().getSocialEarned());
                        }
                    });
                }
            }
        });


    }


}