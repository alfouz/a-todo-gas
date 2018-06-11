package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.DTOs.TripDTO;
import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;
import com.atodogas.brainycar.Utils.Distances;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetAllTripsBD extends AsyncTask<Integer, Void, List<TripDTO>> {
    CallbackInterface callback;
    AppDatabase db;

    public GetAllTripsBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    protected List<TripDTO> doInBackground(Integer... integers) {
        int carId = integers[0];

        List<TripEntity> tripsEntities = db.tripDao().getAllCarTrips(carId);
        List<TripDTO> tripDTOs = new ArrayList<>();

        for(TripEntity t : tripsEntities){
            TripDTO tripDTO = new TripDTO();
            tripDTO.setId(t.getId());
            tripDTO.setStartPlace(t.getStartPlace());
            tripDTO.setEndPlace(t.getEndPlace());
            tripDTO.setHours(t.getDuration()/3600);
            tripDTO.setMinutes((t.getDuration() - (tripDTO.getHours() * 3600)) /60);

            List<TripDataEntity> tripDataEntities = db.tripDataDao().getAllTripData(t.getId());

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
                        mpg += (float) (tData.getSpeed() * 4.795324606 / tData.getMAF());
                    }
                }
            }

            float l100kmAVG = 0;
            if(numberMPGNotEmpty > 0){
                l100kmAVG = (float) (235.2137783/(mpg / numberMPGNotEmpty));
            }

            if(speedNot0 > 0){
                speedAVG /= speedNot0;
            }

            tripDTO.setFuelConsumptionAVG(l100kmAVG);
            tripDTO.setSpeedAVG(speedAVG);
            tripDTO.setKms(kms);

            tripDTOs.add(tripDTO);
        }

        return tripDTOs;
    }

    @Override
    protected void onPostExecute(List<TripDTO> tripDTOS) {
        super.onPostExecute(tripDTOS);

        callback.doCallback(tripDTOS);
    }
}
