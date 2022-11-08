package com.yusufemirbektas.sozlukBeta.loginPage.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.adapter.SectionsPagerAdapter;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public static final String SHARED_PREFS="SHARED_PREFS";
    public static final String SP_USERCODE="SP_USERCODE";

    TabLayout loginTab;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setLoginTab();

        FragmentManager fragmentManager=getSupportFragmentManager();

        viewPager=findViewById(R.id.view_pager);
        SectionsPagerAdapter sectionsPagerAdapter= new SectionsPagerAdapter(this,fragmentManager);

        viewPager.setAdapter(sectionsPagerAdapter);
        loginTab.setupWithViewPager(viewPager);

    }

    private void setLoginTab(){
        loginTab=findViewById(R.id.tab_layout);
        loginTab.addTab(loginTab.newTab().setText("GİRİŞ"));
        loginTab.addTab(loginTab.newTab().setText("KAYIT"));
        loginTab.setTabGravity(TabLayout.GRAVITY_FILL);
    }

}