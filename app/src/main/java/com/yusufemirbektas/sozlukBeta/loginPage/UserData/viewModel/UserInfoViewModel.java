package com.yusufemirbektas.sozlukBeta.loginPage.UserData.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {

    MutableLiveData<Integer> userCode=new MutableLiveData<Integer>();
    MutableLiveData<String> deviceToken=new MutableLiveData<String>();

    public MutableLiveData<Integer> getUserCode() {
        return userCode;
    }

    public void setUserCode(Integer userCode) {
        this.userCode.setValue(userCode);
    }

    public MutableLiveData<String> getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken.setValue(deviceToken);
    }

}
