package com.yusufemirbektas.sozlukBeta.mainApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.UserInfo;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.userInfo_textView);

        getSupportActionBar().setTitle("Main Activity");

        UserInfo userInfo=getIntent().getParcelableExtra("UserInfo");
        textView.setText("user code: "+userInfo.getUserCode());
    }
}