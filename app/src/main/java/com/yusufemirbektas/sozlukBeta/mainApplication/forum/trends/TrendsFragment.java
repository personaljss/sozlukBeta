package com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentForumTrendsBinding;
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

public class TrendsFragment extends Fragment{
    private static final String TAG = "TrendsFragment";
    private FragmentForumTrendsBinding binding;
    private TrendsRvAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<ForumSubject> forumSubjects;
    private boolean isUiSet=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentForumTrendsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TrendsViewModel viewModel = new ViewModelProvider(this).get(TrendsViewModel.class);

        viewModel.getForumSubjects().observe(getViewLifecycleOwner(), new Observer<List<ForumSubject>>() {
            @Override
            public void onChanged(List<ForumSubject> forumSubjects) {
                TrendsFragment.this.forumSubjects=forumSubjects;
                if(isUiSet){
                    adapter.notifyDataSetChanged();
                }else{
                    setUpUi();
                }
            }
        });

        if(forumSubjects==null){
            viewModel.loadTrends("0");
        }else{
            setUpUi();
        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NavController navController= Navigation.findNavController(view);
                navController.navigate(R.id.action_trendsFragment_self);
            }
        });

    }

    private void setUpUi() {
        //making the progress bar gone
        binding.trendsProgressBar.setVisibility(View.INVISIBLE);
        //setting the recycler view
        adapter=new TrendsRvAdapter(getContext(),forumSubjects);
        manager=new LinearLayoutManager(getContext());
        binding.trendsRecyclerView.setHasFixedSize(true);
        binding.trendsRecyclerView.setAdapter(adapter);
        binding.trendsRecyclerView.setLayoutManager(manager);
        isUiSet=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter=null;
        manager=null;
        binding=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
