package com.atodogas.brainycar;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetAllTripsBD;
import com.atodogas.brainycar.AsyncTasks.GetCarBD;
import com.atodogas.brainycar.AsyncTasks.GetLastTripBD;
import com.atodogas.brainycar.DTOs.CarDTO;
import com.atodogas.brainycar.DTOs.TripDTO;

import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, CallbackInterface<TripDTO> {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;
    private MainActivity mainActivity;
    private String bluetoothState;

    private RecyclerView lastTripLayout;
    
    private TextView dashboardValueElement;
    private TextView gasolineAVGValueElement;
    private TextView speedAVGValueElement;
    private TextView gasolineValueElement;
    private TextView batteryValueElement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();

        Button driveButton = view.findViewById(R.id.driveButton);
        driveButton.setOnClickListener(this);
        lastTripLayout = view.findViewById(R.id.lastTripLayout);
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

        int user = getActivity().getIntent().getIntExtra("idUser", -1);
        //LAST TRIP INFO
        final CallbackInterface<TripDTO> scope = this;
        new GetCarBD(new CallbackInterface<CarDTO>() {
            @Override
            public void doCallback(CarDTO carDTO) {
                if(carDTO!=null) {
                    new GetLastTripBD(scope, getContext()).execute(carDTO.getId());
                    updateGeneralDataCar(carDTO);
                }else{
                    new GetLastTripBD(scope, getContext()).execute(-1);
                }
            }
        }, getContext()).execute(user);

        //GENERAL INFO
        dashboardValueElement = view.findViewById(R.id.dashboardValue);
        dashboardValueElement.setText("0 km");

        gasolineAVGValueElement = view.findViewById(R.id.gasolineAVGValue);
        gasolineAVGValueElement.setText("0 l/100km");

        speedAVGValueElement = view.findViewById(R.id.speedAVGValue);
        speedAVGValueElement.setText("0 km/h");

        gasolineValueElement = view.findViewById(R.id.gasolineValue);
        gasolineValueElement.setText("0 l");

        batteryValueElement = view.findViewById(R.id.batteryValue);
        batteryValueElement.setText("0 V");

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
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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
        Intent intent2 = getActivity().getIntent();
        int idUser = intent2.getIntExtra("idUser", -1);

        if(idUser > 0){
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }
    }

    public void tripDetail() {
        Log.d(TAG, "clicked to see details of last trip");
        Toast.makeText(mainActivity,"clicked to see details of last trip",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doCallback(TripDTO tripDTO) {
        ArrayList<TripDTO> trips = new ArrayList<TripDTO>();
        TripDTO trip = tripDTO;

        if(trip==null){
            TripDTO emptyTrip = new TripDTO();
            emptyTrip.setStartDate(Calendar.getInstance().getTime());
            emptyTrip.setEndDate(Calendar.getInstance().getTime());
            emptyTrip.setEndPlace("Sin datos");
            emptyTrip.setFuelConsumptionAVG(0);
            emptyTrip.setHours(0);
            emptyTrip.setMinutes(0);
            emptyTrip.setId(-1);
            emptyTrip.setKms(0);
            emptyTrip.setSpeedAVG(0);
            emptyTrip.setStartPlace("Sin datos");
            trips.add(emptyTrip);
        }else{
            trips.add(trip);
        }

        HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(trips, new HistoricFragmentTripAdapter.OnItemClickListener() {
            @Override public void onItemClick(TripDTO item) {
                Toast.makeText(getContext(), item.getStartPlace() + " - " + item.getEndPlace(), Toast.LENGTH_LONG).show();
            }
        });
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
    }

    public void updateGeneralDataCar(CarDTO carDTO){
        NumberFormat formatter = new DecimalFormat("#0.0");
        dashboardValueElement.setText( (int) carDTO.getKms() + " km");
        gasolineAVGValueElement.setText( formatter.format(carDTO.getFuelConsumptionAVG()) + " l/100km");
        speedAVGValueElement.setText( formatter.format(carDTO.getSpeedAVG()) + " km/h");
        gasolineValueElement.setText( formatter.format(carDTO.getFuelTankLevel()) + " %");
        batteryValueElement.setText( formatter.format(carDTO.getBattery()) + " V");
    }
}
