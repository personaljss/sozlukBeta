package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.entries.EntriesTabFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.tests.TestsTabFragment;


public class ProfileViewPagerAdapter extends FragmentStateAdapter {
    public ProfileViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment=new TestsTabFragment();
                break;
            case 1:
                fragment=new EntriesTabFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
