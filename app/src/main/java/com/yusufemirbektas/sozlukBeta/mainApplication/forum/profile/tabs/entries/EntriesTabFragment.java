package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.tabs.entries;



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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileEntriesBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.EntriesRvAdapter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntriesTabFragment extends Fragment  {
    private FragmentProfileEntriesBinding binding;
    private EntriesRvAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProfileDataViewModel viewModel;
    private List<Entry> entryList;
    private boolean isUiSet=false;
    private boolean isSelf=false;
    private NavController navController;
    PointsViewModel pointsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentProfileEntriesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(getParentFragment()).get(ProfileDataViewModel.class);
        entryList=viewModel.getEntries().getValue();

        viewModel.getEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                if(entries!=null){
                    entryList=entries;
                    if(isUiSet){
                        ((EntriesRvAdapter) adapter).setEntries(entries);
                        adapter.notifyDataSetChanged();
                    }else{
                        setUpUi();
                    }
                }
            }
        });
        //limit=15&date=1666181490&userCode=100
        //limit=15&date=1666181514&userCode=100

        pointsViewModel=new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        pointsViewModel.getEntryItemPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=PointsViewModel.DEFAULT_POS ){
                    Entry updatedEntry=entryList.get(integer);
                    updatedEntry.setLikeStatus(updatedEntry.getLikeStatus()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    updatedEntry.setLikePoint(updatedEntry.getLikePoint()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    entryList.set(integer,updatedEntry);
                    adapter.notifyItemChanged(integer);
                    viewModel.setEntries(entryList);
                }
            }
        });


        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getHeight() - v.getHeight()) {
                    if(entryList.get(entryList.size()-1)==null){
                        return;
                    }else {
                        entryList.add(null);
                        String dateString=entryList.get(entryList.size()-2).getDate();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter=DateTimeFormatter.ofPattern(ForumActivity.targetDatePattern);
                            LocalDateTime localDate=LocalDateTime.parse(dateString,formatter);
                            int entryStartDate= (int) localDate.toEpochSecond(ZoneOffset.UTC);
                            viewModel.loadEntries(entryStartDate,entryList.size()-1);
                        }else{
                            //do something
                        }
                    }
                }
            }
        });

    }

    private void setUpUi(){
        adapter=new EntriesRvAdapter(entryList, getContext());
        manager=new LinearLayoutManager(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pointsViewModel.refresh();
        isUiSet=false;
        manager=null;
        adapter=null;
        binding=null;
        viewModel=null;
        entryList=null;
    }
}
