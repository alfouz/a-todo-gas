package com.atodogas.brainycar.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.ConnectOBD;
import com.atodogas.brainycar.OBD.OBDAdapter;
import com.atodogas.brainycar.OBD.OBDDTO;
import com.atodogas.brainycar.OBD.OBDDatabase;
import com.atodogas.brainycar.OBD.OBDMock;
import com.atodogas.brainycar.OBD.OBDReal;

import java.io.IOException;

public class OBDService extends Service implements CallbackInterface<OBDAdapter> {
    private OBDAdapter adapter;
    private OBDThread obdThread;
    private OBDDatabase obdDatabase;
    private ConnectOBD connectOBD;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    public static final String OBD_DTO = "com.atodogas.brainycar.OBDService.SEND_OBD_DTO";
    public static final String OBD_NOT_CONNECTED = "com.atodogas.brainycar.OBDService.OBD_NOT_CONNECTED";

    public OBDService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        obdDatabase = new OBDDatabase(getApplication());
        connectOBD = new ConnectOBD(this);
        connectOBD.execute();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        obdThread = new OBDThread();
        intent = new Intent();
        intent.setAction(OBD_DTO);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        obdThread.terminate();
        if(adapter != null){
            adapter.close();
        }
        connectOBD.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void doCallback(OBDAdapter obdAdapter) {
        adapter = obdAdapter;

        // TODO: Borrar/Comentar mockup cuando no se necesite
        /*if(obdAdapter == null){
            adapter = new OBDMock(obdDatabase);
        }*/

        if(adapter != null){
            obdThread.start();
        }
        else {
            Intent intentErrorBT = new Intent();
            intentErrorBT.setAction(OBD_NOT_CONNECTED);
            localBroadcastManager.sendBroadcast(intentErrorBT);
            stopSelf();
        }
    }


    public class OBDThread extends Thread{
        private volatile boolean running = true;

        public void terminate() {
            running = false;
        }

        @Override
        public void run(){
            while(running){
                try {
                    OBDDTO dto = adapter.getRealTimeData();

                    intent.putExtra("OBDDTO", dto);
                    localBroadcastManager.sendBroadcast(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
