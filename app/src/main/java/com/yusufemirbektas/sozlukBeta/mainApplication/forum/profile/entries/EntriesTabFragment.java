package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.entries;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileEntriesBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;

import java.util.List;

public class EntriesTabFragment extends Fragment {
    private FragmentProfileEntriesBinding binding;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private ProfileDataViewModel viewModel;
    private List<Entry> entryList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentProfileEntriesBinding.inflate(inflater,container,false);
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
                entryList=entries;
                setUpUi();
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
                        viewModel.loadTests(entryList.size()-1,10);
                    }
                }
            }
        });

    }

    private void setUpUi(){
        adapter=new EntriesRvAdapter(getContext(),entryList);
        manager=new LinearLayoutManager(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        manager=null;
        adapter=null;
        binding=null;
    }
}
