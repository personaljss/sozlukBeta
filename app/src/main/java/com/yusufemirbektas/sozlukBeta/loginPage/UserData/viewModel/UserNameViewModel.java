package com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserNameViewModel extends ViewModel {
    MutableLiveData<String> userName=new MutableLiveData<>();

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.setValue(userName);
    }
}
