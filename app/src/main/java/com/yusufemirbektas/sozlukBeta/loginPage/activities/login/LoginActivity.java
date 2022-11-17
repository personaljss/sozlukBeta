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
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments.LoginFragment;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.login_fragment_container,new LoginFragment()).commit();
    }

}