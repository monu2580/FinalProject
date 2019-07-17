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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.R;

import java.util.ArrayList;

/**
 * Created by Deepesh on 02-10-2017.
 */

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{

    Context context;
    int resource;
    ArrayList<TeacherDetails> list;

    int lastPosition=-1;
    //TextView txtName;
    //TextView txtEmail;
    public UserRecyclerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<TeacherDetails> objects) {
        //super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        list = objects;
    }

    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resource,parent,false);
        ViewHolder holder=new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(UserRecyclerAdapter.ViewHolder holder, int position) {
        TeacherDetails teacherDetails=list.get(position);
        holder.txtName.setText(teacherDetails.getName());
        holder.txtAddr.setText(teacherDetails.getAddr());
        holder.txtSubj.setText(teacherDetails.getSubj());
        //holder.txtSubj.setText(teacherDetails.getMob());

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    //R.anim.up_from_bottom);
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
        TextView txtSubj;
        //Button btnAdd;
        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.eTextName);
            txtAddr = itemView.findViewById(R.id.eTextAddr);
            txtSubj = itemView.findViewById(R.id.eTextSub);
            //btnAdd=itemView.findViewById(R.id.btnAdd);
            /*btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("dbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbfs,j");
                }
            });*/

        }
    }
}
