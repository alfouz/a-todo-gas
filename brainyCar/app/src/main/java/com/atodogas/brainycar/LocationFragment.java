package com.atodogas.brainycar;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements SensorEventListener {

    private SensorManager sm;
    private Sensor sAcc;
    private Sensor sGir;
    private Sensor sMag;

    private TextView tacel;
    private TextView tgir;
    private TextView tmag;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location, container, false);

        sm = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        sAcc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sGir = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sMag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        tacel = view.findViewById(R.id.tacel);
        tgir = view.findViewById(R.id.tgir);
        tmag = view.findViewById(R.id.tmag);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        sm.registerListener(this, sAcc, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sGir, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sMag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }

    private void setValues(){
        if(sAcc!=null){
            tacel.setText("Acelerómetro: " + sAcc.getName() + " " + 0);
        }
        else {
            tacel.setText("Acelerómetro no disponible");
        }
        if(sGir!=null){
            tgir.setText("Giroscopio: " + sGir.getName() + " " + 0);
        }
        else {
            tgir.setText("Giroscopio no disponible");
        }
        if(sMag!=null){
            tmag.setText("Magnetómetro: " + sMag.getName() + " " + 0);
        }
        else {
            tmag.setText("Magnetómetro no disponible");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sAcc!=null)if(event.sensor.getName().equals(sAcc.getName())){
            tacel.setText("Acelerómetro: " + sAcc.getName() + " " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        }
        if(sGir!=null)if(event.sensor.getName().equals(sGir.getName())){
            tgir.setText("Giroscopio: " + sGir.getName() + " " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        }
        if(sMag!=null)if(event.sensor.getName().equals(sMag.getName())){
            tmag.setText("Magnetómetro: " + sMag.getName() + " " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
