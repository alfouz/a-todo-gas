package com.atodogas.brainycar.Services.Extra;

import android.util.Log;

import com.atodogas.brainycar.OBD.OBDDTO;

import java.util.ArrayList;

/**
 * Created by xurxo on 14/04/18.
 */

public class DashboardServiceHelper {
    private ArrayList<OBDDTO> obddtosCola;
    private OBDDTO lastObdDTO;
    private float mpgTotal = 0;
    private int elemsWithSpeedAndMafDistint0;
    private int secondsTrip;
    private long lastUpdateTimeMilis;


    public DashboardServiceHelper(){
        obddtosCola = new ArrayList<>();
        mpgTotal = 0;
        elemsWithSpeedAndMafDistint0 = 0;
        secondsTrip = 0;
        lastUpdateTimeMilis = -1;
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

    public DashboardDTO getLastDashBoardData(){
        secondsTrip += getAgeSeconds();

        DashboardDTO dashboardDTO = new DashboardDTO();
        lastObdDTO = null;
        if(obddtosCola.size() > 0) {
            OBDDTO obddto = obddtosCola.get(0);
            obddtosCola.remove(0);

            float mpg = calculateMpg(obddto);
            if (obddto.speed > 0 && mpg > 0) {
                elemsWithSpeedAndMafDistint0++;
                mpgTotal += mpg;
            }

            dashboardDTO.speed = obddto.speed;
            dashboardDTO.rpm = obddto.rpm;
            dashboardDTO.battery = obddto.moduleVoltage;
            dashboardDTO.fuelTankLevel = obddto.fuelTankLevel;
            dashboardDTO.temperature = obddto.engineCoolantTemp;
        }

        dashboardDTO.l100kmavg = calculateL100kmAvg();
        dashboardDTO.hours = (int) Math.floor((secondsTrip / 3600.0));
        dashboardDTO.minutes = (int) Math.floor((secondsTrip/3600.0 - dashboardDTO.hours) * 60.0);
        dashboardDTO.seconds = (int) Math.floor((((secondsTrip/3600.0 - dashboardDTO.hours) * 60.0) - dashboardDTO.minutes) * 60.0);

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
        int seconds = (int) ((nowMillis - lastUpdateTimeMilis) / 1000);
        if(seconds > 0){
            lastUpdateTimeMilis = nowMillis;
        }
        return seconds;
    }
}
