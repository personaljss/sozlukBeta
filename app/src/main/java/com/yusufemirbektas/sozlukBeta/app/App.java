package com.yusufemirbektas.sozlukBeta.app;

import android.app.Application;
import android.util.Log;

import com.yusufemirbektas.sozlukBeta.data.User;

public class App extends Application{
    private static final String TAG = "App";
    private User user;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        User.init(this);
        user=User.getInstance();
        user.autoLogin();
        Log.i(TAG, "device token onCreate:"+user.getDeviceToken());
    }
}
