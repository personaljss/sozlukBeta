package com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentSubjectEntriesBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.SubjectEntriesResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.dataModel.SubjectEntryModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.viewModel.SubjectEntriesViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SubjectFragment extends Fragment {
    private static final String TAG = "SubjectFragment";
    private FragmentSubjectEntriesBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recycleViewAdapter;
    private List<SubjectEntryModel> entryModels;
    private SubjectEntriesViewModel viewModel;
    private Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entryModels = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectEntriesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SubjectEntriesViewModel.class);
        bundle = getArguments();
        //checking if this page has been loaded previously
        if (savedInstanceState == null) {
            binding.subjectEntriesRecyclerView.setVisibility(View.GONE);
            binding.subjectTextView.setVisibility(View.GONE);
            loadSubjectEntries(bundle.getInt(ForumActivity.BundleKeys.SUBJECT_ID, 1),
                    bundle.getInt(ForumActivity.BundleKeys.COMMENT_ID, 1),
                    UserData.getUserCode());
        } else {
            entryModels = viewModel.getSubjectEntries().getValue();
            setUpUi();
        }


        binding.subjectNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getHeight() - v.getHeight()) {
                    //adding a null reference to the list to make adapter return progress bar
                    if (entryModels.get(entryModels.size() - 1) == null) {
                        //if the user is waiting for update already, return
                        return;
                    }
                    entryModels.add(null);
                    int testsSize = entryModels.size();
                    recycleViewAdapter.notifyItemInserted(testsSize - 1);
                    //load the entry items
                    loadSubjectEntries(bundle.getInt(ForumActivity.BundleKeys.SUBJECT_ID, 1),
                            bundle.getInt(ForumActivity.BundleKeys.COMMENT_ID, entryModels.size()),
                            UserData.getUserCode());
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        recycleViewAdapter = null;
        layoutManager = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setUpUi() {
        layoutManager = new LinearLayoutManager(getContext());
        recycleViewAdapter = new SubjectEntriesRvAdapter(entryModels, bundle.getString(ForumActivity.BundleKeys.SUBJECT_NAME), getContext());
        binding.subjectEntriesRecyclerView.setLayoutManager(layoutManager);
        binding.subjectEntriesRecyclerView.setAdapter(recycleViewAdapter);
        binding.subjectEntriesRecyclerView.setHasFixedSize(true);
        binding.progressBar.setVisibility(View.GONE);
        binding.subjectEntriesRecyclerView.setVisibility(View.VISIBLE);
        binding.subjectTextView.setText(bundle.getString(ForumActivity.BundleKeys.SUBJECT_NAME));
        binding.subjectTextView.setVisibility(View.VISIBLE);
    }

    //subjectID, commentID, userCode
    private void loadSubjectEntries(int subjectId, int commentId, int userCode) {
        Log.d(TAG, "showsubjects: ");

        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("subjectID", String.valueOf(subjectId))
                .add("commentID", String.valueOf(commentId))
                .add("userCode", String.valueOf(userCode))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_SUBJECT_ENTRIES_PHP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    SubjectEntriesResponse entriesResponse = gson.fromJson(jsonResponse, SubjectEntriesResponse.class);
                    SubjectEntryModel[] entryArray = gson.fromJson(entriesResponse.getData(), SubjectEntryModel[].class);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new ShowSubjectEntries(entryArray, bundle.getString(ForumActivity.BundleKeys.SUBJECT_NAME, "")));
                }
            }
        });
    }

    private class ShowSubjectEntries implements Runnable {
        SubjectEntryModel[] entryModelsArray;
        String subjectName;

        public ShowSubjectEntries(SubjectEntryModel[] entryModelsArray, String subjectName) {
            this.entryModelsArray = entryModelsArray;
            this.subjectName = subjectName;
        }

        @Override
        public void run() {
            if (entryModels.size() > 0) {
                if (entryModels.get(entryModels.size() - 1) == null) {
                    entryModels.remove(entryModels.size() - 1);
                }
            }
            entryModels.addAll(Arrays.asList(entryModelsArray));
            viewModel.postSubjectEntries(entryModels);
            setUpUi();
        }
    }

}
