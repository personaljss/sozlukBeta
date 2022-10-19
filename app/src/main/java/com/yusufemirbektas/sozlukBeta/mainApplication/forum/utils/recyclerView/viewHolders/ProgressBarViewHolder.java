package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public ProgressBarViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.recycler_view_progress_bar);
    }
}
