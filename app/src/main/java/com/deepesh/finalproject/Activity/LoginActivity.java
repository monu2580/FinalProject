package com.deepesh.finalproject.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.deepesh.finalproject.Activity.Fragment.AllTeachersFragment;
import com.deepesh.finalproject.Activity.Fragment.DetailsFragment;
import com.deepesh.finalproject.Activity.Fragment.SearchFragment;
import com.deepesh.finalproject.Activity.Fragment.StudentsFragment;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    private static final String TAG = "LogiActivity";

    //private FirebaseDatabase fDB;
    private DatabaseReference nDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference n1Database;
    String thrstd;


    private EditText eTxtUname;
    private EditText eTxtPass;
    private Button btnLogin;
    private Button btnRegister;


    private RadioButton rbStudent;
    private RadioButton rbTeacher;

    private TextView tViewHeading,tViewForgot,tViewLogin;
    private EditText eTxtresetEmail;
    private Button btnReset;
    private CardView cardEmail,cardPass,cardResetEmail;
    private Button visblepass;


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

    String sendUsername;


    TeacherDetails teacherDetails;
    List<TeacherDetails> teacherDetailsList;
    //Teachers teachers;

    FirebaseUser fbUser;
    String id;


    ProgressDialog progressDialog;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        eTxtUname=(EditText)findViewById(R.id.eTxtUsername);
        eTxtPass=(EditText)findViewById(R.id.eTxtPassword);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        rbTeacher=(RadioButton)findViewById(R.id.rbTeacher);
        rbStudent=(RadioButton)findViewById(R.id.rbStudent);

        tViewHeading=(TextView)findViewById(R.id.textView);
        tViewForgot=(TextView)findViewById(R.id.textViewForgate);
        tViewLogin=(TextView)findViewById(R.id.textViewBackToLogin);

        eTxtresetEmail=(EditText)findViewById(R.id.eTxtPasswordReset);
        btnReset=(Button)findViewById(R.id.btnReset);

        cardEmail=(CardView)findViewById(R.id.eCard);
        cardPass=(CardView)findViewById(R.id.pCard);
        cardResetEmail=(CardView)findViewById(R.id.pCardReset);

        visblepass=(Button)findViewById(R.id.visiblePass);

        rbTeacher.setOnClickListener(this);
        rbStudent.setOnClickListener(this);

        tViewForgot.setOnClickListener(this);
        tViewLogin.setOnClickListener(this);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        visblepass.setOnTouchListener(this);


        teacherDetailsList=new ArrayList<TeacherDetails>();
       // ButterKnife.inject(this);
        //teachers=new Teachers();

        //requestQueue = Volley.newRequestQueue(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();

        if (Build.VERSION.SDK_INT >= 23) {
            // Pain in A$$ Marshmallow+ Permission APIs
            checkAndRequestPermissions();

        } else {
            // Pre-Marshmallow

        }

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        //nDatabase=FirebaseDatabase.getInstance().getReference();
        //fbUser=mAuth.getCurrentUser();
        //id=fbUser.getUid();

        //LoginAnonymously();

        progressDialog=new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        //progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.robotlisticon));


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


    //Checking permission & Grant Permission at run time
    private  boolean checkAndRequestPermissions() {
        int internet = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);
        int permissionStorage1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }
        if (permissionStorage1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        //setUpView();

        return true;
    }
    public void LoginTeachers() {
            Log.d(TAG, "LoginTeachers");
            if (!validateForm()) {
                return;
            }
            String email = eTxtUname.getText().toString();
            String password = eTxtPass.getText().toString();
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                            //hideProgressDialog();



                            if (task.isSuccessful()) {

                                editor.putBoolean(Util.KEY_LOG,true);
                                editor.commit();
                                //onAuthSuccess(task.getResult().getUser());
                                retrieveTeacher();
                            } else {
                                Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
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
/*
        String Tablename=fbUser.getUid().getClass().getName();
        System.out.println("Tacle Name : "+Tablename);
        Toast.makeText(this, "Tacle Name : "+Tablename, Toast.LENGTH_SHORT).show();*/

        if(thrstd=="teacher"){

            nDatabase=FirebaseDatabase.getInstance().getReference().child("teacherDetails").child(fbUser.getUid());
            nDatabase.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
            nDatabase.child("uid").setValue(fbUser.getUid());
            nDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Toast.makeText(LoginActivity.this, "DataSnapshot : "+dataSnapshot, Toast.LENGTH_SHORT).show();
                    teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                    sendUsername=teacherDetails.getUname();

                    editor.putString(Util.KEY_ST,"teacher");
                    editor.commit();

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra(Util.KEY_T$S,"teacher");
                    Intent intent0=new Intent(LoginActivity.this,AllTeachersFragment.class);
                    intent0.putExtra(Util.KEY_T$S,"teacher");
                    Intent intent1=new Intent(LoginActivity.this,DetailsFragment.class);
                    intent1.putExtra(Util.KEY_T$S,"teacher");
                    Intent intent2=new Intent(LoginActivity.this,StudentsFragment.class);
                    intent2.putExtra(Util.KEY_T$S,"teacher");
                    Intent intent3=new Intent(LoginActivity.this,SearchFragment.class);
                    intent3.putExtra(Util.KEY_T$S,"teacher");
                    Intent intent4=new Intent(LoginActivity.this,SplashActivity.class);
                    intent4.putExtra(Util.KEY_T$S,"teacher");
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();

                    //Bundle bundle = new Bundle();
                    //bundle.putString(Util.KEY_T$S, sendUsername);
                    //bundle.putString(Util.KEY_T$S,"teacher");
                    //DetailsFragment detailsFragment = new DetailsFragment();
                    //AllTeachersFragment allTeachersFragment=new AllTeachersFragment();
                    //detailsFragment.setArguments(bundle);
                    //allTeachersFragment.setArguments(bundle);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fram1,detailsFragment).commit();

                    //Toast.makeText(LoginActivity.this, "TeacherDetails : "+teacherDetails, Toast.LENGTH_SHORT).show();



                    /*String token= FirebaseInstanceId.getInstance().getToken();
                    System.out.println("Token  :"+token);
                    Toast.makeText(LoginActivity.this, "Token  :"+token, Toast.LENGTH_SHORT).show();*/


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else if(thrstd=="student"){

            nDatabase=FirebaseDatabase.getInstance().getReference().child("studentDetails").child(fbUser.getUid());
            nDatabase.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
            nDatabase.child("uid").setValue(fbUser.getUid());
            nDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Toast.makeText(LoginActivity.this, "DataSnapshot : "+dataSnapshot, Toast.LENGTH_SHORT).show();
                    teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                    //sendUsername=teacherDetails.getUname();

                    editor.putString(Util.KEY_ST,"student");
                    editor.commit();

                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra(Util.KEY_T$S,"student");
                    Intent intent0=new Intent(LoginActivity.this,AllTeachersFragment.class);
                    intent0.putExtra(Util.KEY_T$S,"student");
                    Intent intent1=new Intent(LoginActivity.this,DetailsFragment.class);
                    intent1.putExtra(Util.KEY_T$S,"student");
                    Intent intent2=new Intent(LoginActivity.this,StudentsFragment.class);
                    intent2.putExtra(Util.KEY_T$S,"student");
                    Intent intent3=new Intent(LoginActivity.this,SearchFragment.class);
                    intent3.putExtra(Util.KEY_T$S,"student");
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();


                    /*String token= FirebaseInstanceId.getInstance().getToken();
                    System.out.println("Token  :"+token);
                    Toast.makeText(LoginActivity.this, "Token  :"+token, Toast.LENGTH_SHORT).show();*/

                    /*Bundle bundle = new Bundle();
                    //bundle.putString(Util.KEY_T$S, sendUsername);
                    bundle.putString(Util.KEY_T$S, "student");
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(bundle);*/

                    //Toast.makeText(LoginActivity.this, "StudentDetails  : "+teacherDetails, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }else {
            Toast.makeText(LoginActivity.this, "GetUsername is null", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();

        }

    }

    public void ResetPassword(){
        Log.d(TAG, "LoginTeachers");
        if (!validateEmailForReset()) {
            return;
        }
        progressDialog.setMessage("Resetting..");
        progressDialog.show();
        final String emailForReset=eTxtresetEmail.getText().toString().trim();
        mAuth.sendPasswordResetEmail(emailForReset).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    eTxtresetEmail.setText(null);
                    eTxtresetEmail.setHint("Reset Link has been send Successful");
                    Toast.makeText(LoginActivity.this, "Reset password has sent to your email",
                            Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,
                            "Email don't exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }


    private boolean validateForm() {
        boolean result = true;

        /*if(thrstd.equals(null)){
            rbTeacher.setError("Required");
            rbStudent.setError("Required");
        }else {
            rbTeacher.setError(null);
            rbStudent.setError(null);
        }
*/
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
    //For Reset Email validation
    private boolean validateEmailForReset() {
        boolean result = true;


        if (TextUtils.isEmpty(eTxtresetEmail.getText().toString())) {
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

            case R.id.rbTeacher:
                thrstd="teacher";
                rbStudent.setChecked(false);
                eTxtUname.setText("deep123@abc.com");
                eTxtPass.setText("deep456");
                //Toast.makeText(LoginActivity.this, "you selected "+thrstd, Toast.LENGTH_SHORT).show();

                break;

            case R.id.rbStudent:
                thrstd="student";
                rbTeacher.setChecked(false);

                eTxtUname.setText("monu123@abc.com");
                eTxtPass.setText("monu456");
                //Toast.makeText(LoginActivity.this, "you have selected "+thrstd, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnReset:
                Toast.makeText(LoginActivity.this, "Click on Reset",Toast.LENGTH_SHORT).show();
                    ResetPassword();
                break;

            case R.id.textViewForgate:
                tViewHeading.setText("Reset Password");
                rbStudent.setVisibility(View.INVISIBLE);
                rbTeacher.setVisibility(View.INVISIBLE);
                cardEmail.setVisibility(View.GONE);
                cardPass.setVisibility(View.GONE);
                btnLogin.setVisibility(View.GONE);
                btnRegister.setVisibility(View.GONE);
                tViewForgot.setVisibility(View.GONE);

                cardResetEmail.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.VISIBLE);
                tViewLogin.setVisibility(View.VISIBLE);

                break;

            case R.id.textViewBackToLogin:
                tViewHeading.setText("Login Here...");
                rbStudent.setVisibility(View.VISIBLE);
                rbTeacher.setVisibility(View.VISIBLE);
                cardEmail.setVisibility(View.VISIBLE);
                cardPass.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
                tViewForgot.setVisibility(View.VISIBLE);

                cardResetEmail.setVisibility(View.GONE);
                btnReset.setVisibility(View.GONE);

                tViewLogin.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.visiblePass:
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                                //Toast.makeText(this, "button Pressed", Toast.LENGTH_SHORT).show();
                                //eTxtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                eTxtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            break;
                        case MotionEvent.ACTION_UP:
                                //Toast.makeText(this, "button Realese", Toast.LENGTH_SHORT).show();
                                //eTxtPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                eTxtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            break;
                    }
                break;
        }
        return true;
    }
}
