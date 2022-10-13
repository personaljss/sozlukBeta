package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class EntryViewHolder extends RecyclerView.ViewHolder{
    public TextView subjectTextView;
    public TextView contentTextView;
    public TextView likesTextView;
    public TextView dateTextView;
    public TextView nickNameTextView;
    public ImageButton likeButton;
    public ImageButton dislikeButton;

    public EntryViewHolder(@NonNull View itemView) {
        super(itemView);
        subjectTextView=itemView.findViewById(R.id.entryHeaderText);
        contentTextView=itemView.findViewById(R.id.entryContentText);
        likesTextView=itemView.findViewById(R.id.subjectEntryLikeCount);
        dateTextView=itemView.findViewById(R.id.dateTextView);
        nickNameTextView=itemView.findViewById(R.id.nickNameTextView);
        likeButton=itemView.findViewById(R.id.likeButton);
        dislikeButton=itemView.findViewById(R.id.dislikeButton);
    }
}
