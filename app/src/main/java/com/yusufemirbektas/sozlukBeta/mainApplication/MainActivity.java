package com.yusufemirbektas.sozlukBeta.mainApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button profileNavigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.userInfo_textView);

        getSupportActionBar().setTitle("Main Activity");

        textView.setText("user code: "+ UserData.getUserCode());
        profileNavigationButton=findViewById(R.id.button_toProfile);
        profileNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileActivity();
            }
        });


    }

    private void goToProfileActivity(){
        Intent intent=new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}