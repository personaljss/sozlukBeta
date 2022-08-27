package com.yusufemirbektas.sozlukBeta.loginPage.activities.activation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.UserInfo;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserInfoViewModel;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments.ActivationFragment;

public class ActivationActivity extends AppCompatActivity {
    UserInfoViewModel userInfoViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Aktivasyon");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userInfoViewModel=new ViewModelProvider(this).get(UserInfoViewModel.class);
        UserInfo userInfo=getIntent().getParcelableExtra("UserInfo");
        userInfoViewModel.setUserCode(userInfo.getUserCode());

        setContentView(R.layout.activity_activation);
        ActivationFragment activationFragment=new ActivationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,activationFragment)
                .commit();

    }

}