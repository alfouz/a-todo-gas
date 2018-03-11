package com.atodogas.brainycar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void drive(View view) {
        Log.d(TAG, "clicked button drive");

        Context context = getApplicationContext();
        CharSequence text = "clicked button drive";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void tripDetail(View view) {
        Log.d(TAG, "clicked to see details of last trip");

        Context context = getApplicationContext();
        CharSequence text = "clicked to see details of last trip";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /** Called when the user taps the "location" menu button */
    public void location(View view) {
        Log.d(TAG, "clicked button location");

        Context context = getApplicationContext();
        CharSequence text = "clicked button location";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /** Called when the user taps the "health" menu button */
    public void health(View view) {
        Log.d(TAG, "clicked button health");

        Context context = getApplicationContext();
        CharSequence text = "clicked button health";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /** Called when the user taps the "location" menu button */
    public void historic(View view) {
        Log.d(TAG, "clicked button historic");

        Context context = getApplicationContext();
        CharSequence text = "clicked button historic";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /** Called when the user taps the "profile" menu button */
    public void profile(View view) {
        Log.d(TAG, "clicked button profile");

        Context context = getApplicationContext();
        CharSequence text = "clicked button profile";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
