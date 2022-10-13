package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class EntryTitleViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView pointsTextView;
    public EntryTitleViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.trends_item_title);
        pointsTextView = itemView.findViewById(R.id.trends_item_points);
    }
}

