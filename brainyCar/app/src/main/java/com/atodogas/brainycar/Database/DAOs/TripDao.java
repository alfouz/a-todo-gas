package com.atodogas.brainycar.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.atodogas.brainycar.Database.Entities.TripEntity;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trips WHERE idCar = :idCar ORDER BY id DESC")
    List<TripEntity> getAllCarTrips(int idCar);

    @Query("SELECT * FROM Trips WHERE id = :id")
    TripEntity getTrip(int id);

    @Query("SELECT * FROM Trips WHERE idCar = :idCar ORDER BY id DESC LIMIT 1")
    TripEntity getLastTrip(int idCar);

    @Insert
    long insertTrip(TripEntity trip);

    @Update
    void updateTrip(TripEntity... trip);

    @Delete
    void deleteTrip(TripEntity... trip);
}
