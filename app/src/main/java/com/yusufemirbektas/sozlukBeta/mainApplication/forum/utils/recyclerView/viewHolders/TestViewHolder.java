package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class TestViewHolder extends RecyclerView.ViewHolder {
    public TextView dateTextView;
    public TextView courseTextView;
    public TextView subjectTextView;
    public TextView correctsTextView;
    public TextView wrongsTextView;

    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.date_textView);
        courseTextView = itemView.findViewById(R.id.course_TextView);
        subjectTextView = itemView.findViewById(R.id.subject_textView);
        correctsTextView = itemView.findViewById(R.id.corrects_textView);
        wrongsTextView = itemView.findViewById(R.id.wrongs_textView);
    }
}