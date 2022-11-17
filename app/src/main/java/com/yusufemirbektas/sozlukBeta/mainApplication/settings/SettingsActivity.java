package com.yusufemirbektas.sozlukBeta.mainApplication.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.ActivitySettingsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.settings.viewModel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private final String[] degreeItems={"üniversite", "lise", "ortaokul"};
    private ArrayAdapter<String> adapter;
    private String degree;
    private SettingsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel=new ViewModelProvider(this).get(SettingsViewModel.class);

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

        binding.degreesAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                Toast.makeText(SettingsActivity.this, item, Toast.LENGTH_SHORT).show();
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
                    viewModel.updateProfile(nickName.toString(),null,null);
                }
            }
        });

    }

    private void goToMainActivity() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}