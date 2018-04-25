package com.atodogas.brainycar.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.atodogas.brainycar.Database.Entities.TripDataEntity;

import java.util.List;

@Dao
public interface TripDataDao {
    @Query("SELECT * FROM TripData WHERE idTrip = :idTrip")
    List<TripDataEntity> getAllTripData(int idTrip);

    @Insert
    long insertTripData(TripDataEntity tripData);

    @Update
    void updateTripData(TripDataEntity... tripData);

    @Delete
    void deleteTripData(TripDataEntity... tripData);
}
