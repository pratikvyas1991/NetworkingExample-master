package com.ims.tasol.networkingexample.retrofit;

import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.ListData;
import com.ims.tasol.networkingexample.utils.HttpConstants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by tasol on 18/3/17.
 */

public interface ServiceInterface {

    @GET(HttpConstants.USERDATAJSON)
    Call<ListData>taskData(@Query("method")String method,@Query("stdID")int stdID);

    @GET(HttpConstants.USERDATAJSON)
    Call<AddUser> saveUser(@Query("method")String method, @Query("stdID")int stdID, @Query("stdName")String stdName, @Query("stdAge")String stdAge);

    @GET(HttpConstants.USERDATAJSON)
    Call<AddUser> deleteUser(@Query("method")String method, @Query("stdID")int stdID);

    @GET(HttpConstants.USERDATAJSON)
    Call<AddUser> updateUser(@Query("method")String method, @Query("stdID")int stdID, @Query("stdName")String stdName, @Query("stdAge")String stdAge);

    @Multipart
    @POST(HttpConstants.FILEUPLOADJSON)
    Call<AddUser> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);

}
