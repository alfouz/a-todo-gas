package com.atodogas.brainycar.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "TripData", foreignKeys = @ForeignKey(  entity = TripEntity.class,
                                    parentColumns = "id",
                                    childColumns = "idTrip",
                                    onUpdate = ForeignKey.CASCADE,
                                    onDelete = ForeignKey.CASCADE))
public class TripDataEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idTrip;

    @NonNull
    private int Speed;

    @NonNull
    private int RPM;

    @NonNull
    private float FuelTankLevel;

    @NonNull
    private float EngineTemp;

    @NonNull
    private float MAF;

    @NonNull
    private float Latitude;

    @NonNull
    private float Longitude;

    @NonNull
    private float Battery;

    @NonNull
    private long Time;


    public void setId(int id) {
        this.id = id;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public void setSpeed(int speed) {
        Speed = speed;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }

    public void setFuelTankLevel(float fuelTankLevel) {
        FuelTankLevel = fuelTankLevel;
    }

    public void setEngineTemp(float engineTemp) {
        EngineTemp = engineTemp;
    }

    public void setMAF(float MAF) {
        this.MAF = MAF;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public void setBattery(float battery) {
        Battery = battery;
    }

    public void setTime(long time) {
        Time = time;
    }

    public int getId() {
        return id;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public int getSpeed() {
        return Speed;
    }

    public int getRPM() {
        return RPM;
    }

    public float getFuelTankLevel() {
        return FuelTankLevel;
    }

    public float getEngineTemp() {
        return EngineTemp;
    }

    public float getMAF() {
        return MAF;
    }

    public float getLatitude() {
        return Latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public float getBattery() {
        return Battery;
    }

    public long getTime() {
        return Time;
    }
}
