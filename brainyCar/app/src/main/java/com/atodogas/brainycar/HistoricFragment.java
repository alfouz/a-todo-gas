package com.atodogas.brainycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public HistoricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_historic, container, false);

        TabHost host = root.findViewById(R.id.tabHostHistoric);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Viajes");
        spec.setContent(R.id.viajes);
        spec.setIndicator("Viajes");
        host.addTab(spec);

        ArrayList<TripEntity> trips = new ArrayList<TripEntity>();
        for (int i=0; i <= 10; i++) {
            trips.add(new TripEntity(new Date(2017,07,11), new Time(18,00,00),
                    new Time(23,30,00),"Madrid", "A Coruña", 591, 72, 82));
        }

        HistoricFragmentTripAdapter adapter = new HistoricFragmentTripAdapter(trips);
        RecyclerView myView =  root.findViewById(R.id.viajes);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        //Tab 2
        spec = host.newTabSpec("Estadisticas");
        spec.setContent(R.id.estadisticas);
        spec.setIndicator("Estadisticas");
        host.addTab(spec);


        // Setting options for spinnerMeasure
        Spinner spinnerMeasure = root.findViewById(R.id.spinnerMeasure);
        ArrayAdapter<CharSequence> measureAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.measure_array, android.R.layout.simple_spinner_item);
        measureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeasure.setAdapter(measureAdapter);
        spinnerMeasure.setOnItemSelectedListener(this);

        // Setting options for spinnerPeriod
        Spinner spinnerPeriod = root.findViewById(R.id.spinnerPeriod);
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.period_array, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);
        spinnerPeriod.setOnItemSelectedListener(this);

        // Setting options for graph, default = km/day
        LineChart lineChart = root.findViewById(R.id.lineChart);
        TextView periodGraph = root.findViewById(R.id.periodGraph);
        periodGraph.setText("21/04/2018");

        // Defining values for a line
        List<Entry> entries = new ArrayList<>();
        for (int i=0;i<=10;i++) {
            entries.add(new Entry(i,i));
        }
        // Defining the line with the previous values + styling line
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleColorHole(getResources().getColor(R.color.colorAccent));
        dataSet.setCircleRadius(5f);
        dataSet.setLineWidth(3.0f);
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryExtraDark));
        dataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        dataSet.setDrawFilled(true);
        dataSet.setValueTextSize(12f);

        // Defining data of chart
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
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
        // Styling yAxis
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.colorPrimaryExtraDark));
        leftAxis.setAxisLineWidth(1f);
        leftAxis.setGridColor(getResources().getColor(R.color.colorPrimaryLigth2));
        leftAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        //Zooming
        lineChart.setVisibleXRangeMinimum(entries.size()/2);
        lineChart.setVisibleXRangeMaximum(entries.size()/2);
        // Other styling
        lineChart.setNoDataText(getResources().getString(R.string.noChartData));

        // Refreshing chart
        lineChart.invalidate();


        // Setting buttons for graph
        Button btnPrevious = root.findViewById(R.id.buttonPrevious);
        Button btnNext = root.findViewById(R.id.buttonNext);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);


        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPrevious:
                Log.d("button", "Button Previous");
                Toast.makeText(getActivity(),"clicked button previous",Toast.LENGTH_SHORT).show();

                break;
            case R.id.buttonNext:
                Log.d("button", "Button Next");
                Toast.makeText(getActivity(),"clicked button next",Toast.LENGTH_SHORT).show();

                break;
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(parent.getId()) {
            case R.id.spinnerMeasure:
                Toast.makeText(getActivity(),"Medida: " + parent.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinnerPeriod:
                Toast.makeText(getActivity(),"Periodo: " + parent.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
