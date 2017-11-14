package com.deepesh.finalproject.Activity.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.deepesh.finalproject.Adapter.UserAdapter;
import com.deepesh.finalproject.Model.TeacherDetails;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    //@InjectView(R.id.btnLogin)
    Button btnUpdate;


    //@InjectView(R.id.eTxtName)
    EditText eTxtName;
    //@InjectView(R.id.eTxtUsername)
    EditText eTxtUname;
    //@InjectView(R.id.eTxtEmail)
    EditText eTxtEmail;

    //@InjectView(R.id.eTxtCity)
    EditText eTxtCity;

    //@InjectView(R.id.eTxtAddr)
    EditText eTxtAddr;
    //@InjectView(R.id.imageLocation)
    ImageView imgLoc;


    //@InjectView(R.id.eTxtSub)
    EditText eTxtSubj;

    //@InjectView(R.id.eTxtMob)
    EditText eTxtMob;
    //@InjectView(R.id.eTxtPassword)
    EditText eTxtPassword;

    //ArrayList<Teachers> teacheList;


    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    //Teachers teachers;
    FirebaseUser fbUser;
    FirebaseDatabase fDB;
    DatabaseReference nDatabase;
    DatabaseReference n1Database;
    FirebaseAuth nAuth;
    String id;

    TeacherDetails teacherDetails;

    String name,uname,pass,email,city,addr,subj,mob;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //ButterKnife.inject(this.getActivity());
        eTxtName=(EditText)view.findViewById(R.id.eTxtName);
        eTxtUname=(EditText)view.findViewById(R.id.eTxtUsername);
        eTxtPassword=(EditText)view.findViewById(R.id.eTxtPassword);
        eTxtEmail=(EditText)view.findViewById(R.id.eTxtEmail);
        eTxtCity=(EditText)view.findViewById(R.id.eTxtCity);
        eTxtAddr=(EditText)view.findViewById(R.id.eTxtAddr);
        eTxtSubj=(EditText)view.findViewById(R.id.eTxtSub);
        eTxtMob=(EditText)view.findViewById(R.id.eTxtMob);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdate);


        //Intent rcv=getActivity().getIntent();
        //teachers=(Teachers)rcv.getSerializableExtra(Util.KEY_USER);
        //Toast.makeText(getContext(), "rcv Data  "+teachers, Toast.LENGTH_SHORT).show();

        //eTxtUname.setText(teachers.getUname());
        //eTxtPassword.setText(teachers.getPass());

        teacherDetails=new TeacherDetails();
        nAuth=FirebaseAuth.getInstance();
        fbUser=nAuth.getCurrentUser();
        id=fbUser.getUid();
        retrieveTeacher();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Toast.makeText(getActivity(),"Displaying Toast: ",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

    private void retrieveTeacher(){
        nDatabase=fDB.getInstance().getReference().child("teacherDetails").child(id);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                name=teacherDetails.getName().toString().trim();
                uname=teacherDetails.getUname().toString().trim();
                pass=teacherDetails.getPass().toString().trim();
                email=teacherDetails.getEmail().toString().trim();
                city=teacherDetails.getCity().toString().trim();
                addr=teacherDetails.getAddr().toString().trim();
                subj=teacherDetails.getSubj().toString().trim();
                mob=teacherDetails.getMob().toString().trim();
                /*
                Toast.makeText(getContext(),"Name: "+teacherDetails.getName(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Username: "+teacherDetails.getUname(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Password: "+teacherDetails.getPass(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Email: "+teacherDetails.getEmail(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"City: "+teacherDetails.getCity(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Address: "+teacherDetails.getAddr(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Subject: "+teacherDetails.getSubj(),Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(),"Mobile: "+teacherDetails.getMob(),Toast.LENGTH_LONG).show();*/

                eTxtName.setText(name);
                eTxtUname.setText(uname);
                eTxtPassword.setText(pass);
                eTxtEmail.setText(email);
                eTxtCity.setText(city);
                eTxtAddr.setText(addr);
                eTxtSubj.setText(subj);
                eTxtMob.setText(mob);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
