package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.EntryEventListener;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.ProfileItem;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders.ProfileItemViewHolder;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders.ProgressBarViewHolder;

import java.util.List;

public class ProfilesRvAdapter extends RecyclerView.Adapter {
    private List<ProfileItem> profileItems;
    private Context context;

    private static final int LOADING = 0;
    private static final int DONE = 1;

    public void setProfileItems(List<ProfileItem> profileItems) {
        this.profileItems = profileItems;
    }

    public ProfilesRvAdapter(List<ProfileItem> profileItems, Context context) {
        this.profileItems = profileItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        final RecyclerView.ViewHolder viewHolder;
        if(viewType==DONE){
            view= LayoutInflater.from(context).inflate(R.layout.item_liked_profile,parent,false);
            viewHolder=new ProfileItemViewHolder(view);
            ((ProfileItemViewHolder) viewHolder).nickNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getAdapterPosition();
                    ProfileItem profileItem=profileItems.get(pos);
                    ((EntryEventListener) context).onProfileClicked(profileItem.getUserCode());
                }
            });
            ((ProfileItemViewHolder) viewHolder).ppImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=viewHolder.getAdapterPosition();
                    ProfileItem profileItem=profileItems.get(pos);
                    ((EntryEventListener) context).onProfileClicked(profileItem.getUserCode());
                }
            });
        }else {
            view=LayoutInflater.from(context).inflate(R.layout.item_recycler_view_progress_bar,parent, false);
            viewHolder=new ProgressBarViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==DONE){
            ProfileItem item=profileItems.get(position);
            ((ProfileItemViewHolder) holder).pointsTextView.setText(String.valueOf(item.getLikeStatus()));
            ((ProfileItemViewHolder) holder).nickNameTextView.setText(item.getNickName());
        }else{
            //nothing to do
        }
    }

    @Override
    public int getItemCount() {
        return profileItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        int size= profileItems.size();
        return(profileItems.get(size-1)==null)?LOADING:DONE;
    }
}
