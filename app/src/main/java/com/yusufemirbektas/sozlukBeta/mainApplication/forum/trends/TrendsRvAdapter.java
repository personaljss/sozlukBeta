package com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.ForumSubject;

import java.util.List;

public class TrendsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ForumSubject> forumSubjects;

    public TrendsRvAdapter(Context context, List<ForumSubject> forumSubjects) {
        this.context = context;
        this.forumSubjects = forumSubjects;
    }

    private static class EntryTitleViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        TextView pointsTextView;
        public EntryTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.trends_item_title);
            pointsTextView=itemView.findViewById(R.id.trends_item_points);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.trends_item,parent,false);
        return new EntryTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ForumSubject forumSubject = forumSubjects.get(position);
        ((EntryTitleViewHolder) holder).pointsTextView.setText(String.valueOf(forumSubject.getTotalPoint()));
        ((EntryTitleViewHolder) holder).titleTextView.setText(String.valueOf(forumSubject.getSubjectName()));
    }

    @Override
    public int getItemCount() {
        return forumSubjects.size();
    }
}
