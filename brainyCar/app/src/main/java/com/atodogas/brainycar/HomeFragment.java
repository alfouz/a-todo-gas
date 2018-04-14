package com.atodogas.brainycar;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;
    private MainActivity mainActivity;
    private String bluetoothState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();

        Button driveButton = view.findViewById(R.id.driveButton);
        driveButton.setOnClickListener(this);
        View lastTripLayout = view.findViewById(R.id.lastTripLayout);
        lastTripLayout.setOnClickListener(this);

        //**************************************BLUETOOTH**************************************
        //we see if the device has bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            bluetoothState = getResources().getString(R.string.bluetoothUnable);
        } else {
            // We see if it is enabled and if not we enable it
            if (!mBluetoothAdapter.isEnabled()) {
                bluetoothState = getResources().getString(R.string.bluetoothConecting);
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                bluetoothState = getResources().getString(R.string.bluetoothOn);
            }

            //TODO probando a mostrar dispositivos sincronizados
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                Log.d(TAG, "Dispositivos sincronizados por bluetooth:");
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    Log.d(TAG, device.getName() + "\n" + device.getAddress());
                }
            }
        }
        mainActivity.changeActionBarTitle(bluetoothState);


        //**************************************DATA**************************************
        // CAR BRAND
        TextView cardBrandElement = view.findViewById(R.id.carBrand);
        cardBrandElement.setText("VW Golf TDI 2011");

        //LAST TRIP INFO

        TextView lastTripDateElement = view.findViewById(R.id.lastTripDate);
        lastTripDateElement.setText("17/01/18");

        TextView firstTripTimeElement = view.findViewById(R.id.firstTripTime);
        firstTripTimeElement.setText("18:00h");

        TextView firstPlaceElement = view.findViewById(R.id.firstPlace);
        firstPlaceElement.setText("Madrid");

        TextView secondTripTimeElement = view.findViewById(R.id.secondTripTime);
        secondTripTimeElement.setText("23:30h");

        TextView secondPlaceElement = view.findViewById(R.id.secondPlace);
        secondPlaceElement.setText("A Coruña");

        TextView tripDashboardValueElement = view.findViewById(R.id.tripDashboardValue);
        tripDashboardValueElement.setText("591km");

        TextView tripHourglassValueElement = view.findViewById(R.id.tripHourglassValue);
        tripHourglassValueElement.setText("5h 30m");

        TextView tripGasolineValueElement = view.findViewById(R.id.tripGasolineValue);
        tripGasolineValueElement.setText("72l");

        //GENERAL INFO

        TextView dashboardValueElement = view.findViewById(R.id.dashboardValue);
        dashboardValueElement.setText("2500km");

        TextView gasolineValueElement = view.findViewById(R.id.gasolineValue);
        gasolineValueElement.setText("25/50l");

        TextView coolantValueElement = view.findViewById(R.id.coolantValue);
        coolantValueElement.setText("4l");

        return view;
    }

    // Results bluetooth request
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                switch(resultCode) {
                    case Activity.RESULT_OK:
                        bluetoothState = getResources().getString(R.string.bluetoothOn);
                        break;
                    case Activity.RESULT_CANCELED:
                        bluetoothState = getResources().getString(R.string.bluetoothOff);
                }
                mainActivity.changeActionBarTitle(bluetoothState);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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
        //Pasamos a la activity de dashboard
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }

    public void tripDetail() {
        Log.d(TAG, "clicked to see details of last trip");
        Toast.makeText(mainActivity,"clicked to see details of last trip",Toast.LENGTH_SHORT).show();
    }

}
