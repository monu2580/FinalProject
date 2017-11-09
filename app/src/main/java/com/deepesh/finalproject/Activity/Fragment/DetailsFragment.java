package com.deepesh.finalproject.Activity.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deepesh.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Toast.makeText(getActivity(),"Displaying Toast: ",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);

    }

}
