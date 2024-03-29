package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.viewHolders;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;

public class ProfileItemViewHolder extends RecyclerView.ViewHolder {

    public interface OnImageLoadedListener{
        void onImageLoaded(Bitmap image);
    }

    public ImageView ppImageView;
    public TextView nickNameTextView;
    public TextView pointsTextView;

    public ProfileItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ppImageView=itemView.findViewById(R.id.ppImageView);
        nickNameTextView=itemView.findViewById(R.id.nickNameTextView);
        pointsTextView=itemView.findViewById(R.id.pointsTextView);
    }

}
