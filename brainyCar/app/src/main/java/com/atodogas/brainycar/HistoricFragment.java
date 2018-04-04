package com.atodogas.brainycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricFragment extends Fragment {

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


        // Inflate the layout for this fragment
        return root;
    }

}
