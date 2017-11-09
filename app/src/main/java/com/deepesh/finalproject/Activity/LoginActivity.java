package com.deepesh.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;

    @InjectView(R.id.eTxtUsername)
    EditText eTxtUname;

    @InjectView(R.id.eTxtPassword)
    EditText eTxtPassword;

    RequestQueue requestQueue;
    StringRequest request;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;



    Teachers teachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);
        teachers=new Teachers();

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

       /*preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();*/


    }
    public void LoginTeachers(){

        request=new StringRequest(Request.Method.POST, Util.LOGIN_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();

                    if(success == 1){

                        /*editor.putBoolean(Util.KEY_LOGREG,true);
                        editor.commit();*/

                         Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        //Intent intent = new Intent(LoginActivity.this,SearchTeachersActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(LoginActivity.this,"Some Volley Error"+error,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put("uname",teachers.getUname());
                map.put("pass",teachers.getPass());

                return map;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btnLogin:
                teachers.setUname(eTxtUname.getText().toString().trim());
                teachers.setPass(eTxtPassword.getText().toString().trim());

                LoginTeachers();/*
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);*/
                break;
            case R.id.btnRegister:
                Intent intent1=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;

        }
    }
}
