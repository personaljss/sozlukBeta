package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.tabs.tests;

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


import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileTestsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.EntriesItemDecoration;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ProfileDataViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters.TestsRvAdapter;

import java.util.List;


public class TestsTabFragment extends Fragment {
    private static final String TAG = "TestsTabFragment";
    private static final int VERTICAL_ITEM_SPACE = 15;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private FragmentProfileTestsBinding binding;
    private ProfileDataViewModel viewModel;
    private List<Test> testList;
    private boolean isUiSet=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentProfileTestsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        Bundle args=getArguments();
        if(!args.getBoolean(ProfileViewPagerAdapter.IS_SELF_KEY)){
            viewModel=new ViewModelProvider(getParentFragment()).get(ProfileDataViewModel.class);
        }else{
            viewModel=new ViewModelProvider(getActivity()).get(ProfileDataViewModel.class);
        }
         */
        viewModel=new ViewModelProvider(getParentFragment()).get(ProfileDataViewModel.class);
        testList=viewModel.getTests().getValue();

        //setUpUi();
        //observe the tests change
        viewModel.getTests().observe(getViewLifecycleOwner(), new Observer<List<Test>>() {
            @Override
            public void onChanged(List<Test> tests) {
                if(tests!=null){
                    testList=tests;
                    if(isUiSet){
                        ((TestsRvAdapter)adapter).setTests(tests);
                        adapter.notifyDataSetChanged();
                    }else {
                        setUpUi();
                    }
                }
            }
        });

        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getHeight() - v.getHeight()) {
                    if(testList.get(testList.size()-1)==null){
                        return;
                    }else {
                        testList.add(null);
                        viewModel.loadTests(testList.size()-1,10);
                    }
                }
            }
        });
    }

    private void setUpUi(){
        adapter=new TestsRvAdapter(getContext(),testList);
        manager=new LinearLayoutManager(getContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.addItemDecoration(new EntriesItemDecoration(VERTICAL_ITEM_SPACE));
        isUiSet=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUiSet=false;
        binding=null;
        adapter=null;
        manager=null;
        viewModel=null;
        testList=null;
    }



}
