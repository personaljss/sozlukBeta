package com.yusufemirbektas.sozlukBeta.loginPage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.loginPage.viewModels.LoginViewModel;

public class ActivationFragment extends Fragment {
    Button activateBtn;
    EditText activationCodeEt;
    ProgressBar progressBar;
    private int userCode;
    private User user;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activation_fragment, container, false);
        activateBtn = root.findViewById(R.id.activate_btn);
        activationCodeEt = root.findViewById(R.id.activation_ET);
        progressBar = root.findViewById(R.id.progress_bar);

        viewModel=new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disabling button
                activateBtn.setEnabled(false);
                //making progress bar visible on the button
                progressBar.setVisibility(View.VISIBLE);
                activateBtn.setText("");
                //posting login info to the server
                viewModel.activate(activationCodeEt.getText().toString());
            }
        });

        return root;
    }


}
