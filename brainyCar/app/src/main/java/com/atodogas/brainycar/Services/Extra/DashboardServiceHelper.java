package com.atodogas.brainycar.Services.Extra;

import com.atodogas.brainycar.OBD.OBDDTO;
import com.atodogas.brainycar.Utils.Distances;

import java.util.ArrayList;

public class DashboardServiceHelper {
    private ArrayList<OBDDTO> obddtosCola;
    private OBDDTO lastObdDTO;
    private float mpgTotal = 0;
    private int elemsWithSpeedAndMafDistint0;
    private int secondsTrip;
    private long lastUpdateTimeMilis;
    private double lastLatitude;
    private double lastLongitude;
    private float totalKms;

    public DashboardServiceHelper(){
        obddtosCola = new ArrayList<>();
        mpgTotal = 0;
        elemsWithSpeedAndMafDistint0 = 0;
        secondsTrip = 0;
        lastUpdateTimeMilis = -1;

        lastLatitude = 200;
        lastLongitude = 200;
        totalKms = 0;
    }

    public void setLastUpdateTimeMilis(long lastUpdateTimeMilis) {
        this.lastUpdateTimeMilis = lastUpdateTimeMilis;
    }

    public OBDDTO getLastObdDTO() {
        return lastObdDTO;
    }

    public void insertOBDDTO(OBDDTO obddto){
        obddtosCola.add(obddto);
    }

    public DashboardDTO getLastDashBoardData(double lat, double lon){
        secondsTrip += getAgeSeconds();

        DashboardDTO dashboardDTO = new DashboardDTO();
        lastObdDTO = null;
        if(obddtosCola.size() > 0) {
            OBDDTO obddto = obddtosCola.get(0);
            obddtosCola.remove(0);
            lastObdDTO = obddto;

            float mpg = calculateMpg(obddto);
            if (obddto.speed > 0 && mpg > 0) {
                elemsWithSpeedAndMafDistint0++;
                mpgTotal += mpg;
            }

            if(lastLatitude == 200 && lastLongitude == 200){
                lastLatitude = lat;
                lastLongitude = lon;
            }
            else if(Math.abs(Math.abs(lastLatitude) - Math.abs(lat)) > 0.0001 || Math.abs(Math.abs(lastLongitude) - Math.abs(lastLongitude)) > 0.0001){
                totalKms += Distances.calculateDistance(lastLatitude, lastLongitude, lat, lon);
                lastLatitude = lat;
                lastLongitude = lon;
            }

            dashboardDTO.speed = obddto.speed;
            dashboardDTO.rpm = obddto.rpm;
            dashboardDTO.battery = obddto.moduleVoltage;
            dashboardDTO.fuelTankLevel = obddto.fuelTankLevel;
            dashboardDTO.temperature = obddto.engineCoolantTemp;
        }

        dashboardDTO.km = totalKms;
        dashboardDTO.l100kmavg = calculateL100kmAvg();
        dashboardDTO.hours = (secondsTrip / 3600);
        dashboardDTO.minutes = (secondsTrip - (dashboardDTO.hours*3600))/60;
        dashboardDTO.seconds = secondsTrip - ((dashboardDTO.hours*3600) + (dashboardDTO.minutes*60));

        return dashboardDTO;
    }

    private float calculateMpg(OBDDTO obddto){
        float mpg = 0;
        if(obddto.speed > 0 && obddto.massAirFlow > 0) {
            mpg = (float) (obddto.speed * 4.795324606/obddto.massAirFlow);
        }

        return mpg;
    }

    private float calculateL100kmAvg(){
        if(elemsWithSpeedAndMafDistint0 > 100){
            return (float) (235.2137783/(mpgTotal / elemsWithSpeedAndMafDistint0));
        }

        return -1;
    }

    private int getAgeSeconds(){
        if(lastUpdateTimeMilis == -1){
            lastUpdateTimeMilis = System.currentTimeMillis();
        }

        long nowMillis = System.currentTimeMillis();
        int seconds = (int) (nowMillis - lastUpdateTimeMilis)/1000;
        if(seconds > 0){
            lastUpdateTimeMilis = nowMillis;
        }

        return seconds;
    }

    public double getLastLatitude() {
        return lastLatitude;
    }

    public double getLastLongitude() {
        return lastLongitude;
    }

    public int getSecondsTrip(){
        return secondsTrip;
    }
}
