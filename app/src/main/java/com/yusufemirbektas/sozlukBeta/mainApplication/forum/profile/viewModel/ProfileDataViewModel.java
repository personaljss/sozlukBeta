package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProfileDataViewModel extends ViewModel {

    private MutableLiveData<Integer> userCode=new MutableLiveData<>();
    private MutableLiveData<Header> header=new MutableLiveData<>();
    private MutableLiveData<List<Entry>> entries=new MutableLiveData<>();
    private MutableLiveData<List<Test>> tests=new MutableLiveData<>();

    //method to get the profile data
    public void loadProfileData() {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("testStart", "0")
                .add("entryStart", "0")
                .add("testCount", "10")
                .add("entryCount", "10")
                .add("header", "1")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.PROFILE_PHP)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    setUpProfileDataBg(jsonResponse,"dd/MM/yyyy");
                }
            }
        });
    }

    public void loadTests(int testStart, int testsCount){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("testStart", "0")
                .add("entryStart", "0")
                .add("testCount", "10")
                .add("entryCount", "10")
                .add("header", "1")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.PROFILE_PHP)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson=new Gson();
                    ProfileDataResponse profileDataResponse=gson.fromJson(jsonResponse,ProfileDataResponse.class);
                    addTestsBg(profileDataResponse.tests, "dd/MM/yyyy");
                }
            }
        });
    }

    public void loadEntries(int entryStart, int entryCount){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("testStart", "0")
                .add("entryStart", "0")
                .add("testCount", "10")
                .add("entryCount", "10")
                .add("header", "1")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.PROFILE_PHP)
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson=new Gson();
                    ProfileDataResponse profileDataResponse=gson.fromJson(jsonResponse,ProfileDataResponse.class);
                    addEntriesBg(profileDataResponse.entries, "dd/MM/yyyy");
                }
            }
        });
    }

    public void setUpProfileDataBg(String jsonResponse, String targetPattern){
        Gson gson=new Gson();
        ProfileDataResponse profileDataResponse=gson.fromJson(jsonResponse,ProfileDataResponse.class);
        addTestsBg(profileDataResponse.tests, targetPattern);
        addEntriesBg(profileDataResponse.entries, targetPattern);
        Header header=gson.fromJson(profileDataResponse.header,Header.class);
        postHeader(header);
    }

    public void setUpProfileData(String jsonResponse, String targetPattern){
        Gson gson=new Gson();
        ProfileDataResponse profileDataResponse=gson.fromJson(jsonResponse,ProfileDataResponse.class);
        addTests(profileDataResponse.tests, targetPattern);
        addEntries(profileDataResponse.entries, targetPattern);
        Header header=gson.fromJson(profileDataResponse.header,Header.class);
        setHeader(header);
    }

    //add tests
    private void addTests(String testsJson,String sourcePattern, String targetPattern){
        Gson gson=new Gson();
        List<Test> testList=this.tests.getValue();
        if(testList==null){
            testList=new ArrayList<>();
        }else if(testList.size()>0 && testList.get(testList.size()-1)==null){
            testList.remove(testList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Test.class).getType();
        List<Test> testsFromJson=gson.fromJson(testsJson,ListOfTests);
        if(testsFromJson.size()>0){
            for(Test test : testsFromJson){
                test.formatDate(sourcePattern,targetPattern);
                testList.add(test);
            }
        }

        this.tests.setValue(testList);
    }

    //for epoch time
    private void addTests(String testsJson, String targetPattern){
        Gson gson=new Gson();
        List<Test> testList=this.tests.getValue();
        if(testList==null){
            testList=new ArrayList<>();
        }else if(testList.size()>0 && testList.get(testList.size()-1)==null){
            testList.remove(testList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Test.class).getType();
        List<Test> testsFromJson=gson.fromJson(testsJson,ListOfTests);
        if(testsFromJson.size()>0){
            for(Test test : testsFromJson){
                test.formatDate(targetPattern);
                testList.add(test);
            }
        }
        this.tests.setValue(testList);
    }

    private void addTestsBg(String testsJson,String sourcePattern, String targetPattern){
        Gson gson=new Gson();
        List<Test> testList=this.tests.getValue();
        if(testList==null){
            testList=new ArrayList<>();
        }else if(testList.size()>0 && testList.get(testList.size()-1)==null){
            testList.remove(testList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Test.class).getType();
        List<Test> testsFromJson=gson.fromJson(testsJson,ListOfTests);
        if(testsFromJson.size()>0){
            for(Test test : testsFromJson){
                test.formatDate(sourcePattern,targetPattern);
                testList.add(test);
            }
        }
        this.tests.postValue(testList);
    }
    //for epoch time
    private void addTestsBg(String testsJson, String targetPattern){
        Gson gson=new Gson();
        List<Test> testList=this.tests.getValue();
        if(testList==null){
            testList=new ArrayList<>();
        }else if(testList.size()>0 && testList.get(testList.size()-1)==null){
            testList.remove(testList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Test.class).getType();
        List<Test> testsFromJson=gson.fromJson(testsJson,ListOfTests);

        if(testsFromJson.size()>0){
            for(Test test : testsFromJson){
                test.formatDate(targetPattern);
                testList.add(test);
            }
        }
        this.tests.postValue(testList);
    }

    //add entries
    private void addEntries(String testsJson,String sourcePattern, String targetPattern){
        Gson gson=new Gson();
        List<Entry> entryList=this.entries.getValue();
        if(entryList==null){
            entryList=new ArrayList<>();
        }else if(entryList.size()>0 && entryList.get(entryList.size()-1)==null){
            entryList.remove(entryList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Entry.class).getType();
        List<Entry> entriesFromJson=gson.fromJson(testsJson,ListOfTests);
        if(entriesFromJson.size()>0){
            for(Entry entry : entriesFromJson){
                entry.formatDate(sourcePattern,targetPattern);
                entryList.add(entry);
            }
        }

        this.entries.setValue(entryList);
    }
    //for epoch time
    private void addEntries(String testsJson, String targetPattern){
        Gson gson=new Gson();
        List<Entry> entryList=this.entries.getValue();
        if(entryList==null){
            entryList=new ArrayList<>();
        }else if(entryList.size()>0 && entryList.get(entryList.size()-1)==null){
            entryList.remove(entryList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Entry.class).getType();
        List<Entry> entriesFromJson=gson.fromJson(testsJson,ListOfTests);
        if(entriesFromJson.size()>0){
            for(Entry entry : entriesFromJson){
                entry.formatDate(targetPattern);
                entryList.add(entry);
            }
        }

        this.entries.setValue(entryList);
    }

    private void addEntriesBg(String testsJson,String sourcePattern, String targetPattern){
        Gson gson=new Gson();
        List<Entry> entryList=this.entries.getValue();
        if(entryList==null){
            entryList=new ArrayList<>();
        }else if(entryList.size()>0 && entryList.get(entryList.size()-1)==null){
            entryList.remove(entryList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Entry.class).getType();
        List<Entry> entriesFromJson=gson.fromJson(testsJson,ListOfTests);
        if(entriesFromJson.size()>0){
            for(Entry entry : entriesFromJson){
                entry.formatDate(sourcePattern,targetPattern);
                entryList.add(entry);
            }
        }
        this.entries.postValue(entryList);
    }

    //for epoch time
    private void addEntriesBg(String testsJson, String targetPattern){
        Gson gson=new Gson();
        List<Entry> entryList=this.entries.getValue();
        if(entryList==null){
            entryList=new ArrayList<>();
        }else if(entryList.size()>0 && entryList.get(entryList.size()-1)==null){
            entryList.remove(entryList.size()-1);
        }
        Type ListOfTests=TypeToken.getParameterized(List.class, Entry.class).getType();
        List<Entry> entriesFromJson=gson.fromJson(testsJson,ListOfTests);
        if(entriesFromJson.size()>0){
            for(Entry entry : entriesFromJson){
                entry.formatDate(targetPattern);
                entryList.add(entry);
            }
        }
        this.entries.postValue(entryList);
    }

    //data class to parse json response
    private static class ProfileDataResponse {
        @SerializedName("result")
        private int result;

        @SerializedName("comment")
        private String comment;

        @SerializedName("time")
        private String time;

        @SerializedName("header")
        private String header;

        @SerializedName("tests")
        private String tests;

        @SerializedName("entries")
        private String entries;
    }

    public void setUserCode(int userCode) {
        this.userCode.setValue(userCode);
    }

    //setters
    private void setHeader(Header header){
        this.header.setValue(header);
    }
    private void postHeader(Header header){
        this.header.postValue(header);
    }
    private void setEntries(List<Entry> entries){
        this.entries.setValue(entries);
    }
    private void postEntries(List<Entry> entries){
        this.entries.postValue(entries);
    }
    private void setTests(List<Test> tests){
        this.tests.setValue(tests);
    }
    private void postTests(List<Test> tests){
        this.tests.postValue(tests);
    }

    //getters
    public LiveData<Header> getHeader() {
        return header;
    }

    public LiveData<List<Entry>> getEntries() {
        return entries;
    }

    public LiveData<List<Test>> getTests() {
        return tests;
    }

}
