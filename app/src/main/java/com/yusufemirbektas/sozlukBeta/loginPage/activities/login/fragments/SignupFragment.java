package com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentSignupBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserNameViewModel;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientRetrofit;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private LoginViewModel viewModel;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentSignupBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialising the viewModel object
        viewModel=new ViewModelProvider(this).get(LoginViewModel.class);

        //initialising the fragment manager to provide navigation
        fm=getActivity().getSupportFragmentManager();


        //when user clicks the signup button
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.signupBtn.setEnabled(false);
                binding.signupBtn.setText("");
                String email=binding.emailET.getText().toString();
                String p1=binding.passwordET.getText().toString();
                String p2=binding.passwordCheckET.getText().toString();
                viewModel.signUp(email,p1,p2);
            }
        });

        //observing the login result
        viewModel.httpSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean!=null){
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if(!aBoolean) {
                        Toast.makeText(getContext(), "lütfen internet bağlantınızı kontrol edin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //observing the login result
        viewModel.loginResult.observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(LoginResult loginResult) {
                if(loginResult!=null){
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.signupBtn.setEnabled(true);
                    binding.signupBtn.setText("KAYDOL");
                    handleResult(loginResult);
                }
            }
        });

        binding.toLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginFragment();
            }
        });
    }

    private void handleResult(LoginResult loginResult) {
        final int result=loginResult.getResult();
        String comment= loginResult.getComment();
        Toast message=Toast.makeText(getContext(), comment, Toast.LENGTH_SHORT);
        message.show();
        if(result==0){
            String email=binding.emailET.getText().toString();
            goToLoginFragment(email);
        }
    }

    private void goToLoginFragment(String email) {
        Bundle args=new Bundle();
        args.putString("email", email);
        LoginFragment fragment=new LoginFragment();
        fragment.setArguments(args);
        fm.beginTransaction().replace(R.id.login_fragment_container,fragment).commit();
    }
    private void goToLoginFragment() {
        LoginFragment fragment=new LoginFragment();
        fm.beginTransaction().replace(R.id.login_fragment_container,fragment).commit();
    }
    private void setShowHidePassword(EditText passwordField, ImageView eyeIcon){
        //password hide-show
        passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
        eyeIcon.setImageResource(R.drawable.ic_hide_pwd);
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordField.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password is visible
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.ic_hide_pwd);
                }else{
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyeIcon.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
    }

}