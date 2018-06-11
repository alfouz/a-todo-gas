package com.atodogas.brainycar.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.atodogas.brainycar.Database.Entities.AchievementEntity;
import com.atodogas.brainycar.Database.Entities.CarEntity;

import java.util.List;

@Dao
public interface AchievementDao {
    @Query("SELECT * FROM Achievements WHERE idUser = :idUser")
    List<AchievementEntity> getUserAchievements(int idUser);

    @Insert
    long insertAchievement(AchievementEntity achievement);
}
