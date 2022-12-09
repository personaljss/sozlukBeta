package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yusufemirbektas.sozlukBeta.data.SharedPrefs;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityContactsNotificationsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.fragments.communication.ChatViewPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder.ContactViewHolder;
import com.yusufemirbektas.sozlukBeta.services.FbMessageService;

public class ContactsNotificationActivity extends AppCompatActivity implements ContactViewHolder.OnContactEventListener {
    public static final String EXTRA_USER_CODE="EXTRA_USER_CODE";
    private static final String TAG = "ChatActivity";
    private ActivityContactsNotificationsBinding binding;
    private ChatViewPagerAdapter viewPagerAdapter;
    private final String[] TAB_TITLES={"sohbet","bildirimler"};
    private ActivityResultLauncher<Intent> launcher;
    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void onNavigateToChat(int userCode) {
        /*
        Intent intent=new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_USER_CODE,userCode);
        startActivity(intent);
        */
        Intent i=new Intent(this,ChatActivity.class);
        i.putExtra(EXTRA_USER_CODE,userCode);
        launcher.launch(i);
    }


    public interface OnStartTappedListener{
        void onStartTapped();
    }
    public interface EventListener{
        void onStartTapped();
        void onUnseenMessageSeen(int index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //reinitialising shared prefs
        SharedPrefs.write(FbMessageService.SP_MESSAGE_KEY,0);
        binding= ActivityContactsNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewPagerAdapter=new ChatViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        binding.chatViewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(binding.chatTabs, binding.chatViewPager, true,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TAB_TITLES[position]);
            }
        }).attach();
        
        binding.chatTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                eventListener.onStartTapped();
            }
        });

        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.i(TAG, "onActivityResult: ");
                if(result.getResultCode()== Activity.RESULT_OK){
                    Log.i(TAG, "onActivityResult: OK");
                    Intent intent=result.getData();
                    if(intent.getBooleanExtra(ChatActivity.SUCCESS_EXTRA,false)){
                        Log.i(TAG, "onActivityResult: DATA CAME");
                    }
                }
            }
        });

    }


}