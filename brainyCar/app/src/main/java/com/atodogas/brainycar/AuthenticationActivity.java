package com.atodogas.brainycar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String TAG = AuthenticationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
    }

    /** Called when the user taps the "log with Google" button */
    public void logIn(View view) {
        Log.d(TAG, "clicked button logIn");

        Context context = getApplicationContext();
        CharSequence text = "clicked button logIn";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        //TODO De manera temporal pasamos a la actividad Home
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
