package com.atodogas.brainycar;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string .settingsTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("idUser", getIntent().getIntExtra("idUser", - 1));
                intent.putExtra("personName", getIntent().getStringExtra("personName"));
                intent.putExtra("personPhotoUrl", getIntent().getStringExtra("personPhotoUrl"));
                startActivity(intent);
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.settingsContainer, new SettingsFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("idUser", getIntent().getIntExtra("idUser", - 1));
        intent.putExtra("personName", getIntent().getStringExtra("personName"));
        intent.putExtra("personPhotoUrl", getIntent().getStringExtra("personPhotoUrl"));
        startActivity(intent);
    }
}
