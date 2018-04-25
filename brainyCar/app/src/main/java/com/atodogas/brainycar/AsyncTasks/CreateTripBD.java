package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.TripEntity;

public class CreateTripBD extends AsyncTask<Integer, Void, TripEntity> {
    CallbackInterface callback;
    AppDatabase db;

    public CreateTripBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = this.db = Room.databaseBuilder(context, AppDatabase.class, "brainyCar").build();
    }

    @Override
    protected TripEntity doInBackground(Integer... integers) {
        int idUser = integers[0];

        CarEntity car = db.carDao().getCarByIdUser(idUser);

        TripEntity trip = new TripEntity();
        trip.setIdCar(car.getId());
        trip.setDuration(0);
        trip.setStartPlace("");
        trip.setEndPlace("");
        trip.setId((int) db.tripDao().insertTrip(trip));

        return trip;
    }

    @Override
    protected void onPostExecute(TripEntity tripEntity) {
        super.onPostExecute(tripEntity);

        callback.doCallback(tripEntity);
    }
}
