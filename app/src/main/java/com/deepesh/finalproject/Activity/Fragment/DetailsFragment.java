package com.deepesh.finalproject.Activity.Fragment;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.bumptech.glide.Glide;
import com.deepesh.finalproject.Activity.RegisterActivity;
import com.deepesh.finalproject.Adapter.UserAdapter;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener , LocationListener {

    //@InjectView(R.id.btnLogin)
    Button btnUpdate;
    Button btnLoc;

    LocationManager locatiomanager;
    Double latitude, longitude;



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

    //ImageView profilePic;
    CircleImageView profilePic;


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
    String getfwpusername;

    //Teachers teachers;
    FirebaseUser fbUser;
    FirebaseDatabase fDB;
    DatabaseReference nDatabase;
    StorageReference storageReference;
    DatabaseReference n1Database;
    FirebaseAuth nAuth;
    String id;

    TeacherDetails teacherDetails;

    String name,uname,pass,email,city,addr,subj,mob,currentLocation,imgeurl,imgurl;
    Double lang,lati,sLang,sLati,tLang,tLati;

    ProgressDialog progressDialog;


    int Image_Request_Code = 7;
    Uri FilePathUri;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //ButterKnife.inject(this.getActivity());
        profilePic=(CircleImageView) view.findViewById(R.id.imgProfilepic);
        //profilePic=(ImageView)view.findViewById(R.id.imgProfilepic);
        eTxtName=(EditText)view.findViewById(R.id.eTxtName);
        eTxtUname=(EditText)view.findViewById(R.id.eTxtUsername);
        eTxtPassword=(EditText)view.findViewById(R.id.eTxtPassword);
        eTxtEmail=(EditText)view.findViewById(R.id.eTxtEmail);
        eTxtCity=(EditText)view.findViewById(R.id.eTxtCity);
        eTxtAddr=(EditText)view.findViewById(R.id.eTxtAddr);
        eTxtMob=(EditText)view.findViewById(R.id.eTxtMob);
        eTxtSubj=(EditText)view.findViewById(R.id.eTxtSub);

        btnLoc=(Button)view.findViewById(R.id.btnLocation);
        btnUpdate=(Button)view.findViewById(R.id.btnUpdate);

        profilePic.setOnClickListener(this);
        btnLoc.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

        storageReference= FirebaseStorage.getInstance().getReference();

        locatiomanager=(LocationManager)getActivity().getSystemService(getContext().LOCATION_SERVICE);

        //Intent rcv=getActivity().getIntent();
        //teachers=(Teachers)rcv.getSerializableExtra(Util.KEY_USER);
        //Toast.makeText(getContext(), "rcv Data  "+teachers, Toast.LENGTH_SHORT).show();

        //eTxtUname.setText(teachers.getUname());
        //eTxtPassword.setText(teachers.getPass());

        teacherDetails=new TeacherDetails();

        nAuth=FirebaseAuth.getInstance();
        fbUser=nAuth.getCurrentUser();
        id=fbUser.getUid();



        //get Username or (you are:) from Register Activity
        Intent rcv=getActivity().getIntent();
        getfwpusername=rcv.getStringExtra("youare");

        //Bundle bundle=this.getArguments();
        //getfwpusername= bundle.getString("youare1");

        //System.out.println("Forward Passing Data to DetailsFrag Activity:"+getfwpusername);
        //Toast.makeText(getContext(), "Forward Passing Data to DetailsFrag Activity:"+getfwpusername, Toast.LENGTH_SHORT).show();

        checkTeacherorStudent();
        //retrieveTeacher();



        progressDialog=new ProgressDialog(getContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Updating....");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        //nDatabase=fDB.getInstance().getReference().child("teacherDetails");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*Intent rcv=getActivity().getIntent();
        getfwpusername=rcv.getStringExtra("youare");
        System.out.println("Forward Passing Data to DetailsFrag Activity:"+getfwpusername);
        Toast.makeText(getContext(), "Forward Passing Data to DetailsFrag Activity:"+getfwpusername, Toast.LENGTH_SHORT).show();
*/

        //Toast.makeText(getActivity(),"Displaying Toast: ",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profilePic.setImageBitmap(bitmap);

                //delete Previous Upload mage
                if(teacherDetails.getImageUrl()!=null){
                    StorageReference delImg=storageReference.getStorage().getReferenceFromUrl(teacherDetails.getImageUrl());
                    delImg.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Previous Image has been Deleted...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Image didn't deleted..", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "No Previous Image..", Toast.LENGTH_SHORT).show();
                }


                // After selecting image change choose button above text.
                //btnImgChoose.setText("Image Selected...!");

                //upload Image
                UploadImageFileToFirebaseStorage();

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }


    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            StorageReference storageReference2nd = storageReference.child("photos").child("profilepic"+ System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            // Getting image upload ID.
                            imgurl = taskSnapshot.getDownloadUrl().toString();

                        }
                    })
                    //If something goes wrong.
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setMessage("Uploading...");

                        }
                    });

        }
        else {

            Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

    public void checkTeacherorStudent(){

        try {
            if(getfwpusername.equalsIgnoreCase("teacher")){
                retrieveTeacher();

            }else if(getfwpusername.equalsIgnoreCase("student")){
                retrieveStudent();
            }else {
                Toast.makeText(getContext(), "getIntent in Null", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
        /*if(getfwpusername.equalsIgnoreCase("teacher")){
            retrieveTeacher();

        }else if(getfwpusername.equalsIgnoreCase("student")){
            retrieveStudent();
        }else {
            Toast.makeText(getContext(), "getIntent in Null", Toast.LENGTH_SHORT).show();
        }*/

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
                mob=teacherDetails.getMob().toString().trim();
                subj=teacherDetails.getSubj().toString().trim();
                tLang=teacherDetails.getLang();
                tLati=teacherDetails.getLati();
                imgeurl=teacherDetails.getImageUrl();


                eTxtName.setText(name);
                eTxtUname.setText(uname);
                eTxtPassword.setText(pass);
                eTxtEmail.setText(email);
                eTxtCity.setText(city);
                eTxtAddr.setText(addr);
                eTxtMob.setText(mob);
                eTxtSubj.setText(subj);
                if(imgeurl!=null){

                    Glide.with(getContext())
                            .load(imgeurl)
                            .fitCenter()
                            .centerCrop()
                            .into(profilePic);
                }else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveStudent(){
        nDatabase=fDB.getInstance().getReference().child("studentDetails").child(id);
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
                mob=teacherDetails.getMob().toString().trim();
                subj=teacherDetails.getSubj().toString().trim();
                sLang=teacherDetails.getLang();
                sLati=teacherDetails.getLati();
                imgeurl=teacherDetails.getImageUrl();

                eTxtName.setText(name);
                eTxtUname.setText(uname);
                eTxtPassword.setText(pass);
                eTxtEmail.setText(email);
                eTxtCity.setText(city);
                eTxtAddr.setText(addr);
                eTxtSubj.setText(subj);
                eTxtMob.setText(mob);
                if(imgeurl!=null){
                    Glide.with(getContext()).load(imgeurl).fitCenter().centerCrop().into(profilePic);
                }else {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void UpdateTeacher() {
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String n=eTxtName.getText().toString().trim();
        String u=eTxtUname.getText().toString().trim();
        String p=eTxtPassword.getText().toString().trim();
        String e=eTxtEmail.getText().toString().trim();
        String c=eTxtCity.getText().toString().trim();
        String a=eTxtAddr.getText().toString().trim();
        String m=eTxtMob.getText().toString().trim();
        String s=eTxtSubj.getText().toString().trim();
        Double l1=tLang;
        Double l2=tLati;
        String t1= FirebaseInstanceId.getInstance().getToken();
        String imgUrl;
        if(imgurl==null){
            imgUrl=teacherDetails.getImageUrl();
        }else {
            imgUrl=imgurl;
        }

        final TeacherDetails teacherDetails = new TeacherDetails(n,u,p,e,c,a,m,s,l1,l2,t1,id,imgUrl);

        //System.out.println("Updata Data Id"+id);
        //Toast.makeText(getContext(), "Updata Data Id"+id, Toast.LENGTH_SHORT).show();

        try {

        nDatabase=fDB.getInstance().getReference().child("teacherDetails").child(id);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(teacherDetails);
                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Update Unsuccessful", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });


        nDatabase.setValue(teacherDetails);

        }catch (Exception e1){
            e1.printStackTrace();
            Toast.makeText(getContext(), "Data couldn't be Updated", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void UpdateStudent() {

        progressDialog.setMessage("Updating...");
        progressDialog.show();

        String n=eTxtName.getText().toString().trim();
        String u=eTxtUname.getText().toString().trim();
        String p=eTxtPassword.getText().toString().trim();
        String e=eTxtEmail.getText().toString().trim();
        String c=eTxtCity.getText().toString().trim();
        String a=eTxtAddr.getText().toString().trim();
        String m=eTxtMob.getText().toString().trim();
        String s=eTxtSubj.getText().toString().trim();
        Double l1=sLang;
        Double l2=sLati;
        String t1= FirebaseInstanceId.getInstance().getToken();

        String imgUrl;
        if(imgurl==null){
            imgUrl=teacherDetails.getImageUrl();
        }else {
            imgUrl=imgurl;
        }

        final TeacherDetails teacherDetails = new TeacherDetails(n,u,p,e,c,a,m,s,l1,l2,t1,id,imgUrl);

        //System.out.println("Updata Data Id"+id);
        //Toast.makeText(getContext(), "Updata Data Id"+id, Toast.LENGTH_SHORT).show();

        try {

        nDatabase=fDB.getInstance().getReference().child("studentDetails").child(id);
        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                dataSnapshot.getRef().setValue(teacherDetails);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Update Unsuccessful");
                Toast.makeText(getContext(), "Update Unsuccessful", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        nDatabase.setValue(teacherDetails);

        }catch (Exception e1){
            e1.printStackTrace();
            Toast.makeText(getContext(), "Data couldn't be Updated", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {

            case R.id.btnUpdate:
                //System.out.println("You clicked on Update Button");
                //Toast.makeText(getContext(), "You clicked on Update Button", Toast.LENGTH_SHORT).show();

                //UpdateTeacher();
                if(getfwpusername.equalsIgnoreCase("teacher")){
                        UpdateTeacher();
                    }else if(getfwpusername.equalsIgnoreCase("student")){
                        UpdateStudent();
                    }
                break;
            case R.id.btnLocation:
                locatiomanager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Location not fetch due to permission issues", Toast.LENGTH_SHORT).show();
                }else {
                    locatiomanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, this);
                    eTxtAddr.setText(""+currentLocation);

                    lang=longitude;
                    lati=latitude;
                    //Toast.makeText(this, "Current location ..: "+currentLocation, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgProfilepic:
                Intent intent2=new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Please Select Image"), Image_Request_Code);
            break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();
        try {
            Geocoder geocoder=new Geocoder(getContext());

            List<Address> adrList=geocoder.getFromLocation(latitude,longitude,5);
            //Toast.makeText(this, "Address List"+adrList.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Size of Address List "+adrList.size(), Toast.LENGTH_SHORT).show();

            if(adrList!=null && adrList.size()>0){
                Address addr=adrList.get(0);
                StringBuffer buffer=new StringBuffer();
                //for(int i=0;i<=addr.getMaxAddressLineIndex();i++){  it does not pick current location
                for(int i=0;i<=addr.getMaxAddressLineIndex();i++){
                    buffer.append(addr.getAddressLine(i));
                }
                currentLocation=buffer.toString();
                //eTxtAddr.setText(""+currentLocation);
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
}
