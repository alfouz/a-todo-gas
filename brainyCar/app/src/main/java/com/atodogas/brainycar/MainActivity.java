package com.atodogas.brainycar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment actualFragmentMenu;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);


        //loading the default fragment
        loadFragment(new HomeFragment());

        // Setting bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        changeActionBarTitle(getResources().getString(R.string.home));
    }

    public void changeActionBarTitle(String newTitle) {
        getSupportActionBar().setTitle(newTitle);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            actualFragmentMenu = fragment;
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {

            case R.id.navigation_home:
                fragment = new HomeFragment();
                changeActionBarTitle(getResources().getString(R.string.home));
                break;

            case R.id.navigation_location:
                fragment = new LocationFragment();
                changeActionBarTitle(getResources().getString(R.string.location));
                break;

            case R.id.navigation_diagnostic:
                fragment = new DiagnosticButtonFragment();
                //fragment = new DiagnosticFragment();
                changeActionBarTitle(getResources().getString(R.string.diagnostic));
                break;

            case R.id.navigation_historic:
                fragment = new HistoricFragment();
                changeActionBarTitle(getResources().getString(R.string.historic));
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                changeActionBarTitle(getResources().getString(R.string.profile));
                // Recibimos el nombre de usuario
                Intent myIntent = getIntent();
                String userName = myIntent.getStringExtra("userName");

                // Enviamos el nombre de usuario al fragment de perfil
                // Ampliable a cualquier dato obtenido de la cuenta del usuario
                Bundle bundle = new Bundle();
                String name = userName;
                bundle.putString("userName", name );
                fragment.setArguments(bundle);

                break;
        }

        if (actualFragmentMenu.getClass().equals(fragment.getClass())) {
            return false;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Check botón de atrás
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulse de nuevo atrás para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Log.d(TAG, "clicked on settings");

                //Pasamos a la activity de ajustes
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                // User chose the "About" item, show the app about UI...
                Log.d(TAG, "clicked on about");

                //Pasamos a la activity de acerca de
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                // User chose the "Logout" item, show the authentication about UI...
                Log.d(TAG, "clicked on logout");

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                Task<GoogleSignInAccount> silentSignIn = mGoogleSignInClient.silentSignIn();

                if (silentSignIn.isSuccessful()) {
                    mGoogleSignInClient.signOut();
                    intent = new Intent(this, AuthenticationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}

