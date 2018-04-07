package com.atodogas.brainycar;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TabHost;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricFragment extends Fragment implements View.OnClickListener{

    private View root;

    public HistoricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //change action bar title
        MainActivity activity = (MainActivity) getActivity();
        activity.changeActionBarTitle("Histórico");


        root = inflater.inflate(R.layout.fragment_historic, container, false);

        TabHost host = (TabHost) root.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Viajes");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Viajes");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Estadisticas");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Estadisticas");
        host.addTab(spec);

        Button btnLineas = root.findViewById(R.id.btnLineas);
        Button btnBarras = root.findViewById(R.id.btnBarras);
        btnLineas.setOnClickListener(this);
        btnBarras.setOnClickListener(this);


        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        XYPlot plot;
        switch (v.getId()){
            case R.id.btnLineas:
                Log.d("button", "Button Lineas");
                plot = root.findViewById(R.id.plot);
                plot.clear();
                plot.setVisibility(View.VISIBLE);

                Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};
                Number[] series2Numbers = {5, 2, 10, 5, 20, 10, 40, 20, 80, 40};
                XYSeries series1 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1", series1Numbers);
                XYSeries series2 = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series2", series2Numbers);

                plot.addSeries(series1, new LineAndPointFormatter(Color.GREEN, Color.GREEN, null, null));
                plot.addSeries(series2, new LineAndPointFormatter(Color.BLUE, Color.BLUE, null, null));
                plot.redraw();
                break;
            case R.id.btnBarras:
                Log.d("button", "Button Barras");
                plot = root.findViewById(R.id.plot);
                plot.clear();
                plot.setVisibility(View.VISIBLE);
                XYSeries wins = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "wins", 3, 4, 5, 3, 2, 3, 5, 6, 2, 1, 3, 1);
                XYSeries losses = new SimpleXYSeries(SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "losses", 0, 1, 1, 0, 1, 0, 0, 0, 2, 1, 0, 1);

                BarFormatter bf = new BarFormatter(Color.GREEN, Color.WHITE);
                bf = new BarFormatter(Color.RED, Color.WHITE);
                plot.addSeries(wins, bf);
                plot.addSeries(losses, bf);
                BarRenderer barRenderer = plot.getRenderer(BarRenderer.class);
                barRenderer.setBarOrientation(BarRenderer.BarOrientation.SIDE_BY_SIDE);
                plot.redraw();
                break;
        }
    }
}
