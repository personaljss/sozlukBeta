package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.databinding.ActivityChatBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.ChatViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.ChatMessage;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.ChatRvAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder.ChatMessageViewHolder;
import com.yusufemirbektas.sozlukBeta.services.FbMessageService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SUCCESS_EXTRA="SUCCESS_EXTRA";
    public static final String INDEX_EXTRA="INDEx_EXTRA";
    private ActivityChatBinding binding;
    private ChatRvAdapter rvAdapter;
    private List<ChatMessage> messages;
    private boolean isUiSet=false;
    private ChatViewModel viewModel;
    private Intent inIntent;
    private int chatBuddyUserCode;
    private boolean isSuccess=false;
    private BroadcastReceiver newMessageReceiver;
    private BroadcastReceiver seenMessageReceiver;

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(newMessageReceiver,new IntentFilter(FbMessageService.NEW_MSG_BI));
        LocalBroadcastManager.getInstance(this).registerReceiver(seenMessageReceiver,new IntentFilter(FbMessageService.SEEN_MSG_BI));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(this).get(ChatViewModel.class);
        messages=viewModel.getMessages().getValue();

        FbMessageService.isChatOpened=true;

        newMessageReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String txt=intent.getStringExtra(FbMessageService.MSG_TEXT);
                String userCode=intent.getStringExtra(FbMessageService.MSG_USER_CODE);
                String chatDate=intent.getStringExtra(FbMessageService.MSG_DATE);
                ChatMessage chatMessage=new ChatMessage(messages.size(),chatBuddyUserCode,Integer.parseInt(userCode),txt);
                messages.add(chatMessage);
                viewModel.setChatMessages(messages);
                viewModel.seenReport();
            }
        };

        seenMessageReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //String
                String userCode=intent.getStringExtra(FbMessageService.MSG_USER_CODE);
                int chatDate=Integer.parseInt(intent.getStringExtra(FbMessageService.MSG_DATE));
                //messages from this date are seen
            }
        };

        inIntent=getIntent();
        chatBuddyUserCode=inIntent.getIntExtra(ContactsNotificationActivity.EXTRA_USER_CODE,-1);
        if(messages==null){
            viewModel.setUserCode(chatBuddyUserCode);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    viewModel.fetchChat(0, (int)LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                }
        }


        viewModel.getMessages().observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                if(chatMessages!=null){
                    messages=chatMessages;
                    isSuccess=true;
                    if(!isUiSet){
                        setUpUi();
                    }else{
                        rvAdapter.setMessages(messages);
                        rvAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FbMessageService.isChatOpened=false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(seenMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i =new Intent();
        i.putExtra(SUCCESS_EXTRA,isSuccess);
        //i.putExtra(INDEX_EXTRA,)
        setResult(Activity.RESULT_OK);
        isUiSet=false;
    }

    private void setUpUi(){
        rvAdapter=new ChatRvAdapter();
        rvAdapter.setContext(this);
        rvAdapter.setMessages(messages);
        rvAdapter.setChatBuddyCode(chatBuddyUserCode);
        binding.chatRecyclerView.setAdapter(rvAdapter);
        binding.chatRecyclerView.setHasFixedSize(true);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setOnClickListeners();
        ((LinearLayoutManager)binding.chatRecyclerView.getLayoutManager()).setStackFromEnd(true);
        isUiSet=true;
    }

    private void setOnClickListeners() {
        binding.sendMessageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==binding.sendMessageButton){
            //send message if input is valid
            CharSequence txt=binding.chatEditText.getText();
            if(!TextUtils.isEmpty(txt)){
                binding.chatEditText.setText("");
                hideKeyboardFrom(this,v);
                int selfCode=Integer.parseInt(User.getInstance().getUserCode());
                int getterCode=chatBuddyUserCode;
                int sender=0;
                if(selfCode>getterCode){
                    sender=1;
                }
                int mId=messages.size();
                ChatMessage chatMessage=new ChatMessage(mId,selfCode,0,txt.toString());
                viewModel.sendChatMessage(getterCode,chatMessage);
            }
        }
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}