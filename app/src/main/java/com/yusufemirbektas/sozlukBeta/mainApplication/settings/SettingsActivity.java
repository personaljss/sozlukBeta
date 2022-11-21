package com.yusufemirbektas.sozlukBeta.mainApplication.settings;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.bitmapToBytes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.databinding.ActivitySettingsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.settings.viewModel.SettingsViewModel;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private final String[] degreeItems={"üniversite", "lise", "ortaokul"};
    private ArrayAdapter<String> adapter;
    private String degree;
    private SettingsViewModel viewModel;
    private ActivityResultLauncher<Intent> launcher;
    private Bitmap ppBitmap;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(this).get(SettingsViewModel.class);
        user=User.getInstance();

        viewModel.getResponse().observe(this, new Observer<SettingsViewModel.Response>() {
            @Override
            public void onChanged(SettingsViewModel.Response response) {
                if(response!=null){
                    if(response.result==0){
                        goToMainActivity();
                    }
                }
            }
        });

        //creating an array adapter to show degree options to the user
        adapter=new ArrayAdapter<String>(this,R.layout.item_simple_degree,degreeItems);
        //setting the adapter to this view to make a dropdown menu
        binding.degreesAutoCompleteTextView.setAdapter(adapter);

        binding.nickNameEditTextText.setText(user.getNickname());

        binding.degreesAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                Toast.makeText(SettingsActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

        binding.rotatePpCWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix=new Matrix();
                matrix.postRotate(-90);
                Bitmap src=ppBitmap;
                ppBitmap=Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,false);
                binding.settingsPpCircleImageView.setImageBitmap(ppBitmap);
            }
        });

        binding.rotatePpCCWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix matrix=new Matrix();
                matrix.postRotate(90);
                Bitmap src=ppBitmap;
                ppBitmap=Bitmap.createBitmap(src,0,0,src.getWidth(),src.getHeight(),matrix,false);
                binding.settingsPpCircleImageView.setImageBitmap(ppBitmap);
            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input check
                CharSequence nickName=binding.nickNameEditTextText.getText();
                if(TextUtils.isEmpty(nickName)){
                    Toast.makeText(SettingsActivity.this, "lütfen geçerli bir kullanıcı adı seçin", Toast.LENGTH_SHORT).show();
                }else {
                    String imageEncoded=null;
                    if(ppBitmap!=null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            imageEncoded= ImageUtils.imageToString(ppBitmap,ImageUtils.HIGH_QUALITY_IMAGE_SIZE);
                            viewModel.updatePp(imageEncoded).observe(SettingsActivity.this, new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer integer) {
                                    if(integer!=null){
                                        String message=(integer==0)?"başarılı":"başarısız";
                                        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            //do something
                        }
                    }
                    if(!user.getNickname().equals(nickName.toString())){
                        viewModel.updateProfile(nickName.toString(),null,imageEncoded);
                    }
                }
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
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                    selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            if(selectedImageBitmap!=null){
                                //making rotation  buttons visible when user picks a photo
                                binding.rotatePpCCWBtn.setVisibility(View.VISIBLE);
                                binding.rotatePpCWBtn.setVisibility(View.VISIBLE);
                                ppBitmap=selectedImageBitmap;
                                binding.settingsPpCircleImageView.setImageBitmap(selectedImageBitmap);
                            }
                        }
                    }
                }
            }
        });

        binding.settingsPpCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
    }

    private void goToMainActivity() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}