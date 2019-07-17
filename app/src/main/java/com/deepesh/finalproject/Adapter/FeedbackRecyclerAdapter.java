package com.deepesh.finalproject.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.TeacherReview;
import com.deepesh.finalproject.R;

import java.util.ArrayList;

/**
 * Created by Deepesh on 02-10-2017.
 */

public class FeedbackRecyclerAdapter extends RecyclerView.Adapter<FeedbackRecyclerAdapter.ViewHolder>{

    Context context;
    int resource;
    ArrayList<TeacherReview> list;

    int lastPosition;
    View view;
    //TextView txtName;
    //TextView txtEmail;
    public FeedbackRecyclerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TeacherReview> objects) {
        //super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        list = objects;
    }

    @Override
    public FeedbackRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resource,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(FeedbackRecyclerAdapter.ViewHolder holder, int position) {
        TeacherReview teacherReview=list.get(position);
        holder.txtName.setText(teacherReview.getName());
        holder.txtAddr.setText(teacherReview.getReview());
        holder.txtStar.setText(teacherReview.getRate());
        //holder.txtSubj.setText(teacherDetails.getMob());

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }


    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtAddr;
        TextView txtStar;
        //Button btnAdd;
        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.eTextName);
            txtAddr = itemView.findViewById(R.id.eTextAddr);
            txtStar = itemView.findViewById(R.id.txtStar);

            txtStar.setFocusableInTouchMode(true);
            txtStar.setClickable(false);



        }
    }
    /*private void animate(final View view, final int position){

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
        view.startAnimation(animation);
        lastPosition = position;

    }*/
}
