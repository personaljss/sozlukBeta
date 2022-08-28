package com.yusufemirbektas.sozlukBeta.serverClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiClientOkhttp {
    public static final String SERVER_URL="https://ccaf-212-12-142-150.eu.ngrok.io/sinavSozlukDeneme/";
    public static final String PROFILE_PHP="%5bF%5dshowProfile.php";
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
