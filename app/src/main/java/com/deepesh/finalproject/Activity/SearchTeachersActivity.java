package com.deepesh.finalproject.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deepesh.finalproject.Adapter.UserAdapter;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchTeachersActivity extends AppCompatActivity {

    @InjectView(R.id.tListView)
    ListView tListView;
    UserAdapter userAdapter;

    ArrayList<Teachers> teacheList;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //for Update & Delete

    Teachers teachers;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_teachers);
        
        ButterKnife.inject(this);

        requestQueue= Volley.newRequestQueue(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        //tListView.setOnItemClickListener(SearchTeachersActivity.this);

        retrieveFromServer();
    }

    public void retrieveFromServer() {
        stringRequest=new StringRequest(Request.Method.GET, Util.RETRIEVE_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if(success == 1){

                        Toast.makeText(SearchTeachersActivity.this,message,Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        int id = 0;
                        String n="",u="",m="",e="",c="",p="",a="",s="";

                        teacheList = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            id = jObj.getInt("tid"); // String Inputs are the column names of your table
                            n = jObj.getString("name");
                            u = jObj.getString("uname");
                            e = jObj.getString("pass");
                            p = jObj.getString("email");
                            c = jObj.getString("city");
                            a = jObj.getString("addr");
                            m = jObj.getString("mob");
                            s = jObj.getString("subj");
                            //p = jObj.getString("password"); //it will give an Error Because it gets tthe data From Server DataBase not from th User.java
                            //so it Must be Column Name of Server DataBase Table

                            teachers = new Teachers(id,n,u,p,e,c,a,s,m);
                            teacheList.add(teachers);
                        }

                        /*userAdapter = new UserAdapter(HomeActivity.this,R.layout.listitem,userList);
                        listView.setAdapter(userAdapter);*/
                        userAdapter=new UserAdapter(SearchTeachersActivity.this,R.layout.list_view,teacheList);
                        tListView.setAdapter(userAdapter);

                    }else{
                        Toast.makeText(SearchTeachersActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(SearchTeachersActivity.this,"Exception: "+e,Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(SearchTeachersActivity.this,"Some VolleyError: "+error, Toast.LENGTH_LONG).show();
                //Toast.makeText(HomeActivity.this,"Some VolleyError: "+error, Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                //map.put("id",String.valueOf(user.getUid())); nothing wil happen bacause id is not declare as variable
                map.put("tid",String.valueOf(teachers.getTid()));
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
}
