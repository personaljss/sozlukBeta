package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.entries.EntriesTabFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.tests.TestsTabFragment;

public class ProfileSectionsPagerAdapter extends FragmentStateAdapter {
    public ProfileSectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=new Fragment();
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
