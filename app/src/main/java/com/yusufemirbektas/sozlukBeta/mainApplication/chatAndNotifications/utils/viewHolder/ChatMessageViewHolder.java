package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView receivedMessageTextView;
    public TextView sentMessageTextView;
    public TextView dateTextView;
    public ImageView seenImageView;
    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        receivedMessageTextView=itemView.findViewById(R.id.receivedMessageTextView);
        sentMessageTextView=itemView.findViewById(R.id.sentMessageTextView);
        dateTextView=itemView.findViewById(R.id.chatMessageDateTextView);
        seenImageView=itemView.findViewById(R.id.seenImageView);
    }
}
