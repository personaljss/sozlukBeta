package com.yusufemirbektas.sozlukBeta.mainApplication.forum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityForumBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.ProfileFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.SubjectFragment;

import java.util.HashSet;


public class ForumActivity extends AppCompatActivity {
    private ActivityForumBinding binding;
    private FragmentManager fragmentManager;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager=getSupportFragmentManager();
        NavHostFragment navHostFragment=(NavHostFragment) fragmentManager.findFragmentById(R.id.forum_fragment_container);
        navController=navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.forumBottomNav,navController);

    }


    public static class BundleKeys {
        public static final String SUBJECT_NAME = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.subjectNameKey";
        public static final String SUBJECT_ID = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.subjectId";
        public static final String COMMENT_ID = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.commentId";
        public static final String USERCODE = "com.yusufemirbektas.sozlukBeta.mainApplication.forum.userCode";
    }



}