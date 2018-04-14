package com.atodogas.brainycar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DashboardFragment.class.getSimpleName();
    private View root;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //change action bar title
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.changeActionBarTitle("Dashboard");

        Button pausarButton = root.findViewById(R.id.pausarButton);
        pausarButton.setOnClickListener(this);
        View detenerButton = root.findViewById(R.id.detenerButton);
        detenerButton.setOnClickListener(this);

        //Actualizar pantalla
        updateDashboardInformation(root);


        Intent pepe = new Intent(getContext(),TrackingService.class);
        pepe.setAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        getActivity().startService(pepe);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.pausarButton:
                pausar();
                break;
            case R.id.detenerButton:
                detener();
                break;
        }
    }

    public void pausar() {
        Log.d(TAG, "clicked button Pausar");
        Toast.makeText(getActivity(),"clicked button Pausar",Toast.LENGTH_SHORT).show();

    }

    public void detener() {
        Log.d(TAG, "clicked button Detener");
        Toast.makeText(getActivity(),"clicked button Detener",Toast.LENGTH_SHORT).show();

        Intent pepe = new Intent(getContext(),TrackingService.class);
        getActivity().stopService(pepe);
    }

    ///TODO Obtener los valores a través de bluetooth
    //Metodo para actualizar los valores de la pantalla
    private void updateDashboardInformation(View root) {
        int temperaturaValue = 120;
        int bateriaValue = 14;
        int gasolinaValue = 23;
        int kmRecorridosValue = 175;
        int tiempoTranscurridoHorasValue = 2;
        int tiempoTranscurridoMinutosValue = 15;
        int tiempoTranscurridoSegundosValue = 12;
        int revolucionesValue = 2500;
        int velocidadValue = 80;

        final TextView temperaturaTextView = (TextView) root.findViewById(R.id.temperaturaTextView);
        temperaturaTextView.setText(temperaturaValue + " ºC");

        final TextView bateriaTextView = (TextView) root.findViewById(R.id.bateriaTextView);
        bateriaTextView.setText(bateriaValue + " V");

        final TextView gasolinaTextView = (TextView) root.findViewById(R.id.gasolinaTextView);
        gasolinaTextView.setText(gasolinaValue + " L");

        final TextView kmRecorridosTextView = (TextView) root.findViewById(R.id.kmRecorridosTextView);
        kmRecorridosTextView.setText(kmRecorridosValue + " km");

        final TextView tiempoTranscurridoTextView = (TextView) root.findViewById(R.id.tiempoTranscurridoTextView);
        tiempoTranscurridoTextView.setText(tiempoTranscurridoHorasValue + " h" + tiempoTranscurridoMinutosValue + " m"
            + tiempoTranscurridoSegundosValue + " s");

        final TextView revolucionesTextView = (TextView) root.findViewById(R.id.revolucionesTextView);
        revolucionesTextView.setText("" + revolucionesValue);

        final TextView velocidadTextView = (TextView) root.findViewById(R.id.velocidadTextView);
        velocidadTextView.setText("" + velocidadValue);
    }

}
