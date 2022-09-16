package com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;

import java.util.List;

public class ProfileDataViewModel extends ViewModel{
    MutableLiveData<Integer> userCode=new MutableLiveData<>();
    MutableLiveData<Header> header=new MutableLiveData<>();
    MutableLiveData<List<Entry>> entries=new MutableLiveData<>();
    MutableLiveData<List<Test>> tests=new MutableLiveData<>();

    public MutableLiveData<Integer> getUserCode(){return userCode;}

    public void setUserCode(int userCode){this.userCode.setValue(userCode);}

    public MutableLiveData<Header> getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header.setValue(header);
    }

    public MutableLiveData<List<Entry>> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries.setValue(entries);
    }

    public MutableLiveData<List<Test>> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests.setValue(tests);
    }
}
