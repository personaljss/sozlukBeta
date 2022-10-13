package com.yusufemirbektas.sozlukBeta.mainApplication.forum.entryUtils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.viewModel.SubjectEntriesViewModel;

public class LikeDialog extends DialogFragment {
    private static final int FULL_COLOR = 255;
    private static final int RATIO = 2;
    private PointsViewModel viewModel;
    private int likesSelected;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        int points = viewModel.getPointsAvailable().getValue();
        Bundle args = getArguments();
        ;
        int userCode = args.getInt(ForumActivity.BundleKeys.USERCODE);
        int subjectId = args.getInt(ForumActivity.BundleKeys.SUBJECT_ID);
        int commentId = args.getInt(ForumActivity.BundleKeys.COMMENT_ID);
        int likeStatus = args.getInt(ForumActivity.BundleKeys.LIKE_STATUS);
        int adapterPos = args.getInt(ForumActivity.BundleKeys.ADAPTER_POSITION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_like, null);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.likeEntry(userCode, likesSelected, subjectId, commentId, adapterPos);
                    }
                })
                .setNegativeButton("iptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        int min = Math.max(-128 - likeStatus, -points);
        int max = Math.min(128 - likeStatus, points);
        String[] nums = new String[max - min+1];
        for (int i = 0; i <= max-min; i++) {
            nums[i] = String.valueOf(min+i);
        }

        NumberPicker numberPicker = view.findViewById(R.id.likeNumberPicker);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(max-min);

        numberPicker.setValue(Math.abs(min-likeStatus));
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                int color;
                likesSelected=Integer.parseInt(nums[newVal]);
                if(likesSelected>0){
                    color = Color.rgb(FULL_COLOR - RATIO * likesSelected, FULL_COLOR, FULL_COLOR - RATIO * likesSelected);
                }else{
                    color = Color.rgb(FULL_COLOR, FULL_COLOR+RATIO * likesSelected, FULL_COLOR +RATIO * likesSelected);
                }
                view.setBackgroundColor(color);

            }
        });


        return builder.create();
    }
}
