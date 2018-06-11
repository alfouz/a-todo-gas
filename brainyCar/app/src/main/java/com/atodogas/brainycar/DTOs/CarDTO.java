package com.atodogas.brainycar.DTOs;

import android.support.annotation.NonNull;

public class CarDTO {
    private int id;
    private String model;
    private int fuelType;
    private float kms;
    private float fuelConsumptionAVG;
    private float speedAVG;
    private float fuelTankLevel;
    private float battery;
    private double latitude;
    private double longitude;

    public void setId(int id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setFuelType(int fuelType) {
        this.fuelType = fuelType;
    }

    public void setKms(float kms) {
        this.kms = kms;
    }

    public void setFuelConsumptionAVG(float fuelConsumptionAVG) {
        this.fuelConsumptionAVG = fuelConsumptionAVG;
    }

    public void setSpeedAVG(float speedAVG) {
        this.speedAVG = speedAVG;
    }

    public void setFuelTankLevel(float fuelTankLevel) {
        this.fuelTankLevel = fuelTankLevel;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public int getFuelType() {
        return fuelType;
    }

    public float getKms() {
        return kms;
    }

    public float getFuelConsumptionAVG() {
        return fuelConsumptionAVG;
    }

    public float getSpeedAVG() {
        return speedAVG;
    }

    public float getFuelTankLevel() {
        return fuelTankLevel;
    }

    public float getBattery() {
        return battery;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
