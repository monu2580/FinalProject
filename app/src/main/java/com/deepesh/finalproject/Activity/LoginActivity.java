package com.deepesh.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LogiActivity";

    //private FirebaseDatabase fDB;
    private DatabaseReference nDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference n1Database;


    private EditText eTxtUname;
    private EditText eTxtPass;
    private Button btnLogin;
    private Button btnRegister;
    /*
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;

    @InjectView(R.id.eTxtUsername)
    EditText eTxtUname;

    @InjectView(R.id.eTxtPassword)
    EditText eTxtPassword;
*/
    RequestQueue requestQueue;
    StringRequest request;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    TeacherDetails teacherDetails;
    List<TeacherDetails> teacherDetailsList;
    //Teachers teachers;

    FirebaseUser fbUser;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        eTxtUname=(EditText)findViewById(R.id.eTxtUsername);
        eTxtPass=(EditText)findViewById(R.id.eTxtPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);



        teacherDetailsList=new ArrayList<TeacherDetails>();
       // ButterKnife.inject(this);
        //teachers=new Teachers();

        //requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();


        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        //nDatabase=FirebaseDatabase.getInstance().getReference();
        //fbUser=mAuth.getCurrentUser();
        //id=fbUser.getUid();

        //LoginAnonymously();

    }

    /*public void LoginAnonymously(){
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Anonymously Successful.. ", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, "Login Anonymously Successful.. " + teacherDetails);
                }else {
                    Toast.makeText(LoginActivity.this, "Login Anonymously unSuccessful.. ", Toast.LENGTH_SHORT).show();

                    //Log.d(TAG, "Login Anonymously unSuccessful.. " + teacherDetails);
                }
            }
        });
    }
*/

    public void LoginTeachers() {
            Log.d(TAG, "LoginTeachers");
            if (!validateForm()) {
                return;
            }
            String email = eTxtUname.getText().toString();
            String password = eTxtPass.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                            //hideProgressDialog();

                            if (task.isSuccessful()) {
                                //onAuthSuccess(task.getResult().getUser());
                                retrieveTeacher();
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                                //Toast.makeText(LoginActivity.this, "TeacherDetails: "+teacherDetails, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                });





       /* nDatabase=FirebaseDatabase.getInstance().getReference().child("teacherDetails").child(fbUser.getUid());
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Toast.makeText(LoginActivity.this, "DataSnapshot : "+dataSnapshot, Toast.LENGTH_SHORT).show();

                *//*Iterator<DataSnapshot> dataSnapshotIterator=dataSnapshot.getChildren().iterator();
                while (dataSnapshotIterator.hasNext()){
                    teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                    teacherDetailsList.add(teacherDetails);

                }*//*


                *//*System.out.println(dataSnapshot);
                dataSnapshot.child("city").getValue();
                System.out.println(dataSnapshot.child("city").getValue());
                Toast.makeText(LoginActivity.this, "Teachers Details"+teacherDetailsList, Toast.LENGTH_SHORT).show();*//*

                teacherDetails = dataSnapshot.getValue(TeacherDetails.class);
                System.out.println(teacherDetails);
                //Log.d(TAG, "Value is: " + teacherDetails);
                Toast.makeText(LoginActivity.this, "Teachers Details"+teacherDetails, Toast.LENGTH_SHORT).show();
                //teacherDetailsList.clear();
                *//*for(DataSnapshot ds:dataSnapshot.getChildren()){
                    teacherDetails = ds.getValue(TeacherDetails.class);

                    teacherDetailsList.add(teacherDetails);
                    Toast.makeText(LoginActivity.this, "Value of"+teacherDetailsList, Toast.LENGTH_SHORT).show();

                }*//*



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/




    }
    private void retrieveTeacher(){

        fbUser=mAuth.getCurrentUser();
        nDatabase=FirebaseDatabase.getInstance().getReference().child("teacherDetails").child(fbUser.getUid());
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(LoginActivity.this, "DataSnapshot : "+dataSnapshot, Toast.LENGTH_SHORT).show();
                teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                Toast.makeText(LoginActivity.this, "TeacherDetails: "+teacherDetails, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /*private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
    private void writeNewUser(String userId, String name, String email) {
        TeacherDetails teacherDetails = new TeacherDetails(name,null,email,null,null,null,null,null);

        nDatabase.child("teacherDetails").child(userId).setValue(teacherDetails);

    }
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
*/

    private boolean validateForm() {
        boolean result = true;
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

        return result;
    }



    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btnLogin:

                LoginTeachers();
                break;
            case R.id.btnRegister:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                //intent.putExtra(Util.KEY_USER,teachers);
                startActivity(intent);
                break;

        }
    }
}
