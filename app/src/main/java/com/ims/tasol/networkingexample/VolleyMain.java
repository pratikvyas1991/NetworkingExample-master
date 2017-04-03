package com.ims.tasol.networkingexample;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ims.tasol.networkingexample.model.AddUser;
import com.ims.tasol.networkingexample.model.DataPojo;
import com.ims.tasol.networkingexample.model.ListData;
import com.ims.tasol.networkingexample.model.StudentData;
import com.ims.tasol.networkingexample.model.StudentDataList;
import com.ims.tasol.networkingexample.model.Task;
import com.ims.tasol.networkingexample.model.TaskData;
import com.ims.tasol.networkingexample.multilanguage.LanguageService;
import com.ims.tasol.networkingexample.retrofit.RaytaApi;
import com.ims.tasol.networkingexample.retrofit.RaytaApiClient;
import com.ims.tasol.networkingexample.retrofit.RaytaServiceClass;
import com.ims.tasol.networkingexample.utils.HttpConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VolleyMain extends AppCompatActivity {
    Button btnRefresh,btnSingleUser;
    ListData listData;
    FloatingActionButton fbAddUser;
    RecyclerView rvVolley;
    LinearLayoutManager linearLayoutManager;
    List<DataPojo> dataList= new ArrayList<>();
    public static final String JSON_URL = HttpConstants.baseUrl;
    VolleyAdapter volleyAdapter= new VolleyAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_main);
        rvVolley=(RecyclerView)findViewById(R.id.rvVolley);
        linearLayoutManager = new LinearLayoutManager(VolleyMain.this);
        rvVolley.setLayoutManager(linearLayoutManager);



        fbAddUser=(FloatingActionButton)findViewById(R.id.fbAddUser);
        fbAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();


                startService();
                rvVolley.setAdapter(volleyAdapter);
                volleyAdapter.notifyDataSetChanged();
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

                DataPojo dataPojo = null;
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    JSONObject rowItem=null;
                    for (int i = 0; i < array.length(); i++) {
                        rowItem=array.getJSONObject(i);


                        dataPojo=new DataPojo(Integer.parseInt(rowItem.getString("user_id")),rowItem.getString("user_name"),rowItem.getString("user_age"),rowItem.getString("profile_photo"));
                        dataList.add(dataPojo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                getFavouriteLanguage(response,"en","hi");

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


    private void getFavouriteLanguage(final String text, String languageFrom , String languageTo, final TextView tv){
        String url="https://translate.yandex.net/api/v1.5/tr.json/translate";
        final String key="trnsl.1.1.20170403T061115Z.d68673df9a328e43.08e4aba8d201329f0eeaf6f3e439e401f83859a9";
        final String lang=languageFrom+"-"+languageTo;
        final String[] resposeText = {""};

        StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String name="";
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray arr=jsonObject.getJSONArray("text");
                    name=arr.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("@@@WWE"," Translated Output "+response.toString());
                Log.v("@@@WWE"," Translated Output "+response.toString());
                tv.setText(name.toString());
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("@@@WWE"," Translated Output "+error.getMessage().toString());
                Log.v("@@@WWE"," Translated Output "+error.getMessage().toString());
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params= new HashMap<>();
                params.put("key",key);
                params.put("text",text);
                params.put("lang",lang);

                return params;
            }
        };


        RequestQueue requestQueue=Volley.newRequestQueue(VolleyMain.this);

        requestQueue.add(stringRequest);
    }


    public class VolleyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.volley_item,parent,false);
            RecyclerView.ViewHolder holder=new VolleyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DataPojo dataPojo=dataList.get(position);

            Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/gujarati_1_font.ttf");

            VolleyViewHolder viewHolder=(VolleyViewHolder)holder;

            getFavouriteLanguage(dataPojo.getUserName().toString(),"en","gu",viewHolder.tvName);
            viewHolder.tvName.setTypeface(typeface);
            viewHolder.tvName.setText(dataPojo.getUserName());
            viewHolder.tvAge.setText(dataPojo.getUserAge());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    public class VolleyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvAge;
        public VolleyViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvAge=(TextView)itemView.findViewById(R.id.tvAge);
        }
    }

    public void startService(){
        Intent serviceIntent=new Intent(VolleyMain.this, LanguageService.class);
        serviceIntent.putExtra("language","gujarati");
        serviceIntent.putExtra("text","this is boy");
        startService(serviceIntent);
    }

}
