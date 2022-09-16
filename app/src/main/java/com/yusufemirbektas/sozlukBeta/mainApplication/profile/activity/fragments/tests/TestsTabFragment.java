package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.tests;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.ProfileTestsTabFragmentBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.ProfileActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.ProfileDataString;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class TestsTabFragment extends Fragment {
    private static final String TAG = "TestsTabFragment";
    private ProfileDataViewModel profileDataViewModel;
    private List<Test> testList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProfileTestsTabFragmentBinding binding;
    private int userCode;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=ProfileTestsTabFragmentBinding.inflate(inflater,container,false);
        profileDataViewModel=new ViewModelProvider(getActivity()).get(ProfileDataViewModel.class);
        testList=profileDataViewModel.getTests().getValue();
        profileDataViewModel.getTests().observe(getViewLifecycleOwner(), new Observer<List<Test>>() {
            @Override
            public void onChanged(List<Test> tests) {
                testList=tests;
                adapter=new TestViewAdapter(getContext(),testList);
                manager=new LinearLayoutManager(getActivity());
                binding.profileTestsRecyclerView.setHasFixedSize(true);
                binding.profileTestsRecyclerView.setLayoutManager(manager);
                binding.profileTestsRecyclerView.setAdapter(adapter);
            }
        });

        userCode=profileDataViewModel.getUserCode().getValue();
        profileDataViewModel.getUserCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                userCode=integer;
            }
        });
        //formatting the date of the test
        for(Test test: testList){
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("ddMMyy");
            LocalDate date=LocalDate.parse(test.getDate(),formatter);
            DateTimeFormatter formatter1=DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateString=formatter1.format(date);
            test.setDate(dateString);
        }
        //setting up the recycler view
        adapter=new TestViewAdapter(getContext(),testList);
        manager=new LinearLayoutManager(getActivity());
        binding.profileTestsRecyclerView.setHasFixedSize(true);
        binding.profileTestsRecyclerView.setLayoutManager(manager);
        binding.profileTestsRecyclerView.setAdapter(adapter);

        //when user scrolls down and there is no item
        binding.profileTestsNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>=v.getChildAt(0).getHeight()-v.getHeight()){
                    //adding a null reference to the list to make adapter return progress bar
                    if(testList.get(testList.size()-1)==null){
                        //if the user is waiting for update already, return
                        return;
                    }
                    testList.add(null);
                    int testsSize= testList.size();
                    adapter.notifyItemInserted(testsSize-1);
                    profileDataViewModel.setTests(testList);
                    for(Test test : testList){
                        if(test==null){
                            Log.d(TAG, "onScrollChange: null element");
                        }
                    }
                    //load the test items
                    postShowProfile(userCode,testsSize-1,0,ProfileActivity.NOT_FIRST_TIME);
                }
            }
        });

        return binding.getRoot();
    }
    //method to get the profile data
    private void postShowProfile(int userCode, int testStart, int entryStart, int flag) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode))
                .add("testStart", String.valueOf(testStart))
                .add("entryStart", String.valueOf(entryStart))
                .add("testCount","15")
                .add("entryCount","0")
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
                    //deleting the null item in the list
                    testList.remove(testList.size()-1);
                    adapter.notifyItemRemoved(testList.size());
                    //parsing the json string
                    Gson gson = new Gson();
                    ProfileDataString profileDataString = gson.fromJson(jsonResponse, ProfileDataString.class);
                    String testString = profileDataString.getTests();
                    Test[] newTests = gson.fromJson(testString, Test[].class);
                    //creating handler and preparing the looper to run the code block on the main thread
                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.post(new UpdateTestsViewModel(newTests));
                }
            }
        });
    }
    private class UpdateTestsViewModel implements Runnable {
        Test[] newTests;
        public UpdateTestsViewModel(Test[] newTests) {
            this.newTests = newTests;
        }
        @Override
        public void run() {
            if(newTests.length==0){
                //if there is not any more tests to display, warn the user
                Toast.makeText(getContext(), "mevcut tüm testler görüntülendi", Toast.LENGTH_SHORT).show();
                return;
            }
            for(Test test : newTests){
                testList.add(test);
                Log.d(TAG, "onResponse: ");
            }
            profileDataViewModel.setTests(testList);
        }
    }

}
