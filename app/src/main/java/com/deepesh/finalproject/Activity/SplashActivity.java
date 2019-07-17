package com.deepesh.finalproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.deepesh.finalproject.Activity.Fragment.AllTeachersFragment;
import com.deepesh.finalproject.Activity.Fragment.DetailsFragment;
import com.deepesh.finalproject.Activity.Fragment.SearchFragment;
import com.deepesh.finalproject.Activity.Fragment.StudentsFragment;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;
    boolean checkLogin;

    String getfwpusername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Intent rcv =getIntent();

        preferences=getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);

        getfwpusername = preferences.getString(Util.KEY_ST,null);

        //Toast.makeText(SplashActivity.this, "you are  :"+getfwpusername, Toast.LENGTH_SHORT).show();

        checkLogin=preferences.getBoolean(Util.KEY_LOG,false);


        if(checkLogin &&getfwpusername!=null){
            handler.sendEmptyMessageDelayed(102,3000);
        }else {
            handler.sendEmptyMessageDelayed(101,3000);
        }


    }



    public void passDataForTeacher(){
        Intent intent=new Intent(SplashActivity.this,FingerPrintActivity.class);
        intent.putExtra(Util.KEY_T$S,"teacher");
        Intent intent1=new Intent(SplashActivity.this,MainActivity.class);
        intent1.putExtra(Util.KEY_T$S,"teacher");
        Intent intent2=new Intent(SplashActivity.this,AllTeachersFragment.class);
        intent2.putExtra(Util.KEY_T$S,"teacher");
        Intent intent3=new Intent(SplashActivity.this,DetailsFragment.class);
        intent3.putExtra(Util.KEY_T$S,"teacher");
        Intent intent4=new Intent(SplashActivity.this,StudentsFragment.class);
        intent4.putExtra(Util.KEY_T$S,"teacher");
        Intent intent5=new Intent(SplashActivity.this,SearchFragment.class);
        intent5.putExtra(Util.KEY_T$S,"teacher");

        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startActivity(intent1);
            finish();
            Toast.makeText(this, "This Android version does not support fingerprint authentication.", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(intent);
            finish();
        }*/

        startActivity(intent1);
        finish();


    }
    public void passDataForStudent(){
        Intent intent=new Intent(SplashActivity.this,FingerPrintActivity.class);
        intent.putExtra(Util.KEY_T$S,"student");
        Intent intent1=new Intent(SplashActivity.this,MainActivity.class);
        intent1.putExtra(Util.KEY_T$S,"student");
        Intent intent2=new Intent(SplashActivity.this,AllTeachersFragment.class);
        intent2.putExtra(Util.KEY_T$S,"student");
        Intent intent3=new Intent(SplashActivity.this,DetailsFragment.class);
        intent3.putExtra(Util.KEY_T$S,"student");
        Intent intent4=new Intent(SplashActivity.this,StudentsFragment.class);
        intent4.putExtra(Util.KEY_T$S,"student");
        Intent intent5=new Intent(SplashActivity.this,SearchFragment.class);
        intent5.putExtra(Util.KEY_T$S,"student");

        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startActivity(intent1);
            finish();
            Toast.makeText(this, "This Android version does not support fingerprint authentication.", Toast.LENGTH_SHORT).show();
        }else {
            startActivity(intent);
            finish();
        }*/

        startActivity(intent1);
        finish();

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 102){
                if (getfwpusername.equalsIgnoreCase("teacher")) {
                    passDataForTeacher();
                } else if (getfwpusername.equalsIgnoreCase("student")) {
                    passDataForStudent();
                } else {
                    Toast.makeText(SplashActivity.this, "getIntent in SplashActivity in Null", Toast.LENGTH_SHORT).show();
                }
            }else{
                //Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
