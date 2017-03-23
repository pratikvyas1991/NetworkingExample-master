package com.ims.tasol.networkingexample;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Button btnRefresh,btnSingleUser;
    ListData listData;
    List<DataPojo> dataList;
    RecyclerView rvStudent;
    UserAdapter userAdapter;
    String studentID=null;
    ProgressBar pbLoader;
    FloatingActionButton fbAddUser;
    SwipeRefreshLayout swipeRefresh;
    RaytaServiceClass serviceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefresh=(Button)findViewById(R.id.btnRefresh);
        pbLoader=(ProgressBar)findViewById(R.id.pbLoader);
        btnSingleUser=(Button)findViewById(R.id.btnSingleUser);

        fbAddUser=(FloatingActionButton)findViewById(R.id.fbAddUser);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        dataList= new ArrayList<>();
        rvStudent=(RecyclerView)findViewById(R.id.rvStudent);
        getAllUsers();
        userAdapter = new UserAdapter();

        LinearLayoutManager layoutManager= new LinearLayoutManager(MainActivity.this);
        rvStudent.setLayoutManager(layoutManager);

        rvStudent.setAdapter(userAdapter);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUsers();
            }
        });

        btnSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSingleUser();
            }
        });


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this," Call Initiated ",Toast.LENGTH_LONG).show();
                getAllUsers();
            }
        });
        fbAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentID=String.valueOf(dataList.size()+1);
                Intent intent = new Intent(MainActivity.this,AddNewUser.class);
                intent.putExtra("STUDENT_ID",Integer.parseInt(studentID));
                intent.putExtra("OPR_TYPE","ADD");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pbLoader.setVisibility(View.VISIBLE);
        getAllUsers();
    }

    public void getAllUsers(){
        RaytaApi service= RaytaApiClient.getApiService();
        Call<ListData> call=service.getAllUser();
        Log.v("@@@WWE","Retrofit Request Method =  "+call.request().method());
        Log.v("@@@WWE","Retrofit Request Body =  "+call.request().body());
        Log.v("@@@WWE","Retrofit Request Url = "+call.request().url());
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                pbLoader.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Log.v("@@@","Response");
                if (response.isSuccessful()){
                    Log.v("@@@","Sucess");
                    dataList=response.body().getData();
                    userAdapter.notifyDataSetChanged();
                    printStudentDetails(dataList);
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                Log.v("@@@","Response");
                pbLoader.setVisibility(View.GONE);
            }
        });
    }


    public void getSingleUserAd(){
        TaskData taskData = new TaskData();
        taskData.setUserID("1");
        Task task= new Task();
        task.setTask("singleUser");
        task.setTaskData(taskData);

        RaytaApi service= RaytaApiClient.getApiService();

        HashMap<String,Task> taskMap = new HashMap<>();
        taskMap.put("reqObject",task);

        Call<StudentDataList>call=service.getSingleUserHash(task);

        String url=call.request().url().toString();
        String subUrl=url.substring((url.indexOf("?")+1));
        List<String> paramList= Arrays.asList(subUrl.split("&"));
        Log.v("@@@WWE","SingleUser Request Call : "+call.request().toString());
        Log.v("@@@WWE","SingleUser Request Method : "+call.request().method().toString());
        Log.v("@@@WWE","SingleUser Request Url : "+url);
        Log.v("@@@WWE","Request Sub Url : "+subUrl);

        for (int i=0;i<paramList.size();i++){
            Log.v("@@@WWE","Params : "+paramList.get(i));
        }


        call.enqueue(new Callback<StudentDataList>() {
            @Override
            public void onResponse(Call<StudentDataList> call, Response<StudentDataList> response) {

                Log.v("@@@WWE","Response SingleUser");
                List<StudentData> list=new ArrayList<StudentData>();
                if (response.isSuccessful()){
                    Log.v("@@@WWE","Sucess Single User Details");
                    list=response.body().getData();
                    printSingleStudentDetails(list);
                }
            }

            @Override
            public void onFailure(Call<StudentDataList> call, Throwable t) {
                Log.v("@@@WWE","Failure SingleUser");
                Log.v("@@@WWE","Failure Message "+t.getMessage());
            }
        });
    }

    public void getSingleUser(){
        TaskData taskData = new TaskData();
        taskData.setUserID("1");
        Task task= new Task();
        task.setTask("singleUser");
        task.setTaskData(taskData);

        RaytaApi service= RaytaApiClient.getApiService();


        Call<StudentDataList>call=service.getSingleUserObj(task);

        String url=call.request().url().toString();
        String subUrl=url.substring((url.indexOf("?")+1));
        List<String> paramList= Arrays.asList(subUrl.split("&"));
        Log.v("@@@WWE","Request Call : "+call.request().toString());
        Log.v("@@@WWE","Request Method : "+call.request().method().toString());
        Log.v("@@@WWE","Request Url : "+url);
        Log.v("@@@WWE","Request Sub Url : "+subUrl);

        for (int i=0;i<paramList.size();i++){
            Log.v("@@@WWE","Params : "+paramList.get(i));
        }


        call.enqueue(new Callback<StudentDataList>() {
            @Override
            public void onResponse(Call<StudentDataList> call, Response<StudentDataList> response) {

                Log.v("@@@WWE","Response");
                List<StudentData> list=new ArrayList<StudentData>();
                if (response.isSuccessful()){
                    Log.v("@@@WWE","Sucess Single User Details");
                    list=response.body().getData();
                    printSingleStudentDetails(list);
                }
            }

            @Override
            public void onFailure(Call<StudentDataList> call, Throwable t) {
                Log.v("@@@WWE","Failure ");
                Log.v("@@@WWE","Failure Message "+t.getMessage());
            }
        });
    }

    public void printStudentDetails(List<DataPojo> list){
        Log.v("@@@WWe","Student List");
        for (DataPojo dataPojo:list){
            Log.d("Student ID ",String.valueOf(dataPojo.getUserId()));
            Log.d("Student Name ",dataPojo.getUserName());
            Log.d("Student Age ",dataPojo.getUserAge());
        }
    }
    public void printSingleStudentDetails(List<StudentData> list){
        Log.v("@@@WWe","Student List");
        for (StudentData dataPojo:list){
            Log.d("Student ID ",String.valueOf(dataPojo.getUserId()));
            Log.d("Student Name ",dataPojo.getUserName());
            Log.d("Student Age ",dataPojo.getUserAge());
        }
    }



    public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View parentView= LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item,parent,false);
            RecyclerView.ViewHolder holder=new UserViewHolder(parentView);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final UserViewHolder viewHolder=(UserViewHolder)holder;
            viewHolder.studentName.setText(dataList.get(position).getUserName());
            viewHolder.studentAge.setText(dataList.get(position).getUserAge());
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteUser(dataList.get(position).getUserId());
                }
            });
            viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    studentID=String.valueOf(dataList.size()+1);
                    Intent intent = new Intent(MainActivity.this,AddNewUser.class);
                    intent.putExtra("STUDENT_ID",dataList.get(position).getUserId());
                    intent.putExtra("STUDENT_NAME",dataList.get(position).getUserName());
                    intent.putExtra("STUDENT_AGE",dataList.get(position).getUserAge());
                    intent.putExtra("OPR_TYPE","UPDATE");
                    startActivity(intent);
                }
            });

            viewHolder.imgEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(MainActivity.this,UploadImageActivity.class);
                    intent.putExtra("INT_STDID",dataList.get(position).getUserId());
                    startActivity(intent);
                }
            });

            Picasso.with(MainActivity.this)
                    .load(dataList.get(position).getProfilePhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .resize(370,200)
                    .into(viewHolder.profileImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.pbProfileImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            viewHolder.pbProfileImage.setVisibility(View.GONE);
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
    public class UserViewHolder extends RecyclerView.ViewHolder{

        public TextView studentName,studentAge;
        public ImageView btnDelete,btnUpdate;
        public ImageView profileImage,imgEditImage;
        public ProgressBar pbProfileImage;
        public UserViewHolder(View itemView) {
            super(itemView);
            btnDelete=(ImageView)itemView.findViewById(R.id.btnDelete);
            btnUpdate=(ImageView)itemView.findViewById(R.id.btnUpdate);
            studentName=(TextView)itemView.findViewById(R.id.studentName);
            studentAge=(TextView)itemView.findViewById(R.id.studenAge);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            pbProfileImage=(ProgressBar) itemView.findViewById(R.id.pbProfileImage);
            imgEditImage=(ImageView) itemView.findViewById(R.id.imgEditImage);

        }
    }

    public void deleteUser(int userID){
        RaytaApi raytaApi=RaytaServiceClass.getApiService();
        Call<AddUser>call=raytaApi.deleteUser("deleteUser",userID);
        call.enqueue(new Callback<AddUser>() {
            @Override
            public void onResponse(Call<AddUser> call, Response<AddUser> response) {
                Log.v("@@@","Response");
                if (response.isSuccessful()){
                    Log.v("@@@","Sucess");
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AddUser> call, Throwable t) {
                Log.v("@@@","Response");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_add_single){
//            getAllUsers();
            getSingleUserAd();
        }
        return true;
    }
}
