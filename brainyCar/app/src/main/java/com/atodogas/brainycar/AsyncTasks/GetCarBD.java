package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.DTOs.CarDTO;
import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.TripDataEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;

import java.util.List;

public class GetCarBD extends AsyncTask<Integer, Void, CarDTO> {
    CallbackInterface callback;
    AppDatabase db;

    public GetCarBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "brainyCar").build();
    }

    @Override
    protected CarDTO doInBackground(Integer... integers) {
        int userId = integers[0];

        CarEntity carEntity = db.carDao().getCarByIdUser(userId);
        TripEntity tripEntity = db.tripDao().getLastTrip(carEntity.getId());
        List<TripDataEntity> tripDataEntities = db.tripDataDao().getAllTripData(tripEntity.getId());

        CarDTO carDTO  = new CarDTO();
        carDTO.setId(carEntity.getId());
        carDTO.setModel(carEntity.getModel());
        carDTO.setFuelType(carEntity.getFuelType());
        carDTO.setKms(carEntity.getKms());
        carDTO.setFuelConsumptionAVG(carEntity.getAVGFuelConsumption());
        carDTO.setSpeedAVG(carEntity.getAVGSpeed());
        carDTO.setFuelTankLevel(tripDataEntities.get(0).getFuelTankLevel());

        return carDTO;
    }

    @Override
    protected void onPostExecute(CarDTO carDTO) {
        super.onPostExecute(carDTO);
        callback.doCallback(carDTO);
    }
}
