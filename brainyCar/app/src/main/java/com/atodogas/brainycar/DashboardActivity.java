package com.atodogas.brainycar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.Services.Extra.DashboardDTO;
import com.atodogas.brainycar.Services.TrackingService;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import in.unicodelabs.kdgaugeview.KdGaugeView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = DashboardActivity.class.getSimpleName();

    private TextView temperaturaTextView, bateriaTextView, gasolinaTextView, kmRecorridosTextView,
            tiempoTranscurridoTextView;
    private KdGaugeView revolucionesTextView, velocidadTextView;

    private LocalBroadcastManager localBroadcastManager;
    private LinearLayout loadingLayout, dashboardDataLayout;
    private TextView dashboardLoadingTextView;

    private String bluetoothState;
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Setting action bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Dashboard");

        //Otener campos para rellenar en las actualizaciones
        temperaturaTextView = (TextView) findViewById(R.id.temperaturaTextView);
        bateriaTextView = (TextView) findViewById(R.id.bateriaTextView);
        gasolinaTextView = (TextView) findViewById(R.id.gasolinaTextView);
        kmRecorridosTextView = (TextView) findViewById(R.id.kmRecorridosTextView);
        tiempoTranscurridoTextView = findViewById(R.id.tiempoTranscurridoTextView);
        revolucionesTextView = (KdGaugeView) findViewById(R.id.rpmMeter);
        velocidadTextView = (KdGaugeView) findViewById(R.id.kmHourMeter);
        dashboardLoadingTextView = (TextView) findViewById(R.id.dashboardLoadingTextView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);
        dashboardDataLayout = findViewById(R.id.dashboardDataLayout);
        dashboardDataLayout.setVisibility(View.INVISIBLE);

        View detenerButton = findViewById(R.id.detenerButton);
        detenerButton.setOnClickListener(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        boolean isRunningServices = getIntent().getBooleanExtra("isRunningServices", false);
        if(!isRunningServices){
            int idUser = getIntent().getIntExtra("idUser", - 1);
            Intent trackingServiceIntent = new Intent(this, TrackingService.class);
            trackingServiceIntent.putExtra("idUser", idUser);
            startService(trackingServiceIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bluetoothConexion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        localBroadcastManager.unregisterReceiver(dashboardDTOReceive);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.detenerButton:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                detener();
                break;
        }
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        NumberFormat formatter = new DecimalFormat("#0.0");

        if(dashboardDTO.temperature != -1){
            temperaturaTextView.setText(formatter.format(dashboardDTO.temperature) + " ºC");
        }

        if(dashboardDTO.battery != -1){
            bateriaTextView.setText(formatter.format(dashboardDTO.battery) + " V");
        }

        if(dashboardDTO.l100kmavg != -1){
            gasolinaTextView.setText(formatter.format(dashboardDTO.l100kmavg) + " L/100km");
        }
        else {
            gasolinaTextView.setText("-");
        }

        kmRecorridosTextView.setText(formatter.format(dashboardDTO.km) + " km");

        tiempoTranscurridoTextView.setText(dashboardDTO.hours + " h " + dashboardDTO.minutes + " m " + dashboardDTO.seconds + " s");

        if(dashboardDTO.rpm != -1){
            revolucionesTextView.setSpeed(dashboardDTO.rpm/1000);
        }

        if(dashboardDTO.speed != -1){
            velocidadTextView.setSpeed(dashboardDTO.speed);
        }
    }

    private void bluetoothConexion() {
        //we see if the device has bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            dashboardLoadingTextView.setText(getResources().getString(R.string.bluetoothLoading));
            // Device does support Bluetooth
            // We see if it is enabled and if not we enable it
            if (!mBluetoothAdapter.isEnabled()) {
                bluetoothState = getResources().getString(R.string.bluetoothConecting);
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                dashboardLoadingTextView.setText(getResources().getString(R.string.obdLoading));
                localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.DASHBOARD_DTO));
            }
        } else {
            localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.OBD_NOT_CONNECTED));
        }
    }

    // Results bluetooth request
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    dashboardLoadingTextView.setText(getResources().getString(R.string.obdLoading));
                }
                
                // TODO esto se hace igual tnato si el bluetooth se conecta como si no?
                localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.DASHBOARD_DTO));
                localBroadcastManager.registerReceiver(dashboardDTOReceive, new IntentFilter(TrackingService.OBD_NOT_CONNECTED));
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
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
                dashboardDataLayout.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    };
}
