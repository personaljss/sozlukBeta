package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.entries;

import android.content.Context;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders.EntryViewHolder;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders.ProgressBarViewHolder;

import java.util.List;

public class EntriesRvAdapter extends RecyclerView.Adapter {
    Context context;
    List<Entry> entries;

    private static final int LOADING=0;
    private static final int DONE=1;

    public EntriesRvAdapter(Context context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        View view=null;
        if(viewType==DONE){
            view= LayoutInflater.from(context).inflate(R.layout.item_entry,parent,false);
            return new EntryViewHolder(view);
        }else {
            view= LayoutInflater.from(context).inflate(R.layout.item_recycler_view_progress_bar,parent,false);
            return new ProgressBarViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType()==DONE){
            Entry currentEntry= entries.get(position);
            ((EntryViewHolder) holder).dateTextView.setText(currentEntry.getDate());
            ((EntryViewHolder) holder).subjectTextView.setText(currentEntry.getSubjectName());
            ((EntryViewHolder) holder).likesTextView.setText(String.valueOf(currentEntry.getLikePoint()));
            ((EntryViewHolder) holder).contentTextView.setText(currentEntry.getComment());
        }else{
            ((ProgressBarViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (entries.get(position)==null)?LOADING:DONE;
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
