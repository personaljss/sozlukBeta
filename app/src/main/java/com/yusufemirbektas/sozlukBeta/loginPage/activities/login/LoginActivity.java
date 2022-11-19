package com.yusufemirbektas.sozlukBeta.loginPage.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments.ActivationFragment;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.adapter.SectionsPagerAdapter;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments.LoginFragment;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private FragmentManager fm;
    private LoginViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel=new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(R.layout.activity_login);
        fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.login_fragment_container,new LoginFragment()).commit();

        //observing the login result
        viewModel.loginResult.observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if(loginResult!=null){
                    handleResult(loginResult);
                }
            }
        });


    }
    private void handleResult(LoginResult loginResult) {
        final int result=loginResult.getResult();
        String comment= loginResult.getComment();
        Toast message=Toast.makeText(this, comment, Toast.LENGTH_SHORT);
        switch (result){
            case 0:
                //login is succesful
                goToMainActivity();
                break;
            case 1:
                //activation required, open activation fragment
                String userCode= loginResult.getUserCode();
                openActivationFragment(userCode);
                break;
            default:
                //there is a problem, show it to the user
                if(comment!=null){
                    message.show();
                }
                break;
        }
    }

    private void openActivationFragment(String userCode) {
        Bundle args=new Bundle();
        args.putString(BundleKeys.USERCODE, userCode);
        ActivationFragment activationFragment=new ActivationFragment();
        activationFragment.setArguments(args);
        fm.beginTransaction().replace(R.id.login_fragment_container,activationFragment).commit();
    }


    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}