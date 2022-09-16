package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.entries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;

import java.util.List;

public class EntriesViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Entry> entryList;
    private final Context context;
    private static final int LOADING = 0;
    private static final int DONE = 1;

    public EntriesViewAdapter(List<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    private static class EntryViewHolder extends RecyclerView.ViewHolder {
        TextView entryHeaderText;
        TextView entryContentText;
        TextView likeCount;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            entryHeaderText = itemView.findViewById(R.id.entryHeaderText);
            entryContentText = itemView.findViewById(R.id.entryContentText);
            likeCount = itemView.findViewById(R.id.likeCount);
        }
    }

    private static class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressBarViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.recycler_view_progress_bar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==LOADING){
            view= LayoutInflater.from(context).inflate(R.layout.recycler_view_progress_bar_item,parent,false);
            return new ProgressBarViewHolder(view);
        }else{
            view=LayoutInflater.from(context).inflate(R.layout.entry_item,parent,false);
            return new EntryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==DONE){
            Entry entry=entryList.get(position);
            ((EntryViewHolder)holder).likeCount.setText(String.valueOf(entry.getLikePoint()));
            ((EntryViewHolder)holder).entryContentText.setText(entry.getComment());
            ((EntryViewHolder)holder).entryHeaderText.setText(entry.getSubjectName());
        }else{
            //loading
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (entryList.get(position) == null) ? LOADING : DONE;
    }
}
