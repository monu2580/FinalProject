package com.deepesh.finalproject.Activity.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTeachersFragment extends Fragment {


    ListView tListView;
    Teachers teachers;
    UserAdapter userAdapter;

    ArrayList<Teachers> teacheList;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public AllTeachersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getContext());
        tListView=(ListView)view.findViewById(R.id.tListView);
        teacheList=new ArrayList<Teachers>();
        retrieveFromServer();
        super.onViewCreated(view, savedInstanceState);
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tListView=(ListView)view.findViewById(R.id.tListView);
        teacheList=new ArrayList<Teachers>();
        retrieveFromServer();
        super.onViewCreated(view, savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_teachers, container, false);
    }


    private void retrieveFromServer() {
        stringRequest=new StringRequest(Request.Method.GET, Util.RETRIEVE_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if(success == 1){

                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        int id = 0;
                        String n="",u="",m="",e="",p="",a="",s="";

                        teacheList = new ArrayList<Teachers>();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            id = jObj.getInt("tid"); // String Inputs are the column names of your table
                            n = jObj.getString("name");
                            u = jObj.getString("uname");
                            m = jObj.getString("mob");
                            p = jObj.getString("email");
                            e = jObj.getString("pass");
                            a = jObj.getString("addr");
                            s = jObj.getString("subj");
                            //p = jObj.getString("password"); //it will give an Error Because it gets tthe data From Server DataBase not from th User.java
                            //so it Must be Column Name of Server DataBase Table

                            teachers = new Teachers(id,n,u,p,e,a,m,s);
                            //Toast.makeText(getContext(),teachers.toString(),Toast.LENGTH_LONG).show();
                            teacheList.add(teachers);
                        }

                        /*userAdapter = new UserAdapter(HomeActivity.this,R.layout.listitem,userList);
                        listView.setAdapter(userAdapter);*/
                        userAdapter=new UserAdapter(getContext(),R.layout.list_view,teacheList);
                        tListView.setAdapter(userAdapter);

                    }else{
                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getContext(),"Exception: "+e,Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(),"Some VolleyError: "+error, Toast.LENGTH_LONG).show();
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
