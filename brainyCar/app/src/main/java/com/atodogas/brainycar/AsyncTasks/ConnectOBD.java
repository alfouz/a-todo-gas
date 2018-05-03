package com.atodogas.brainycar.AsyncTasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

import com.atodogas.brainycar.OBD.OBDAdapter;
import com.atodogas.brainycar.OBD.OBDReal;

import java.io.IOException;
import java.util.UUID;

public class ConnectOBD extends AsyncTask<Void, Void, OBDAdapter> {
    private CallbackInterface callback;
    private BluetoothSocket socket;

    public ConnectOBD(CallbackInterface callback) {
        this.callback = callback;
    }

    @Override
    protected OBDAdapter doInBackground(Void... voids) {
        OBDAdapter adapter = null;
        socket = null;

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter.isEnabled()){
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            for (BluetoothDevice device : btAdapter.getBondedDevices()){
                socket = null;
                try {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                    adapter = new OBDReal(socket.getInputStream(), socket.getOutputStream());

                    if(adapter.isConnected()){
                        break;
                    }

                    socket.close();
                    adapter = null;
                } catch (IOException e) {
                    socket = null;
                    adapter = null;
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    socket = null;
                    adapter = null;
                    e.printStackTrace();
                }
            }
        }


        return adapter;
    }

    @Override
    protected void onPostExecute(OBDAdapter obdAdapter) {
        super.onPostExecute(obdAdapter);
        callback.doCallback(obdAdapter);
    }

    public void close(){
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter.isEnabled() && socket != null){
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
