package com.atodogas.brainycar.OBD;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class OBDDTO implements Parcelable {
    public int speed;
    public int rpm;
    public float fuelTankLevel;
    public float moduleVoltage;
    public float engineCoolantTemp;
    public float massAirFlow;

    public OBDDTO(){
        this.speed = -1;
        this.rpm = -1;
        this.fuelTankLevel = -1;
        this.moduleVoltage = -1;
        this.engineCoolantTemp = -1;
        this.massAirFlow = -1;
    }

    public OBDDTO(Parcel in) {
        this.speed = in.readInt();
        this.rpm = in.readInt();
        this.fuelTankLevel = in.readFloat();
        this.moduleVoltage = in.readFloat();
        this.engineCoolantTemp = in.readFloat();
        this.massAirFlow = in.readFloat();
    }

    public void toDebug(){
        Log.d("SPEED", Integer.toString(speed));
        Log.d("RPM", Integer.toString(rpm));
        Log.d("NIVEL GASOLINA", Float.toString(fuelTankLevel));
        Log.d("VOLTAJE", Float.toString(moduleVoltage));
        Log.d("TEMP MOTOR", Float.toString(engineCoolantTemp));
        Log.d("MASA FLUJO AIRE", Float.toString(massAirFlow));
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
        dest.writeFloat(this.moduleVoltage);
        dest.writeFloat(this.engineCoolantTemp);
        dest.writeFloat(this.massAirFlow);
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
