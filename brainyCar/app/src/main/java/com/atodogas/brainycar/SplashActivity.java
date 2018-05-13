package com.atodogas.brainycar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by belenvb on 10/05/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start authentication activity
        startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));

        // close splash activity
        finish();

    }

}
