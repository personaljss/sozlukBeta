package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.adapter.ProfileSectionsPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.pp.DisplayPpActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.ProfileDataString;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.UploadPPResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    public static final String USERCODE_EXTRA = "com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.userCode";
    ProfileDataViewModel profileDataViewModel;
    private ActivityProfileBinding binding;

    private final String[] TAB_TITLES = {"TESTLER", "ENTRILER"};

    private ActivityResultLauncher<String> pickPhoto;
    //constants of image size when uploading the profile photo
    private static final int LOW_QUALITY_IMAGE_SIZE = 30000;
    private static final int HIGH_QUALITY_IMAGE_SIZE = 120000;
    //flags to display the profile
    public static final int FIRST_TIME = 1;
    public static final int NOT_FIRST_TIME = 0;
    //Image type constant to get profile picture from server
    public static final int IMAGE_TYPE_PROFILE = 0;
    //user code
    int userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ppProgressBar.setVisibility(View.GONE);

        profileDataViewModel = new ViewModelProvider(this).get(ProfileDataViewModel.class);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //when user clicks the home button, return to main activity
        binding.homeButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //getting the user code
        int userCodeExtra = getIntent().getIntExtra(USERCODE_EXTRA, -1);
        if (userCodeExtra != -1) {
            userCode = userCodeExtra;
            binding.settingsImageView.setVisibility(View.INVISIBLE);
        } else {
            userCode = UserData.getUserCode();
        }
        profileDataViewModel.setUserCode(userCode);

        if (userCode == UserData.getUserCode()) {
            //if this is the profile of the user himself
            //registering the pp ImageView to show context menu
            registerForContextMenu(binding.profilePhotoImageView);
        }else{
            binding.profilePhotoImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ProfileActivity.this, DisplayPpActivity.class);
                    intent.putExtra(USERCODE_EXTRA,userCode);
                    startActivity(intent);
                }
            });
        }

        if(binding.nicknameTextView.getText().equals("")){
            postShowProfile(userCode, 0, 0, FIRST_TIME);
        }else{
            Log.d(TAG, "onCreate: else:"+binding.nicknameTextView.getText());
            runOnUiThread(new ShowProfileTask(profileDataViewModel.getHeader().getValue(),
                    Objects.requireNonNull(profileDataViewModel.getEntries().getValue()).toArray(new Entry[0]),
                    Objects.requireNonNull(profileDataViewModel.getTests().getValue()).toArray(new Test[0])));
        }

        //implementing the ActivityResultLauncher to pick a photo and put it into ppImageView
        pickPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onActivityResult(Uri result) {
                        if(result==null){
                            Log.d(TAG, "onActivityResult: uri is null");
                            postShowProfile(userCode, 0, 0, FIRST_TIME);
                            return;
                        }
                        try {
                            Bitmap ppHighBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                            Bitmap ppLowBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                            uploadProfilePhoto(ppLowBitmap, ppHighBitmap, IMAGE_TYPE_PROFILE, userCode);
                        } catch (IOException e) {
                            Log.d(TAG, "onActivityResult: media selection exception happened");
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, "Problem ocurred...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

    }

    private class ShowProfileTask implements Runnable {
        private final Header header;
        private final List<Entry> entries;
        private final List<Test> tests;

        public ShowProfileTask(Header header, Entry[] entries, Test[] tests) {
            this.header = header;
            this.entries = new ArrayList<Entry>(Arrays.asList(entries));
            this.tests = new ArrayList<Test>(Arrays.asList(tests));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            Log.d(TAG, "run: ");
            //saving all the data to the profileViewModel object to reach them from the fragments
            profileDataViewModel.setHeader(header);
            profileDataViewModel.setEntries(entries);
            profileDataViewModel.setTests(tests);

            //Displaying the user info
            Header headerUi = profileDataViewModel.getHeader().getValue();
            binding.nicknameTextView.setText(headerUi.getNickName());
            binding.totalTestsTextView.setText("testler: " + headerUi.getTotalTests());
            binding.totalChallengesTextView.setText("günlük challenge: " + headerUi.getTotalChallenges());
            binding.totalPointsTextView.setText("puan: " + headerUi.getTotalPoints());
            binding.socialEarnedTextView.setText("sosyal: " + headerUi.getSocialEarned());

            //the page is loaded so progress bar is gone
            binding.profileProgressBar.setVisibility(View.GONE);

            //setting the tabs
            ProfileSectionsPagerAdapter pagerAdapter = new ProfileSectionsPagerAdapter(ProfileActivity.this);
            binding.viewPagerProfile.setAdapter(pagerAdapter);
            new TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(TAB_TITLES[position]);
                }
            }).attach();

            //setting the profile photo
            if (!header.getProfilePhoto().equals("0")) {
                binding.profilePhotoImageView.setImageBitmap(stringToBitmap(header.getProfilePhoto()));
            }

        }
    }

    //method to get the profile data
    private void postShowProfile(int userCode, int testStart, int entryStart, int flag) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode))
                .add("testStart", String.valueOf(testStart))
                .add("entryStart", String.valueOf(entryStart))
                .add("testCount", "10")
                .add("entryCount", "10")
                .add("header", String.valueOf(flag))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.PROFILE_PHP)
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
                    runOnUiThread(new ShowProfileTask(header, entries, tests));
                }
            }
        });
    }

    //$type = $_POST["imageType"]; //0: profilePhoto, 1: commentPhoto, 2: questionPhoto
    //$image = $_POST["image"];
    //$userCode = $_POST["userCode"];
    //$_POST["imageFull"]
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadProfilePhoto(Bitmap srcLow, Bitmap srcHigh, int imageType, int userCode) {
        //making the ui elements invisible and creating the progress bar on the pp
        Toast.makeText(this, "Profil fotoğrafınız değiştiriliyor lütfen bekleyin", Toast.LENGTH_SHORT).show();
        binding.profilePhotoImageView.setImageResource(R.drawable.profile_photo);
        binding.ppProgressBar.setVisibility(View.VISIBLE);
        //setting up the post request
        OkHttpClient client = ApiClientOkhttp.getInstance();

        String highStr = imageToString(srcHigh, HIGH_QUALITY_IMAGE_SIZE);
        String lowStr = imageToString(srcLow, LOW_QUALITY_IMAGE_SIZE);

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", String.valueOf(userCode))
                .add("imageType", String.valueOf(imageType))
                .add("image", lowStr)
                .add("imageFull", highStr)
                .build();
        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.UPLOAD_PP_PHP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    //"Profil fotoğrafı güncellendi."
                    //"SQL hatası. Eğer zaten profil fotoğrafı yoksa yeni fotoğraf işlenemedi.";
                    //result:0 işlem başarılı
                    //result:1 eğer halihazırda bi profil fotoğrafı varsa ve kaldırılmamışsa başarılı
                    //result: 404 işlem başarısız
                    //setting the ui elements visible and removing the progress bar
                    //making the ui elements invisible and creating the progress bar on the pp
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.ppProgressBar.setVisibility(View.GONE);
                        }
                    });

                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    UploadPPResponse uploadPPResponse = gson.fromJson(jsonResponse, UploadPPResponse.class);
                    if (uploadPPResponse.getResult() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.profilePhotoImageView.setImageBitmap(srcLow);
                                Toast.makeText(ProfileActivity.this, "Profil fotoğrafınız başarıyla değiştirildi...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileActivity.this, "Bir sorun yaşandı...", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    //method to convert an image to string
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String imageToString(Bitmap bitmap, int targetBytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int currQuality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        while (byteArray.length > targetBytes) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, currQuality, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            currQuality = (int) (currQuality * 0.9);
            Log.d(TAG, "imageToString: quality=" + currQuality + " array size=" + byteArray.length);
        }

        Log.d(TAG, "imageToString: byteArray final size: " + byteArray.length);
        return Base64.getEncoder().encodeToString(byteArray);
    }

    //method to convert a string to a bitmap
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap stringToBitmap(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    //profile photo add/remove process
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == binding.profilePhotoImageView.getId()) {
            getMenuInflater().inflate(R.menu.profile_pp_change_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pp_change:
                pickPhoto.launch("image/*");
                return true;
            case R.id.pp_remove:
                binding.profilePhotoImageView.setImageResource(R.drawable.profile_photo);
                Toast.makeText(this, "profil fotoğrafı kaldırıldı", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.pp_show:
                Intent intent=new Intent(ProfileActivity.this, DisplayPpActivity.class);
                intent.putExtra(USERCODE_EXTRA,userCode);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
}