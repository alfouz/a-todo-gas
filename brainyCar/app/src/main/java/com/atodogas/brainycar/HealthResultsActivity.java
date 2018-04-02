package com.atodogas.brainycar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HealthResultsActivity extends AppCompatActivity {
    private static final String TAG = HealthResultsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_results);
        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ListView erroresListView = findViewById(R.id.erroresListView);
        String[] errores = { "P0123", "B0523", "P1234", "C0983", "B0983", "P1983"};
        erroresListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, errores));

        erroresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                Context context = getApplicationContext();
                CharSequence text = "clicked on " + selectedItem;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Log.d(TAG, text.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Log.d(TAG, "clicked on settings");

                Context context = getApplicationContext();
                CharSequence text = "clicked on settings";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //Sobreescrito para pasar al menú ajustes
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /** Called when the user taps the "health" menu button */
    public void home(View view) {
        Log.d(TAG, "clicked button home");

        Context context = getApplicationContext();
        CharSequence text = "clicked button home";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(this, HomeFragment.class);
        startActivity(intent);
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

    /** Called when the user taps the "location" menu button */
    public void historic(View view) {
        Log.d(TAG, "clicked button historic");

        Context context = getApplicationContext();
        CharSequence text = "clicked button historic";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(this, HistoricActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the "profile" menu button */
    public void profile(View view) {
        Log.d(TAG, "clicked button profile");

        Context context = getApplicationContext();
        CharSequence text = "clicked button profile";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void restartHealth(View view){
        Log.d(TAG, "clicked button restart health");

        Context context = getApplicationContext();
        CharSequence text = "clicked button restart health";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
