package com.atodogas.brainycar.DTOs;

import android.support.annotation.NonNull;

import java.util.Date;

public class TripDataDTO {
    private int id;
    private int speed;
    private int rpm;
    private float fuelTankLevel;
    private float engineTemp;
    private float mgp;
    private double latitude;
    private double longitude;
    private float battery;
    private float acelX;
    private float acelY;
    private float acelZ;
    private Date date;

    public void setId(int id) {
        this.id = id;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setRPM(int RPM) {
        this.rpm = RPM;
    }

    public void setFuelTankLevel(float fuelTankLevel) {
        this.fuelTankLevel = fuelTankLevel;
    }

    public void setEngineTemp(float engineTemp) {
        this.engineTemp = engineTemp;
    }

    public void setMgp(float mgp) {
        this.mgp = mgp;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public void setAcelX(float acelX) {
        this.acelX = acelX;
    }

    public void setAcelY(float acelY) {
        this.acelY = acelY;
    }

    public void setAcelZ(float acelZ) {
        this.acelZ = acelZ;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getRPM() {
        return this.rpm;
    }

    public float getFuelTankLevel() {
        return this.fuelTankLevel;
    }

    public float getEngineTemp() {
        return this.engineTemp;
    }

    public float getMgp() {
        return mgp;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public float getBattery() {
        return this.battery;
    }

    public float getAcelX() {
        return this.acelX;
    }

    public float getAcelY() {
        return this.acelY;
    }

    public float getAcelZ() {
        return this.acelZ;
    }

    public Date getDate() {
        return date;
    }
}
