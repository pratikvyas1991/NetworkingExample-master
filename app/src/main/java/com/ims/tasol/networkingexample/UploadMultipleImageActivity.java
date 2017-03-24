package com.ims.tasol.networkingexample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ims.tasol.networkingexample.model.Result;
import com.ims.tasol.networkingexample.permission.PermissionsActivity;
import com.ims.tasol.networkingexample.permission.PermissionsChecker;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.ims.tasol.networkingexample.utils.InternetConnection;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tasol on 21/3/17.
 */

public class UploadMultipleImageActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;
    String imagePath="";
    ImageView imageView1,imageView2;
    TextView textView;
    Button btnChooseImage2,btnChooseImage1,btnUpload;
    int INT_STDID=1;
    String UPLOAD_TYPE="";
    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_multiple_image);
        checker = new PermissionsChecker(this);

        textView = (TextView) findViewById(R.id.textView);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        btnChooseImage2=(Button)findViewById(R.id.btnChooseImage2);
        btnChooseImage1=(Button)findViewById(R.id.btnChooseImage1);

        btnUpload=(Button)findViewById(R.id.btnUpload);

        INT_STDID=getIntent().getIntExtra("INT_STDID",1);

        btnChooseImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkPermission = ContextCompat.checkSelfPermission(UploadMultipleImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            UploadMultipleImageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
                } else {
                    openGallery(SELECT_FILE1);
                }

            }
        });
        btnChooseImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkPermission = ContextCompat.checkSelfPermission(UploadMultipleImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            UploadMultipleImageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
                } else {
                    openGallery(SELECT_FILE2);
                }

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(selectedPath1)&&!TextUtils.isEmpty(selectedPath2)){
                    if(InternetConnection.checkConnection(UploadMultipleImageActivity.this)){
                        uploadImage();
                    }else {
                        Toast.makeText(UploadMultipleImageActivity.this,"Internet Connection not Available",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UploadMultipleImageActivity.this,"First attach file to upload",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void uploadImage(){


        //try this if not worked
        //http://stackoverflow.com/questions/39866676/retrofit-uploading-multiple-images-to-a-single-key
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UploadMultipleImageActivity.this);
        progressDialog.setMessage("Uploading.. Wait for a while...");
        progressDialog.show();

        RaytaApi service= RaytaServiceClass.getApiService();

//        File file1 = new File(selectedPath1);
//        File file2 = new File(selectedPath2);
//
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
//
//
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("image1", file1.getName(), requestFile);
//
//        MultipartBody.Part body2 =
//                MultipartBody.Part.createFormData("image2", file2.getName(), requestFile2);
//
//
//        Call<Result> resultCall=service.uploadMultipleImage(body,body2,2,1);
//        Log.v("@@@@WWE","REquest "+resultCall.toString());
//        Log.v("@@@WWE","Retrofit Request Method =  "+resultCall.request().method());
//        Log.v("@@@WWE","Retrofit Request Body =  "+resultCall.request().body());
//        Log.v("@@@WWE","Retrofit Request Url = "+resultCall.request().url());
//        final Result[] result = {new Result()};
//
//        resultCall.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                progressDialog.dismiss();
//                Log.v("@@@WWE","Respnse");
//                result[0] =response.body();
//                Log.v("@@@WWE","Response Result "+result[0].getResult());
//                if(response.isSuccessful()){
//                    Toast.makeText(UploadMultipleImageActivity.this,"Sucess",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(UploadMultipleImageActivity.this,"Press Refresh Button",Toast.LENGTH_LONG).show();
//                    supportFinishAfterTransition();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.v("@@@WWE","Failure ");
//                Log.v("@@@WWE","MEssage "+t.getMessage());
//            }
//        });


//        File propertyImageFile = new File(surveyModel.getPropertyImagePath());
//        RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), propertyImageFile);
//        MultipartBody.Part propertyImagePart = MultipartBody.Part.createFormData("PropertyImage", propertyImageFile.getName(), propertyImage);
//
//        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[surveyModel.getPicturesList().size()];
//
//        for (int index = 0; index < 2; index++) {
////            Log.d(TAG, "requestUploadSurvey: survey image " + index + "  " + surveyModel.getPicturesList().get(index).getImagePath());
//            File file = new File(surveyModel.getPicturesList().get(index).getImagePath());
//            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
//            surveyImagesParts[index] = MultipartBody.Part.createFormData("SurveyImage", file.getName(), surveyBody);
//        }
//
//        final WebServicesAPI webServicesAPI = RetrofitManager.getInstance().getRetrofit().create(WebServicesAPI.class);
//        Call<UploadSurveyResponseModel> surveyResponse = null;
//        if (surveyImagesParts != null) {
//            surveyResponse = webServicesAPI.uploadSurvey(surveyImagesParts, propertyImagePart, draBody);
//        }
//        surveyResponse.enqueue(this);

    }


    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUriMul = data.getData();
        if (resultCode == RESULT_OK && requestCode == SELECT_FILE1)
        {
//            selectedPath1 = getPath(selectedImageUriMul);
//            System.out.println("selectedPath1 : " + selectedPath1);
//
//            Picasso.with(UploadMultipleImageActivity.this).load(new File(selectedPath1))
//                    .into(imageView1);

            if (data == null) {
                Toast.makeText(UploadMultipleImageActivity.this,"Unable to Pickup Image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPath1 = cursor.getString(columnIndex);

                Picasso.with(UploadMultipleImageActivity.this).load(new File(selectedPath1))
                        .into(imageView1);

                Toast.makeText(UploadMultipleImageActivity.this,"Click on Image to Reselec",Toast.LENGTH_LONG).show();
                cursor.close();

            } else {
                imageView1.setVisibility(View.GONE);
                Toast.makeText(UploadMultipleImageActivity.this,"Unable to Load Image",Toast.LENGTH_LONG).show();
            }



        }
        if (resultCode == RESULT_OK && requestCode == SELECT_FILE2)
        {
//            selectedPath2 = getPath(selectedImageUriMul);
//            System.out.println("selectedPath2 : " + selectedPath2);
//            Picasso.with(UploadMultipleImageActivity.this).load(new File(selectedPath2))
//                    .into(imageView2);

            if (data == null) {
                Toast.makeText(UploadMultipleImageActivity.this,"Unable to Pickup Image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedPath2 = cursor.getString(columnIndex);

                Picasso.with(UploadMultipleImageActivity.this).load(new File(selectedPath2))
                        .into(imageView2);

                Toast.makeText(UploadMultipleImageActivity.this,"Click on Image to Reselec",Toast.LENGTH_LONG).show();
                cursor.close();

            } else {
                imageView2.setVisibility(View.GONE);
                Toast.makeText(UploadMultipleImageActivity.this,"Unable to Load Image",Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public void openGallery(int req_code){
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(chooserIntent, req_code);
    }


}
