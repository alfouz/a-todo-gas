package com.atodogas.brainycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosticFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DiagnosticFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diagnostic, container, false);

        Bundle arguments  = getArguments();
        String time = arguments.getString("time");
        ArrayList<BugEntity> bugEntities = arguments.getParcelableArrayList("bugEntities");

        // Setting general info
        TextView timeText = view.findViewById(R.id.timeText);
        timeText.setText(time);
        TextView bugText = view.findViewById(R.id.bugText);
        bugText.setText(Integer.toString(bugEntities.size()));

        DiagnosticFragmentBugAdapter adapter = new DiagnosticFragmentBugAdapter(bugEntities);
        RecyclerView myView =  view.findViewById(R.id.bugsList);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        // Setting button restart
        Button reiniciarButton = view.findViewById(R.id.reiniciarButton);
        reiniciarButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.reiniciarButton:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                reiniciarButton();
                break;
        }
    }

    public void reiniciarButton(){
        Log.d(TAG, "Click on Button Reiniciar");
        Fragment fragment = new DiagnosticButtonFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
