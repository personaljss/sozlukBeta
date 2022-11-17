package com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments;


import android.content.Intent;
import android.os.Bundle;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentLoginBinding;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.ActivationActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments.ActivationFragment;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private FragmentManager fm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialising the viewModel object
        viewModel=new ViewModelProvider(this).get(LoginViewModel.class);
        //initialising the fragment manager
        fm=getActivity().getSupportFragmentManager();
        //setting the eye icon behaviour
        setShowHidePassword(binding.passwordEditText,binding.showHidePassword);

        //when user clicks login
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String email=binding.emailEditText.getText().toString();
                String password=binding.passwordEditText.getText().toString();
                viewModel.logIn(email,password);
            }
        });

        binding.toSignupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.login_fragment_container,new SignupFragment()).commit();
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
                    handleResult(loginResult);
                }
            }
        });

    }

    private void handleResult(LoginResult loginResult) {
        final int result=loginResult.getResult();
        String comment= loginResult.getComment();
        Toast message=Toast.makeText(getContext(), comment, Toast.LENGTH_SHORT);
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
                message.show();
                break;
        }
    }

    private void openActivationFragment(String userCode) {
        Bundle args=new Bundle();
        args.putString(BundleKeys.USERCODE, userCode);
        ActivationFragment activationFragment=new ActivationFragment();
        activationFragment.setArguments(args);
        fm=getChildFragmentManager();
        fm.beginTransaction().replace(R.id.login_fragment_container,activationFragment).commit();
    }


    private void goToMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void goToActivationActivity(int userCode){
        Intent intent = new Intent(getActivity(), ActivationActivity.class);
        intent.putExtra(BundleKeys.USERCODE,userCode);
        startActivity(intent);
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

    /*
    private void validateLogin(LoginResult loginResult){
        //0: hatasız login, 1: aktiveli login, 2: geçersiz girdi, 3: yanlış girdi, 404: sistemsel hata
        try {
            Toast.makeText(getContext(),loginResult.getComment(),Toast.LENGTH_LONG).show();
            if(loginResult.getResult()==0){
                //save the user code
                UserData.setUserCode(loginResult.getUserCode());
                //save the data
                saveToSharedPrefs(loginResult.getUserCode());
                //if login is successful go to main activity;
                goToMainActivity();
            }else if(loginResult.getResult()==1){
                //if activation is required go to activation activity
                //UserData.setUserCode(loginResult.getUserCode());
                goToActivationActivity(loginResult.getUserCode());
                //when I solve the bugs, I will use it
                //openActivationDialog();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getContext(),"bir şeyler ters gitti, lütfen internet bağlantınızı kontrol edin",Toast.LENGTH_LONG).show();
        }
    }
     */

    /*
    private void saveLoginData(String userName, String password){
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(USER_NAME,userName);
        //editor.putString(PASSWORD,password);
        editor.apply();
    }
     */
/*
    private void saveToSharedPrefs(int userCode){
        SharedPreferences sharedPrefs= getContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPrefs.edit();
        editor.putInt(LoginActivity.SP_USERCODE, userCode);
        editor.apply();
    }
 */

}