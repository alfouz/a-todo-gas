package com.atodogas.brainycar.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.atodogas.brainycar.DashboardActivity;
import com.atodogas.brainycar.OBD.OBDDTO;
import com.atodogas.brainycar.R;
import com.atodogas.brainycar.Services.Extra.DashboardDTO;
import com.atodogas.brainycar.Services.Extra.DashboardServiceHelper;


public class TrackingService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.atodogas.brainycar.SERVICE";
    public static final String DASHBOARD_DTO = "com.atodogas.brainycar.SEND_DASBHOARD_DTO";

    public static final int SERVICE_ID = 9201;

    private DashboardCalcsThread dashboardCalcsThread;
    private DashboardServiceHelper dashboardServiceHelper;
    private LocalBroadcastManager localBroadcastManager;
    private Intent intent;

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
        dashboardCalcsThread = new DashboardCalcsThread();

        startForeground(SERVICE_ID, notification);

        LocalBroadcastManager.getInstance(this).registerReceiver(OBDDTOReceibe, new IntentFilter(OBDService.OBD_DTO));
        Intent intent2 = new Intent(this, OBDService.class);
        startService(intent2);
        dashboardCalcsThread.start();

        return Service.START_STICKY;
    }



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        dashboardCalcsThread.terminate();
        Intent intent = new Intent(this, OBDService.class);
        stopService(intent);
        localBroadcastManager.unregisterReceiver(OBDDTOReceibe);
    }

    private final BroadcastReceiver OBDDTOReceibe = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(OBDService.OBD_DTO.equals(action)) {
                OBDDTO obdDTO = intent.getParcelableExtra("OBDDTO");
                dashboardServiceHelper.insertOBDDTO(obdDTO);
            }
        }
    };

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
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
