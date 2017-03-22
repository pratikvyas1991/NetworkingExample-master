package com.ims.tasol.networkingexample.retrofit;

import com.ims.tasol.networkingexample.utils.HttpConstants;

/**
 * Created by tasol on 18/3/17.
 */

public class ServiceClass {
    static ServiceInterface serviceInterface;
//    public static final String baseUrl= HttpConstants.BASE_URL_GEONAME;
    public static final String baseUrl= HttpConstants.baseUrl;

//    public static ServiceInterface connection(){
//        if(serviceInterface==null){
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient client = new OkHttpClient();
//            client.interceptors().add(new okhttp3.Interceptor() {
//                @Override
//                public okhttp3.Response intercept(Chain chain) throws IOException {
//                    Response response=chain.proceed(chain.request());
//                    return response;
//                }
//            });
//            Retrofit retrofit = new Retrofit.Builder()
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl(baseUrl)
//                    .build();
//            serviceInterface=retrofit.create(ServiceInterface.class);
//        }
//        return serviceInterface;
//    }
}
