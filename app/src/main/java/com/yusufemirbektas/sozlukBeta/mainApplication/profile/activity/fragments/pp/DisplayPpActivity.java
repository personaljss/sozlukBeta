package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.pp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityDisplayPpBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.ProfileActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.DisplayPpResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayPpActivity extends AppCompatActivity {
    private static final String TAG = "DisplayPpActivity";
    public static final String EXTRA_SHOW_PP="coming from show pp activity";
    ActivityDisplayPpBinding binding;
    int userCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDisplayPpBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        binding.displayPpImageView.setImageResource(R.drawable.profile_photo);
        userCode=getIntent().getIntExtra(ProfileActivity.USERCODE_EXTRA,-1);

        binding.displayPpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
        showProfilePhoto(userCode);
    }
//ilk post değeri "folder"
// profilePhotos yazarsan düşük çözünürlüklü, profilePhotosFullRes yazarsan yüksek çözünürlüklü foto alır
//ikinci post değeri "image" user code giriliyor
    private void showProfilePhoto(int userCode){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("folder","profilePhotosFullRes")
                .add("image",String.valueOf(userCode))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.DISPLAY_PP_PHP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "onResponse: ");
                if(response.isSuccessful()){
                    Gson gson=new Gson();
                    DisplayPpResponse ppResponse=gson.fromJson(response.body().string(), DisplayPpResponse.class);
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            Type imagesType=new TypeToken<HashMap<String,String>>(){}.getType();
                            HashMap<String,String> images=gson.fromJson(ppResponse.getImagesEncoded(),imagesType);
                            if(images.get(String.valueOf(userCode)).equals("")){
                                binding.displayPpProgressBar.setVisibility(View.GONE);
                                binding.displayPpImageView.setImageResource(R.drawable.profile_photo);
                                return;
                            }
                            Bitmap image=stringToBitmap((String) images.get(String.valueOf(userCode)));
                            binding.displayPpProgressBar.setVisibility(View.GONE);
                            binding.displayPpImageView.setImageBitmap(image);
                        }
                    });
                }
            }
        });
    }
    private void goToProfile(int userCode){
        Intent intent=new Intent(DisplayPpActivity.this,ProfileActivity.class);
        intent.putExtra(ProfileActivity.USERCODE_EXTRA,userCode);
        intent.putExtra(EXTRA_SHOW_PP,0);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap stringToBitmap(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

}