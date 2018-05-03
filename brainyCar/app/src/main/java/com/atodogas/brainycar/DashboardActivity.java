package com.atodogas.brainycar;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.Services.Extra.DashboardDTO;
import com.atodogas.brainycar.Services.OBDService;
import com.atodogas.brainycar.Services.TrackingService;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private LocalBroadcastManager localBroadcastManager;
    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Dashboard");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        Button pausarButton = findViewById(R.id.pausarButton);
        pausarButton.setOnClickListener(this);
        View detenerButton = findViewById(R.id.detenerButton);
        detenerButton.setOnClickListener(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        //Actualizar pantalla
        //updateDashboardInformation();
        int idUser = getIntent().getIntExtra("idUser", - 1);
        Intent trackingServiceIntent = new Intent(this, TrackingService.class);
        trackingServiceIntent.putExtra("idUser", idUser);
        startService(trackingServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.DASHBOARD_DTO));
        localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.OBD_NOT_CONNECTED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(dashboardDTOReceive);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.pausarButton:
                pausar();
                break;
            case R.id.detenerButton:
                detener();
                break;
        }
    }

    public void pausar() {
        Context context = getApplicationContext();
        CharSequence text = "clicked button Pausar";
        int duration = Toast.LENGTH_SHORT;

        Log.d(TAG, "clicked button Pausar");
        Toast.makeText(context, text, duration).show();
    }

    public void detener() {
        Context context = getApplicationContext();
        CharSequence text = "clicked button Detener";
        int duration = Toast.LENGTH_SHORT;

        Log.d(TAG, "clicked button Detener");
        Toast.makeText(context, text, duration).show();


        Intent trackingServiceIntent = new Intent(this, TrackingService.class);
        stopService(trackingServiceIntent);

        Intent intent = new Intent(this, MainActivity.class);
        int idUser = getIntent().getIntExtra("idUser", - 1);
        intent.putExtra("idUser", idUser);
        startActivity(intent);
    }

    // Check botón de atrás
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulse el botón de Detener para salir de este modo", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    ///TODO Obtener los valores a través de bluetooth
    //Metodo para actualizar los valores de la pantalla
    private void updateDashboardInformation(DashboardDTO dashboardDTO) {
        float temperaturaValue = dashboardDTO.temperature;
        float bateriaValue = dashboardDTO.battery;
        int gasolinaValue = 23;
        int kmRecorridosValue = 175;
        int tiempoTranscurridoHorasValue = 2;
        int tiempoTranscurridoMinutosValue = 15;
        int tiempoTranscurridoSegundosValue = 12;
        int revolucionesValue = dashboardDTO.rpm;
        int velocidadValue = dashboardDTO.speed;

        if(dashboardDTO.temperature != -1){
            final TextView temperaturaTextView = (TextView) findViewById(R.id.temperaturaTextView);
            temperaturaTextView.setText(dashboardDTO.temperature + " ºC");
        }

        if(dashboardDTO.battery != -1){
            final TextView bateriaTextView = (TextView) findViewById(R.id.bateriaTextView);
            bateriaTextView.setText(dashboardDTO.battery + " V");
        }


        final TextView gasolinaTextView = (TextView) findViewById(R.id.gasolinaTextView);
        gasolinaTextView.setText(gasolinaValue + " L");

        final TextView kmRecorridosTextView = (TextView) findViewById(R.id.kmRecorridosTextView);
        kmRecorridosTextView.setText(kmRecorridosValue + " km");


        final TextView tiempoTranscurridoTextView = findViewById(R.id.tiempoTranscurridoTextView);
        tiempoTranscurridoTextView.setText(dashboardDTO.hours + " h " + dashboardDTO.minutes + " m "
                + dashboardDTO.seconds + " s");

        if(dashboardDTO.rpm != -1){
            final TextView revolucionesTextView = (TextView) findViewById(R.id.revolucionesTextView);
            revolucionesTextView.setText("" + dashboardDTO.rpm);
        }

        if(dashboardDTO.speed != -1){
            final TextView velocidadTextView = (TextView) findViewById(R.id.velocidadTextView);
            velocidadTextView.setText("" + dashboardDTO.speed);
        }
    }

    private final BroadcastReceiver dashboardDTOReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(TrackingService.DASHBOARD_DTO.equals(action)) {
                DashboardDTO dashboardDTO = intent.getParcelableExtra("DashboardDTO");
                updateDashboardInformation(dashboardDTO);
            }
            else if (TrackingService.OBD_NOT_CONNECTED.equals(action)){
                CharSequence text = "OBD no conectado";
                int duration = Toast.LENGTH_SHORT;

                Log.d(TAG, "OBD no conectado");
                Toast.makeText(context, text, duration).show();
            }

            if(loadingLayout.getVisibility() == View.VISIBLE){
                loadingLayout.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    };
}
