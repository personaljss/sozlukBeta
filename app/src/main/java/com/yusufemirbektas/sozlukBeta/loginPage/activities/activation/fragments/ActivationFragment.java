package com.yusufemirbektas.sozlukBeta.loginPage.activities.activation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginResult;
import com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel.UserNameViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientRetrofit;
import com.yusufemirbektas.sozlukBeta.loginPage.http.retrofitUtils.LoginApiInterface;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivationFragment extends Fragment {
    Button activateBtn;
    EditText activationCodeEt;
    UserNameViewModel userNameViewModel;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activation_fragment, container, false);
        activateBtn = root.findViewById(R.id.activate_btn);
        activationCodeEt = root.findViewById(R.id.activation_ET);
        progressBar = root.findViewById(R.id.progress_bar);

        userNameViewModel = new ViewModelProvider(getActivity()).get(UserNameViewModel.class);

        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disabling button
                activateBtn.setEnabled(false);
                //making progress bar visible on the button
                progressBar.setVisibility(View.VISIBLE);
                activateBtn.setText("");
                //posting login info to the server
                Retrofit retrofit = ApiClientRetrofit.getInstance();
                Call<LoginResult> call = retrofit.create(LoginApiInterface.class)
                        .postActivation(UserData.getUserCode()
                                , activationCodeEt.getText().toString());

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {
                            validateActivation(response.body());
                        } else {
                            Log.e("activationFragment", "response is not successful");
                        }
                        //making progress bar gone
                        progressBar.setVisibility(View.GONE);
                        //enabling button
                        activateBtn.setEnabled(true);
                        activateBtn.setText("AKTİVE ET");
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.e("activationFragment", "onFailure: ");
                        //making progress bar gone
                        progressBar.setVisibility(View.GONE);
                        //enabling button
                        activateBtn.setEnabled(true);
                        activateBtn.setText("AKTİVE ET");
                    }
                });
            }

        });

        return root;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


    private void validateActivation(LoginResult loginResult) {
        //404: sistem hatası, 0: aktivasyon başarılı, 1: kod eşleşmiyor
        Toast.makeText(getContext(), loginResult.getComment(), Toast.LENGTH_LONG).show();
        if (loginResult.getResult() == 0) {
            goToMainActivity();
        }
    }
}
