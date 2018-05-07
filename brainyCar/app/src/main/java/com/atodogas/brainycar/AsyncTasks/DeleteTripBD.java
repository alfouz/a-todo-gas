package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;
import com.atodogas.brainycar.Utils.Distances;

import java.util.List;

public class DeleteTripBD extends AsyncTask<Integer, Void, Void> {
    private AppDatabase db;
    private Context context;

    public DeleteTripBD(Context context) {
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "brainyCar").build();
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        int idTrip = integers[0];

        TripEntity tripEntity = db.tripDao().getTrip(idTrip);
        List<TripDataEntity> tripDataEntities = db.tripDataDao().getAllTripData(tripEntity.getId());

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

        CarEntity carEntity = db.carDao().getCarById(tripEntity.getIdCar());
        List<TripEntity> tripEntities = db.tripDao().getAllCarTrips(carEntity.getId());

        carEntity.setKms(carEntity.getKms() - kms);

        if(tripEntities.size() - 1 == 0){
            carEntity.setAVGSpeed(0);
            carEntity.setAVGFuelConsumption(0);
        }
        else {
            l100kmAVG = (carEntity.getAVGFuelConsumption() * tripEntities.size() - l100kmAVG)/(tripEntities.size() - 1);
            speedAVG = (carEntity.getAVGSpeed() * tripEntities.size() - speedAVG)/(tripEntities.size() - 1);

            carEntity.setAVGFuelConsumption(l100kmAVG);
            carEntity.setAVGSpeed(speedAVG);
        }

        db.carDao().updateCar(carEntity);
        db.tripDao().deleteTrip(tripEntity);

        return null;
    }
}
