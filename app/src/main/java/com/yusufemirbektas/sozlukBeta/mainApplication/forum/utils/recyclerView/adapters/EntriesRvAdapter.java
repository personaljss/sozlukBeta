package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.EntryEventListener;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders.EntryViewHolder;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders.ProgressBarViewHolder;

import java.util.List;

public class EntriesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Entry> entries;
    private Context context;

    private static final int LOADING = 0;
    private static final int DONE = 1;

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public EntriesRvAdapter(List<Entry> entries, Context context) {
        this.entries = entries;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        final RecyclerView.ViewHolder viewHolder;
        if (viewType == DONE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_entry1_0, parent, false);
            viewHolder = new EntryViewHolder(view);
            //holder
            //nick
            ((EntryViewHolder) viewHolder).nickNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Entry entryModel = entries.get(viewHolder.getAdapterPosition());
                    ((EntryEventListener) context).onProfileClicked(entryModel.getUserCode());
                }
            });
            //subject name
            ((EntryViewHolder) viewHolder).subjectTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((EntryViewHolder) viewHolder).subjectTextView.getText().equals("")){
                        return;
                    }
                    Entry entryModel = entries.get(viewHolder.getAdapterPosition());
                    ((EntryEventListener) context).onSubjectClicked(entryModel.getSubjectID(), entryModel.getCommentID(), entryModel.getSubjectName());
                }
            });
            //like button
            ((EntryViewHolder) viewHolder).likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    Entry entryModel = entries.get(pos);
                    if (entryModel.getUserCode() == UserData.getUserCode()) {
                        Toast.makeText(context, "kendi paylaşımızı beğenemezsiniz", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((EntryEventListener) context).onLiked(entryModel.getUserCode(), entryModel.getSubjectID(), entryModel.getCommentID(), entryModel.getLikeStatus(), pos);
                }
            });
            //dislike button
            ((EntryViewHolder) viewHolder).dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    Entry entryModel = entries.get(pos);
                    if (entryModel.getUserCode() == UserData.getUserCode()) {
                        Toast.makeText(context, "kendi paylaşımızı beğenemezsiniz", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((EntryEventListener) context).onLiked(entryModel.getUserCode(), entryModel.getSubjectID(), entryModel.getCommentID(), entryModel.getLikeStatus(), pos);
                }
            });

            //likes TextView
            ((EntryViewHolder) viewHolder).likesTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getAdapterPosition();
                    Entry entryModel = entries.get(pos);
                    ((EntryEventListener) context).onLikeDetails(entryModel.getSubjectID(), entryModel.getCommentID());
                }
            });

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_progress_bar, parent, false);
            viewHolder = new ProgressBarViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == DONE) {
            Entry entryModel = entries.get(position);
            ((EntryViewHolder) holder).contentTextView.setText(entryModel.getComment());
            ((EntryViewHolder) holder).nickNameTextView.setText(entryModel.getNickName());
            ((EntryViewHolder) holder).dateTextView.setText(entryModel.getDate());
            ((EntryViewHolder) holder).likesTextView.setText(String.valueOf(entryModel.getLikePoint()));
            ((EntryViewHolder) holder).nickNameTextView.setText(entryModel.getNickName());
            ((EntryViewHolder) holder).subjectTextView.setText(entryModel.getSubjectName());
            if (entryModel.getLikeStatus() > 0) {
                ((EntryViewHolder) holder).likesTextView.setTextColor(context.getResources().getColor(R.color.like_bg));
            } else if (entryModel.getLikeStatus() < 0) {
                ((EntryViewHolder) holder).likesTextView.setTextColor(context.getResources().getColor(R.color.dislike_bg));
            }
        } else {
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
