package com.deepesh.finalproject.Activity.Fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.deepesh.finalproject.Adapter.UserRecyclerAdapter;
import com.deepesh.finalproject.Model.RecyclerItemClickListener;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.content.Context.MODE_PRIVATE;
import static com.deepesh.finalproject.R.id.eTxtCitySearch;
import static com.deepesh.finalproject.R.id.eTxtPassword;
import static com.deepesh.finalproject.R.id.eTxtSubjSearch;
import static com.deepesh.finalproject.R.id.icon;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, OnMapReadyCallback {


    //ListView stListView1;
    RecyclerView tRecyclerView;
    UserRecyclerAdapter recyclerAdapter;
    int pos;
    MapView map1;

    EditText stCity;
    EditText stSubj;
    Button btnst;

    //@InjectView(R.id.tListView)
    //ListView tListView;
    Teachers teachers;
    UserAdapter userAdapter;


    //ArrayList<Teachers> teacheList;

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


    List<HashMap<String, String>> AllUserslist;
    List<TeacherDetails> tdList5;
    ArrayList<TeacherDetails> AllData= new ArrayList<>();
    ArrayList<TeacherDetails> tdList6;
    String SelectedCourseUserUId;
    List<String> EmergencyContacts;


    String getfwpusername;

    GoogleMap googleMap;
    double latitude;
    double longitude;
    SupportMapFragment supportMapFragment;

    String name, uname, pass, email, city, addr, subj, mob;
    Double lang, lati;

    LinearLayout gmapLayout;


    ArrayList<TeacherDetails> subSrchList;

    public SearchFragment() {
        // Required empty public constructor


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        stCity = (EditText) view.findViewById(R.id.eTxtCitySearch);
        stSubj = (EditText) view.findViewById(R.id.eTxtSubjSearch);
        btnst = (Button) view.findViewById(R.id.btnTSearch);

        gmapLayout=(LinearLayout)view.findViewById(R.id.mapLayout);

        //gmapLayout.setVisibility(View.GONE);
        //map1=(MapView)view.findViewById(R.id.mapView1);
        btnst.setOnClickListener(this);
        teachers = new Teachers();

        tdList6 = new ArrayList<>();
        subSrchList=new ArrayList<>();


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


        Intent rcv = getActivity().getIntent();
        getfwpusername = rcv.getStringExtra("youare");

        //System.out.println("Forward Passing Data to Studentfrag Activity:" + getfwpusername);
        //Toast.makeText(getContext(), "Forward Passing Data to Studentfrag Activity:" + getfwpusername, Toast.LENGTH_SHORT).show();

        //retrieveFromServer();
        RetrieveDataUsingHasMap();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    private void RetrieveDataUsingHasMap() {

        //List<HashMap<String,String>> AllUserslist;
        //String SelectedCourseUserUId;
        tdList5 = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("teacherDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //                        emergencyContactsList = new ArrayList<>();
                AllUserslist = new ArrayList<HashMap<String, String>>();


                if (dataSnapshot.exists()) {
                    AllUserslist.clear();
                    tdList6.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        //tdList5.clear();
                        TeacherDetails userDetails = postSnapShot.getValue(TeacherDetails.class);
                        HashMap<String, String> map = new HashMap<String, String>();

                        //String user_id = postSnapShot.getKey();
                        String name1 = userDetails.getName();
                        String unam1 = userDetails.getUname();
                        String pass1 = userDetails.getPass();
                        String email1 = userDetails.getEmail();
                        String city1 = userDetails.getCity();
                        String addr1 = userDetails.getAddr();
                        String mob1 = userDetails.getMob();
                        String subj1 = userDetails.getSubj();
                        Double lang1 = userDetails.getLang();
                        Double lati1 = userDetails.getLati();
                        String token1= userDetails.getToken();
                        String uid1=userDetails.getUid();

                        //Toast.makeText(getContext(), "lang1  :" + lang1 + "lati1  :" + lati1, Toast.LENGTH_SHORT).show();
                        //longitude=lang1;
                        //latitude=lati1;

                        //map.put("user_id", user_id);
                        map.put("name1", name1);
                        map.put("uname1", unam1);
                        map.put("pass1", pass1);
                        map.put("emai1", email1);
                        map.put("city1", city1);
                        map.put("addr1", addr1);
                        map.put("mob1", mob1);
                        map.put("subj1", subj1);
                        map.put("lang1", String.valueOf(lang1));
                        map.put("lati1", String.valueOf(lati1));
                        map.put("token1", token1);
                        map.put("uid1", uid1);

                        //for user Adapter
                        TeacherDetails td2 = new TeacherDetails(name1, unam1, pass1, email1, city1, addr1, mob1, subj1, lang1, lati1,token1,uid1,null);
                        tdList6.add(td2);

                        AllUserslist.add(map);
                        tdList5.add(userDetails);


                        //if u have another Child
                        for (DataSnapshot teacherUID : postSnapShot.getChildren()) {
                            //pass the all above maping code here
                        }
                    }
                    AllData.clear();
                    AllData.addAll(tdList6);
                    //System.out.println("Total teachers  :" + AllUserslist.size());
                    //Toast.makeText(getContext(), "Total teachers  :" + AllUserslist.size(), Toast.LENGTH_LONG).show();



                    /*String sCity,sSubj;
                    sCity=stCity.getText().toString().trim();
                    sSubj=stSubj.getText().toString().trim();
                    //EmergencyContacts=new ArrayList<>();
                    List<String> courseIdList = new ArrayList<String>();
                    List<String> courseIdList1 = new ArrayList<String>();
                    for (int i = 0; i < AllUserslist.size(); i++) {
                        String course_id_list;
                        String course_id_list1;
                        String course_id_list3;
                        course_id_list = AllUserslist.toString();
                        course_id_list3 = AllUserslist.get(i).get("city1");
                        course_id_list1 = AllUserslist.get(i).get("subj1");
                        //System.out.println("city  "+course_id_list);
                        //System.out.println("sibject  "+course_id_list1);
                        //System.out.println("AllData   "+course_id_list3);
                        courseIdList.add(course_id_list);
                        courseIdList.add(course_id_list1);
                        courseIdList1.add(course_id_list3);

                    }
                    System.out.println("courseIdList  "+courseIdList+"\n");
                    System.out.println("courseIdList1  "+courseIdList1);

                    if(courseIdList.size()>0 && courseIdList!=null) {
                        for (int i = 0; i < courseIdList.size(); i++) {   //used
                            String arr[] = courseIdList.get(i).split(" ");
                            while (arr[0].equals(sCity)&&arr[0].equals(sSubj)) {
                                String c=SelectedCourseUserUId = arr[0];
                                Toast.makeText(getContext(),"city  "+c,Toast.LENGTH_LONG).show();
                                System.out.println("city  "+c);
                                break;

                            }

                        }
                    }*/
                    //userAdapter=new UserAdapter(getContext(),R.layout.list_view,tdList6);
                    //stListView1.setAdapter(userAdapter);
                    //LinearLayoutManager manager=new LinearLayoutManager(getContext().getApplicationContext());

                    //recyclerAdapter=new UserRecyclerAdapter(getContext(),R.layout.list_view,tdList6);
                    //LinearLayoutManager manager=new LinearLayoutManager(getActivity().getApplicationContext());
                    //tRecyclerView.setLayoutManager(manager);
                    //tRecyclerView.setAdapter(recyclerAdapter);


                } else {
                    //NavigationActivity.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        subSrchList.clear();
        String subSrchName=stSubj.getText().toString().trim();
        if(subSrchName.length()==0){
            subSrchList.addAll(AllData);
        }else {
            for (TeacherDetails tsd:AllData){
                if(tsd.getSubj().toLowerCase(Locale.getDefault()).contains(subSrchName.toLowerCase(Locale.getDefault()))){
                    subSrchList.add(tsd);
                }
            }
        }
        Toast.makeText(getContext(), "Total "+subSrchName+" Teachers : "+subSrchList.size(), Toast.LENGTH_SHORT).show();
    }


    private void retrieveFromServer() {
        nDatabase = fDB.getInstance().getReference().child("teacherDetails");
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TeacherDetails teacherDetails1 = dataSnapshot.getValue(TeacherDetails.class);
                name = teacherDetails1.getName().toString().trim();
                uname = teacherDetails1.getUname().toString().trim();
                pass = teacherDetails1.getPass().toString().trim();
                email = teacherDetails1.getEmail().toString().trim();
                city = teacherDetails1.getCity().toString().trim();
                addr = teacherDetails1.getAddr().toString().trim();
                mob = teacherDetails1.getMob().toString().trim();
                subj = teacherDetails1.getSubj().toString().trim();
                lang = teacherDetails1.getLang();
                lati = teacherDetails1.getLati();


                Toast.makeText(getContext(), "Retrieve Current User in Search Fragment :" + teacherDetails1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btnTSearch:


                gmapLayout.setVisibility(View.VISIBLE);
                RetrieveDataUsingHasMap();

                googleMap.clear();
                for (TeacherDetails tdL:subSrchList){

                    //get imgUrl and Convert it into Image
                    /*int height=100;
                    int weight=100;
                    Bitmap img= BitmapFactory.decodeFile(tdL.getImageUrl());
                    Bitmap imgIcon=Bitmap.createScaledBitmap(img,weight,height,false);*/

                    Double longitude1=tdL.getLang();
                    Double latitude1=tdL.getLati();
                    LatLng position = new LatLng(latitude1, longitude1);

                    CreateMarker(latitude1,longitude1,tdL.getName(),tdL.getSubj());
                    CameraUpdate updatePosition = CameraUpdateFactory.newLatLng(position);
                    googleMap.moveCamera(updatePosition);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    protected Marker CreateMarker(double latitude, double longitude, String title, String snippet){

        return googleMap.addMarker(new MarkerOptions()
            .position(new LatLng(latitude,longitude))
                .anchor(0.5f,0.5f)
                .title(title)
                .snippet(snippet)
        );


    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap= map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //googleMap.getUiSettings().setMapToolbarEnabled(true);

        CameraUpdate updateZoom = CameraUpdateFactory.zoomBy(10);
        googleMap.animateCamera(updateZoom);



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);



    }
}
