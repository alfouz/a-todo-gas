package com.atodogas.brainycar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


        // Setting general info
        TextView timeText = view.findViewById(R.id.timeText);
        timeText.setText("2:33 minutos");
        TextView bugText = view.findViewById(R.id.bugText);
        bugText.setText("5");

        // Setting errors list
        ArrayList<BugEntity> bugs = new ArrayList<>();
        for (int i=0; i <= 10; i++) {
            bugs.add(new BugEntity("P0123", "Lorem ipsum dolor sit amet, consectetur " +
                    "adipiscing elit. Etiam consectetur malesuada iaculis. Proin dictum mattis lorem. " +
                    "Quisque vel facilisis nisi, id interdum nisl. Integer imperdiet lorem augue, " +
                    "sit amet dapibus tortor consectetur ac. Cras vel leo at enim auctor laoreet non" +
                    " in metus. In hac habitasse platea dictumst."));
        }

        DiagnosticFragmentBugAdapter adapter = new DiagnosticFragmentBugAdapter(bugs);
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
                reiniciarButton();
                break;
        }
    }

    public void reiniciarButton(){
        Log.d(TAG, "Click on Button Reiniciar");
        Toast.makeText(getActivity(), "Click on Button Reiniciar", Toast.LENGTH_SHORT).show();
    }
}
