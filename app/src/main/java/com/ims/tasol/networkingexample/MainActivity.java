package com.ims.tasol.networkingexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ims.tasol.networkingexample.model.Task;
import com.ims.tasol.networkingexample.model.TaskData;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    Button btnAdd,btnRefresh,btnUpload,btnSingleUser;
    ListData listData;
    List<DataPojo> dataList;
    RecyclerView rvStudent;
    UserAdapter userAdapter;
    String studentID=null;
    ProgressBar pbLoader;
    RaytaServiceClass serviceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRefresh=(Button)findViewById(R.id.btnRefresh);
        btnUpload=(Button)findViewById(R.id.btnUpload);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        pbLoader=(ProgressBar)findViewById(R.id.pbLoader);
        btnSingleUser=(Button)findViewById(R.id.btnSingleUser);

        dataList= new ArrayList<>();
        rvStudent=(RecyclerView)findViewById(R.id.rvStudent);
        getAllUsers();
        userAdapter = new UserAdapter();

        LinearLayoutManager layoutManager= new LinearLayoutManager(MainActivity.this);
        rvStudent.setLayoutManager(layoutManager);

        rvStudent.setAdapter(userAdapter);



        btnSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSingleUser();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent picIntent= new Intent(MainActivity.this,UploadImageActivity.class);
                startActivity(picIntent);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this," Call Initiated ",Toast.LENGTH_LONG).show();
                getAllUsers();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
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

    public void getAllUsers(){
        RaytaApi service= RaytaServiceClass.getApiService();
        Call<ListData> call=service.getAllUser();
        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                pbLoader.setVisibility(View.GONE);
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

    public void getSingleUser(){
//        TaskData taskData = new TaskData();
//        taskData.setUserID("1");
//        Task task= new Task();
//        task.setTask("singleUser");
//        task.setTaskData(taskData);
//
//        RaytaApi service= RaytaServiceClass.getApiService();
//        Call<ListData> call=service.getSingleUserObj("",task);
//        call.enqueue(new Callback<ListData>() {
//            @Override
//            public void onResponse(Call<ListData> call, Response<ListData> response) {
//                Log.v("@@@","Response");
//                if (response.isSuccessful()){
//                    Log.v("@@@","Sucess Single User Details");
//                    dataList=response.body().getData();
//                    printStudentDetails(dataList);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ListData> call, Throwable t) {
//                Log.v("@@@","Response");
//            }
//        });
    }
    public void printStudentDetails(List<DataPojo> list){
        Log.v("@@@WWe","Student List");
        for (DataPojo dataPojo:list){
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
                            viewHolder.pbProfileImage.setVisibility(View.VISIBLE);
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
        public Button btnDelete,btnUpdate;
        public ImageView profileImage;
        public ProgressBar pbProfileImage;
        public UserViewHolder(View itemView) {
            super(itemView);
            btnDelete=(Button)itemView.findViewById(R.id.btnDelete);
            btnUpdate=(Button)itemView.findViewById(R.id.btnUpdate);
            studentName=(TextView)itemView.findViewById(R.id.studentName);
            studentAge=(TextView)itemView.findViewById(R.id.studenAge);
            profileImage=(ImageView)itemView.findViewById(R.id.profileImage);
            pbProfileImage=(ProgressBar) itemView.findViewById(R.id.pbProfileImage);
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
}
