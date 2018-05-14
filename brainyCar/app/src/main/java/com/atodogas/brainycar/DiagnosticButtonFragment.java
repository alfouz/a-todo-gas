package com.atodogas.brainycar;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.ConnectOBD;
import com.atodogas.brainycar.OBD.OBDAdapter;
import com.atodogas.brainycar.OBD.OBDDatabase;
import com.atodogas.brainycar.OBD.OBDMock;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by belenvb on 22/04/2018.
 */

public class DiagnosticButtonFragment extends Fragment implements View.OnClickListener, CallbackInterface<OBDAdapter>{

    private LinearLayout loadingLayout;
    private TextView loadingTextView;
    private long startMilis;
    private ConnectOBD connectOBD;

    public DiagnosticButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_diagnostic_button, container, false);

        loadingLayout = root.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.INVISIBLE);
        loadingTextView = root.findViewById(R.id.loadingTextView);

        connectOBD = new ConnectOBD(this);

        // Setting buttons for graph
        Button diagnosticButton = root.findViewById(R.id.diagnosticButton);
        diagnosticButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        loadingLayout.setVisibility(View.VISIBLE);
        loadingTextView.setText(getString(R.string.loading_obteniendo_codigos_error));
        startMilis = System.currentTimeMillis();
        connectOBD.execute();
    }

    @Override
    public void doCallback(OBDAdapter obdAdapter) {
        // TODO: Para hacer pruebas con datos mockeados
        if(obdAdapter == null){
            obdAdapter = new OBDMock(null);
        }

        try {
            if(obdAdapter != null){
                ArrayList<String> troubleCodes = obdAdapter.getTroubleCodes();
                ArrayList<BugEntity> bugEntities = new ArrayList<>();

                for(String troubleCode : troubleCodes){
                    String description = "";

                    int id = getContext().getResources().getIdentifier("FIRST_CHAR_" + troubleCode.charAt(0),"string", getContext().getPackageName());
                    description += troubleCode.charAt(0) + ": " + getString(id) + "\n";

                    id = getContext().getResources().getIdentifier("SECOND_CHAR_" + troubleCode.charAt(1),"string", getContext().getPackageName());
                    description += troubleCode.charAt(1) + ": " + getString(id) + "\n";

                    id = getContext().getResources().getIdentifier("THIRD_CHAR_" + troubleCode.charAt(2),"string", getContext().getPackageName());
                    description += troubleCode.charAt(2) + ": " + getString(id) + "\n";

                    bugEntities.add(new BugEntity(troubleCode, description));
                }

                int seconds = (int) (System.currentTimeMillis() - startMilis)/1000;
                int minutes = seconds/60;
                seconds = seconds - minutes*60;

                String time = minutes + ":" + seconds;
                if(seconds < 10){
                    time = minutes + ":0" + seconds;
                }

                obdAdapter.close();
                connectOBD.close();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("bugEntities", bugEntities);
                bundle.putString("time", time);

                Fragment fragment = new DiagnosticFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

                loadingLayout.setVisibility(View.INVISIBLE);
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_conectar_obd), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_obtener_datos_obd), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.error_obtener_datos_obd), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
