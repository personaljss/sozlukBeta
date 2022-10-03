package com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders.ProgressBarViewHolder;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.dataModel.SubjectEntryModel;

import java.util.List;

public class SubjectEntriesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SubjectEntryModel> entries;
    String subjectName;
    Context context;


    private static final int LOADING = 0;
    private static final int DONE = 1;

    public SubjectEntriesRvAdapter(List<SubjectEntryModel> entries, String subjectName, Context context) {
        this.entries = entries;
        this.subjectName = subjectName;
        this.context = context;
    }

    private class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView entryContent;
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            entryContent = itemView.findViewById(R.id.entryContentText);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder=null;
        if(viewType==DONE){
            view = LayoutInflater.from(context).inflate(R.layout.item_subject_entry, parent, false);
            viewHolder = new EntryViewHolder(view);
        }else{
            view=LayoutInflater.from(context).inflate(R.layout.item_recycler_view_progress_bar,parent,false);
            viewHolder=new ProgressBarViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==DONE){
            SubjectEntryModel entryModel = entries.get(position);
            ((EntryViewHolder) holder).entryContent.setText(entryModel.getComment());
        }else{
            //loading
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (entries.get(position) == null) ? LOADING : DONE;
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
