package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView date;
    public TextView unseenMessages;
    public TextView text;
    public CircleImageView ppImage;

    public interface OnContactEventListener{
        void onNavigateToChat(int userCode);
    }

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        userName=itemView.findViewById(R.id.contactNameTextView);
        date=itemView.findViewById(R.id.contactDateTextView);
        unseenMessages=itemView.findViewById(R.id.unseenMessagesTextView);
        text=itemView.findViewById(R.id.contactMessageTextView);
        ppImage=itemView.findViewById(R.id.contactPp);
    }
}
