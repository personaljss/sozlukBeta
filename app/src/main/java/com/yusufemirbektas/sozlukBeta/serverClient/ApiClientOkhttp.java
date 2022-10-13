package com.yusufemirbektas.sozlukBeta.serverClient;

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
                    .build();

        }
        return client;
    }
}
