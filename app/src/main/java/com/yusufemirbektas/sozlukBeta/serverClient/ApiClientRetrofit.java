package com.yusufemirbektas.sozlukBeta.serverClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientRetrofit {
    private static final String SERVER_URL="https://ccaf-212-12-142-150.eu.ngrok.io/sinavSozlukDeneme/";
    private static Retrofit retrofit=null;

    public static Retrofit getInstance(){
        if(retrofit==null){
            //Http logging interceptor
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient=new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            Gson gson=new GsonBuilder().setLenient().create();
            retrofit=new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
