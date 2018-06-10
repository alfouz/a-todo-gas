package com.atodogas.brainycar.AsyncTasks;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.atodogas.brainycar.Database.AppDatabase;
import com.atodogas.brainycar.Database.Entities.CarEntity;
import com.atodogas.brainycar.Database.Entities.UserEntity;

public class CheckUserAndInsertBD extends AsyncTask<String, Void, UserEntity> {
    CallbackInterface callback;
    AppDatabase db;


    public CheckUserAndInsertBD(CallbackInterface callback, Context context) {
        this.callback = callback;
        this.db = AppDatabase.getInstance(context);
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
            car.setKms(0);
            car.setAVGFuelConsumption(0);
            car.setAVGSpeed(0);
            int idCar = (int) db.carDao().insertCar(car);
        }

        return user;
    }

    @Override
    protected void onPostExecute(UserEntity user) {
        callback.doCallback(user);
    }
}
