package com.ims.tasol.networkingexample.multilanguage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ims.tasol.networkingexample.VolleyMain;
import com.ims.tasol.networkingexample.model.DataPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ims.tasol.networkingexample.VolleyMain.JSON_URL;

/**
 * Created by tasol on 3/4/17.
 */

public class LanguageService extends Service {
    List<DataPojo> dataList= new ArrayList<>();
    String languageType="";
    String lang="";
    String text="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v("@@@WWE"," Service Called ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        String url="https://translate.yandex.net/api/v1.5/tr.json/translate";
        final String key="trnsl.1.1.20170403T061115Z.d68673df9a328e43.08e4aba8d201329f0eeaf6f3e439e401f83859a9";
        languageType=(String) intent.getExtras().get("language");
//        text=(String) intent.getExtras().get("text");

        switch (languageType){
            case "hindi":
                lang="en-hi";
                break;
            case "gujarati":
                lang="en-gu";
                break;
        }


        StringRequest request = new StringRequest(JSON_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("@@@Response"," From Service "+response.toString());

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


                getFavouriteLanguage(response,languageType);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("@@@Error"," "+error.toString());
            }
        }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);


//        getFavouriteLanguage("Hello We are Gujarati","es");



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service is being destroyed",Toast.LENGTH_LONG).show();
    }


    private void getFavouriteLanguage(final String text, String languageFrom ){
        String url="https://translate.yandex.net/api/v1.5/tr.json/translate";
        final String key="trnsl.1.1.20170403T061115Z.d68673df9a328e43.08e4aba8d201329f0eeaf6f3e439e401f83859a9";
        final String lang="en-"+languageFrom;
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
                Log.v("@@@WWE"," Service Translated Output "+response.toString());
                Log.v("@@@WWE","Service  Translated Output "+response.toString());

                Intent intentBroadcast=new Intent(LanguageService.this,VolleyMain.class);
                intentBroadcast.putExtra("data",response.toString());
                sendBroadcast(intentBroadcast);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("@@@WWE","Service Translated Output "+error.getMessage().toString());
                Log.v("@@@WWE","Service  Translated Output "+error.getMessage().toString());
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


        RequestQueue requestQueue=Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }
}
