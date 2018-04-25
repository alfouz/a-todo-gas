package com.atodogas.brainycar.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.atodogas.brainycar.Database.DAOs.CarDao;
import com.atodogas.brainycar.Database.DAOs.TripDao;
import com.atodogas.brainycar.Database.DAOs.TripDataDao;
import com.atodogas.brainycar.Database.DAOs.UserDao;
import com.atodogas.brainycar.Database.Entities.*;

@Database(entities = {UserEntity.class, CarEntity.class, TripEntity.class, TripDataEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CarDao carDao();
    public abstract TripDao tripDao();
    public abstract TripDataDao tripDataDao();
}
