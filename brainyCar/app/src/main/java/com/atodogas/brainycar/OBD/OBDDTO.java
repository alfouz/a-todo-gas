package com.atodogas.brainycar.OBD;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class OBDDTO implements Parcelable {
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
    public float l100;
    public float l100avg;

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
        this.l100 = -1;
        this.l100avg = -1;
    }

    public OBDDTO(Parcel in) {
        this.speed = in.readInt();
        this.rpm = in.readInt();
        this.fuelTankLevel = in.readFloat();
        this.fuelRate = in.readFloat();
        this.moduleVoltage = in.readFloat();
        this.throttlePos = in.readFloat();
        this.ambientTemp = in.readFloat();
        this.engineCoolantTemp = in.readFloat();
        this.airIntakeTemp = in.readFloat();
        this.massAirFlow = in.readFloat();
        this.intakeManifoldPresure = in.readInt();
        this.l100 = in.readFloat();
        this.l100avg = in.readFloat();
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
        Log.d("L100km", Float.toString(l100));
        Log.d("AVG L100km", Float.toString(l100avg));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.speed);
        dest.writeInt(this.rpm);
        dest.writeFloat(this.fuelTankLevel);
        dest.writeFloat(this.fuelRate);
        dest.writeFloat(this.moduleVoltage);
        dest.writeFloat(this.throttlePos);
        dest.writeFloat(this.ambientTemp);
        dest.writeFloat(this.engineCoolantTemp);
        dest.writeFloat(this.airIntakeTemp);
        dest.writeFloat(this.massAirFlow);
        dest.writeInt(this.intakeManifoldPresure);
        dest.writeFloat(this.l100);
        dest.writeFloat(this.l100avg);
    }

    public static final Parcelable.Creator<OBDDTO> CREATOR
            = new Parcelable.Creator<OBDDTO>() {
        public OBDDTO createFromParcel(Parcel in) {
            return new OBDDTO(in);
        }

        public OBDDTO[] newArray(int size) {
            return new OBDDTO[size];
        }
    };
}
