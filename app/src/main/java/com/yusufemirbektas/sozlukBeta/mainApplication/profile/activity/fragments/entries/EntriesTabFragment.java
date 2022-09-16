package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.entries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.tests.TestsTabFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.networkResponse.ProfileDataString;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EntriesTabFragment extends Fragment {

    private static final String TAG = "EntriesTabFragment";
    ProfileTestsTabFragmentBinding binding;
    ProfileDataViewModel profileDataViewModel;
    RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private int userCode;
    List<Entry> entryList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=ProfileTestsTabFragmentBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        profileDataViewModel=new ViewModelProvider(getActivity()).get(ProfileDataViewModel.class);
        entryList=profileDataViewModel.getEntries().getValue();
        profileDataViewModel.getEntries().observe(getActivity(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                entryList=entries;
                adapter=new EntriesViewAdapter(entryList,getActivity());
                binding.profileTestsRecyclerView.setHasFixedSize(true);
                binding.profileTestsRecyclerView.setLayoutManager(manager);
                binding.profileTestsRecyclerView.setAdapter(adapter);
            }
        });
        manager=new LinearLayoutManager(getActivity());
        adapter=new EntriesViewAdapter(entryList,getActivity());
        binding.profileTestsRecyclerView.setHasFixedSize(true);
        binding.profileTestsRecyclerView.setLayoutManager(manager);
        binding.profileTestsRecyclerView.setAdapter(adapter);
        //getting user code
        userCode=profileDataViewModel.getUserCode().getValue();
        profileDataViewModel.getUserCode().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                userCode=integer;
            }
        });

        //handling the scroll-down event
        binding.profileTestsNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>=v.getChildAt(0).getHeight()-v.getHeight()){
                    //adding a null reference to the list to make adapter return progress bar
                    if(entryList.get(entryList.size()-1)==null){
                        //if the user is waiting for update already, return
                        return;
                    }
                    entryList.add(null);
                    int testsSize= entryList.size();
                    adapter.notifyItemInserted(testsSize-1);
                    profileDataViewModel.setEntries(entryList);
                    for(Entry entry : entryList){
                        if(entry==null){
                            Log.d(TAG, "onScrollChange: null element");
                        }
                    }
                    //load the entry items
                    postShowProfile(userCode,0,entryList.size()-1,ProfileActivity.NOT_FIRST_TIME);
                }
            }
        });

        return view;
    }
    private void postShowProfile(int userCode, int testStart, int entryStart, int flag) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode))
                .add("testStart", String.valueOf(testStart))
                .add("entryStart", String.valueOf(entryStart))
                .add("testCount","0")
                .add("entryCount","15")
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

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "onResponse: response body: " + jsonResponse);
                    //deleting the null item in the list
                    entryList.remove(entryList.size()-1);
                    adapter.notifyItemRemoved(entryList.size());
                    //parsing the json string
                    Gson gson = new Gson();
                    ProfileDataString profileDataString = gson.fromJson(jsonResponse, ProfileDataString.class);
                    String testString = profileDataString.getTests();
                    Entry[] newEntries = gson.fromJson(testString, Entry[].class);
                    //creating handler and preparing the looper to run the code block on the main thread
                    Handler handler=new Handler(Looper.getMainLooper());
                    handler.post(new UpdateEntriesViewModel(newEntries));
                }
            }
        });
    }
    private class UpdateEntriesViewModel implements Runnable {
        Entry[] newEntries;
        public UpdateEntriesViewModel(Entry[] newEntries) {
            this.newEntries = newEntries;
        }
        @Override
        public void run() {
            if(newEntries.length==0){
                //if there is not any more tests to display, warn the user
                Toast.makeText(getContext(), "mevcut tüm entryler görüntülendi", Toast.LENGTH_SHORT).show();
                return;
            }
            for(Entry entry : newEntries){
                entryList.add(entry);
                Log.d(TAG, "onResponse: ");
            }
            profileDataViewModel.setEntries(entryList);
        }
    }
}
