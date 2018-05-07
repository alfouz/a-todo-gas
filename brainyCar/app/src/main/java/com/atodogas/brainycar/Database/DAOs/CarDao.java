package com.atodogas.brainycar.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.atodogas.brainycar.Database.Entities.CarEntity;

@Dao
public interface CarDao {
    @Query("SELECT * FROM Cars WHERE idUser = :idUser LIMIT 1")
    CarEntity getCarByIdUser(int idUser);

    @Query("SELECT * FROM Cars WHERE id = :idCar")
    CarEntity getCarById(int idCar);

    @Insert
    long insertCar(CarEntity user);

    @Update
    void updateCar(CarEntity... user);

    @Delete
    void deleteCar(CarEntity... user);
}
