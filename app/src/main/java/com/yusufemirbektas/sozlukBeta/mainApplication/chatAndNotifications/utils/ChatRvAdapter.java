package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.ChatMessage;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder.ChatMessageViewHolder;

import java.util.List;

public class ChatRvAdapter extends RecyclerView.Adapter {
    public static final int DONE=1;
    public static final int LOADING=0;
    private Context context;
    private List<ChatMessage> messages;
    private int chatBuddyCode;

    public int getChatBuddyCode() {
        return chatBuddyCode;
    }

    public void setChatBuddyCode(int chatBuddyCode) {
        this.chatBuddyCode = chatBuddyCode;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        View view;
        if(viewType==DONE){
            view= LayoutInflater.from(context).inflate(R.layout.chat_message_item,parent,false);
            holder=new ChatMessageViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==DONE){
            ChatMessage chatMessage=messages.get(position);
            ((ChatMessageViewHolder) holder).dateTextView.setText(chatMessage.stringDate("dd/MM/yyyy HH:mm"));
            int selfCode= Integer.parseInt(User.getInstance().getUserCode());
            if(chatMessage.getSender()==0){
                //if sender is 0 küçük olan mesajı yolladı
                if(selfCode>chatBuddyCode){
                    ((ChatMessageViewHolder) holder).receivedMessageTextView.setText(chatMessage.getText());
                }else{
                    ((ChatMessageViewHolder) holder).sentMessageTextView.setText(chatMessage.getText());
                }
            }else {
                if(selfCode<chatBuddyCode){
                    ((ChatMessageViewHolder) holder).receivedMessageTextView.setText(chatMessage.getText());
                }else{
                    ((ChatMessageViewHolder) holder).sentMessageTextView.setText(chatMessage.getText());
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (messages.get(position)==null)?LOADING:DONE;
    }
}
