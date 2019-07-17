package com.deepesh.finalproject.Activity;

import android.app.Dialog;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.Model.Util;
import com.deepesh.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
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

import java.io.IOException;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener , LocationListener,View.OnTouchListener{

    private static final String TAG = "RegisterActivity";
    FirebaseUser fUser;
    private DatabaseReference nDatabase;
    private FirebaseAuth nAuth;


    private Button btnLogin;
    private Button btnRegister;

    private EditText eTxtName;
    //private EditText eTxtUname;

    private EditText eTxtCity;
    private EditText eTxtAddr;
    private Button btnLoc;
    private Button btnLoc1;
    private EditText eTxtPass;
    private EditText eTxtEmail;
    private EditText eTxtSubj;
    private EditText eTxtMob;
    private Button visblepass;

    private RadioButton rbStudent;
    private RadioButton rbTeacher;


    LocationManager locatiomanager;
    Double latitude, longitude;



    TeacherDetails teacherDetails;

    //UserData userData;



    RequestQueue requestQueue;
    StringRequest request;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    //String url = "https://asymmetrical-steril.000webhostapp.com/User/insert.php";
    Teachers teachers;

    int id=0;
    String name, uname,email,city,pass,mob,addr,subj,currentLocation,token;

    ProgressDialog progressDialog;

    Button dBtnSubmit;

    Dialog dialog;
    RatingBar ratingBar;
    EditText review;
    Button btnFeedback;

    Button btnImgChoose;
    Button btnImgUpload;
    private ImageView profilepic;
    int Image_Request_Code = 7;
    Uri FilePathUri;

    StorageReference storageReference;

    String imgUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);
        //nDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        nDatabase=FirebaseDatabase.getInstance().getReference();
        nAuth = FirebaseAuth.getInstance();

        profilepic=(ImageView)findViewById(R.id.imgProfilepic);
        eTxtName=(EditText)findViewById(R.id.eTxtName);
        //eTxtUname=(EditText)findViewById(R.id.eTxtUsername);
        eTxtPass=(EditText)findViewById(R.id.eTxtPassword);
        eTxtEmail=(EditText)findViewById(R.id.eTxtEmail);
        eTxtCity=(EditText)findViewById(R.id.eTxtCity);
        eTxtAddr=(EditText)findViewById(R.id.eTxtAddr);
        eTxtSubj=(EditText)findViewById(R.id.eTxtSub);
        eTxtMob=(EditText)findViewById(R.id.eTxtMob);

        btnImgChoose=(Button)findViewById(R.id.btnImageChoose);
        btnImgUpload=(Button)findViewById(R.id.btnImageUpload);
        rbTeacher=(RadioButton)findViewById(R.id.rbTeacher);
        rbStudent=(RadioButton)findViewById(R.id.rbStudent);

        rbTeacher.setOnClickListener(this);
        rbStudent.setOnClickListener(this);

        btnImgChoose.setOnClickListener(this);
        btnImgUpload.setOnClickListener(this);

        btnLoc=(Button)findViewById(R.id.btnLocation);
        btnLoc1=(Button)findViewById(R.id.btnLocation1);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        visblepass=(Button)findViewById(R.id.visiblePass);

        //dBtnSubmit=(Button)findViewById(R.id.dbutton2);

        /*ButterKnife.inject(this);
        requestQueue = Volley.newRequestQueue(this);
*/
        //teachers=new Teachers();
        teacherDetails=new TeacherDetails();
        //userData=new UserData();

        locatiomanager=(LocationManager)getSystemService(LOCATION_SERVICE);

        profilepic.setOnClickListener(this);
        btnLoc.setOnClickListener(this);
        btnLoc1.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        visblepass.setOnTouchListener(this);

        preferences = getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor = preferences.edit();

        progressDialog=new ProgressDialog(RegisterActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);


    }

    //for image Selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profilepic.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                btnImgChoose.setText("Image Selected...!");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

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
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            // Getting image upload ID.
                            imgUrl = taskSnapshot.getDownloadUrl().toString();

                        }
                    })
                    //If something goes wrong.
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(RegisterActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
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

            Toast.makeText(RegisterActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


    private void RegisterTeacher() {
        Log.d(TAG, "RegisterTeacher");
        if (!validateForm()) {
            return;
        }

        progressDialog.setMessage("Creating....");
        progressDialog.show();

        //showProgressDialog();
        name=eTxtName.getText().toString().trim(); //it shoud not be declare in OnCreate() caught it will not be initilaized
        //uname=eTxtUname.getText().toString().trim();
        uname=teacherDetails.getUname();
        pass=eTxtPass.getText().toString().trim();
        email=eTxtEmail.getText().toString().trim();
        city=eTxtCity.getText().toString().trim();
        addr=eTxtAddr.getText().toString().trim();
        mob=eTxtMob.getText().toString().trim();
        subj=eTxtSubj.getText().toString().trim();

        System.out.println("You Are : "+uname);
        //Toast.makeText(RegisterActivity.this, "You Are : "+uname, Toast.LENGTH_SHORT).show();

        //For teacher Data Insertion
        if(teacherDetails.getUname().equalsIgnoreCase("teacher")){


            try {
            nAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        fUser=nAuth.getCurrentUser();
                        token=FirebaseInstanceId.getInstance().getToken();
                        teacherDetails=new TeacherDetails(name,uname,pass,email,city,addr,mob,subj,longitude,latitude,token,fUser.getUid(),imgUrl);

                        /*String Tablename=fUser.getDisplayName();
                        System.out.println("Tacle Name : "+Tablename);
                        Toast.makeText(RegisterActivity.this, "Tacle Name : "+Tablename, Toast.LENGTH_SHORT).show();*/

                        //nDatabase=FirebaseDatabase.getInstance().getReference();
                        nDatabase.child("teacherDetails").child(fUser.getUid()).setValue(teacherDetails);

                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        intent.putExtra("youare",teacherDetails.getUname());
                        intent.putExtra(Util.KEY_USER,teacherDetails);
                        startActivity(intent);
                        progressDialog.dismiss();

                        //token
                        /*token= FirebaseInstanceId.getInstance().getToken();
                        System.out.println("Token  :"+token);
                        Toast.makeText(RegisterActivity.this, "Token  :"+token, Toast.LENGTH_SHORT).show();*/

                        System.out.println("Teacher Registration Successful");
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        System.out.println("Teacher Registration unSuccessful");
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Registration Failed due to Exception", Toast.LENGTH_SHORT).show();
            }

            try {
            nDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    teacherDetails = dataSnapshot.getValue(TeacherDetails.class);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Data couldn't Retrieve", Toast.LENGTH_SHORT).show();
            }

        }
        //for Student Data Insertion
        //else if(teacherDetails.getUname().equalsIgnoreCase("student")){
        else if(teacherDetails.getUname()=="student"){

            try {
            nAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        fUser=nAuth.getCurrentUser();
                        token=FirebaseInstanceId.getInstance().getToken();
                        teacherDetails=new TeacherDetails(name,uname,pass,email,city,addr,mob,subj,longitude,latitude,token,fUser.getUid(),imgUrl);

                        //nDatabase=FirebaseDatabase.getInstance().getReference();
                        nDatabase.child("studentDetails").child(fUser.getUid()).setValue(teacherDetails);

                        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                        intent.putExtra("youare",teacherDetails.getUname());
                        intent.putExtra(Util.KEY_USER,teacherDetails);
                        startActivity(intent);


                        //token
                        /*token= FirebaseInstanceId.getInstance().getToken();
                        System.out.println("Token  :"+token);
                        Toast.makeText(RegisterActivity.this, "Token  :"+token, Toast.LENGTH_SHORT).show();*/

                        System.out.println("Student Registration Successful");
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        System.out.println("Student Registration unSuccessful");
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }

            try {
            nDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    teacherDetails = dataSnapshot.getValue(TeacherDetails.class);
                    System.out.println(teacherDetails.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(RegisterActivity.this, "Data couldn't Retrieve", Toast.LENGTH_SHORT).show();
        }

        }else{

            System.out.println("Plese Select You are teacher/Student");
            Toast.makeText(RegisterActivity.this, "Plese Select You are teacher/Student", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean validateForm() {
        boolean result = true;

        if(teacherDetails.getUname()==null){
            rbTeacher.setError("Required");
            rbStudent.setError("Required");
        }else {
            rbTeacher.setError(null);
            rbStudent.setError(null);
        }

        if (TextUtils.isEmpty(eTxtName.getText().toString())) {
            eTxtName.setError("Required");
            result = false;
        } else {
            eTxtName.setError(null);
        }


        /*if (TextUtils.isEmpty(eTxtUname.getText().toString())) {
            eTxtUname.setError("Required");
            result = false;
        } else {
            eTxtUname.setError(null);
        }*/
        if (TextUtils.isEmpty(eTxtPass.getText().toString())) {
            eTxtPass.setError("Required");
            result = false;
        } else {
            eTxtPass.setError(null);
        }

        if (TextUtils.isEmpty(eTxtEmail.getText().toString())) {
            eTxtEmail.setError("Required");
            result = false;
        } else {
            eTxtEmail.setError(null);
        }
        if (TextUtils.isEmpty(eTxtCity.getText().toString())) {
            eTxtCity.setError("Required");
            result = false;
        } else {
            eTxtCity.setError(null);
        }
        if (TextUtils.isEmpty(eTxtAddr.getText().toString())) {
            eTxtAddr.setError("Required");
            result = false;
        } else {
            eTxtAddr.setError(null);
        }
        if (TextUtils.isEmpty(eTxtSubj.getText().toString())) {
            eTxtSubj.setError("Required");
            result = false;
        } else {
            eTxtSubj.setError(null);
        }
        if (TextUtils.isEmpty(eTxtMob.getText().toString())) {
            eTxtMob.setError("Required");
            result = false;
        } else {
            eTxtMob.setError(null);
        }

        return result;
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude=location.getLongitude();
        latitude=location.getLatitude();
            //txt.setText("Location is"+latitude+"--"+longitude);
        try {
            Geocoder geocoder=new Geocoder(this);

            List<Address> adrList=geocoder.getFromLocation(latitude,longitude,5);
            //Toast.makeText(this, "Address List"+adrList.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Size of Address List "+adrList.size(), Toast.LENGTH_SHORT).show();

            if(adrList!=null && adrList.size()>0){
                Address addr=adrList.get(0);
                StringBuffer buffer=new StringBuffer();
                //for(int i=0;i<addr.getMaxAddressLineIndex();i++){  it does not pick current location
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


    public void showCustomDialog(){



        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_feedback);
        dialog.setCancelable(true);
        //dialog.setCancelable(false);
        dialog.show();

        ratingBar=(RatingBar)dialog.findViewById(R.id.ratingBar);
        review=(EditText)dialog.findViewById(R.id.eTxtReview);
        btnFeedback=(Button)dialog.findViewById(R.id.btnFeedback);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Submit Dialog box...", Toast.LENGTH_SHORT).show();
                review.setText(String.valueOf(ratingBar.getRating()));

                token= FirebaseInstanceId.getInstance().getToken();
                System.out.println("Token  :"+token);
                Toast.makeText(RegisterActivity.this, "Token  :"+token, Toast.LENGTH_SHORT).show();

                //dialog.dismiss();
            }
        });

    }



    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.btnLogin:
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegister:

                RegisterTeacher();
                break;
            case R.id.btnLocation:
                //Toast.makeText(this, "You Clicked On Current Location", Toast.LENGTH_SHORT).show();
                locatiomanager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location not fetch due to permission issues", Toast.LENGTH_SHORT).show();
                }else {try {

                    locatiomanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 5, this);
                    eTxtAddr.setText(""+currentLocation);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Location not fetch due to Network issues", Toast.LENGTH_SHORT).show();

                }

                    //for location on map
                    /*if(eTxtAddr.getText()!=null && eTxtAddr.getText().toString()!="null"){
                        Intent intent1=new Intent(RegisterActivity.this,Googlemap.class);
                        intent1.putExtra("lang",longitude);
                        intent1.putExtra("lati",latitude);
                        startActivity(intent1);
                    }else {
                        Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
                    }*/

                    //Toast.makeText(this, "Current location ..: "+currentLocation, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLocation1:

                Toast.makeText(this, " clicked ", Toast.LENGTH_SHORT).show();



                //showCustomDialog();



                /*Intent intent11=new Intent(Intent.ACTION_CALL);
                intent11.setData(Uri.parse("tel:9803475225"));
                startActivity(intent11);*/


                /*Geocoder gCoad=new Geocoder(this);
                try {
                    ArrayList<Address> addr3=(ArrayList<Address>)gCoad.getFromLocationName(eTxtAddr.getText().toString(),50);
                    for(Address addr4:addr3){
                        if(addr4 !=null){
                            Double lt=addr4.getLatitude();
                            Double lg1=addr4.getLongitude();
                            System.out.println(lt);
                            System.out.println(lg1);
                            Toast.makeText(this, " "+lt, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, " "+lg1, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, " Address is null ", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
                /*Intent intent1=new Intent(RegisterActivity.this,Googlemap.class);
                intent1.putExtra("lang",longitude);
                intent1.putExtra("lati",latitude);
                startActivity(intent1);*/

                break;
            case R.id.rbTeacher:
                rbStudent.setChecked(false);
                teacherDetails.setUname("teacher");
                Toast.makeText(RegisterActivity.this, "Image Url :"+imgUrl, Toast.LENGTH_SHORT).show();

                break;
            case R.id.rbStudent:
                rbTeacher.setChecked(false);
                teacherDetails.setUname("student");
                break;
            case R.id.imgProfilepic:

                break;

            case R.id.btnImageChoose:
                Intent intent2=new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Please Select Image"), Image_Request_Code);
                //Toast.makeText(RegisterActivity.this, "Clicked On ChooseImage", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnImageUpload:
                UploadImageFileToFirebaseStorage();
                Toast.makeText(RegisterActivity.this, "Clicked On UploadImage", Toast.LENGTH_SHORT).show();
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
