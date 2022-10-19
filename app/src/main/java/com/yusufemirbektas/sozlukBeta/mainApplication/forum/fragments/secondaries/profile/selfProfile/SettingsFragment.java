package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.selfProfile;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.bitmapToBytes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentSettingsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ProfileDataViewModel;

import java.io.IOException;


public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private ProfileDataViewModel profileDataViewModel;
    private ActivityResultLauncher<Intent> launcher;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileDataViewModel = new ViewModelProvider(getParentFragment()).get(ProfileDataViewModel.class);
        navController= Navigation.findNavController(view);
        binding.uploadPpTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


         launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap=null;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                                    getContext().getContentResolver(),
                                    selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            if(selectedImageBitmap!=null){
                                byte[] photoBytes=bitmapToBytes(selectedImageBitmap);
                                Bundle args=new Bundle();
                                args.putByteArray(BundleKeys.PHOTO_BYTES_KEY,photoBytes);
                                navController.navigate(R.id.action_settingsFragment_to_editPpFragment,args);
                            }
                        }
                    }
                }
            }
        });

    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

}