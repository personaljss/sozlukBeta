package com.yusufemirbektas.sozlukBeta.loginPage.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufemirbektas.sozlukBeta.data.User;

public class LoginViewModel extends ViewModel {
    private User user=User.getInstance();
    MutableLiveData<Boolean> responseArrived=new MutableLiveData<>();

}
