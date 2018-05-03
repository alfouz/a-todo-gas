package com.atodogas.brainycar;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        Resources res = getResources();

        TabHost tabs= root.findViewById(R.id.tabHostProfile);
        tabs.setup();

        //Tab 1
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

        // Nombre de perfil obtenido de la cuenta del usuario
        String name = this.getArguments().getString("userName");
        TextView accountName = root.findViewById(R.id.accountName);
        accountName.setText(name);

        //Tab 2
        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Logros);
        spec.setIndicator("Logros",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        ArrayList<AchievementEntity> achievements = new ArrayList<>();
        for (int i=0; i <= 4; i++) {
            achievements.add(new AchievementEntity("Logro 1", "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit.", true, 1));
        }
        for (int i=0; i <= 3; i++) {
            achievements.add(new AchievementEntity("Logro 2", "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit.", false, 2));
        }
        for (int i=0; i <= 3; i++) {
            achievements.add(new AchievementEntity("Logro 3", "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit.", false, 3));
        }

        ProfileFragmentAchievementAdapter adapter = new ProfileFragmentAchievementAdapter(achievements);
        RecyclerView myView =  root.findViewById(R.id.logrosLayout);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);


        // TODO hay que adaptar el botón de share a cada logro
        /*Button btnCompartir = root.findViewById(R.id.share_button);
        btnCompartir.setOnClickListener(this);*/

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
