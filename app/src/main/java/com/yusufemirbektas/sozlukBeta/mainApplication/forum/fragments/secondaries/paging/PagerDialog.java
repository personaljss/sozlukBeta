package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.paging;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;


public class PagerDialog extends DialogFragment {

    private NumberPicker pagePicker;
    private int pageSelected;
    private OnPageSelectedListener listener;

    public interface OnPageSelectedListener {
        void onPageSelected(int page);
    }

    public void setOnPageSelectedListener(OnPageSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fragment_dialog_pager, null);
        builder.setView(view);

        pagePicker = view.findViewById(R.id.pagePicker);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPageSelected(pageSelected);
            }
        });

        builder.setNegativeButton("iptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nothing to do
            }
        });
        Bundle args = getArguments();

        int maxPage = args.getInt(BundleKeys.MAX_PAGE, 1);

        pagePicker.setMaxValue(maxPage);
        pagePicker.setMinValue(1);
        pagePicker.setValue(args.getInt(BundleKeys.CURRENT_PAGE));
        pageSelected=args.getInt(BundleKeys.CURRENT_PAGE);
        pagePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                pageSelected = newVal;
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener = null;
    }
}
