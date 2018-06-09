package com.atodogas.brainycar;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.GetAllTripsBD;
import com.atodogas.brainycar.AsyncTasks.GetCarBD;
import com.atodogas.brainycar.AsyncTasks.GetLastTripBD;
import com.atodogas.brainycar.DTOs.CarDTO;
import com.atodogas.brainycar.DTOs.TripDTO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, CallbackInterface {

    private View root;
    private Spinner spinnerMeasure;
    private Spinner spinnerPeriod;
    private List<TripDTO> trips;
    private LineChart lineChart;
    private java.util.Date date;

    public HistoricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        root = inflater.inflate(R.layout.fragment_historic, container, false);

        TabHost host = root.findViewById(R.id.tabHostHistoric);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Viajes");
        spec.setContent(R.id.tabViajes);
        spec.setIndicator("Viajes");
        host.addTab(spec);

        setContentViajes(root);

        //Tab 2
        spec = host.newTabSpec("Estadisticas");
        spec.setContent(R.id.tabEstadisticas);
        spec.setIndicator("Estadisticas");
        host.addTab(spec);

        setContentEstadisticas(root);

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSelectDate:
                DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        date = calendar.getTime();

                        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
                        day = day.length() == 1 ? "0" + day : day;
                        month = month.length() == 1 ? "0" + month : month;

                        TextView periodGraph = root.findViewById(R.id.periodGraph);
                        periodGraph.setText( day + "/" + month + "/" + calendar.get(Calendar.YEAR));
                        drawPlot(date);
                    }
                };

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(parent.getId()) {
            case R.id.spinnerMeasure:
            case R.id.spinnerPeriod:
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                drawPlot(date);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void setContentViajes(View root){
        /*ArrayList<TripEntity> trips = new ArrayList<TripEntity>();
        for (int i=0; i <= 10; i++) {
            trips.add(new TripEntity(new Date(2017,07,11), new Time(18,00,00),
                    new Time(23,30,00),"Madrid", "A Coruña", 591, 72, 82));
        }*/

        final View rootAux = root;
        /*CallbackInterface callback = new CallbackInterface() {
            @Override
            public void doCallback(Object object) {
                List<TripDTO> trips = (List<TripDTO>) object;

                Log.d("pepe", Integer.toString(trips.size()));
                if(trips.size()==0){
                    TripDTO emptyTrip = new TripDTO();
                    emptyTrip.setStartDate(Calendar.getInstance().getTime());
                    emptyTrip.setEndDate(Calendar.getInstance().getTime());
                    emptyTrip.setEndPlace("Sin localización");
                    emptyTrip.setFuelConsumptionAVG(0);
                    emptyTrip.setHours(0);
                    emptyTrip.setMinutes(0);
                    emptyTrip.setId(-1);
                    emptyTrip.setKms(0);
                    emptyTrip.setSpeedAVG(0);
                    emptyTrip.setStartPlace("Sin localización");
                    trips.add(emptyTrip);
                }
                //trips.add(new TripEntity(new Date(2017,07,11), new Time(18,00,00),
                //        new Time(23,30,00),"Madrid", "A Coruña", 591, 72, 82));

                HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(trips, new HistoricFragmentTripAdapter.OnItemClickListener() {
                    @Override public void onItemClick(TripDTO item) {
                        Toast.makeText(getContext(), item.getStartPlace() + " - " + item.getEndPlace(), Toast.LENGTH_LONG).show();

                        //Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        //intent.putExtra("idUser", item);
                        //startActivity(intent);
                    }
                });
                RecyclerView myView =  rootAux.findViewById(R.id.viajes);
                myView.setHasFixedSize(true);
                myView.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                myView.setLayoutManager(llm);
            }
        };*/
        final CallbackInterface scope = this;
        new GetCarBD(new CallbackInterface<CarDTO>() {
            @Override
            public void doCallback(CarDTO carDTO) {
                new GetAllTripsBD(scope,getContext()).execute(carDTO.getId());
            }
        }, getContext()).execute(getActivity().getIntent().getIntExtra("idUser", -1));


    }

    @Override
    public void doCallback(Object object) {
       trips = (List<TripDTO>) object;

        if(trips.size()==0){
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
        }
        HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(trips, new HistoricFragmentTripAdapter.OnItemClickListener() {
            @Override public void onItemClick(TripDTO item) {
                Toast.makeText(getContext(), item.getStartPlace() + " - " + item.getEndPlace(), Toast.LENGTH_LONG).show();

                //Intent intent = new Intent(getActivity(), DashboardActivity.class);
                //intent.putExtra("idUser", item);
                //startActivity(intent);
            }
        });

        RecyclerView myView =  root.findViewById(R.id.viajes);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        LinearLayout loadingLayout = root.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setContentEstadisticas(View root){
        // Setting options for spinnerMeasure
        spinnerMeasure = root.findViewById(R.id.spinnerMeasure);
        ArrayAdapter<CharSequence> measureAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.measure_array, android.R.layout.simple_spinner_item);
        measureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeasure.setAdapter(measureAdapter);
        spinnerMeasure.setOnItemSelectedListener(this);

        // Setting options for spinnerPeriod
        spinnerPeriod = root.findViewById(R.id.spinnerPeriod);
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.period_array, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);
        spinnerPeriod.setOnItemSelectedListener(this);

        // Setting options for graph, default = km/week
        lineChart = root.findViewById(R.id.lineChart);
        date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1) ;
        day = day.length() == 1 ? "0" + day : day;
        month = month.length() == 1 ? "0" + month : month;

        TextView periodGraph = root.findViewById(R.id.periodGraph);
        periodGraph.setText( day + "/" + month + "/" + calendar.get(Calendar.YEAR));

        // Styling description
        Description description = lineChart.getDescription();
        description.setEnabled(false);
        // Styling legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        // Styling xAxis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineColor(getResources().getColor(R.color.colorPrimaryExtraDark));
        xAxis.setAxisLineWidth(1f);
        xAxis.setGridColor(getResources().getColor(R.color.colorPrimaryLigth2));
        xAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        // Other styling
        lineChart.setNoDataText(getResources().getString(R.string.noChartData));

        // Refreshing chart
        lineChart.invalidate();


        // Setting buttons for graph
        Button btnSelectDate = root.findViewById(R.id.buttonSelectDate);
        btnSelectDate.setOnClickListener(this);
    }

    private void drawPlot(java.util.Date date) {
        List<TripDTO> tripsFilter = new ArrayList<>();
        List<TripDTO> tripsToDraw = new ArrayList<>();
        java.util.Date dateTrip1 = null;
        java.util.Date dateTrip2 = null;

        if(spinnerPeriod.getSelectedItemPosition() == 0){
            for(TripDTO trip : trips){
                if(compareByWeek(date, trip.getStartDate())){
                    tripsFilter.add(trip);
                }
            }

            for(int i = 0; i < tripsFilter.size(); i++){
                List<TripDTO> tripAux= new ArrayList<>();
                tripAux.add(tripsFilter.get(i));

                dateTrip1 = tripsFilter.get(i).getStartDate();
                for(int j = i + 1; j < tripsFilter.size(); j++){
                    dateTrip2 = tripsFilter.get(j).getStartDate();
                    if(compareByDay(dateTrip1, dateTrip2)){
                        tripAux.add(tripsFilter.get(j));
                        i++;
                    }
                }

                TripDTO trip = new TripDTO();
                for(TripDTO t : tripAux){
                    trip.setKms(trip.getKms() + t.getKms());
                    trip.setSpeedAVG(trip.getSpeedAVG() + t.getSpeedAVG());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG() + t.getFuelConsumptionAVG());
                }

                if(tripAux.size() > 0){
                    trip.setStartDate(tripAux.get(0).getStartDate());
                    trip.setSpeedAVG(trip.getSpeedAVG() / tripAux.size());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG()/tripAux.size());
                    tripsToDraw.add(trip);
                }
            }
        }
        else if(spinnerPeriod.getSelectedItemPosition() == 1){
            for(TripDTO trip : trips){
                if(compareByMonth(date, trip.getStartDate())){
                    tripsFilter.add(trip);
                }
            }

            for(int i = 0; i < tripsFilter.size(); i++){
                List<TripDTO> tripAux= new ArrayList<>();
                tripAux.add(tripsFilter.get(i));

                dateTrip1 = tripsFilter.get(i).getStartDate();
                for(int j = i + 1; j < tripsFilter.size(); j++){
                    dateTrip2 = tripsFilter.get(j).getStartDate();
                    if(compareByDay(dateTrip1, dateTrip2)){
                        tripAux.add(tripsFilter.get(j));
                        i++;
                    }
                }

                TripDTO trip = new TripDTO();
                for(TripDTO t : tripAux){
                    trip.setKms(trip.getKms() + t.getKms());
                    trip.setSpeedAVG(trip.getSpeedAVG() + t.getSpeedAVG());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG() + t.getFuelConsumptionAVG());

                    if(trip.getSpeedAVG() > 0){
                        trip.setSpeedAVG(trip.getSpeedAVG() / tripAux.size());
                    }

                    if(trip.getFuelConsumptionAVG() > 0){
                        trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG()/tripAux.size());
                    }
                }

                if(tripAux.size() > 0){
                    trip.setStartDate(tripAux.get(0).getStartDate());
                    trip.setSpeedAVG(trip.getSpeedAVG() / tripAux.size());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG()/tripAux.size());
                    tripsToDraw.add(trip);
                }
            }
        }
        else if(spinnerPeriod.getSelectedItemPosition() == 2){
            for(TripDTO trip : trips){
                if(compareByYear(date, trip.getStartDate())){
                    tripsFilter.add(trip);
                }
            }

            for(int i = 0; i < tripsFilter.size(); i++){
                List<TripDTO> tripAux= new ArrayList<>();
                tripAux.add(tripsFilter.get(i));

                dateTrip1 = tripsFilter.get(i).getStartDate();
                for(int j = i + 1; j < tripsFilter.size(); j ++){
                    dateTrip2 = tripsFilter.get(j).getStartDate();
                    if(compareByMonth(dateTrip1, dateTrip2)){
                        tripAux.add(tripsFilter.get(j));
                        i++;
                    }
                }

                TripDTO trip = new TripDTO();
                for(TripDTO t : tripAux){
                    trip.setKms(trip.getKms() + t.getKms());
                    trip.setSpeedAVG(trip.getSpeedAVG() + t.getSpeedAVG());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG() + t.getFuelConsumptionAVG());
                }

                if(tripAux.size() > 0){
                    trip.setStartDate(tripAux.get(0).getStartDate());
                    trip.setSpeedAVG(trip.getSpeedAVG() / tripAux.size());
                    trip.setFuelConsumptionAVG(trip.getFuelConsumptionAVG()/tripAux.size());
                    tripsToDraw.add(trip);
                }
            }
        }

        List<Entry> entries = new ArrayList<>();
        for(TripDTO trip : tripsToDraw) {
            int period = 0;
            float value = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(trip.getStartDate());

            switch (spinnerMeasure.getSelectedItemPosition()){
                case 0:
                    value = trip.getKms();
                    break;
                case 1:
                    value = trip.getSpeedAVG();
                    break;
                case 2:
                    value = trip.getFuelConsumptionAVG();
                    break;
            }

            switch (spinnerPeriod.getSelectedItemPosition()){
                case 0:
                    // 0 Sun - 7 Sat
                    period = calendar.get(Calendar.DAY_OF_WEEK);

                    if(period == 0){
                        period = 7; //Sun
                    }
                    else {
                        period--;
                    }
                    break;
                case 1:
                    period = calendar.get(Calendar.DAY_OF_MONTH);
                    break;
                case 2:
                    //0 Jan
                    period = period = calendar.get(Calendar.MONTH) + 1;
                    break;
            }

            entries.add(new Entry(period, value));
        }

        lineChart.clear();
        if(entries.size() > 0){
            LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
            dataSet.setColor(getResources().getColor(R.color.colorAccent));
            dataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
            dataSet.setCircleColorHole(getResources().getColor(R.color.colorAccent));
            dataSet.setCircleRadius(5f);
            dataSet.setLineWidth(3.0f);
            dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryExtraDark));
            dataSet.setFillColor(getResources().getColor(R.color.colorAccent));
            dataSet.setDrawFilled(false);
            dataSet.setValueTextSize(12f);
            dataSet.notifyDataSetChanged();

            // Defining data of chart
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.setVisibleXRangeMinimum(entries.size()*3);
            lineChart.setVisibleXRangeMaximum(entries.size()*3);
        }

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private boolean compareByDay(java.util.Date date1, java.util.Date date2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        calendar2.setTime(date2);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar1.equals(calendar2);
    }

    private boolean compareByWeek(java.util.Date date1, java.util.Date date2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR);
    }

    private boolean compareByMonth(java.util.Date date1, java.util.Date date2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar1.set(Calendar.YEAR, 0);
        calendar1.set(Calendar.DAY_OF_MONTH, 0);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        calendar2.setTime(date2);
        calendar2.set(Calendar.YEAR, 0);
        calendar2.set(Calendar.DAY_OF_MONTH, 0);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);

        return calendar1.equals(calendar2);
    }

    private boolean compareByYear(java.util.Date date1, java.util.Date date2){
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date1);
        calendar2.setTime(date2);

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }
}
