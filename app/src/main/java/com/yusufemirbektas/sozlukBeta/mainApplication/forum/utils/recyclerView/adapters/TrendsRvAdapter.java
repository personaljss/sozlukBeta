package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.ForumSubject;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders.TrendsViewHolder;

import java.util.List;

public class TrendsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private  List<ForumSubject> forumSubjects;

    public void setForumSubjects(List<ForumSubject> forumSubjects) {
        this.forumSubjects = forumSubjects;
    }

    public TrendsRvAdapter(Context context, List<ForumSubject> forumSubjects) {
        this.context = context;
        this.forumSubjects = forumSubjects;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_trends,parent,false);
        TrendsViewHolder holder=new TrendsViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumSubject forumSubject=forumSubjects.get(holder.getAdapterPosition());
                NavController navController=Navigation.findNavController(holder.itemView);
                Bundle args=new Bundle();
                args.putString(BundleKeys.SUBJECT_NAME,forumSubject.getSubjectName());
                args.putInt(BundleKeys.SUBJECT_ID,forumSubject.getSubjectID());
                args.putInt(BundleKeys.COMMENT_ID,1);
                navController.navigate(R.id.action_trendsFragment_to_forum_subject,args);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ForumSubject forumSubject = forumSubjects.get(position);
        ((TrendsViewHolder) holder).pointsTextView.setText(String.valueOf(forumSubject.getTotalPoint()));
        ((TrendsViewHolder) holder).titleTextView.setText(String.valueOf(forumSubject.getSubjectName()));
    }

    @Override
    public int getItemCount() {
        return forumSubjects.size();
    }

}
