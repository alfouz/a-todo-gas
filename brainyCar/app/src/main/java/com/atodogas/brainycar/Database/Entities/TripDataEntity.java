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
    private double Latitude;

    @NonNull
    private double Longitude;

    @NonNull
    private float Battery;

    private Float AcelX;

    private Float AcelY;

    private Float AcelZ;

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

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setBattery(float battery) {
        Battery = battery;
    }

    public void setAcelX(Float acelX) {
        this.AcelX = acelX;
    }

    public void setAcelY(Float acelY) {
        this.AcelY = acelY;
    }

    public void setAcelZ(Float acelZ) {
        this.AcelZ = acelZ;
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

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public float getBattery() {
        return Battery;
    }

    public Float getAcelX() {
        return AcelX;
    }

    public Float getAcelY() {
        return AcelY;
    }

    public Float getAcelZ() {
        return AcelZ;
    }

    public long getTime() {
        return Time;
    }
}
