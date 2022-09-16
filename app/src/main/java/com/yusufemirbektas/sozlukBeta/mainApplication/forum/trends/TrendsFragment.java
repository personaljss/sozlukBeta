package com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.databinding.ForumTrendsFragmentBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.ForumSubject;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.TrendsResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrendsFragment extends Fragment {
    private static final String TAG = "TrendsFragment";
    private ForumTrendsFragmentBinding binding;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<ForumSubject> forumSubjects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=ForumTrendsFragmentBinding.inflate(getLayoutInflater());
        //binding.trendsProgressBar.setVisibility(View.GONE);
        forumSubjects=new ArrayList<>();
        adapter=new TrendsRvAdapter(getContext(),forumSubjects);
        manager=new LinearLayoutManager(getContext());
        binding.trendsRecyclerView.setHasFixedSize(true);
        binding.trendsRecyclerView.setAdapter(adapter);
        binding.trendsRecyclerView.setLayoutManager(manager);
        showTrends();
        return binding.getRoot();
    }
    //{"SubjectID":179,"totalPoint":401,"subjectName":"salağın hamlesinin yok olması","channel":2,"totalComments":18,"comment24ID":1}
    private void showTrends(){
        Log.d(TAG, "showTrends: ");
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("channels", "0")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_CHANNEL_SUBJECTS_PHP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String responseString=response.body().string();
                    Gson gson=new Gson();
                    TrendsResponse trendsResponse=gson.fromJson(responseString,TrendsResponse.class);
                    Type forumSubjectListType=TypeToken.getParameterized(ArrayList.class,ForumSubject.class).getType();
                    forumSubjects=gson.fromJson(trendsResponse.getData(),forumSubjectListType);
                    Handler mainHandler=new Handler(Looper.getMainLooper());
                    mainHandler.post(new ShowTrends(forumSubjects));
                }
            }
        });
    }

    private class ShowTrends implements Runnable{
        private List<ForumSubject> forumSubjects;
        public ShowTrends(List<ForumSubject> forumSubjects) {
            this.forumSubjects = forumSubjects;
        }

        @Override
        public void run() {
            binding.trendsProgressBar.setVisibility(View.GONE);
            adapter=new TrendsRvAdapter(getContext(),forumSubjects);
            manager=new LinearLayoutManager(getContext());
            binding.trendsRecyclerView.setHasFixedSize(true);
            binding.trendsRecyclerView.setAdapter(adapter);
            binding.trendsRecyclerView.setLayoutManager(manager);
        }
    }
}
