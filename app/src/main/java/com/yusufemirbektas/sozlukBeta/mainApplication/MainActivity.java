package com.yusufemirbektas.sozlukBeta.mainApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button profileNavigationButton;
    EditText userCodeEt;
    Button goForum;
    int userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.userInfo_textView);
        userCodeEt=findViewById(R.id.userCodeEditTextMain);
        goForum=findViewById(R.id.go_to_forum_button);

        getSupportActionBar().setTitle("Main Activity");

        textView.setText("user code: "+ UserData.getUserCode());
        profileNavigationButton=findViewById(R.id.button_toProfile);
        profileNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCode=Integer.parseInt(userCodeEt.getText().toString());
                goToProfileActivity(userCode);
            }
        });
        goForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void goToProfileActivity(int userCode){
        Intent intent=new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USERCODE_EXTRA,userCode);
        startActivity(intent);
    }
}