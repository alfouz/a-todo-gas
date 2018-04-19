package com.atodogas.brainycar.Services.Extra;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xurxo on 14/04/18.
 */

public class DashboardDTO implements Parcelable {
    public int speed;
    public int rpm;
    public float battery;
    public float fuelTankLevel;
    public float temperature;
    public float l100kmavg;
    public float km;
    public int hours;
    public int minutes;
    public int seconds;

    public DashboardDTO() {
        this.speed = -1;
        this.rpm = -1;
        this.battery = -1;
        this.fuelTankLevel = -1;
        this.temperature = -1;
        this.l100kmavg = -1;
        this.km = -1;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public DashboardDTO(Parcel in){
        this.speed = in.readInt();
        this.rpm = in.readInt();
        this.battery = in.readFloat();
        this.fuelTankLevel = in.readFloat();
        this.temperature = in.readFloat();
        this.l100kmavg = in.readFloat();
        this.km = in.readFloat();
        this.hours = in.readInt();
        this.minutes = in.readInt();
        this.seconds = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.speed);
        dest.writeInt(this.rpm);
        dest.writeFloat(this.battery);
        dest.writeFloat(this.fuelTankLevel);
        dest.writeFloat(this.temperature);
        dest.writeFloat(this.l100kmavg);
        dest.writeFloat(this.km);
        dest.writeInt(this.hours);
        dest.writeInt(this.minutes);
        dest.writeInt(this.seconds);
    }

    public static final Creator<DashboardDTO> CREATOR = new Creator<DashboardDTO>() {
        @Override
        public DashboardDTO createFromParcel(Parcel in) {
            return new DashboardDTO(in);
        }

        @Override
        public DashboardDTO[] newArray(int size) {
            return new DashboardDTO[size];
        }
    };
}
