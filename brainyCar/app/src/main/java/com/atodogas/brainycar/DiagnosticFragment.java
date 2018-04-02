package com.atodogas.brainycar;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiagnosticFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DiagnosticFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diagnostic, container, false);

        ListView erroresListView = view.findViewById(R.id.erroresListView);
        String[] errores = { "P0123", "B0523", "P1234", "C0983", "B0983", "P1983", "A1231"};
        erroresListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, errores));
        erroresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);
                ;
                CharSequence text = "clicked on " + selectedItem;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
                Log.d(TAG, text.toString());
            }
        });

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
