package com.deepesh.finalproject.Activity.Fragment;


import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.deepesh.finalproject.Activity.Googlemap;
import com.deepesh.finalproject.Adapter.FeedbackRecyclerAdapter;
import com.deepesh.finalproject.Adapter.UserAdapter;
import com.deepesh.finalproject.Adapter.UserRecyclerAdapter;
import com.deepesh.finalproject.Model.RecyclerItemClickListener;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.TeacherReview;
import com.deepesh.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTeachersFragment extends Fragment implements AdapterView.OnItemClickListener {


    //ListView tListView;
    EditText eTxtCitySearch;
    EditText eTxtSubjSearch;
    EditText eTxtNameSearch;

    //@InjectView(R.id.tListView)
    RecyclerView tRecyclerView, fRecyclerView;

    //ArrayList<TeacherDetails> teacherDetailsList;
    //UserAdapter userAdapter;
    UserRecyclerAdapter recyclerAdapter;
    FeedbackRecyclerAdapter feedbackRecyclerAdapter;
    int pos;
    //Teachers teachers;
    TeacherDetails teacherDetails, cuTeacherDetails;
    UserAdapter userAdapter;

    //ArrayList<Teachers> teacheList;

    ArrayList<TeacherDetails> teacherDetailsList;
    ArrayList<TeacherDetails> tdList6;
    ArrayList<TeacherReview> tdList7;

    ArrayList<TeacherDetails> mAllData = new ArrayList<TeacherDetails>();


    RequestQueue requestQueue;
    StringRequest stringRequest;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String getfwpusername;

    FirebaseUser fUser;
    DatabaseReference dRef;
    FirebaseAuth fAuth;
    String id, tableName;

    String name, uname, pass, email, city, addr, subj, mob;
    Double lati, lang;

    //current User
    String cuName, cuUname, cuPass, cuEmail, cuCity, cuAddr, cuSubj, cuMob;
    Double cuLati, cuLang;

    List<HashMap<String, String>> AllUserscourselist;

    LinearLayoutManager manager;
    LinearLayoutManager manager1;

    Dialog dialog;
    RatingBar ratingBar;
    EditText review;
    Button btnFeedback;

    String rName, reviewText;
    String rRate = "0.0";


    FirebaseDatabase fDB;
    DatabaseReference nDatabase;

    Googlemap googleMap;

    public AllTeachersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //requestQueue = Volley.newRequestQueue(getContext());
        tRecyclerView = (RecyclerView) view.findViewById(R.id.tRecyclerView);
        //fRecyclerView=(RecyclerView) view.findViewById(R.id.fRecyclerView);

        eTxtCitySearch = (EditText) view.findViewById(R.id.eTxtSearchCity);
        eTxtSubjSearch = (EditText) view.findViewById(R.id.eTxtSearchSubj);
        eTxtNameSearch = (EditText) view.findViewById(R.id.eTxtSearchName);
        //text1 = eTxtCitySearch.getText().toString().toLowerCase(Locale.getDefault());
        //text2 = eTxtSubjSearch.getText().toString().toLowerCase(Locale.getDefault());
        //teacheList=new ArrayList<Teachers>();

        //onClickListner on RecyclerView Item
        tdList6 = new ArrayList<>();
        tdList7 = new ArrayList<>();

        tRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), tRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                teacherDetails = tdList6.get(position);
                pos = position;
                //Toast.makeText(getContext(), "You Have Clicked on RecylerView Item", Toast.LENGTH_SHORT).show();
                showOption();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //get Username or (you are:) from Register Activity
        Intent rcv = getActivity().getIntent();
        getfwpusername = rcv.getStringExtra("youare");

        manager = new LinearLayoutManager(getActivity().getApplicationContext());
        manager1 = new LinearLayoutManager(getActivity().getApplicationContext());


        //System.out.println("Forward Passing Data to AllTeacher Activity:" + getfwpusername);
        //Toast.makeText(getContext(), "Forward Passing Data to AllTeacher Activity:" + getfwpusername, Toast.LENGTH_SHORT).show();


        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        id = fUser.getUid();

        retrieveCurrentUser();

        //Boolean UpdateMode = rcv.hasExtra(Util.KEY_USER);
        teacherDetails = new TeacherDetails();
        RetrieveDataUsingHasMap();
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_teachers, container, false);
    }


    private void retrieveCurrentUser() {
        try {
            if (getfwpusername.equalsIgnoreCase("teacher")) {

                tableName = "teacherDetails";

            } else if (getfwpusername.equalsIgnoreCase("student")) {
                tableName = "studentDetails";
            } else {
                Toast.makeText(getContext(), "getIntent in Null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
        nDatabase = fDB.getInstance().getReference().child(tableName).child(id);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cuTeacherDetails = dataSnapshot.getValue(TeacherDetails.class);
                cuName = cuTeacherDetails.getName().toString().trim();
                cuUname = cuTeacherDetails.getUname().toString().trim();
                cuPass = cuTeacherDetails.getPass().toString().trim();
                cuEmail = cuTeacherDetails.getEmail().toString().trim();
                cuCity = cuTeacherDetails.getCity().toString().trim();
                cuAddr = cuTeacherDetails.getAddr().toString().trim();
                cuMob = cuTeacherDetails.getMob().toString().trim();
                cuSubj = cuTeacherDetails.getSubj().toString().trim();
                cuLang = cuTeacherDetails.getLang();
                cuLati = cuTeacherDetails.getLati();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void RetrieveDataUsingHasMap() {


        //List<HashMap<String,String>> AllUserscourselist;
        String SelectedCourseUserUId;
        final List<TeacherDetails> tdList5 = new ArrayList<>();
        //tdList6=new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("teacherDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //                        emergencyContactsList = new ArrayList<>();
                AllUserscourselist = new ArrayList<HashMap<String, String>>();


                if (dataSnapshot.exists()) {
                    AllUserscourselist.clear();
                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
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
                        String mob1 = teacherDetails.getMob();
                        String subj1 = teacherDetails.getSubj();
                        Double lang1 = teacherDetails.getLang();
                        Double lati1 = teacherDetails.getLati();
                        String token1 = teacherDetails.getToken();
                        String uid1 = teacherDetails.getUid();
                        String imgUrl=teacherDetails.getImageUrl();


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
                        map.put("imgUrl", imgUrl);

                        //for user Adapter
                        TeacherDetails td2 = new TeacherDetails(name1, unam1, pass1, email1, city1, addr1, mob1, subj1, lang1, lati1, token1, uid1,imgUrl);
                        tdList6.add(td2);

                        //AllUserscourselist.add(map);
                        tdList5.add(teacherDetails);

                        //System.out.println("Class_AllUser5  :"+ teacherDetails);
                        //Log.e("Class_AllUser5",""+tdList5);
                        //Log.e("Class_AllUser6",""+tdList6);
                        //Log.e("HasMapA_U_Courselist",""+AllUserscourselist);

                        //if u have another Child
                        for (DataSnapshot teacherUID : postSnapShot.getChildren()) {
                            //pass the all above maping code
                        }

                        //AllUserscourselist.add(new UsercourseDetails(teacherDetails.getcourse_type(),teacherDetails.getcourse_brand(),teacherDetails.getcourse_number_plate_url(),teacherDetails.getcourse_id()));
                    }
                    mAllData.addAll(tdList6);
                    recyclerAdapter = new UserRecyclerAdapter(getContext(), R.layout.list_view, tdList6);

                    //LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
                    //LinearLayoutManager manager=new LinearLayoutManager(getActivity().getApplicationContext());
                    tRecyclerView.setLayoutManager(manager);
                    tRecyclerView.setAdapter(recyclerAdapter);

                    recyclerAdapter.notifyDataSetChanged();

                    //***For ListView
                    //userAdapter=new UserAdapter(getContext(),R.layout.list_view,tdList6);
                    //tRecyclerView.setAdapter(userAdapter);

                                /*courseIdList = new ArrayList<String>();
                                for (int i = 0; i < AllUserscourselist.size(); i++) {
                                    String course_id_list;
                                    course_id_list = AllUserscourselist.get(i).get("course_id")+" "+ AllUserscourselist.get(i).get("user_id");
                                    courseIdList.add(course_id_list);
                                }

                                if(courseIdList.size()>0 && courseIdList!=null) {
                                    for (int i = 0; i < courseIdList.size(); i++) {   //used
                                        String arr[] = courseIdList.get(i).split(" ");
                                        if (arr[0].equals(coursenumber)) {
                                            SelectedcourseUserUId = arr[1];
                                            getUserEmergencyContacts(SelectedcourseUserUId);
                                            break;
                                        }
                                    }
                                }*/

                } else {
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
                //*************************************************
                //adapter.filter(charSequence.toString());

                String text = eTxtCitySearch.getText().toString().toLowerCase(Locale.getDefault());
                filterCity(text);

                //recyclerAdapter.getFilter().filter(s);
                //Toast.makeText(getContext(), " "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //String text1 = eTxtCitySearch.getText().toString().toLowerCase(Locale.getDefault());
                //filter(text1);
                //userAdapter.getFilter().filter(s);
            }
        });
        //Subject Search
        eTxtSubjSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text2 = eTxtSubjSearch.getText().toString().toLowerCase(Locale.getDefault());
                filterSubj(text2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//Name Search
        eTxtNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text2 = eTxtNameSearch.getText().toString().toLowerCase(Locale.getDefault());
                filterName(text2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    //Filter for Search
    public void filterCity(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tdList6.clear();
        if (charText.length() == 0) {
            tdList6.addAll(mAllData);
        } else {
            for (TeacherDetails wp : mAllData) {

                //System.out.println("Search Input : " + charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getCity().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "City : "+wp.getCity());
                    tdList6.add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    public void filterSubj(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tdList6.clear();
        if (charText.length() == 0) {
            tdList6.addAll(mAllData);
        } else {
            for (TeacherDetails wp : mAllData) {

                //System.out.println("Search Input : " + charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getSubj().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "Subj : "+wp.getSubj());
                    tdList6.add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    public void filterName(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        tdList6.clear();
        if (charText.length() == 0) {
            tdList6.addAll(mAllData);
        } else {
            for (TeacherDetails wp : mAllData) {

                //System.out.println("Search Input : " + charText);
                //Toast.makeText(getContext(), "Search Input : "+charText, Toast.LENGTH_SHORT).show();
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    //System.out.println( "name : "+wp.getName());
                    tdList6.add(wp);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //AlertDialogBox for RecyclerView Item
    void showOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = {"Review Teacher", "Call Teacher", "View Details", "Send Request"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //ShowTeacher();
                        Intent intent = new Intent(getContext(), FeedbackActivity.class);
                        intent.putExtra("cuName", cuName);
                        intent.putExtra("tId", teacherDetails.getUid());
                        intent.putExtra("tName", teacherDetails.getName());
                        intent.putExtra("tpic", teacherDetails.getImageUrl());
                       startActivity(intent);
                        break;
                    case 1:
                        CallTeacher();
                        //askForDeletion();
                        break;
                    case 2:
                        Intent intent1 = new Intent(getContext(), DetailsActivity.class);
                        intent1.putExtra("uId", teacherDetails.getUid());
                        intent1.putExtra("uName", teacherDetails.getName());
                        intent1.putExtra("uPic", teacherDetails.getImageUrl());
                        intent1.putExtra("uEmail", teacherDetails.getEmail());
                        intent1.putExtra("uSubj", teacherDetails.getSubj());
                        intent1.putExtra("uMob", teacherDetails.getMob());
                        intent1.putExtra("uCity", teacherDetails.getCity());
                        startActivity(intent1);
                        break;
                    case 3:
                        CallTeacher();
                        //askForDeletion();
                        break;
                }
            }
        });
        builder.create().show();
    }

    //for dialog
    public void showCustomDialog() {

        final ArrayList<TeacherReview> teacherReviewsList = new ArrayList<>();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_feedback);
        dialog.setCancelable(true);
        //dialog.setCancelable(false);
        dialog.show();

        ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        review = (EditText) dialog.findViewById(R.id.eTxtReview);
        btnFeedback = (Button) dialog.findViewById(R.id.btnFeedback);

        rName = cuName;
        //on Rating Change
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rRate = String.valueOf(ratingBar.getRating());
            }
        });

        //Intent intent=new Intent(getContext(),dia)

        //Retrieve Review
       /* DatabaseReference dRef3=FirebaseDatabase.getInstance().getReference().child("teacherReview").child(teacherDetails.getUid());
        dRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(), "DataSnapshot"+dataSnapshot, Toast.LENGTH_SHORT).show();
                if(dataSnapshot.exists()){
                    for(DataSnapshot trSnapshot:dataSnapshot.getChildren()){
                        TeacherReview teacherReview=trSnapshot.getValue(TeacherReview.class);
                        String n=teacherReview.getName().toString().trim();
                        String r=teacherReview.getRate().toString().trim();
                        String rvu=teacherReview.getReview().toString().trim();

                        TeacherReview tr=new TeacherReview(n,r,rvu);
                        teacherReviewsList.add(tr);
                        tdList7.add(tr);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        //feedback button Click
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Toast.makeText(getContext(), "You gave :"+rRate+"  Stars"+"  to  "+cuTeacherDetails.getName(), Toast.LENGTH_SHORT).show();
                reviewText = review.getText().toString().trim();

                DatabaseReference dRef2 = FirebaseDatabase.getInstance().getReference().child("teacherReview").child(teacherDetails.getUid());
                TeacherReview teacherReview = new TeacherReview(rName, rRate, reviewText);
                dRef2.push().setValue(teacherReview);


                //teacherReviewsList.add(teacherReview);
                //tdList7.add(teacherReview);

                //dialog.dismiss();
            }
        });


        //RecyclerView
        /*//fRecyclerView=(RecyclerView) view.findViewById(R.id.fRecyclerView);
        feedbackRecyclerAdapter=new FeedbackRecyclerAdapter(getContext(),R.layout.recyclre_view_feedback,tdList7);
        //LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        //LinearLayoutManager manager1=new LinearLayoutManager(getActivity().getApplicationContext());
        fRecyclerView.setLayoutManager(manager1);
        fRecyclerView.setAdapter(recyclerAdapter);
        feedbackRecyclerAdapter.notifyDataSetChanged();*/


    }

    void ShowTeacher() {
        showCustomDialog();
        /*AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Name of teacher :"+teacherDetails.getName());
        //builder.setMessage("Are you Sure");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "You clicked on Ok ", Toast.LENGTH_SHORT).show();
                //deleteUser();
            }
        });
        builder.setNegativeButton("Cancle",null);
        builder.create().show();*/
    }

    void CallTeacher() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Call  :" + teacherDetails.getMob());
        builder.setMessage("Are you Sure");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent11 = new Intent(Intent.ACTION_CALL);
                intent11.setData(Uri.parse("tel:" + teacherDetails.getMob()));
                startActivity(intent11);
                //Toast.makeText(getContext(), "You clicked to Call " + teacherDetails.getMob(), Toast.LENGTH_SHORT).show();
                //deleteUser();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }



}
