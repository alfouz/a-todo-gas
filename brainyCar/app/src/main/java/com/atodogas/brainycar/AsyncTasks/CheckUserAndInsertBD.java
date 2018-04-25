package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.UserEntity;

public class CheckUserAndInsertBD extends AsyncTask<String, Void, UserEntity> {
    CallbackInterface callback;
    AppDatabase db;


    public CheckUserAndInsertBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = Room.databaseBuilder(context, AppDatabase.class, "brainyCar").build();
    }

    @Override
    protected UserEntity doInBackground(String... strings) {
        String idGoogle = strings[0];

        UserEntity user = db.userDao().getUserByIdGoogle(idGoogle);
        if(user == null){
            user = new UserEntity();
            user.setIdGoogle(idGoogle);
            db.userDao().insertUser(user);
            user = db.userDao().getUserByIdGoogle(idGoogle);
        }

        CarEntity car = db.carDao().getCarByIdUser(user.getId());
        if(car == null){
            car = new CarEntity();
            car.setIdUser(user.getId());
            car.setFuelType(0);
            car.setModel("VW GOLFO TDI 2011");
            car.setKms(20000);
            car.setAVGFuelConsumption(6.2f);
            car.setAVGSpeed(36.5f);
            int idCar = (int) db.carDao().insertCar(car);
        }

        return user;
    }

    @Override
    protected void onPostExecute(UserEntity user) {
        callback.doCallback(user);
    }
}
