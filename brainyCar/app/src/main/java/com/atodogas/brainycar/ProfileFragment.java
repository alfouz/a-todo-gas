package com.atodogas.brainycar;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

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

        TabHost tabs= root.findViewById(R.id.my_host);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.Resumen);
        spec.setIndicator("Resumen",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Logros);
        spec.setIndicator("Logros",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        // Inflate the layout for this fragment
        return root;
    }

}
