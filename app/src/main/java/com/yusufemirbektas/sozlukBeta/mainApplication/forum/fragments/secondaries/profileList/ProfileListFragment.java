package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profileList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileListBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.ProfileItem;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters.ProfilesRvAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ProfileListViewModel;

import java.util.List;


public class ProfileListFragment extends Fragment {
    public static final int FOLLOWERS_CODE=0;
    public static final int FOLLOWED_BYs_CODE=1;
    public static final int LIKED_ONEs_CODE=2;
    private FragmentProfileListBinding binding;
    private ProfileListViewModel viewModel;
    private List<ProfileItem> profileItemList;
    private boolean isUiSet=false;
    private RecyclerView.Adapter recyclerViewAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileListBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(this).get(ProfileListViewModel.class);
        profileItemList=viewModel.getUsers().getValue();
        Bundle args=getArguments();
        final int aim=args.getInt(BundleKeys.PROFILE_LIST_KEY);

        if(profileItemList==null){
            loadUsers(aim,args);
        }else {
            setUpUi();
        }
        viewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<ProfileItem>>() {
            @Override
            public void onChanged(List<ProfileItem> profileItems) {
                binding.swipeRefreshLayout.setRefreshing(false);
                profileItemList=profileItems;
                if(isUiSet){
                    ((ProfilesRvAdapter) recyclerViewAdapter).setProfileItems(profileItems);
                    recyclerViewAdapter.notifyDataSetChanged();
                }else{
                    setUpUi();
                }
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUsers(aim,args);
            }
        });
    }

    private void setUpUi(){
        recyclerViewAdapter=new ProfilesRvAdapter(profileItemList,getContext());
        binding.recyclerView.setAdapter(recyclerViewAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.progressBar.setVisibility(View.GONE);
        isUiSet=true;
    }

    private void loadUsers(int aim, Bundle args){
        switch (aim){
            case FOLLOWERS_CODE:
                viewModel.loadFollowers(args.getInt(BundleKeys.USERCODE));
                break;
            case FOLLOWED_BYs_CODE:
                viewModel.loadFollowedBys(args.getInt(BundleKeys.USERCODE));
                break;
            case LIKED_ONEs_CODE:
                viewModel.loadLikeDetails(args.getInt(BundleKeys.SUBJECT_ID), args.getInt(BundleKeys.COMMENT_ID));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUiSet=false;
    }
}