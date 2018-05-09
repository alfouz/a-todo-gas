package com.atodogas.brainycar.DTOs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripDTO {
    private int id;
    private Date startDate;
    private Date endDate;
    private String startPlace;
    private String endPlace;
    private float kms;
    private int hours;
    private int minutes;
    private float fuelConsumptionAVG;
    private float speedAVG;
    private List<TripDataDTO> tripData;

    public TripDTO() {
        tripData = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public void setKms(float kms) {
        this.kms = kms;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setFuelConsumptionAVG(float fuelConsumptionAVG) {
        this.fuelConsumptionAVG = fuelConsumptionAVG;
    }

    public void setSpeedAVG(float speedAVG) {
        this.speedAVG = speedAVG;
    }

    public void setTripData(List<TripDataDTO> tripData) {
        this.tripData = tripData;
    }

    public int getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public float getKms() {
        return kms;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public float getFuelConsumptionAVG() {
        return fuelConsumptionAVG;
    }

    public float getSpeedAVG() {
        return speedAVG;
    }

    public List<TripDataDTO> getTripData() {
        return tripData;
    }
}
