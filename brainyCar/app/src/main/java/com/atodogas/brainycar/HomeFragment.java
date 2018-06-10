package com.atodogas.brainycar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetCarBD;
import com.atodogas.brainycar.AsyncTasks.GetLastTripBD;
import com.atodogas.brainycar.DTOs.CarDTO;
import com.atodogas.brainycar.DTOs.TripDTO;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, CallbackInterface<TripDTO> {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private MainActivity mainActivity;
    
    private TextView dashboardValueElement, gasolineAVGValueElement, speedAVGValueElement,
            gasolineValueElement, batteryValueElement, lastTripDate, lastTripStartTime,
            lastTripStartPlace, lastTripEndTime, lastTripEndPlace, lastTripKmText,
            lastTripDurationText, lastTripGasolineText, lastTripPointsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();

        Button driveButton = view.findViewById(R.id.driveButton);
        driveButton.setOnClickListener(this);

        View lastTrip = view.findViewById(R.id.lastTrip);
        lastTrip.setOnClickListener(this);


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

        //LAST TRIP INFO
        lastTripDate = view.findViewById(R.id.date);
        lastTripDate.setText("--/--/----");

        lastTripStartTime = view.findViewById(R.id.startTime);
        lastTripStartTime.setText("--:-- h");

        lastTripStartPlace = view.findViewById(R.id.startPlace);
        lastTripStartPlace.setText("");

        lastTripEndTime = view.findViewById(R.id.endTime);
        lastTripEndTime.setText("--:-- h");

        lastTripEndPlace = view.findViewById(R.id.endPlace);
        lastTripEndPlace.setText("");

        lastTripKmText = view.findViewById(R.id.kmText);
        lastTripKmText.setText("- km");

        lastTripDurationText = view.findViewById(R.id.durationText);
        lastTripDurationText.setText("--:-- h");

        lastTripGasolineText = view.findViewById(R.id.gasolineText);
        lastTripGasolineText.setText("- l/100km");

        lastTripPointsText = view.findViewById(R.id.pointsText);
        lastTripPointsText.setText("- km/h");


        //GENERAL INFO
        dashboardValueElement = view.findViewById(R.id.dashboardValue);
        dashboardValueElement.setText("- km");

        gasolineAVGValueElement = view.findViewById(R.id.gasolineAVGValue);
        gasolineAVGValueElement.setText("- l/100km");

        speedAVGValueElement = view.findViewById(R.id.speedAVGValue);
        speedAVGValueElement.setText("- km/h");

        gasolineValueElement = view.findViewById(R.id.gasolineValue);
        gasolineValueElement.setText("- l");

        batteryValueElement = view.findViewById(R.id.batteryValue);
        batteryValueElement.setText("- V");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.driveButton:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                drive();
                break;
            case R.id.lastTrip:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
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
    public void doCallback(TripDTO trip) {

        if(trip!=null) {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("H:mm");
            lastTripDate.setText(DATE_FORMAT.format(trip.getStartDate()).toString());
            lastTripStartTime.setText(TIME_FORMAT.format(trip.getStartDate()).toString() + " h");
            lastTripEndTime.setText(TIME_FORMAT.format(trip.getEndDate()).toString() + " h");
            lastTripStartPlace.setText(trip.getStartPlace());
            lastTripEndPlace.setText(trip.getEndPlace());
            lastTripKmText.setText(String.format("%.2f", trip.getKms()) + " km");
            Time duration = new Time(trip.getEndDate().getTime() - trip.getStartDate().getTime());
            lastTripDurationText.setText(TIME_FORMAT.format(duration).toString() + " h");
            lastTripGasolineText.setText(String.format("%.2f", trip.getFuelConsumptionAVG()) + " l/km");
            lastTripPointsText.setText(String.format("%.2f", trip.getSpeedAVG()) + " km/h");
        }
    }

    public void updateGeneralDataCar(CarDTO carDTO){
        NumberFormat formatter = new DecimalFormat("#0.00");
        dashboardValueElement.setText( (int) carDTO.getKms() + " km");
        gasolineAVGValueElement.setText( formatter.format(carDTO.getFuelConsumptionAVG()) + " l/100km");
        speedAVGValueElement.setText( formatter.format(carDTO.getSpeedAVG()) + " km/h");
        gasolineValueElement.setText( formatter.format(carDTO.getFuelTankLevel()) + " %");
        batteryValueElement.setText( formatter.format(carDTO.getBattery()) + " V");
    }
}
