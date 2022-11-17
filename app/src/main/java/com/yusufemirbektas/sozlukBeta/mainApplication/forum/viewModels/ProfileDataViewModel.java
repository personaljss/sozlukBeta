package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.activity.ForumActivity.targetDatePattern;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.data.User;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.FollowResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.ProfileDataResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileDataViewModel extends ViewModel {
    private static final String TAG = "ProfileDataViewModel";
    private User user=User.getInstance();

    private MutableLiveData<String> userCode=new MutableLiveData<>();
    private MutableLiveData<Header> header=new MutableLiveData<>();
    private MutableLiveData<List<Entry>> entries=new MutableLiveData<>();
    private MutableLiveData<List<Test>> tests=new MutableLiveData<>();
    private MutableLiveData<Integer> followResult=new MutableLiveData<>(-1);
    public final MutableLiveData<Integer> following=new MutableLiveData<>(0);
    private String followComment;

    //method to get the profile data
    public void loadProfileData() {
        entries.setValue(null);
        tests.setValue(null);
        int entryStartDate= 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            entryStartDate = (int) LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        }
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("viewer", user.getUserCode())
                .add("testStart", "0")
                .add("entryStartDate", String.valueOf(entryStartDate))
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
                    setUpProfileDataBg(jsonResponse,targetDatePattern);
                }
            }
        });
    }

    public void loadTests(int testStart, int testsCount){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("viewer", user.getUserCode())
                .add("testStart", String.valueOf(testStart))
                .add("entryStartDate", "0")
                .add("testCount", String.valueOf(testsCount))
                .add("entryCount", "0")
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
                    addTestsBg(profileDataResponse.tests, targetDatePattern);
                }
            }
        });
    }

    public void loadEntries(int entryStartDate, int entryCount){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("usercode", String.valueOf(userCode.getValue()))
                .add("viewer",user.getUserCode())
                .add("testStart", "0")
                .add("entryStartDate", String.valueOf(entryStartDate))
                .add("testCount", "0")
                .add("entryCount", String.valueOf(entryCount))
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
                    addEntriesBg(profileDataResponse.entries, targetDatePattern);
                }
            }
        });
    }

    public void upLoadProfilePhoto(String imageStr){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", User.getInstance().getUserCode())
                .add("image", imageStr)
                .add("imageType","0")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.UPLOAD_PP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    public void followUser(String userCode){
        followHelper(userCode,"1");
    }

    public void unFollowUser(String userCode){
        followHelper(userCode,"0");
    }

    public void followHelper(String userCode, String operation){
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", user.getUserCode())
                .add("followed", userCode)
                .add("operation",operation)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.FOLLOW_USER)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                followResult.postValue(404);
                followComment="lütfen internet bağnatınızı kontrol edin";
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    FollowResponse srlsdResponse=new Gson().fromJson(response.body().string(),FollowResponse.class);
                    followResult.postValue(srlsdResponse.result);
                    followComment= srlsdResponse.comment;
                    if(operation.equals("1")){
                        following.postValue(1);
                    }else if(operation.equals("0")){
                        following.postValue(0);
                    }
                }
            }
        });
    }

    public void setUpProfileDataBg(String jsonResponse, String targetPattern){
        Gson gson=new Gson();
        ProfileDataResponse profileDataResponse=gson.fromJson(jsonResponse,ProfileDataResponse.class);
        if(profileDataResponse.result!=404){
            addTestsBg(profileDataResponse.tests, targetPattern);
            addEntriesBg(profileDataResponse.entries, targetPattern);
            Header header=gson.fromJson(profileDataResponse.header,Header.class);
            postHeader(header);
            following.postValue(header.getFollowing());
        }else {
            following.postValue(null);
        }
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

    public void setUserCode(String userCode) {
        this.userCode.setValue(userCode);
    }

    //setters
    public void setHeader(Header header){
        this.header.setValue(header);
    }
    public void postHeader(Header header){
        this.header.postValue(header);
    }
    public void setEntries(List<Entry> entries){
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

    public LiveData<String> getUserCode() {
        return userCode;
    }

    public LiveData<List<Entry>> getEntries() {
        return entries;
    }

    public LiveData<List<Test>> getTests() {
        return tests;
    }

    public LiveData<Integer> getFollowResult() {
        return followResult;
    }

    public void setFollowResult(int followResult) {
        this.followResult.setValue(followResult);
    }

    public String getFollowComment() {
        return followComment;
    }
}
