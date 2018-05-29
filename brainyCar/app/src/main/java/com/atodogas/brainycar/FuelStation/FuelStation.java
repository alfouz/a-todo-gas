package com.atodogas.brainycar.FuelStation;

public class FuelStation {
    private String title;
    private String time;
    private double latitude;
    private double longitude;
    private float dieselA;
    private float gasoline95;
    private float gasoline98;

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getDieselA() {
        return dieselA;
    }

    public float getGasoline95() {
        return gasoline95;
    }

    public float getGasoline98() {
        return gasoline98;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDieselA(float dieselA) {
        this.dieselA = dieselA;
    }

    public void setGasoline95(float gasoline95) {
        this.gasoline95 = gasoline95;
    }

    public void setGasoline98(float gasoline98) {
        this.gasoline98 = gasoline98;
    }
}
