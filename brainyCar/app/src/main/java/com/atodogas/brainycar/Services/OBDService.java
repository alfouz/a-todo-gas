package com.atodogas.brainycar.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.atodogas.brainycar.OBD.OBDAdapter;
import com.atodogas.brainycar.OBD.OBDDTO;
import com.atodogas.brainycar.OBD.OBDDatabase;
import com.atodogas.brainycar.OBD.OBDMock;
import com.atodogas.brainycar.OBD.OBDReal;

import java.io.IOException;

public class OBDService extends Service {
    private OBDAdapter adapter;
    private OBDThread obdThread;
    private OBDDatabase obdDatabase;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    public static final String OBD_DTO = "com.atodogas.brainycar.SEND_OBD_DTO";

    public OBDService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();



        //try {
            obdDatabase = new OBDDatabase(getApplication());
            //adapter = new OBDReal(null, null);
            adapter = new OBDMock(obdDatabase);
            localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            intent = new Intent();
            intent.setAction(OBD_DTO);
            obdThread = new OBDThread();
        /*} catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        obdThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        obdThread.terminate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

                    if(adapter instanceof OBDReal) {
                        obdDatabase.insertRow(dto);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
