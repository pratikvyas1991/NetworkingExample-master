package com.ims.tasol.networkingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.DataPojo;
import com.ims.tasol.networkingexample.model.ListData;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.ims.tasol.networkingexample.retrofit.ServiceClass;
import com.ims.tasol.networkingexample.retrofit.ServiceInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddNewUser extends AppCompatActivity {
    Button btnAdd,btnUpdate;
    DataPojo dataPojo;
    int studentID=0;
    String studentName="";
    String studentAge="";
    String OPR_TYPE=null;
    List<DataPojo> dataList;
    EditText etStudentName,etStudentAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataPojo= new DataPojo();
        setContentView(R.layout.add_student);
        etStudentName=(EditText)findViewById(R.id.etStudentName);
        etStudentAge=(EditText)findViewById(R.id.etStudentAge);

        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        studentID=getIntent().getIntExtra("STUDENT_ID",0);
        OPR_TYPE=getIntent().getStringExtra("OPR_TYPE");
        if(OPR_TYPE.equals("ADD")){
            btnAdd.setVisibility(View.VISIBLE);
        }else {
            btnUpdate.setVisibility(View.VISIBLE);
            studentName=getIntent().getStringExtra("STUDENT_NAME");
            studentAge=getIntent().getStringExtra("STUDENT_AGE");
        }
        etStudentName.setText(studentName);
        etStudentAge.setText(studentAge);
        dataList= new ArrayList<>();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPojo.setUserId(studentID);
                dataPojo.setUserName(etStudentName.getText().toString());
                dataPojo.setUserAge(etStudentAge.getText().toString());
                addUser(dataPojo);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPojo.setUserId(studentID);
                dataPojo.setUserName(etStudentName.getText().toString());
                dataPojo.setUserAge(etStudentAge.getText().toString());
                updateUser(dataPojo);
            }
        });
    }

    public void addUser(DataPojo pojo){
          RaytaApi raytaApi= RaytaServiceClass.getApiService();
        Call<AddUser>call=raytaApi.addupdateUser("addNewUser",pojo.getUserId(),pojo.getUserName(),pojo.getUserId());
        call.enqueue(new Callback<AddUser>() {
            @Override
            public void onResponse(Call<AddUser> call, Response<AddUser> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AddNewUser.this,"User Added",Toast.LENGTH_LONG).show();
                    supportFinishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<AddUser> call, Throwable t) {
                Log.v("@@@Failure"," Message"+t.getMessage());
            }
        });
    }

    public void updateUser(DataPojo pojo){
        RaytaApi raytaApi= RaytaServiceClass.getApiService();
        Call<AddUser>call=raytaApi.addupdateUser("updateUser",pojo.getUserId(),pojo.getUserName(),pojo.getUserId());
        call.enqueue(new Callback<AddUser>() {
            @Override
            public void onResponse(Call<AddUser> call, Response<AddUser> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AddNewUser.this,"User Updated",Toast.LENGTH_LONG).show();
                    supportFinishAfterTransition();
                }
            }

            @Override
            public void onFailure(Call<AddUser> call, Throwable t) {
                Log.v("@@@Failure"," Message"+t.getMessage());
            }
        });
    }

}
