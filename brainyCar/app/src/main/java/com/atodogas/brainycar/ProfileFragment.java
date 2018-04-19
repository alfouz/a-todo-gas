package com.atodogas.brainycar;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    private View root;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //change action bar title
        MainActivity activity = (MainActivity) getActivity();
        activity.changeActionBarTitle("Perfil");


        root = inflater.inflate(R.layout.fragment_profile, container, false);

        Resources res = getResources();

        TabHost tabs= root.findViewById(R.id.tabHostProfile);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.Resumen);
        spec.setIndicator("Resumen",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        TextView numberPointsText = root.findViewById(R.id.numberPointsText);
        numberPointsText.setText("725 puntos");
        TextView numberRankingText = root.findViewById(R.id.numberRankingText);
        numberRankingText.setText("12 th");
        TextView numberAchievementsText = root.findViewById(R.id.numberAchievementsText);
        numberAchievementsText.setText("12 de 30");

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Logros);
        spec.setIndicator("Logros",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        // Botón de share

        Button btnCompartir = root.findViewById(R.id.share_button);
        btnCompartir.setOnClickListener(this);

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        startActivity(sharingIntent);

    }
}
