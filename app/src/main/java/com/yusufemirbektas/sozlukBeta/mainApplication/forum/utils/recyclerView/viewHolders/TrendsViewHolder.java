package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class TrendsViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTextView;
    public TextView pointsTextView;
    public TrendsViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.trends_item_title);
        pointsTextView = itemView.findViewById(R.id.trends_item_points);
    }
}

