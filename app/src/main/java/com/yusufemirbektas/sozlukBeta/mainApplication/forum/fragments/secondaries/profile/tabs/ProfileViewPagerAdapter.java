package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.tabs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.tabs.entries.EntriesTabFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.tabs.tests.TestsTabFragment;


public class ProfileViewPagerAdapter extends FragmentStateAdapter {
    public static final String IS_SELF_KEY="is it the user's profile?";
    private boolean isSelf=false;

    public ProfileViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        Bundle args=new Bundle();
        args.putBoolean(IS_SELF_KEY,isSelf);
        switch (position){
            case 0:
                fragment=new TestsTabFragment();
                break;
            case 1:
                fragment=new EntriesTabFragment();
                break;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
