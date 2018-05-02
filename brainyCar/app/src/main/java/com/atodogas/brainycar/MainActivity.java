package com.atodogas.brainycar;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.android.gms.tasks.OnCompleteListener;
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

                //Pasamos a la activity de ajustes
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                Task<GoogleSignInAccount> silentSignIn = mGoogleSignInClient.silentSignIn();

                if (silentSignIn.isSuccessful()) {
                    mGoogleSignInClient.signOut();
                    Intent intent2 = new Intent(this, AuthenticationActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent2);
                }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}

