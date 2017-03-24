package com.ims.tasol.networkingexample.retrofit;

import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.DataPojo;
import com.ims.tasol.networkingexample.model.ListData;
import com.ims.tasol.networkingexample.model.Result;
import com.ims.tasol.networkingexample.model.StudentDataList;
import com.ims.tasol.networkingexample.model.Task;
import com.ims.tasol.networkingexample.model.TaskData;
import com.ims.tasol.networkingexample.utils.HttpConstants;

import java.util.HashMap;

import javax.security.auth.callback.Callback;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


/**
 * Created by tasol on 21/3/17.
 */

public interface RaytaApi {
    //Single Image
    @Multipart
    @POST(HttpConstants.FILEUPLOADJSON1)
    Call<Result> uploadImage(@Part MultipartBody.Part file, @Part("stdID") int stdID);

    //Multiple Images
    @Multipart
    @POST(HttpConstants.FILEMULTIPLEUPLOAD)
    Call<Result> uploadMultipleImage(@Part MultipartBody.Part files1, @Part MultipartBody.Part files2, @Query("total_images") int total, @Query("stdID") int stdID);


    @Multipart
    @POST(HttpConstants.FILEMULTIPLEUPLOAD)
    Call<Result> uploadMult(@Part MultipartBody.Part[] surveyImage,@Query("total_images") int total, @Query("stdID") int stdID);


    @GET(".")
    Call<ListData> getAllUser();

    @GET(HttpConstants.USERDATAJSON)
    Call<ListData> getSingleUser(@Query("method") String method, @Query("stdID") int stdID);

    @GET(HttpConstants.USERDATAJSON)
    Call<AddUser> addupdateUser(@Query("method") String method, @Query("stdID") int stdID, @Query("stdName") String stdName, @Query("stdAge") int stdAge);

    @GET(HttpConstants.USERDATAJSON)
    Call<AddUser> deleteUser(@Query("method") String method, @Query("stdID") int stdID);

    @POST(HttpConstants.SINGLEUSER)
    Call<StudentDataList> getSingleUserObj(@Body Task method);

    @POST(HttpConstants.SINGLEUSER)
    Call<StudentDataList> getSingleUserHash(@Query("reqObject") Task method);

}
