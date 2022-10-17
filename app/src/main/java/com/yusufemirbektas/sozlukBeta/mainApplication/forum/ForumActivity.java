package com.yusufemirbektas.sozlukBeta.mainApplication.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityForumBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.entryUtils.EntryEventListener;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.entryUtils.EntryManager;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.entryUtils.LikeDialog;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;


public class ForumActivity extends AppCompatActivity implements EntryEventListener {
    private static final String TAG = "ForumActivity";
    public static final String targetDatePattern = "dd/MM/yyyy HH:mm";
    private NavController navController;
    private PointsViewModel pointsViewModel;
    private EntryManager entryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.yusufemirbektas.sozlukBeta.databinding.ActivityForumBinding binding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProfileDataViewModel profileDataViewModel = new ViewModelProvider(this).get(ProfileDataViewModel.class);
        profileDataViewModel.setUserCode(UserData.getUserCode());
        profileDataViewModel.loadProfileData();

        pointsViewModel=new ViewModelProvider(this).get(PointsViewModel.class);
        profileDataViewModel.getHeader().observe(this, new Observer<Header>() {
            @Override
            public void onChanged(Header header) {
                if(header!=null){
                    pointsViewModel.setPointsAvailable(header.getTotalPoints());
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.forum_fragment_container);
        navController = navHostFragment.getNavController();

        entryManager=new EntryManager(navController);


        binding.forumBottomNav.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int targetDestinationId = item.getItemId();
                navController.popBackStack(targetDestinationId, true);
                navController.navigate(targetDestinationId);
            }
        });
        NavigationUI.setupWithNavController(binding.forumBottomNav, navController);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: "+navController.getCurrentBackStackEntry().getDestination().getDisplayName());
        if(navController.getCurrentBackStackEntry()!=null){
            int destId=navController.getCurrentBackStackEntry().getDestination().getId();
            if(destId!=R.id.trendsFragment && destId!=R.id.profileFragment && destId!=R.id.newSubjectFragment){
                navController.popBackStack();
            }else{
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entryManager=null;
    }

    //Entry events
    @Override
    public void onLiked(int userCode, int subjectId,int commentId, int likeStatus, int adapterPosition) {
        Bundle args=new Bundle();
        args.putInt(BundleKeys.USERCODE,userCode);
        args.putInt(BundleKeys.SUBJECT_ID,subjectId);
        args.putInt(BundleKeys.COMMENT_ID,commentId);
        args.putInt(BundleKeys.LIKE_STATUS,likeStatus);
        args.putInt(BundleKeys.ADAPTER_POSITION,adapterPosition);
        LikeDialog likeDialog=new LikeDialog();
        likeDialog.setArguments(args);
        likeDialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onSubjectClicked(int subjectId, int commentId, String subjectName) {
        entryManager.goToSubject(subjectId,commentId,subjectName);
    }

    @Override
    public void onProfileClicked(int userCode) {
        entryManager.goToProfile(userCode);
    }

}