package com.yusufemirbektas.sozlukBeta.loginPage.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserInfoViewModel;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.adapter.SectionsPagerAdapter;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TabLayout loginTab;
    ViewPager viewPager;

    UserInfoViewModel userInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setLoginTab();
        Objects.requireNonNull(getSupportActionBar()).hide();

        userInfoViewModel=new ViewModelProvider(this).get(UserInfoViewModel.class);

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