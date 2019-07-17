package com.deepesh.finalproject.Activity.Fragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.deepesh.finalproject.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {

    CircleImageView profilpic;
    TextView txtName;
    TextView txtEmail;
    TextView txtSubj;
    TextView txtCity;
    TextView txtRate;
    TextView txtMob;
    String uid,imgUrl,name,subj,email,city,rate,mob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        profilpic=(CircleImageView)findViewById(R.id.imageViewTeacher);
        txtName=(TextView)findViewById(R.id.txtName);
        txtEmail=(TextView)findViewById(R.id.txtEmail);
        txtSubj=(TextView)findViewById(R.id.txtSubj);
        txtCity=(TextView)findViewById(R.id.txtCity);
        txtRate=(TextView)findViewById(R.id.txtRating);
        txtMob=(TextView)findViewById(R.id.txtMob);

        Intent rcv=getIntent();
        uid=rcv.getStringExtra("uUid");
        imgUrl=rcv.getStringExtra("uPic");
        name=rcv.getStringExtra("uName");
        email=rcv.getStringExtra("uEmail");
        subj=rcv.getStringExtra("uSubj");
        city=rcv.getStringExtra("uCity");
        rate=rcv.getStringExtra("uRating");
        mob=rcv.getStringExtra("uMob");

        UserProfile();


    }

    private void UserProfile(){


        Glide.with(this).load(imgUrl).fitCenter().centerCrop().into(profilpic);
        txtName.setText(name);
        txtSubj.setText(subj);
        txtEmail.setText(email);
        txtCity.setText(city);
        txtRate.setText(rate);
        txtMob.setText(mob);
        txtRate.setText("5.0");

    }
}
