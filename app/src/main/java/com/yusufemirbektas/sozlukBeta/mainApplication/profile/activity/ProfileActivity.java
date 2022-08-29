package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.adapter.ProfileSectionsPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.ProfileDataString;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;


import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/* olmayan kullanıcı
 {"result":0,"comment":"Gösterim başarılı.","time":"0.78678131103516 ms","header":"false","tests":"[]","entries":"[]"}
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    ProfileDataViewModel profileDataViewModel;
    private ActivityProfileBinding binding;

    private final String[] TAB_TITLES={"TESTLER","BAŞLIKLAR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.nicknameTextView.setText("");
        binding.totalTestsTextView.setText("");
        binding.totalChallengesTextView.setText("");
        binding.totalPointsTextView.setText("");
        binding.socialEarnedTextView.setText("");

        profileDataViewModel = new ViewModelProvider(this).get(ProfileDataViewModel.class);

        Objects.requireNonNull(getSupportActionBar()).hide();

        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(UserData.getUserCode()))
                .build();


        Request request = new Request.Builder()
                .url(ApiClientOkhttp.SERVER_URL + ApiClientOkhttp.PROFILE_PHP)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "onResponse: response body: " + jsonResponse);

                    Gson gson = new Gson();
                    ProfileDataString profileDataString = gson.fromJson(jsonResponse, ProfileDataString.class);
                    String headerString = profileDataString.getHeader();
                    Header header = gson.fromJson(headerString, Header.class);
                    String testString = profileDataString.getTests();
                    Test[] tests = gson.fromJson(testString, Test[].class);
                    String entriesString = profileDataString.getEntries();
                    Entry[] entries = gson.fromJson(entriesString, Entry[].class);
                
                    runOnUiThread(new ShowProfileTask(header,entries,tests));
                }
            }
        });

    }

    private class ShowProfileTask implements Runnable{
        private final Header header;
        private final Entry[] entries;
        private final Test[] tests;

        public ShowProfileTask(Header header, Entry[] entries, Test[] tests) {
            this.header = header;
            this.entries = entries;
            this.tests = tests;
        }

        @Override
        public void run() {
            Log.d(TAG, "run: ");
            //saving all the data to the profileViewModel object to reach them from the fragments
            profileDataViewModel.setHeader(header);
            profileDataViewModel.setEntries(Arrays.asList(entries));
            profileDataViewModel.setTests(Arrays.asList(tests));

            //the page is loaded so progress bar is gone
            binding.profileProgressBar.setVisibility(View.GONE);

            //Displaying the user info
            Header headerUi = profileDataViewModel.getHeader().getValue();
            binding.nicknameTextView.setText(headerUi.getNickName());
            binding.totalTestsTextView.setText("testler: " + headerUi.getTotalTests());
            binding.totalChallengesTextView.setText("günlük challenge: " + headerUi.getTotalChallenges());
            binding.totalPointsTextView.setText("puan: " + headerUi.getTotalPoints());
            binding.socialEarnedTextView.setText("sosyal: " + headerUi.getSocialEarned());

            //when user clicks the home button, return to main activity
            binding.homeButtonImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //setting the tabs
            ProfileSectionsPagerAdapter pagerAdapter=new ProfileSectionsPagerAdapter(ProfileActivity.this);
            binding.viewPagerProfile.setAdapter(pagerAdapter);
            new TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(TAB_TITLES[position]);
                }
            }).attach();

            //settings icon clicked event


        }
    }


}