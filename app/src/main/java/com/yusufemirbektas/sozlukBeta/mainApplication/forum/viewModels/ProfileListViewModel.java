package com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.ProfileItem;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.DisplayPpResponse;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileListViewModel extends ViewModel {
    MutableLiveData<List<ProfileItem>> users = new MutableLiveData<>();
    MutableLiveData<Boolean> photosLoaded=new MutableLiveData<>();
    private final Gson gson=new Gson();

    public void loadLikeDetails(int subjectId, int commentId) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("subjectID", String.valueOf(subjectId))
                .add("commentID", String.valueOf(commentId))
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.SHOW_LIKE_DETAILS)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    ServerResponse sr = gson.fromJson(response.body().string(), ServerResponse.class);
                    Type ListOfUsers = TypeToken.getParameterized(List.class, ProfileItem.class).getType();
                    List<ProfileItem> likedUserData = gson.fromJson(sr.data, ListOfUsers);
                    users.postValue(likedUserData);
                    photosLoaded.postValue(false);
                }
            }
        });
    }

    public void loadFollowers(String userCode) {
        followHelper(userCode, "2");
    }

    public void loadFollowedBys(String userCode) {
        followHelper(userCode, "1");
    }

    private void followHelper(String userCode, String flag) {
        OkHttpClient client = ApiClientOkhttp.getInstance();

        RequestBody requestBody = new FormBody.Builder()
                .add("userCode", userCode)
                .add("follower", flag)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.FOLLOWERS)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    ServerResponse sr = gson.fromJson(response.body().string(), ServerResponse.class);
                    Type ListOfUsers = TypeToken.getParameterized(List.class, ProfileItem.class).getType();
                    List<ProfileItem> likedUserData = gson.fromJson(sr.data, ListOfUsers);
                    users.postValue(likedUserData);
                }
            }
        });
    }

    public void loadProfilePhotos() {
        if(users.getValue()==null){
            return;
        }
        StringBuilder sb=new StringBuilder();
        for(ProfileItem item : users.getValue()){
            sb.append(item.getUserCode()+",");
        }
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("image", sb.toString())
                .add("folder", "profilePhotos")
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.DISPLAY_PP)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Type MapType=new TypeToken<HashMap<String,String>>(){}.getType();
                    DisplayPpResponse displayPpResponse=gson.fromJson(response.body().string(),DisplayPpResponse.class);
                    HashMap<String,String> map=gson.fromJson(displayPpResponse.getImagesEncoded(),MapType);
                    List<ProfileItem> items=users.getValue();
                    for(ProfileItem item : items){
                        item.setPhoto(map.get(item.getUserCode()));
                    }
                    users.postValue(items);
                    photosLoaded.postValue(true);
                }
            }
        });
    }

    public LiveData<Boolean> getPhotosLoaded() {
        return photosLoaded;
    }

    public LiveData<List<ProfileItem>> getUsers() {
        return users;
    }

    private static class ServerResponse {
        @SerializedName("data")
        String data;
    }
}
