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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
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
        RecyclerView lastTripLayout = view.findViewById(R.id.lastTripLayout);
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

        ArrayList<TripEntity> trips = new ArrayList<TripEntity>();
        trips.add(new TripEntity(new Date(2017,07,11), new Time(18,00,00),
                new Time(23,30,00),"Madrid", "A Coruña", 591, 72, 82));

        HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(trips);
        lastTripLayout.setHasFixedSize(true);
        lastTripLayout.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lastTripLayout.setLayoutManager(llm);


        //GENERAL INFO

        TextView dashboardValueElement = view.findViewById(R.id.dashboardValue);
        dashboardValueElement.setText("2500km");

        TextView gasolineValueElement = view.findViewById(R.id.gasolineValue);
        gasolineValueElement.setText("25/50l");

        TextView coolantValueElement = view.findViewById(R.id.coolantValue);
        coolantValueElement.setText("4l");

        TextView dashboardValueElement2 = view.findViewById(R.id.dashboardValue2);
        dashboardValueElement2.setText("2500km");

        TextView gasolineValueElement2 = view.findViewById(R.id.gasolineValue2);
        gasolineValueElement2.setText("25/50l");

        TextView coolantValueElement2 = view.findViewById(R.id.coolantValue2);
        coolantValueElement2.setText("4l");

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
