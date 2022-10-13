package com.yusufemirbektas.sozlukBeta.mainApplication.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
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
    EntryManager entryManager;

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
                navController.popBackStack(targetDestinationId, false);
                navController.navigate(targetDestinationId);
            }
        });
        NavigationUI.setupWithNavController(binding.forumBottomNav, navController);
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


    public static class BundleKeys {
        public static final String SUBJECT_NAME = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.subjectNameKey";
        public static final String SUBJECT_ID = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.subjectId";
        public static final String COMMENT_ID = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.commentId";
        public static final String USERCODE = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.userCode";
        public static final String LIKE_STATUS="like status";
        public static final String ADAPTER_POSITION="adapterPos";
    }


}