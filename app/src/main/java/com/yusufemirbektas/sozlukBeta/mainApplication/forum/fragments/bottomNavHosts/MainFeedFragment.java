package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.bottomNavHosts;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity.targetDatePattern;

import android.os.Build;
import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusufemirbektas.sozlukBeta.databinding.FragmentMainFeedBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters.EntriesRvAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.EntriesViewModel;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFeedFragment extends Fragment {
    private FragmentMainFeedBinding binding;
    private EntriesViewModel viewModel;
    private RecyclerView.Adapter recyclerViewAdapter;
    private List<Entry> entryList;
    private PointsViewModel pointsViewModel;
    private NavController navController;
    private boolean isUiSet=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentMainFeedBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(this).get(EntriesViewModel.class);
        entryList=viewModel.getEntries().getValue();
        /*
        //first time check
        if(viewModel.getCommentId()==-1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                viewModel.loadMainFeed((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }else{
                //do something
            }
        }else {
            setUpUi();
        }
         */
        if(entryList==null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                viewModel.loadMainFeed((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
            }else{
                //do something
            }
        }

        viewModel.getEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                entryList=entries;
                if(isUiSet){
                    ((EntriesRvAdapter) recyclerViewAdapter).setEntries(entryList);
                    recyclerViewAdapter.notifyDataSetChanged();
                    binding.swipeRefreshLayout.setRefreshing(false);
                }else{
                    setUpUi();
                }
            }
        });

        pointsViewModel=new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        pointsViewModel.getEntryItemPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=PointsViewModel.DEFAULT_POS && entryList!=null ){
                    Entry updatedEntry=entryList.get(integer);
                    updatedEntry.setLikeStatus(updatedEntry.getLikeStatus()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    updatedEntry.setLikePoint(updatedEntry.getLikePoint()+pointsViewModel.getEntryItemLikeStatus().getValue());
                    entryList.set(integer,updatedEntry);
                    recyclerViewAdapter.notifyItemChanged(integer);
                }
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pointsViewModel.refresh();
                /*
                navController= Navigation.findNavController(view);
                int actionId=navController.getCurrentDestination().getId();
                NavOptions navOptions=new NavOptions.Builder().setPopUpTo(actionId,true).build();
                navController.navigate(actionId,null,navOptions);
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.loadMainFeed((int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                }else {
                    //!!!!!!!!!!!!
                }
            }
        });


        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getHeight() - v.getHeight()) {
                    //adding a null reference to the list to make adapter return progress bar
                    if (entryList.get(entryList.size() - 1) == null) {
                        //if the user is waiting for update already, return
                        return;
                    }
                    entryList.add(null);
                    int testsSize = entryList.size();
                    recyclerViewAdapter.notifyItemInserted(testsSize - 1);
                    //load the entry items
                    String startDateStr=entryList.get(testsSize-2).getDate();
                    int startDateEpoch=0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        DateTimeFormatter formatter=DateTimeFormatter.ofPattern(targetDatePattern);
                        LocalDateTime localDateTime=LocalDateTime.parse(startDateStr,formatter);
                        startDateEpoch=(int)localDateTime.toEpochSecond(ZoneOffset.UTC);
                    }else{
                        //do something else
                    }
                    viewModel.loadMainFeed(startDateEpoch);
                }
            }
        });

    }

    private void setUpUi() {
        recyclerViewAdapter=new EntriesRvAdapter(entryList,getContext());
        binding.entriesRecyclerView.setAdapter(recyclerViewAdapter);
        binding.entriesRecyclerView.setHasFixedSize(true);
        binding.entriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.progressBar.setVisibility(View.GONE);
        isUiSet=true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
        pointsViewModel.refresh();
        isUiSet=false;
        /*
        pointsViewModel=null;
        viewModel=null;
        recyclerViewAdapter=null;
        entryList=null;
        navController=null;
        isUiSet=false;

         */

    }
}