package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.DTOs.TripDTO;
import com.atodogas.brainycar.DTOs.TripDataDTO;
import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;
import com.atodogas.brainycar.Utils.Distances;

import java.util.Date;
import java.util.List;

public class GetTripWithDataBD extends AsyncTask<Integer, Void, TripDTO> {
    CallbackInterface callback;
    AppDatabase db;

    public GetTripWithDataBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "brainyCar").build();
    }

    @Override
    protected TripDTO doInBackground(Integer... integers) {
        int idTrip = integers[0];

        TripEntity tripEntity = db.tripDao().getTrip(idTrip);
        List<TripDataEntity> tripDataEntities = db.tripDataDao().getAllTripData(tripEntity.getId());

        TripDTO tripDTO = new TripDTO();
        tripDTO.setId(tripEntity.getId());
        tripDTO.setStartPlace(tripEntity.getStartPlace());
        tripDTO.setEndPlace(tripEntity.getEndPlace());
        tripDTO.setHours(tripEntity.getDuration()/3600);
        tripDTO.setMinutes((tripEntity.getDuration() - (tripDTO.getHours() * 3600)) /60);

        if(tripDataEntities.size() > 0){
            tripDTO.setStartDate(new Date(tripDataEntities.get(0).getTime()));
            tripDTO.setEndDate(new Date(tripDataEntities.get(tripDataEntities.size() - 1).getTime()));
        }

        double lastLatitude = 255;
        double lastLongitude = 255;
        float kms = 0;
        float mpg = 0;
        int numberMPGNotEmpty = 0;
        float speedAVG = 0;
        int speedNot0 = 0;
        for(TripDataEntity tData : tripDataEntities){
            TripDataDTO tripDataDTO = new TripDataDTO();
            tripDataDTO.setId(tData.getId());
            tripDataDTO.setLatitude(tData.getLatitude());
            tripDataDTO.setLongitude(tData.getLongitude());
            tripDataDTO.setAcelX(tData.getAcelX());
            tripDataDTO.setAcelY(tData.getAcelY());
            tripDataDTO.setAcelZ(tData.getAcelZ());
            tripDataDTO.setBattery(tData.getBattery());
            tripDataDTO.setDate(new Date(tData.getTime()));
            tripDataDTO.setEngineTemp(tData.getEngineTemp());
            tripDataDTO.setFuelTankLevel(tData.getFuelTankLevel());
            tripDataDTO.setSpeed(tData.getSpeed());
            tripDataDTO.setRPM(tData.getRPM());

            if(lastLatitude == 255 && lastLongitude == 255 ){
                lastLatitude = tData.getLatitude();
                lastLongitude = tData.getLongitude();
            }
            else {
                kms += Distances.calculateDistance(lastLatitude, lastLongitude, tData.getLatitude(), tData.getLongitude());
                lastLatitude = tData.getLatitude();
                lastLongitude = tData.getLongitude();
            }

            if(tData.getSpeed() > 0){
                speedNot0++;
                speedAVG += tData.getSpeed();

                if(tData.getMAF() > 0){
                    numberMPGNotEmpty++;
                    tripDataDTO.setMgp((float) (tData.getSpeed() * 4.795324606 / tData.getMAF()));
                    mpg += tripDataDTO.getMgp();
                }
            }

            tripDTO.getTripData().add(tripDataDTO);
        }

        float l100kmAVG = 0;
        if(numberMPGNotEmpty > 0){
            l100kmAVG = (float) (235.2137783/(mpg / numberMPGNotEmpty));
        }

        if(speedNot0 > 0){
            speedAVG /= speedNot0;
        }


        tripDTO.setFuelConsumptionAVG(l100kmAVG);
        tripDTO.setKms(kms);
        tripDTO.setSpeedAVG(speedAVG);

        return tripDTO;
    }

    @Override
    protected void onPostExecute(TripDTO tripDTO) {
        super.onPostExecute(tripDTO);

        callback.doCallback(tripDTO);
    }
}
