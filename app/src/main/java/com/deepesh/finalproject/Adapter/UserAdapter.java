package com.deepesh.finalproject.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deepesh.finalproject.Model.TeacherDetails;
import com.deepesh.finalproject.Model.Teachers;
import com.deepesh.finalproject.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Deepesh on 28-10-2017.
 */

public class UserAdapter extends ArrayAdapter<TeacherDetails> {

    Context context;
    int resource;
    //ArrayList<Teachers> teacherList;
    ArrayList<TeacherDetails> teacherList;

    TextView txtName;
    TextView txtAddr;
    TextView txtSubj;

    public UserAdapter(@NonNull Context context,@LayoutRes int resource,@NonNull  ArrayList<TeacherDetails> objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        teacherList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        view = LayoutInflater.from(context).inflate(resource,parent,false);

        txtName = (TextView)view.findViewById(R.id.eTextName);
        txtAddr = (TextView)view.findViewById(R.id.eTextAddr);
        txtSubj = (TextView)view.findViewById(R.id.eTextSub);

        //Teachers teacher = list.get(position);
        TeacherDetails teacherDetails=teacherList.get(position);


        txtName.setText(teacherDetails.getName());
        txtAddr.setText(teacherDetails.getAddr());
        txtSubj.setText(teacherDetails.getSubj());

        return view;
    }
}
