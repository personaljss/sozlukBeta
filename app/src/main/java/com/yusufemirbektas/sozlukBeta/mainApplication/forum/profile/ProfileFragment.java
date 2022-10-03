package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FragmentProfileBinding binding;
    private ProfileDataViewModel viewModel;
    //tab titles
    private final String[] TAB_TITLES = {"TESTLER", "ENTRILER"};
    //constants of image size when uploading the profile photo
    private static final int LOW_QUALITY_IMAGE_SIZE = 30000;
    private static final int HIGH_QUALITY_IMAGE_SIZE = 120000;
    //flags to display the profile
    public static final int FIRST_TIME = 1;
    public static final int NOT_FIRST_TIME = 0;
    //Image type constant to get profile picture from server
    public static final int IMAGE_TYPE_PROFILE = 0;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(this).get(ProfileDataViewModel.class);

        //get the user code, if it is -1, this means no usercode is passed as an argument so this is the user's profile
        Bundle args=getArguments();
        int userCode= UserData.getUserCode();
        if(args!=null){
            if(args.getInt(ForumActivity.BundleKeys.USERCODE,-1)!=-1){
                userCode=args.getInt(ForumActivity.BundleKeys.USERCODE,-1);
            }
        }
        viewModel.setUserCode(userCode);
        //if it is the first time that this page is opened, load the contents
        if(viewModel.getHeader().getValue()==null){
            setProgressBarsVisible();
            viewModel.loadProfileData();
        }else{
            setUpHeaderUi(viewModel.getHeader().getValue());
        }

        viewModel.getHeader().observe(getViewLifecycleOwner(), new Observer<Header>() {
            @Override
            public void onChanged(Header header) {
                setUpHeaderUi(header);
            }
        });

        binding.viewPagerProfile.setAdapter(new ProfileViewPagerAdapter(this));

        new TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TAB_TITLES[position]);
            }
        }).attach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.homeButtonImageView) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
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
        }

        return Base64.getEncoder().encodeToString(byteArray);
    }

    //method to convert a string to a bitmap
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap stringToBitmap(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void setProgressBarsVisible(){
        binding.ppProgressBar.setVisibility(View.VISIBLE);
        binding.ppProgressBar.setVisibility(View.VISIBLE);
    }

    private void setUpHeaderUi(Header header){
        //the page is loaded so progress bar is gone
        binding.profileProgressBar.setVisibility(View.GONE);
        binding.ppProgressBar.setVisibility(View.GONE);
        binding.nicknameTextView.setText(header.getNickName());
        binding.totalTestsTextView.setText("testler: " + header.getTotalTests());
        binding.totalChallengesTextView.setText("günlük challenge: " + header.getTotalChallenges());
        binding.totalPointsTextView.setText("puan: " + header.getTotalPoints());
        binding.socialEarnedTextView.setText("sosyal: " + header.getSocialEarned());
        //setting the profile photo

        if (!header.getProfilePhoto().equals("1")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.profilePhotoImageView.setImageBitmap(stringToBitmap(header.getProfilePhoto()));
            }else{
                //do something
            }
        }

    }

}
