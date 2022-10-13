package com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.ForumSubject;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders.EntryTitleViewHolder;

import java.util.List;

public class TrendsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<ForumSubject> forumSubjects;


    public TrendsRvAdapter(Context context, List<ForumSubject> forumSubjects) {
        this.context = context;
        this.forumSubjects = forumSubjects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_trends,parent,false);
        EntryTitleViewHolder holder=new EntryTitleViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumSubject forumSubject=forumSubjects.get(holder.getAdapterPosition());
                NavController navController=Navigation.findNavController(holder.itemView);
                Bundle args=new Bundle();
                args.putString(ForumActivity.BundleKeys.SUBJECT_NAME,forumSubject.getSubjectName());
                args.putInt(ForumActivity.BundleKeys.SUBJECT_ID,forumSubject.getSubjectID());
                args.putInt(ForumActivity.BundleKeys.COMMENT_ID,1);
                navController.navigate(R.id.action_trendsFragment_to_forum_subject,args);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ForumSubject forumSubject = forumSubjects.get(position);
        ((EntryTitleViewHolder) holder).pointsTextView.setText(String.valueOf(forumSubject.getTotalPoint()));
        ((EntryTitleViewHolder) holder).titleTextView.setText(String.valueOf(forumSubject.getSubjectName()));
        /*
        ((EntryTitleViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController=Navigation.findNavController(holder.itemView);
                Bundle args=new Bundle();
                args.putString(ForumActivity.BundleKeys.SUBJECT_NAME,forumSubject.getSubjectName());
                args.putInt(ForumActivity.BundleKeys.SUBJECT_ID,forumSubject.getSubjectID());
                args.putInt(ForumActivity.BundleKeys.COMMENT_ID,1);
                navController.navigate(R.id.action_trendsFragment_to_forum_subject,args);
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return forumSubjects.size();
    }

}
