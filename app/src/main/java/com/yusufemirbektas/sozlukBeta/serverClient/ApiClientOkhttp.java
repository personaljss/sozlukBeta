package com.yusufemirbektas.sozlukBeta.serverClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClientOkhttp {
    private static OkHttpClient client;

    public static OkHttpClient getInstance(){
        if(client==null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client=new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

        }
        return client;
    }
}
