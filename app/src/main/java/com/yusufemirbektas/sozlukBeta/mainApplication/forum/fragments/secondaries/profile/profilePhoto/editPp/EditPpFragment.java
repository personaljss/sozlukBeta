package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.profilePhoto.editPp;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.HIGH_QUALITY_IMAGE_SIZE;
import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.bytesToBitmap;
import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.imageToString;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentEditPpBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ProfileDataViewModel;

public class EditPpFragment extends Fragment {
    private ProfileDataViewModel viewModel;
    private Bitmap imageBitmap;
    private EditPPView editPPView;
    private FragmentEditPpBinding binding;
    private Drawable image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_pp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(getParentFragment()).get(ProfileDataViewModel.class);
        //Button approveBtn = view.findViewById(R.id.editPpAproveBtn);
        //ImageView photoImageView=view.findViewById(R.id.editPhotoImageView);
        //settings fragment sends a byte array when a photo is selected from the storage
        Bundle args=getArguments();
        byte[] bytes=args.getByteArray(BundleKeys.PHOTO_BYTES_KEY);
        //converting the bytearray to bitmap for ease of use
        imageBitmap=bytesToBitmap(bytes);
        //photoImageView.setImageBitmap(imageBitmap);
        image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0,bytes.length));
        //---
        /**
        you should create a ui that permits the user to
         1)scale the photo
         2)cut the photo
         3)understand what it looks like in his profile(Pp is going to be a circle)
        I am using the approve button's click event to send new Pp to the server.
         So it is required to reach selected photo's bitmap or byte[] there.
         Therefore, I created a Bitmap member variable of which you should upload value.
        **/
        //---
        //selected image is going to server after clicking this button
        /*approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //network operation
                if(imageBitmap!=null){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        viewModel.upLoadProfilePhoto(imageToString(imageBitmap,HIGH_QUALITY_IMAGE_SIZE));
                    }else{
                        //not necessary yet
                    }
                }
            }
        });*/
    }

}