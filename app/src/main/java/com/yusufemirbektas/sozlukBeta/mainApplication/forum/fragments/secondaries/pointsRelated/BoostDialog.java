package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.pointsRelated;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.PointsViewModel;

public class BoostDialog extends DialogFragment {
    private static final int FULL_COLOR = 255;
    private static final int RATIO = 2;
    private PointsViewModel viewModel;
    private OnPickListener listener;
    private int likesSelected;

    public interface OnPickListener{
        void onPick(String num);
    }

    public void setListener(OnPickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        int points = viewModel.getPointsAvailable().getValue();

        int min = 0;
        int max = Math.min(127,points);
        String[] nums = new String[max - min+1];
        for (int i = max-min; i >=0  ; i--) {
            nums[i] = String.valueOf(min+i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_like, null);


        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPick(nums[likesSelected]);
                        listener=null;
                    }
                })
                .setNegativeButton("iptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        NumberPicker numberPicker = view.findViewById(R.id.likeNumberPicker);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDisplayedValues(nums);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(max-min);
        numberPicker.setValue(0);
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
