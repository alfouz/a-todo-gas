package com.atodogas.brainycar.OBD;

import android.util.Log;


public class OBDDTO {
    public int speed;
    public int rpm;
    public float fuelTankLevel;
    public float fuelRate;
    public float moduleVoltage;
    public float throttlePos;
    public float ambientTemp;
    public float engineCoolantTemp;
    public float airIntakeTemp;
    public float massAirFlow;
    public int intakeManifoldPresure;

    public OBDDTO(){
        this.speed = -1;
        this.rpm = -1;
        this.fuelTankLevel = -1;
        this.fuelRate = -1;
        this.moduleVoltage = -1;
        this.throttlePos = -1;
        this.ambientTemp = -1;
        this.engineCoolantTemp = -1;
        this.airIntakeTemp = -1;
        this.massAirFlow = -1;
        this.intakeManifoldPresure = -1;
    }

    public void toDebug(){
        Log.d("SPEED", Integer.toString(speed));
        Log.d("RPM", Integer.toString(rpm));
        Log.d("NIVEL GASOLINA", Float.toString(fuelTankLevel));
        Log.d("CAUDAL GASOLINA", Float.toString(fuelRate));
        Log.d("VOLTAJE", Float.toString(moduleVoltage));
        Log.d("ACELERADOR %", Float.toString(throttlePos));
        Log.d("TEMP AMBIENTE", Float.toString(ambientTemp));
        Log.d("TEMP MOTOR", Float.toString(engineCoolantTemp));
        Log.d("TEMP AIRE ENTRADA", Float.toString(airIntakeTemp));
        Log.d("MASA FLUJO AIRE", Float.toString(massAirFlow));
        Log.d("MAP", Integer.toString(intakeManifoldPresure));
    }
}
