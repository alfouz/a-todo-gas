package com.atodogas.brainycar;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //change action bar title
        MainActivity activity = (MainActivity) getActivity();
        activity.changeActionBarTitle("Estado bluetooth");

        Button driveButton = view.findViewById(R.id.driveButton);
        driveButton.setOnClickListener(this);
        View lastTripLayout = view.findViewById(R.id.lastTripLayout);
        lastTripLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.driveButton:
                drive();
                break;
            case R.id.lastTripLayout:
                tripDetail();
                break;
        }
    }

    public void drive() {
        Log.d(TAG, "clicked button drive");
        Toast.makeText(getActivity(),"clicked button drive",Toast.LENGTH_SHORT).show();
    }

    public void tripDetail() {
        Log.d(TAG, "clicked to see details of last trip");
        Toast.makeText(getActivity(),"clicked to see details of last trip",Toast.LENGTH_SHORT).show();
    }

}
