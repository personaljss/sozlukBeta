package com.yusufemirbektas.sozlukBeta.loginPage.activities.activation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments.ActivationFragment;

public class ActivationActivity extends AppCompatActivity {    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_activation);
        ActivationFragment activationFragment=new ActivationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,activationFragment)
                .commit();

    }

}