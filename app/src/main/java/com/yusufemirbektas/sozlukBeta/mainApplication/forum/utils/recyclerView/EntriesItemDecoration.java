package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class EntriesItemDecoration extends RecyclerView.ItemDecoration {
    private final int verticalSpaceWidth;


    public EntriesItemDecoration(int verticalSpaceWidth) {
        this.verticalSpaceWidth = verticalSpaceWidth;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceWidth;
    }
}
