package com.yusufemirbektas.sozlukBeta.loginPage.activities.login.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.UserInfo;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserInfoViewModel;
import com.yusufemirbektas.sozlukBeta.loginPage.activities.login.LoginActivity;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiClient;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiInterface;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivationDialog extends AppCompatDialogFragment {
    private EditText activationCodeEt;
    private ProgressBar progressBar;
    private TextView textView;
    private UserInfoViewModel userInfoViewModel;
    private AlertDialog.Builder builder;
    private static Activity activity;

    DialogInterface.OnClickListener negativeButtonListener;
    DialogInterface.OnClickListener positiveButtonListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activation_fragment_dialog,null);
        activity=getActivity();

        userInfoViewModel=new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        int userCode=userInfoViewModel.getUserCode().getValue();

        negativeButtonListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };


        positiveButtonListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //disabling cancel button
                progressBar.setVisibility(View.VISIBLE);

                Retrofit retrofit= LoginApiClient.getInstance();
                Call<LoginResult> call=retrofit.create(LoginApiInterface.class)
                        .postActivation(userInfoViewModel.getUserCode().getValue()
                                ,activationCodeEt.getText().toString());

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.isSuccessful()){
                            validateActivation(response.body(),userInfoViewModel);
                        }else{
                            Log.e("activationFragment", "response is not successful");
                        }
                        //enabling canceling
                        builder.setCancelable(true);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.e("activationFragment", "onFailure: ");
                        //enabling canceling
                        builder.setCancelable(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        };

        builder.setView(view)
                .setTitle("Aktivasyon")
                .setNegativeButton("iptal",negativeButtonListener)
                .setPositiveButton("onayla",positiveButtonListener);
        builder.setCancelable(false);

        activationCodeEt=view.findViewById(R.id.activation_code_ET);
        progressBar=view.findViewById(R.id.progressBarActivation);
        textView=view.findViewById(R.id.activation_textView);

        progressBar.setVisibility(View.GONE);

        return builder.create();
    }

    private void validateActivation(LoginResult loginResult, UserInfoViewModel userInfoViewModel){
        //404: sistem hatası, 0: aktivasyon başarılı, 1: kod eşleşmiyor
        Toast.makeText(activity, loginResult.getComment(),Toast.LENGTH_LONG).show();
        if(loginResult.getResult()==0){
            goToMainActivity(new UserInfo(userInfoViewModel.getUserCode().getValue()));
        }
    }

    private void goToMainActivity(UserInfo userInfo){
        Intent intent=new Intent(getActivity(), MainActivity.class).putExtra("UserInfo",userInfo);
        startActivity(intent);
    }

}
