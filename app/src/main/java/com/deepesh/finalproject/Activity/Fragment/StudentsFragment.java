package com.deepesh.finalproject.Activity.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.deepesh.finalproject.Adapter.UserAdapter;
import com.deepesh.finalproject.Adapter.UserRecyclerAdapter;
import com.deepesh.finalproject.Model.RecyclerItemClickListener;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentsFragment extends Fragment implements AdapterView.OnItemClickListener{


    //ListView tListView;
    EditText eTxtCitySearch;
    EditText eTxtSubjSearch;
    EditText eTxtNameSearch;

    //@InjectView(R.id.tListView)
    RecyclerView tRecyclerView;

    //ArrayList<TeacherDetails> studentDetailsList;
    //UserAdapter userAdapter;
    UserRecyclerAdapter recyclerAdapter;
    int pos;
    //Teachers teachers;
    TeacherDetails teacherDetails;
    UserAdapter userAdapter;

    //ArrayList<Teachers> teacheList;

    ArrayList<TeacherDetails> studentDetailsList;
    ArrayList<TeacherDetails> sList;

    ArrayList<TeacherDetails> sAllData=new ArrayList<TeacherDetails>();


    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FirebaseUser fUser;
    DatabaseReference dRef;
    FirebaseAuth fAuth;

    String name,uname,pass,email,city,addr,subj,mob,getfwpusername;
    Double lang,lati;

    List<HashMap<String,String>> AllStudentsList;

    LinearLayoutManager manager;



    public StudentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tRecyclerView=(RecyclerView) view.findViewById(R.id.tRecyclerView);

        eTxtCitySearch=(EditText)view.findViewById(R.id.eTxtSearchCity);
        eTxtSubjSearch=(EditText)view.findViewById(R.id.eTxtSearchSubj);
        eTxtNameSearch=(EditText)view.findViewById(R.id.eTxtSearchName);

        sList=new ArrayList<>();
        tRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(),tRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                teacherDetails=sList.get(position);
                pos=position;
                //Toast.makeText(getContext(), "you are intrested to know About"+teacherDetails.getName(), Toast.LENGTH_SHORT).show();
                showOption();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        Intent rcv=getActivity().getIntent();
        getfwpusername = rcv.getStringExtra("youare");

        manager=new LinearLayoutManager(getActivity().getApplicationContext());

        //System.out.println("Forward Passing Data to Studentfrag Activity:"+getfwpusername);
        //Toast.makeText(getContext(), "Forward Passing Data to Studentfrag Activity:"+getfwpusername, Toast.LENGTH_SHORT).show();

        fAuth=FirebaseAuth.getInstance();

        teacherDetails=new TeacherDetails();
        RetrieveDataUsingHasMap();
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_students, container, false);
    }

    //retrieve Id
    private void retrieveFromServer(){
        dRef= FirebaseDatabase.getInstance().getReference().child("studentDetails");
        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> tdList1=new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    tdList1.add(ds.getKey());

                    String key=ds.getKey();
                    System.out.println("key  :"+key);

                    final List<String> tdList3=new ArrayList<>();
                    DatabaseReference dRef1=FirebaseDatabase.getInstance().getReference().child("studentDetails");
                    dRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            System.out.println("DataSnapshot Col name  :"+dataSnapshot);
                            tdList3.clear();
                            for(DataSnapshot dShot:dataSnapshot.getChildren()){
                                teacherDetails=dShot.getValue(TeacherDetails.class);
                                //String s1=dShot.getValue(String.class);
                                //TeacherDetails s1=dShot.getValue(TeacherDetails.class);

                                String cname=dShot.getKey();
                                System.out.println("Col name  :"+cname);
                                tdList3.add(dShot.getKey());
                                //Toast.makeText(getContext(), "DSnapshot if teacherDetails snapshot :"+dShot, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getContext(), "teacherDetails in List :"+s1.toString(), Toast.LENGTH_SHORT).show();

                            }
                            //System.out.println("Col name Group  :"+tdList3);
                            //Toast.makeText(getContext(), "DShot(Col name) in studentDetails StudentFrag ::"+tdList3.toString(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //retrieve Column Data
    private void colRetrieve(){
        final List<String> tdList=new ArrayList<>();
        DatabaseReference dRef1=FirebaseDatabase.getInstance().getReference().child("studentDetails");
        dRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("DataSnapshot Col name  :"+dataSnapshot);
                tdList.clear();
                for(DataSnapshot dShot:dataSnapshot.getChildren()){
                    teacherDetails=dShot.getValue(TeacherDetails.class);
                    //String s1=dShot.getValue(String.class);
                    //TeacherDetails s1=dShot.getValue(TeacherDetails.class);

                    String cname=dShot.getKey();
                    System.out.println("Col name  :"+cname);
                    tdList.add(dShot.getKey());
                    //Toast.makeText(getContext(), "DSnapshot if teacherDetails snapshot :"+dShot, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "teacherDetails in List :"+s1.toString(), Toast.LENGTH_SHORT).show();

                }
                //System.out.println("Col name Group  :"+tdList);
                Toast.makeText(getContext(), "DShot(Col name) in studentDetails StudentFrag ::"+tdList.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    //Retrieve Data Using Hasmap
    private void RetrieveDataUsingHasMap(){

        //List<HashMap<String,String>> AllStudentsList;
        String SelectedCourseUserUId;
        final List<TeacherDetails> tdList5=new ArrayList<>();
        //sList=new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("studentDetails").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //                        emergencyContactsList = new ArrayList<>();
                AllStudentsList = new ArrayList<HashMap<String, String>>();


                if(dataSnapshot.exists())
                {
                    AllStudentsList.clear();
                    for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                    {
                        //tdList5.clear();
                        TeacherDetails teacherDetails = postSnapShot.getValue(TeacherDetails.class);
                        HashMap<String, String> map = new HashMap<String, String>();

                        //String user_id = postSnapShot.getKey();
                        String name1 = teacherDetails.getName();
                        String unam1 = teacherDetails.getUname();
                        String pass1 = teacherDetails.getPass();
                        String email1 = teacherDetails.getEmail();
                        String city1 = teacherDetails.getCity();
                        String addr1 = teacherDetails.getAddr();
                        String subj1 = teacherDetails.getSubj();
                        String mob1 = teacherDetails.getMob();
                        Double lang1=teacherDetails.getLang();
                        Double lati1=teacherDetails.getLati();
                        String token1= teacherDetails.getToken();
                        String uid1= teacherDetails.getUid();

                        //map.put("user_id", user_id);
                        map.put("name1", name1);
                        map.put("uname1", unam1);
                        map.put("pass1", pass1);
                        map.put("emai1", email1);
                        map.put("city1", city1);
                        map.put("addr1", addr1);
                        map.put("subj1", subj1);
                        map.put("mob1", mob1);
                        map.put("lang1", String.valueOf(lang1));
                        map.put("lati1", String.valueOf(lati1));
                        map.put("token1", token1);
                        map.put("uid1", uid1);

/*

                                    System.out.println("***************");
                                    System.out.println("name  :"+ name1);
                                    System.out.println("uname  :"+ unam1);
                                    System.out.println("pass  :"+ pass1);
                                    System.out.println("emai  :"+ email1);
                                    System.out.println("city  :"+ city1);
                                    System.out.println("addr  :"+ addr1);
                                    System.out.println("subj  :"+ subj1);
                                    System.out.println("mob  :"+ mob1);
                                    System.out.println("***************");
*/

                        //for user Adapter
                        TeacherDetails td2=new TeacherDetails(name1,unam1,pass1,email1,city1,addr1,mob1,subj1,lang1,lati1,token1,uid1,null);
                        sList.add(td2);

                        //AllStudentsList.add(map);
                        tdList5.add(teacherDetails);

                        //System.out.println("Class_AllUser5  :"+ teacherDetails);
                        //Log.e("Class_AllUser5",""+tdList5);
                        //Log.e("Class_AllUser6",""+sList);
                        //Log.e("HasMapA_U_Courselist",""+AllStudentsList);

                        //if u have another Child
                        for (DataSnapshot teacherUID : postSnapShot.getChildren()){
                            //pass the all above maping code
                        }

                        //AllStudentsList.add(new UsercourseDetails(teacherDetails.getcourse_type(),teacherDetails.getcourse_brand(),teacherDetails.getcourse_number_plate_url(),teacherDetails.getcourse_id()));
                    }
                    sAllData.addAll(sList);
                    recyclerAdapter=new UserRecyclerAdapter(getContext(),R.layout.list_view1,sList);
                    //LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
                    //LinearLayoutManager manager=new LinearLayoutManager(getActivity().getApplicationContext());
                    tRecyclerView.setLayoutManager(manager);
                    tRecyclerView.setAdapter(recyclerAdapter);



                }else{
                    //NavigationActivity.this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //CitySearch
        eTxtCitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = eTxtCitySearch.getText().toString().toLowerCase(Locale.getDefault());
                filterCity(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eTxtSubjSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = eTxtSubjSearch.getText().toString().toLowerCase(Locale.getDefault());
                filterSubj(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eTxtNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = eTxtNameSearch.getText().toString().toLowerCase(Locale.getDefault());
                filterName(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void filterCity(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sList .clear();
        if (charText.length() == 0) {
            sList.addAll(sAllData);
        } else {
            for (TeacherDetails wp : sAllData) {

                //System.out.println( "Search Input : "+charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getCity().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "name : "+wp.getName());
                    sList .add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    public void filterSubj(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sList .clear();
        if (charText.length() == 0) {
            sList.addAll(sAllData);
        } else {
            for (TeacherDetails wp : sAllData) {

                //System.out.println( "Search Input : "+charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getSubj().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "name : "+wp.getName());
                    sList .add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }
    public void filterName(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sList .clear();
        if (charText.length() == 0) {
            sList.addAll(sAllData);
        } else {
            for (TeacherDetails wp : sAllData) {

                //System.out.println( "Search Input : "+charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "name : "+wp.getName());
                    sList .add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }


    //AlertDialogBox for RecyclerView Item
    void showOption(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        String[] items={"View Student","Call Student","View Location"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        ShowTeacher();
                        break;
                    case 1:
                        CallTeacher();
                        //askForDeletion();
                        break;
                    case 2:
                        ShowTeacherLocation();
                        // Intent intent = new Intent(HomeActivity.this,RegisterActivity.class);
                        // intent.putExtra(Util.KEY_USER,user);
                        // startActivity(intent);
                        break;
                }
            }
        });
        builder.create().show();
    }
    void ShowTeacher(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Name of Student :"+teacherDetails.getName());
        //builder.setMessage("Are you Sure");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getContext(), "You clicked on Ok ", Toast.LENGTH_SHORT).show();
                //deleteUser();
            }
        });
        builder.setNegativeButton("Cancle",null);
        builder.create().show();
    }

    void CallTeacher(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Call  :"+teacherDetails.getMob());
        builder.setMessage("Are you Sure");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent11=new Intent(Intent.ACTION_CALL);
                intent11.setData(Uri.parse("tel:"+teacherDetails.getMob()));
                //Toast.makeText(getContext(), "You clicked to Call "+teacherDetails.getMob(), Toast.LENGTH_SHORT).show();
                //deleteUser();
            }
        });
        builder.setNegativeButton("No",null);
        builder.create().show();
    }

    void ShowTeacherLocation(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Location :"+teacherDetails.getLang()+" , "+teacherDetails.getLati());
        //builder.setMessage("Are you Sure");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "You clicked on Ok ", Toast.LENGTH_SHORT).show();
                //deleteUser();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
