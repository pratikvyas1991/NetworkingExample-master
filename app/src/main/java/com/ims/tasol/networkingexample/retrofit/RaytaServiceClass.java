package com.ims.tasol.networkingexample.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ims.tasol.networkingexample.utils.HttpConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tasol on 21/3/17.
 */

public class RaytaServiceClass {
    public RaytaServiceClass() {
    }

    private static Retrofit getRetroClient(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(HttpConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static RaytaApi getApiService(){
        return getRetroClient().create(RaytaApi.class);
    }
}
