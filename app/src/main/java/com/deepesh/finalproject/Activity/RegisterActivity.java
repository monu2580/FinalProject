package com.deepesh.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener , LocationListener{

    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;

    @InjectView(R.id.eTxtName)
    EditText eTxtName;
    @InjectView(R.id.eTxtUsername)
    EditText eTxtUname;
    @InjectView(R.id.eTxtEmail)
    EditText eTxtEmail;

    @InjectView(R.id.eTxtAddr)
    EditText eTxtAddr;
    @InjectView(R.id.imageLocation)
    ImageView imgLoc;
    LocationManager locatiomanager;
    Double latitude, longitude;


    @InjectView(R.id.eTxtSub)
    EditText eTxtSubj;

    @InjectView(R.id.eTxtMob)
    EditText eTxtMob;
    @InjectView(R.id.eTxtPassword)
    EditText eTxtPassword;

    RequestQueue requestQueue;
    StringRequest request;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    //String url = "https://asymmetrical-steril.000webhostapp.com/User/insert.php";
    Teachers teachers;

    String name, uname,email,pass,mob,addr,subj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.inject(this);
        requestQueue = Volley.newRequestQueue(this);

        teachers=new Teachers();
        locatiomanager=(LocationManager)getSystemService(LOCATION_SERVICE);

        imgLoc.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
    }
    public void RegisterTeacher(){

        String url  = "https://asymmetrical-steril.000webhostapp.com/Teachers/insert.php";
        //String url1 = "https://asymmetrical-steril.000webhostapp.com/Teachers/insert.php";
        name=eTxtName.getText().toString().trim(); //it shoud not be declare in OnCreate() caught it will not be initilaized
        uname=eTxtUname.getText().toString().trim();
        pass=eTxtPassword.getText().toString().trim();
        email=eTxtEmail.getText().toString().trim();
        addr=eTxtAddr.getText().toString().trim();
        subj=eTxtSubj.getText().toString().trim();
        mob=eTxtMob.getText().toString().trim();

        request=new StringRequest(Request.Method.POST, Util.REGISTER_ENDPOINT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();

                    if(success == 1){

                        editor.putBoolean(Util.KEY_LOGREG,true);
                        editor.commit();

                        //Intent intent = new Intent(RegisterActivity.this,HomeActivityTwo.class);
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this,"Exception: "+e,Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"Error: "+error,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("uname",uname);
                map.put("pass",pass);
                map.put("email",email);
                map.put("addr",addr);
                map.put("subj",subj);
                map.put("mob",mob);
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
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegister:
                /*Intent intent1=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent1);*/

                teachers.setName(name);
                teachers.setUname(uname);
                teachers.setPass(pass);
                teachers.setEmail(email);
                teachers.setAddr(addr);
                teachers.setSubj(subj);
                teachers.setMob(mob);
                RegisterTeacher();
                break;
            case R.id.imageView:
                locatiomanager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }else {
                    locatiomanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, this);
                }
                break;

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();

        //txt.setText("Location is"+latitude+"--"+longitude);
        try {
            Geocoder geocoder=new Geocoder(this);
            List<Address> adrList=geocoder.getFromLocation(latitude,longitude,5);
            if((adrList!=null)&&(adrList.size())>0){
                Address addr=adrList.get(0);
                StringBuffer buffer=new StringBuffer();
                for(int i=0;i<addr.getMaxAddressLineIndex();i++){
                    buffer.append(addr.getAddressLine(i));
                }
                eTxtAddr.setText(" "+buffer.toString());
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
