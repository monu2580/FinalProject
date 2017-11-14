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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener , LocationListener{

    private static final String TAG = "RegisterActivity";
    private FirebaseDatabase fDB;
    private DatabaseReference nDatabase;
    private FirebaseAuth nAuth;

    private Button btnLogin;
    private Button btnRegister;

    private EditText eTxtName;
    private EditText eTxtUname;

    private EditText eTxtCity;
    private EditText eTxtAddr;
    private Button btnLoc;
    private EditText eTxtPass;
    private EditText eTxtEmail;
    private EditText eTxtSubj;
    private EditText eTxtMob;

    LocationManager locatiomanager;
    Double latitude, longitude;

    TeacherDetails teacherDetails;


/*

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

    @InjectView(R.id.eTxtCity)
    EditText eTxtCity;

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
*/

    RequestQueue requestQueue;
    StringRequest request;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    //String url = "https://asymmetrical-steril.000webhostapp.com/User/insert.php";
    Teachers teachers;

    int id=0;
    String name, uname,email,city,pass,mob,addr,subj,currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        nDatabase = FirebaseDatabase.getInstance().getReference();
        nAuth = FirebaseAuth.getInstance();

        eTxtName=(EditText)findViewById(R.id.eTxtName);
        eTxtUname=(EditText)findViewById(R.id.eTxtUsername);
        eTxtPass=(EditText)findViewById(R.id.eTxtPassword);
        eTxtEmail=(EditText)findViewById(R.id.eTxtEmail);
        eTxtCity=(EditText)findViewById(R.id.eTxtCity);
        eTxtAddr=(EditText)findViewById(R.id.eTxtAddr);
        eTxtSubj=(EditText)findViewById(R.id.eTxtSub);
        eTxtMob=(EditText)findViewById(R.id.eTxtMob);
        
        btnLoc=(Button)findViewById(R.id.btnLocation);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        /*ButterKnife.inject(this);
        requestQueue = Volley.newRequestQueue(this);
*/
        //teachers=new Teachers();
        teacherDetails=new TeacherDetails();
        locatiomanager=(LocationManager)getSystemService(LOCATION_SERVICE);

        btnLoc.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();
    }
    private void RegisterTeacher() {
        Log.d(TAG, "RegisterTeacher");
        if (!validateForm()) {
            return;
        }




        //showProgressDialog();
        name=eTxtName.getText().toString().trim(); //it shoud not be declare in OnCreate() caught it will not be initilaized
        uname=eTxtUname.getText().toString().trim();
        pass=eTxtPass.getText().toString().trim();
        email=eTxtEmail.getText().toString().trim();
        city=eTxtCity.getText().toString().trim();
        addr=eTxtAddr.getText().toString().trim();
        subj=eTxtSubj.getText().toString().trim();
        mob=eTxtMob.getText().toString().trim();

        nAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    teacherDetails=new TeacherDetails(name,uname,pass,email,city,addr,subj,mob);
                    FirebaseUser fUser=nAuth.getCurrentUser();
                    nDatabase.child("teacherDetails").child(fUser.getUid()).setValue(teacherDetails);

                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    intent.putExtra(Util.KEY_USER,teacherDetails);
                    startActivity(intent);

                    Toast.makeText(RegisterActivity.this, "Teacher Registration Successful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Teacher Registration unSuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                teacherDetails = dataSnapshot.getValue(TeacherDetails.class);
                System.out.println(teacherDetails.toString());
                //Log.d(TAG, "Value is: " + userDetails.toString());
                //Toast.makeText(RegisterActivity.this, "Teachers Details"+userDetails, Toast.LENGTH_SHORT).show();


                /*for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });






    }

    /*
    public void RegisterTeacher(){

        String url  = "https://asymmetrical-steril.000webhostapp.com/Teachers/insert.php";
        //String url1 = "https://asymmetrical-steril.000webhostapp.com/Teachers/insert.php";
        name=eTxtName.getText().toString().trim(); //it shoud not be declare in OnCreate() caught it will not be initilaized
        uname=eTxtUname.getText().toString().trim();
        pass=eTxtPass.getText().toString().trim();
        email=eTxtEmail.getText().toString().trim();
        city=eTxtCity.getText().toString().trim();
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
                map.put("city",city);
                map.put("addr",addr);
                map.put("subj",subj);
                map.put("mob",mob);
                return map;
            }
        };

        requestQueue.add(request);
    }*/

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(eTxtName.getText().toString())) {
            eTxtName.setError("Required");
            result = false;
        } else {
            eTxtName.setError(null);
        }

        if (TextUtils.isEmpty(eTxtUname.getText().toString())) {
            eTxtUname.setError("Required");
            result = false;
        } else {
            eTxtUname.setError(null);
        }
        if (TextUtils.isEmpty(eTxtPass.getText().toString())) {
            eTxtPass.setError("Required");
            result = false;
        } else {
            eTxtPass.setError(null);
        }

        if (TextUtils.isEmpty(eTxtEmail.getText().toString())) {
            eTxtEmail.setError("Required");
            result = false;
        } else {
            eTxtEmail.setError(null);
        }
        if (TextUtils.isEmpty(eTxtCity.getText().toString())) {
            eTxtCity.setError("Required");
            result = false;
        } else {
            eTxtCity.setError(null);
        }
        if (TextUtils.isEmpty(eTxtAddr.getText().toString())) {
            eTxtAddr.setError("Required");
            result = false;
        } else {
            eTxtAddr.setError(null);
        }
        if (TextUtils.isEmpty(eTxtSubj.getText().toString())) {
            eTxtSubj.setError("Required");
            result = false;
        } else {
            eTxtSubj.setError(null);
        }
        if (TextUtils.isEmpty(eTxtMob.getText().toString())) {
            eTxtMob.setError("Required");
            result = false;
        } else {
            eTxtMob.setError(null);
        }

        return result;
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
                currentLocation=buffer.toString();
                eTxtAddr.setText(""+buffer.toString());
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
                startActivity(intent1);
                teachers.setName(name);
                teachers.setUname(uname);
                teachers.setPass(pass);
                teachers.setEmail(email);
                teachers.setCity(city);
                teachers.setAddr(addr);
                teachers.setSubj(subj);
                teachers.setMob(mob);*/
                RegisterTeacher();
                break;
            case R.id.btnLocation:
                locatiomanager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location not fetch due to permission issues", Toast.LENGTH_SHORT).show();
                }else {
                    locatiomanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, this);
                    //eTxtAddr.setText(locatiomanager);
                    Toast.makeText(this, "Current location"+currentLocation, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


}
