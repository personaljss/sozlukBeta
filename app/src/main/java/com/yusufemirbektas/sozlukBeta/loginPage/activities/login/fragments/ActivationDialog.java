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
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientRetrofit;
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
    private AlertDialog.Builder builder;

    DialogInterface.OnClickListener negativeButtonListener;
    DialogInterface.OnClickListener positiveButtonListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.activation_fragment_dialog,null);



        int userCode= UserData.getUserCode();

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

                Retrofit retrofit= ApiClientRetrofit.getInstance();
                Call<LoginResult> call=retrofit.create(LoginApiInterface.class)
                        .postActivation(UserData.getUserCode()
                                ,activationCodeEt.getText().toString());

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.isSuccessful()){
                            validateActivation(response.body());
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

    private void validateActivation(LoginResult loginResult){
        //404: sistem hatası, 0: aktivasyon başarılı, 1: kod eşleşmiyor
        if(loginResult.getResult()==0){
            goToMainActivity();
        }
    }

    private void goToMainActivity(){
        Intent intent=new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

}
