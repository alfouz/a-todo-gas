package com.atodogas.brainycar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by belenvb on 22/04/2018.
 */

public class DiagnosticButtonFragment extends Fragment implements View.OnClickListener{

    public DiagnosticButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_diagnostic_button, container, false);

        // Setting buttons for graph
        Button diagnosticButton = root.findViewById(R.id.diagnosticButton);
        diagnosticButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new DiagnosticFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
