package com.ims.tasol.networkingexample.retrofit;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ims.tasol.networkingexample.model.Task;
import com.ims.tasol.networkingexample.utils.HttpConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by tasol on 22/3/17.
 */

public class RaytaApiClient {

    public RaytaApiClient() {
    }
    private static Retrofit getRetroClient(){


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Level.BODY);

        OkHttpClient.Builder client=new OkHttpClient.Builder();

        Gson gson= new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Task.class,new CustomGsonAdapter.UserAdapter())
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(HttpConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();
    }

    public static RaytaApi getApiService(){
        return getRetroClient().create(RaytaApi.class);
    }

}
