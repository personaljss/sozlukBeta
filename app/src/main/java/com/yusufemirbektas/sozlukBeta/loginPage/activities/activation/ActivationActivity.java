package com.yusufemirbektas.sozlukBeta.loginPage.activities.activation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments.ActivationFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;

public class ActivationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i=getIntent();
        Bundle args=new Bundle();
        args.putInt(BundleKeys.USERCODE,i.getIntExtra(BundleKeys.USERCODE,-1));

        setContentView(R.layout.activity_activation);
        ActivationFragment activationFragment = new ActivationFragment();
        activationFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, activationFragment)
                .commit();

    }

}