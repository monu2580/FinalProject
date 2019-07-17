package com.deepesh.finalproject.Activity.Fragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.deepesh.finalproject.Adapter.FeedbackRecyclerAdapter;
import com.deepesh.finalproject.Model.TeacherReview;
import com.deepesh.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    RatingBar ratingBar;
    EditText review;
    Button btnFeedback;
    RecyclerView fRecyclerView;
    String cuName,tId,tName;
    TextView teacherName;
    //ImageView teacherPic;
    CircleImageView teacherPic;

    ArrayList<TeacherReview> trList=new ArrayList<>();

    FeedbackRecyclerAdapter feedbackRecyclerAdapter;
    LinearLayoutManager manager;
    String rName,rRate,reviewText,tpic;
    float rfRate=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        fRecyclerView=(RecyclerView)findViewById(R.id.fRecyclerView);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        review=(EditText)findViewById(R.id.eTxtReview);
        btnFeedback=(Button)findViewById(R.id.btnFeedback);
        teacherName=(TextView)findViewById(R.id.textViewName);
        //teacherPic=(ImageView)findViewById(R.id.imageViewTeacher);
        teacherPic=(CircleImageView) findViewById(R.id.imageViewTeacher);
        manager=new LinearLayoutManager(getApplicationContext());

        Intent rcv=getIntent();
        cuName=rcv.getStringExtra("cuName");
        tId=rcv.getStringExtra("tId");
        tName=rcv.getStringExtra("tName");
        tpic=rcv.getStringExtra("tpic");

        //Toast.makeText(FeedbackActivity.this, "cuName .."+cuName, Toast.LENGTH_SHORT).show();
        //Toast.makeText(FeedbackActivity.this, "tName .."+tName, Toast.LENGTH_SHORT).show();
        //Toast.makeText(FeedbackActivity.this, "tId .."+tId, Toast.LENGTH_SHORT).show();
        //Toast.makeText(FeedbackActivity.this, "tpic .."+tpic, Toast.LENGTH_SHORT).show();
        if(tpic!=null){

            Glide.with(FeedbackActivity.this)
                    .load(tpic)
                    .fitCenter()
                    .centerCrop()
                    .into(teacherPic);
        }else {

        }
        teacherName.setText(tName);
        //review.setText("nvbfshjvmg");
        //Toast.makeText(FeedbackActivity.this, "getIntet .."+cuName+"  "+tId, Toast.LENGTH_SHORT).show();

        RetrieveReview();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rRate=String.valueOf(ratingBar.getRating());
                rfRate=ratingBar.getRating();
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                trList.clear();
                ///Toast.makeText(getContext(), "You gave :"+rRate+"  Stars"+"  to  "+cuTeacherDetails.getName(), Toast.LENGTH_SHORT).show();
                reviewText=review.getText().toString().trim();

                DatabaseReference dRef2= FirebaseDatabase.getInstance().getReference().child("teacherReview").child(tId);
                TeacherReview teacherReview=new TeacherReview(cuName,rRate,reviewText);
                dRef2.push().setValue(teacherReview);


                review.setText(null);
                ratingBar.setRating(0);



                //teacherReviewsList.add(teacherReview);
                /*trList.add(teacherReview);
                feedbackRecyclerAdapter=new FeedbackRecyclerAdapter(FeedbackActivity.this,R.layout.recyclre_view_feedback,trList);
                fRecyclerView.setLayoutManager(manager);
                fRecyclerView.setAdapter(feedbackRecyclerAdapter);
                feedbackRecyclerAdapter.notifyDataSetChanged();*/

                //dialog.dismiss();
            }
        });

    }



    public void RetrieveReview(){
        //Retrieve Review
        try {


        DatabaseReference dRef3=FirebaseDatabase.getInstance().getReference().child("teacherReview").child(tId);
        dRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot trSnapshot:dataSnapshot.getChildren()){
                        TeacherReview teacherReview=trSnapshot.getValue(TeacherReview.class);
                        try {


                        String n=teacherReview.getName().toString().trim();
                        String r=teacherReview.getRate().toString().trim();
                        String rvu=teacherReview.getReview().toString().trim();


                        TeacherReview tr=new TeacherReview(n,r,rvu);
                        trList.add(tr);
                        }catch (Exception e){

                        }
                    }

                    //Appand row at the top of Recycler View   by default row append at the Buttom
                    Collections.reverse(trList);

                    feedbackRecyclerAdapter=new FeedbackRecyclerAdapter(FeedbackActivity.this,R.layout.recyclre_view_feedback,trList);
                    fRecyclerView.setLayoutManager(manager);
                    fRecyclerView.setAdapter(feedbackRecyclerAdapter);
                    feedbackRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "check again", Toast.LENGTH_SHORT).show();
        }

        //RecyclerView
        //fRecyclerView=(RecyclerView) view.findViewById(R.id.fRecyclerView);
         //LinearLayoutManager manager=new LinearLayoutManager(getApplicationContext());
        //LinearLayoutManager manager1=new LinearLayoutManager(getActivity().getApplicationContext());
        /*feedbackRecyclerAdapter=new FeedbackRecyclerAdapter(FeedbackActivity.this,R.layout.recyclre_view_feedback,trList);
        fRecyclerView.setLayoutManager(manager);
        fRecyclerView.setAdapter(feedbackRecyclerAdapter);
        feedbackRecyclerAdapter.notifyDataSetChanged();*/
    }

    private void UserFeedback(){

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///Toast.makeText(getContext(), "You gave :"+rRate+"  Stars"+"  to  "+cuTeacherDetails.getName(), Toast.LENGTH_SHORT).show();
                reviewText=review.getText().toString().trim();

                DatabaseReference dRef2= FirebaseDatabase.getInstance().getReference().child("teacherReview").child(tId);
                TeacherReview teacherReview=new TeacherReview(rName,rRate,reviewText);
                dRef2.push().setValue(teacherReview);

                //teacherReviewsList.add(teacherReview);
                trList.add(teacherReview);
                feedbackRecyclerAdapter=new FeedbackRecyclerAdapter(FeedbackActivity.this,R.layout.recyclre_view_feedback,trList);
                fRecyclerView.setLayoutManager(manager);
                fRecyclerView.setAdapter(feedbackRecyclerAdapter);
                feedbackRecyclerAdapter.notifyDataSetChanged();

                //dialog.dismiss();
            }
        });

    }

    private boolean validateForm() {
        boolean result = true;

        if(ratingBar.equals(null)){
            ratingBar.setRating(0);

        }else {
            ratingBar.setRating(rfRate);
        }
        if (TextUtils.isEmpty(review.getText().toString())) {
            review.setError("Required");
            result = false;
        } else {
            review.setError(null);
        }


        return result;
    }




    @Override
    public void onClick(View v) {

    }
}
