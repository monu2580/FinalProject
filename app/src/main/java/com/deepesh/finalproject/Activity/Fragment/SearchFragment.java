package com.deepesh.finalproject.Activity.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.content.Context.MODE_PRIVATE;
import static com.deepesh.finalproject.R.id.eTxtCitySearch;
import static com.deepesh.finalproject.R.id.eTxtPassword;
import static com.deepesh.finalproject.R.id.eTxtSubjSearch;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener{

    ListView stListView;
    EditText stCity;
    EditText stSubj;
    Button btnst;

   //@InjectView(R.id.tListView)
    //ListView tListView;
    Teachers teachers;
    UserAdapter userAdapter;

    ArrayList<Teachers> teacheList;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FirebaseUser fbUser;
    FirebaseDatabase fDB;
    DatabaseReference nDatabase;
    DatabaseReference n1Database;
    FirebaseAuth nAuth;
    String id;

    TeacherDetails teacherDetails;

    public SearchFragment() {
        // Required empty public constructor



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        stListView=(ListView)view.findViewById(R.id.sTListView);
        stCity=(EditText)view.findViewById(R.id.eTxtCitySearch);
        stSubj=(EditText)view.findViewById(R.id.eTxtSubjSearch);
        btnst=(Button) view.findViewById(R.id.btnTSearch);
        btnst.setOnClickListener(this);
        teachers=new Teachers();

        //requestQueue = Volley.newRequestQueue(getContext());
        //tListView=(ListView)view.findViewById(R.id.tListView);
        teacheList=new ArrayList<Teachers>();
        //retrieveFromServer();

        /*nDatabase=fDB.getInstance().getReference();
        n1Database=fDB.getInstance().getReference();*/
        teacherDetails=new TeacherDetails();
        nAuth=FirebaseAuth.getInstance();
        fbUser=nAuth.getCurrentUser();
        id=fbUser.getUid();


        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //requestQueue = Volley.newRequestQueue(getContext());

        //teachers=new Teachers();





        //userAdapter=new UserAdapter(SearchFragment.this),R.layout.list_view,teacheList);
        /*userAdapter=new UserAdapter(this.getContext(),R.layout.list_view,teacheList);
        tListView.setAdapter(userAdapter);*/

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false);




    }


    private void retrieveFromServer(){
        nDatabase=fDB.getInstance().getReference().child("teacherDetails").child(id);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "DataSnapshot"+dataSnapshot, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    /*private void retrieveFromServer() {
        stringRequest=new StringRequest(Request.Method.GET, Util.SEARCH_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_LONG).show();
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");
                    if(success == 1){

                        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("users");

                        int id = 0;
                        String n="",u="",m="",e="",c="",p="",a="",s="";

                        teacheList = new ArrayList<Teachers>();
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
                            //Toast.makeText(getContext(),teachers.toString(),Toast.LENGTH_LONG).show();
                            teacheList.add(teachers);
                        }


                        //userAdapter = new UserAdapter(HomeActivity.this,R.layout.listitem,userList);
                        //listView.setAdapter(userAdapter);

                        userAdapter=new UserAdapter(getContext(),R.layout.list_view,teacheList);
                        stListView.setAdapter(userAdapter);

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
                map.put("city",teachers.getCity());
                map.put("subj",teachers.getSubj());
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }*/

    @Override
    public void onClick(View v) {

        int id=v.getId();
        switch (id) {
            case R.id.btnTSearch:
                /*teachers.setCity(stCity.getText().toString().trim());
                teachers.setSubj(stSubj.getText().toString().trim());*/
                //Toast.makeText(getContext(),teachers.getAddr()+"  "+teachers.getSubj(),Toast.LENGTH_LONG).show();
               retrieveFromServer();
                /*
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);*/
                break;
        }
    }

}
