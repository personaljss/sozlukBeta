package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.fragments.communication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.fragments.ContactsFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.fragments.NotificationsFragment;

public class ChatViewPagerAdapter extends FragmentStateAdapter {

    public ChatViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment=new ContactsFragment();
                break;
            case 1:
                fragment=new NotificationsFragment();
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
