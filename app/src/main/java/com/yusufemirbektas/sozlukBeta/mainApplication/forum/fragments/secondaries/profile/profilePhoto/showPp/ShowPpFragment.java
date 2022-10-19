package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.profilePhoto.showPp;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.stringToBitmap;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ShowPpViewModel;

public class ShowPpFragment extends Fragment {
    private ShowPpViewModel viewModel;
    private String imageStr;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_pp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ShowPpViewModel.class);
        imageView = view.findViewById(R.id.ppImageView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle args = getArguments();
        int userCode = args.getInt(BundleKeys.USERCODE);

        ImageButton backButton=view.findViewById(R.id.backIcon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        viewModel.loadProfilePhoto(userCode);
        viewModel.getImageStr().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    imageStr = s;
                    setProfilePhoto();
                }catch (IllegalArgumentException e){
                    e.fillInStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setProfilePhoto() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Bitmap image = stringToBitmap(imageStr);
            imageView.setImageBitmap(image);
        } else {
            //do something
        }
    }
}