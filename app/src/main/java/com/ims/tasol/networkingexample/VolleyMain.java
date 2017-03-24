package com.ims.tasol.networkingexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.DataPojo;
import com.ims.tasol.networkingexample.model.ListData;
import com.ims.tasol.networkingexample.model.StudentData;
import com.ims.tasol.networkingexample.model.StudentDataList;
import com.ims.tasol.networkingexample.model.Task;
import com.ims.tasol.networkingexample.model.TaskData;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaApiClient;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.ims.tasol.networkingexample.utils.HttpConstants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VolleyMain extends AppCompatActivity {
    Button btnRefresh,btnSingleUser;
    ListData listData;
    List<DataPojo> dataList;
    FloatingActionButton fbAddUser;
    public static final String JSON_URL = HttpConstants.baseUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_main);


        fbAddUser=(FloatingActionButton)findViewById(R.id.fbAddUser);
        fbAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllUsers();
    }

    public void getAllUsers(){
    }



    public void printStudentDetails(List<DataPojo> list){
        Log.v("@@@WWe","Student List");
        for (DataPojo dataPojo:list){
            Log.d("Student ID ",String.valueOf(dataPojo.getUserId()));
            Log.d("Student Name ",dataPojo.getUserName());
            Log.d("Student Age ",dataPojo.getUserAge());
        }
    }

    private void sendRequest(){
        StringRequest request = new StringRequest(JSON_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("@@@Response"," "+response.toString());
            }
        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("@@@Error"," "+error.toString());
                        Toast.makeText(VolleyMain.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(VolleyMain.this);
        requestQueue.add(request);
    }

}
