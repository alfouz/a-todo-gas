package com.atodogas.brainycar.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.atodogas.brainycar.AsyncTasks.CallbackInterface;
import com.atodogas.brainycar.AsyncTasks.CreateTripBD;
import com.atodogas.brainycar.DashboardActivity;
import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.OBD.OBDDTO;
import com.atodogas.brainycar.R;
import com.atodogas.brainycar.Services.Extra.DashboardDTO;
import com.atodogas.brainycar.Services.Extra.DashboardServiceHelper;
import com.atodogas.brainycar.TripEntity;

import java.util.List;


public class TrackingService extends Service implements SensorEventListener, CallbackInterface<com.atodogas.brainycar.Database.Entities.TripEntity> {

    public static final String TAG = "TrackingService";
    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.atodogas.brainycar.TrackingService.SERVICE";
    public static final String DASHBOARD_DTO = "com.atodogas.brainycar.TrackingService.SEND_DASBHOARD_DTO";
    public static final String OBD_NOT_CONNECTED = "com.atodogas.brainycar.TrackingService.OBD_NOT_CONNECTED";

    public static final int SERVICE_ID = 9201;

    private DashboardCalcsThread dashboardCalcsThread;
    private DashboardServiceHelper dashboardServiceHelper;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;
    private AppDatabase db;
    private com.atodogas.brainycar.Database.Entities.TripEntity trip;

    //Gestión sensores
    private SensorManager sm;
    private Sensor sAcc;
    private Sensor sGir;
    private Sensor sMag;
    private Sensor sBar;
    private float [] vAcc;
    private float [] vGir;
    private float [] vMag;
    private float [] vBar;

    public void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    @Override
    public void onCreate() {
        initChannel();
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        intent = new Intent();
        intent.setAction(DASHBOARD_DTO);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.trackingNotificationTitle))
                .setContentText(getString(R.string.trackingNotificationSubtitle))
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        dashboardServiceHelper = new DashboardServiceHelper();

        startForeground(SERVICE_ID, notification);

        localBroadcastManager.registerReceiver(OBDDTOReceibe, new IntentFilter(OBDService.OBD_DTO));
        localBroadcastManager.registerReceiver(OBDDTOReceibe, new IntentFilter(OBDService.OBD_NOT_CONNECTED));
        Intent intent2 = new Intent(this, OBDService.class);
        startService(intent2);

        //Inicialización de sensores
        sm = (SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sAcc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sGir = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sMag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sBar = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sm.registerListener(this, sAcc, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sGir, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sMag, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sBar, SensorManager.SENSOR_DELAY_NORMAL);

        int idUser = intent.getIntExtra("idUser", -1);
        new CreateTripBD(this, getApplicationContext()).execute(idUser);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "brainyCar").build();

        return Service.START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sAcc!=null && event.sensor.getName().equals(sAcc.getName())){
            vAcc = event.values;
        }
        if(sGir!=null && event.sensor.getName().equals(sGir.getName())){
            vGir = event.values;
        }
        if(sMag!=null && event.sensor.getName().equals(sMag.getName())){
            vMag = event.values;
        }
        if(sBar!=null && event.sensor.getName().equals(sBar.getName())){
            vBar = event.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float [] getValorGiroscopio () {
        return vGir;
    }

    public float [] getValorAcelerometro () {
        return vAcc;
    }

    public float [] getValorMagnetometro () {
        return vMag;
    }

    public float [] getValorBarometro () {
        return vBar;
    }



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        if(dashboardCalcsThread != null){
            dashboardCalcsThread.terminate();
        }

        Intent intent = new Intent(this, OBDService.class);
        stopService(intent);
        localBroadcastManager.unregisterReceiver(OBDDTOReceibe);
        //Unregister de los sensores
        sm.unregisterListener(this);
    }

    private final BroadcastReceiver OBDDTOReceibe = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(OBDService.OBD_DTO.equals(action)) {
                OBDDTO obdDTO = intent.getParcelableExtra("OBDDTO");
                dashboardServiceHelper.insertOBDDTO(obdDTO);

                if(dashboardCalcsThread == null){
                    dashboardCalcsThread = new DashboardCalcsThread();
                    dashboardCalcsThread.start();
                }
            }
            else if(OBDService.OBD_NOT_CONNECTED.equals(action)) {
                intent = new Intent();
                intent.setAction(TrackingService.OBD_NOT_CONNECTED);
                localBroadcastManager.sendBroadcast(intent);
                stopSelf();
            }
        }
    };

    @Override
    public void doCallback(com.atodogas.brainycar.Database.Entities.TripEntity trip) {
        this.trip = trip;

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(OBDDTOReceibe, new IntentFilter(OBDService.OBD_DTO));
        Intent intent2 = new Intent(getApplicationContext(), OBDService.class);
        startService(intent2);
    }

    public class DashboardCalcsThread  extends Thread{
        public volatile boolean running = true;

        public void terminate(){
            running = false;
        }

        @Override
        public void run() {
            while(running){
                DashboardDTO dashboardDTO = dashboardServiceHelper.getLastDashBoardData();

                if(dashboardDTO != null){
                    intent.putExtra("DashboardDTO", dashboardDTO);

                    localBroadcastManager.sendBroadcast(intent);

                    if(dashboardDTO.speed != -1){
                        OBDDTO obddto = dashboardServiceHelper.getLastObdDTO();
                        TripDataEntity tripData = new TripDataEntity();
                        tripData.setIdTrip(trip.getId());
                        tripData.setBattery(obddto.moduleVoltage);
                        tripData.setEngineTemp(obddto.engineCoolantTemp);
                        tripData.setFuelTankLevel(obddto.fuelTankLevel);
                        tripData.setSpeed(obddto.speed);
                        tripData.setRPM(obddto.rpm);
                        tripData.setLatitude(0);
                        tripData.setLongitude(0);
                        tripData.setMAF(obddto.massAirFlow);
                        tripData.setTime(System.currentTimeMillis());

                        if (getValorAcelerometro() != null){
                            tripData.setAcelX(getValorAcelerometro()[0]);
                            tripData.setAcelY(getValorAcelerometro()[1]);
                            tripData.setAcelZ(getValorAcelerometro()[2]);
                        }

                        db.tripDataDao().insertTripData(tripData);
                    }
                }

               /* ///TODO Eliminar logs de prueba de obtención de valores de sensores
                if (getValorAcelerometro() != null)
                    Log.i(TAG, "Acelerómetro (Valor x):" + getValorAcelerometro()[0]);
                else
                    Log.i(TAG, "No se encuentra valor para el acelerómetro");
                if (getValorGiroscopio() != null)
                    Log.i(TAG, "Giroscopio (Valor x):" + getValorGiroscopio()[0]);
                else
                    Log.i(TAG, "No se encuentra valor para el giroscopio");
                if (getValorMagnetometro() != null)
                    Log.i(TAG, "Magnetómetro (Valor x):" + getValorMagnetometro()[0]);
                else
                    Log.i(TAG, "No se encuentra valor para el magnetómetro");
                if (getValorBarometro() != null)
                    Log.i(TAG, "Barómetro (Valor x):" + getValorBarometro()[0]);
                else
                    Log.i(TAG, "No se encuentra valor para el barómetro");*/

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
