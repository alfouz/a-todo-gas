package com.atodogas.brainycar.Services.Extra;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by ADRIAN on 23/04/2018.
 */

public class SensorHelper implements SensorEventListener{

        private Context mContext;
        private SensorManager sm;
        private Sensor sAcc;
        private Sensor sGir;
        private Sensor sMag;

        private float [] vAcc;
        private float [] vGir;
        private float [] vMag;

        public SensorHelper(Context mcontext) {
            this.mContext = mContext;

            sm = (SensorManager)mContext.getSystemService(mContext.SENSOR_SERVICE);
            sAcc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sGir = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sMag = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }

        public void registerListeners(){
            sm.registerListener(this, sAcc, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(this, sGir, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(this, sMag, SensorManager.SENSOR_DELAY_NORMAL);
        }

        public void unregisterListeners() {
            sm.unregisterListener(this);
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


}
