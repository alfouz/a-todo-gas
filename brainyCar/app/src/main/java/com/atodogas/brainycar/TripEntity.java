package com.atodogas.brainycar;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by belenvb on 18/04/2018.
 */

public class TripEntity {
    private Date day;
    private Time startTime;
    private Time endTime;
    private String startPlace;
    private String endPlace;
    private Integer km;
    private Integer gasoline;
    private Integer points;

    public TripEntity(Date day, Time startTime, Time endTime, String startPlace, String endPlace, Integer km, Integer gasoline, Integer points) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.km = km;
        this.gasoline = gasoline;
        this.points = points;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getGasoline() {
        return gasoline;
    }

    public void setGasoline(Integer gasoline) {
        this.gasoline = gasoline;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
