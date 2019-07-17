package com.deepesh.finalproject.Activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.deepesh.finalproject.Activity.Fragment.AllTeachersFragment;
import com.deepesh.finalproject.Activity.Fragment.DetailsFragment;
import com.deepesh.finalproject.Activity.Fragment.SearchFragment;
import com.deepesh.finalproject.Activity.Fragment.StudentsFragment;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;


@TargetApi(Build.VERSION_CODES.M)
public class FingerprintAuthenticationHandler extends FingerprintManager.AuthenticationCallback {



    private Context context;

    FingerPrintActivity fingerPrintActivity=new FingerPrintActivity();


    // Constructor
    public FingerprintAuthenticationHandler(Context mContext) {
        context = mContext;
    }



    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void passDataForTeacher(){
        Intent intent1=new Intent(context,MainActivity.class);
        intent1.putExtra(Util.KEY_T$S,"teacher");
        Intent intent2=new Intent(context,AllTeachersFragment.class);
        intent2.putExtra(Util.KEY_T$S,"teacher");
        Intent intent3=new Intent(context,DetailsFragment.class);
        intent3.putExtra(Util.KEY_T$S,"teacher");
        Intent intent4=new Intent(context,StudentsFragment.class);
        intent4.putExtra(Util.KEY_T$S,"teacher");
        Intent intent5=new Intent(context,SearchFragment.class);
        intent5.putExtra(Util.KEY_T$S,"teacher");

        context.startActivity(intent1);




    }
    public void passDataForStudent(){
        Intent intent1=new Intent(context,MainActivity.class);
        intent1.putExtra(Util.KEY_T$S,"student");
        Intent intent2=new Intent(context,AllTeachersFragment.class);
        intent2.putExtra(Util.KEY_T$S,"student");
        Intent intent3=new Intent(context,DetailsFragment.class);
        intent3.putExtra(Util.KEY_T$S,"student");
        Intent intent4=new Intent(context,StudentsFragment.class);
        intent4.putExtra(Util.KEY_T$S,"student");
        Intent intent5=new Intent(context,SearchFragment.class);
        intent5.putExtra(Util.KEY_T$S,"student");


        context.startActivity(intent1);

    }

    public void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.eTxtFingerPrint);
        textView.setText(e);
        if(success){
            //textView.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
            if (fingerPrintActivity.getfwpusername.equalsIgnoreCase("teacher")) {
                passDataForTeacher();
            } else if (fingerPrintActivity.getfwpusername.equalsIgnoreCase("student")) {
                passDataForStudent();
            } else {
                Toast.makeText(context, "getIntent in Null", Toast.LENGTH_SHORT).show();
            }
            /*Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);*/
            Toast.makeText(context, "Fingerprint Authentication successful .", Toast.LENGTH_SHORT).show();
        }
    }
}