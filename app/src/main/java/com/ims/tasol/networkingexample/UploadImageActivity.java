package com.ims.tasol.networkingexample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MultipartBody;

import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.Result;
import com.ims.tasol.networkingexample.permission.PermissionsActivity;
import com.ims.tasol.networkingexample.permission.PermissionsChecker;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.ims.tasol.networkingexample.utils.InternetConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;

import static android.R.attr.action;

/**
 * Created by tasol on 21/3/17.
 */

public class UploadImageActivity extends AppCompatActivity {
    private static final String[] PERMISSIONS_READ_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    PermissionsChecker checker;
    String imagePath="";
    ImageView imageView;
    TextView textView;
    Button btnChooseImage,btnUpload;
    int INT_STDID=1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);
        checker = new PermissionsChecker(this);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnChooseImage=(Button)findViewById(R.id.btnChooseImage);
        btnUpload=(Button)findViewById(R.id.btnUpload);

        INT_STDID=getIntent().getIntExtra("INT_STDID",1);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkPermission = ContextCompat.checkSelfPermission(UploadImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            UploadImageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
                } else {
                    showImagePopup(view);
                }

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(imagePath)){
                    if(InternetConnection.checkConnection(UploadImageActivity.this)){
                        uploadImage();
                    }else {
                        Toast.makeText(UploadImageActivity.this,"Internet Connection not Available",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UploadImageActivity.this,"First attach file to upload",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void uploadImage(){

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(UploadImageActivity.this);
        progressDialog.setMessage("Uploading.. Wait for a while...");
        progressDialog.show();

        RaytaApi service= RaytaServiceClass.getApiService();

        File file = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        Call<Result> resultCall=service.uploadImage(body,INT_STDID);
        Log.v("@@@@WWE","REquest "+resultCall.toString());
        final Result[] result = {new Result()};

        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                progressDialog.dismiss();
                Log.v("@@@WWE","Respnse");
                result[0] =response.body();
                Log.v("@@@WWE","Response Result "+result[0].getResult());
                if(response.isSuccessful()){
                    Toast.makeText(UploadImageActivity.this,"Sucess",Toast.LENGTH_SHORT).show();
                    Toast.makeText(UploadImageActivity.this,"Press Refresh Button",Toast.LENGTH_LONG).show();
                    supportFinishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.dismiss();
                Log.v("@@@WWE","Failure ");
                Log.v("@@@WWE","MEssage "+t.getMessage());
            }
        });

    }

    public void showImagePopup(View view) {
//        if (checker.lacksPermissions(PERMISSIONS_READ_STORAGE)) {
//            startPermissionsActivity(PERMISSIONS_READ_STORAGE);
//        } else {
            // File System.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            // Chooser of file system options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Image");
            startActivityForResult(chooserIntent, 1010);
//        }
    }
    private void startPermissionsActivity(String[] permission) {
        PermissionsActivity.startActivityForResult(this, 0, permission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data == null) {
                Toast.makeText(UploadImageActivity.this,"Unable to Pickup Image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                Picasso.with(UploadImageActivity.this).load(new File(imagePath))
                        .into(imageView);

                Toast.makeText(UploadImageActivity.this,"Click on Image to Reselec",Toast.LENGTH_LONG).show();
                cursor.close();

                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                Toast.makeText(UploadImageActivity.this,"Unable to Load Image",Toast.LENGTH_LONG).show();
            }
        }
    }
}
