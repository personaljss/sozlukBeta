package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.subject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentSubjectEntriesBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters.EntriesRvAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.EntriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SubjectFragment";
    private FragmentSubjectEntriesBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recycleViewAdapter;
    private List<Entry> entryModels;
    private EntriesViewModel viewModel;
    private Bundle bundle;
    private NavController navController;
    private PointsViewModel pointsViewModel;
    private boolean isUiSet=false;

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
        viewModel = new ViewModelProvider(this).get(EntriesViewModel.class);

        binding.swipeRefreshLayout.setRefreshing(false);

        bundle = getArguments();
        navController= Navigation.findNavController(view);

        //checking if this page has been loaded previously
        if (viewModel.getSubjectId()==-1) {
            binding.subjectEntriesRecyclerView.setVisibility(View.GONE);
            binding.subjectTextView.setVisibility(View.GONE);
            viewModel.setSubjectId(bundle.getInt(BundleKeys.SUBJECT_ID, -1));
            viewModel.setCommentId(bundle.getInt(BundleKeys.COMMENT_ID, -1));
            viewModel.loadSubjectEntries();

        } else {
            entryModels = viewModel.getEntries().getValue();
            setUpUi();
        }

        viewModel.getEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> subjectEntryModels) {
                entryModels=subjectEntryModels;
                if(isUiSet){
                    ((EntriesRvAdapter) recycleViewAdapter).setEntries(subjectEntryModels);
                    recycleViewAdapter.notifyDataSetChanged();
                }else {
                    setUpUi();
                }
            }
        });

        pointsViewModel=new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        pointsViewModel.getEntryItemPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=PointsViewModel.DEFAULT_POS && entryModels!=null && entryModels.size()>integer){
                    Entry updatedEntry=entryModels.get(integer);
                    updatedEntry.setLikeStatus(updatedEntry.getLikeStatus()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    updatedEntry.setLikePoint(updatedEntry.getLikePoint()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    entryModels.set(integer,updatedEntry);
                    recycleViewAdapter.notifyItemChanged(integer);
                }
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int actionId=navController.getCurrentDestination().getId();
                NavOptions navOptions=new NavOptions.Builder().setPopUpTo(actionId,true).build();
                navController.navigate(actionId,bundle,navOptions);
            }
        });


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
                    viewModel.setCommentId(entryModels.size()-1);
                    viewModel.loadSubjectEntries(entryModels.size()-1);
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUiSet=false;
        pointsViewModel.refresh();
        binding = null;
        recycleViewAdapter = null;
        layoutManager = null;
        bundle=null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setUpUi() {
        setUpRecyclerView();
        binding.progressBar.setVisibility(View.GONE);
        binding.subjectTextView.setText(bundle.getString(BundleKeys.SUBJECT_NAME));
        binding.subjectTextView.setVisibility(View.VISIBLE);
        binding.subjectFragmentNewEntry.setOnClickListener(this);
        binding.progressBar.setVisibility(View.GONE);
        isUiSet=true;
    }

    private void setUpRecyclerView(){
        layoutManager = new LinearLayoutManager(getContext());
        recycleViewAdapter = new EntriesRvAdapter(entryModels, getContext());
        binding.subjectEntriesRecyclerView.setLayoutManager(layoutManager);
        binding.subjectEntriesRecyclerView.setAdapter(recycleViewAdapter);
        binding.subjectEntriesRecyclerView.setHasFixedSize(true);
        binding.subjectEntriesRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if(v==binding.subjectFragmentNewEntry){
            Bundle outArgs=new Bundle();
            int subjectId=viewModel.getSubjectId();
            String subjectName=binding.subjectTextView.getText().toString();
            outArgs.putInt(BundleKeys.SUBJECT_ID, subjectId);
            outArgs.putString(BundleKeys.SUBJECT_NAME,subjectName);
            navController.navigate(R.id.action_subjectFragment_to_newEntryFragment,outArgs);
        }
    }

}
