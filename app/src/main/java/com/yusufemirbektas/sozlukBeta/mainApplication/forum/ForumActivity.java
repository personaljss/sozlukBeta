package com.yusufemirbektas.sozlukBeta.mainApplication.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityForumBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends.TrendsFragment;

public class ForumActivity extends AppCompatActivity {
    ActivityForumBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.forum_fragment_container,new TrendsFragment()).commit();
        binding.forumBottomNav.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.trends_bottom_nav:
                        fragment=new TrendsFragment();
                        break;
                    default:
                        fragment=new Fragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.forum_fragment_container,fragment).commit();
            }
        });
    }
}