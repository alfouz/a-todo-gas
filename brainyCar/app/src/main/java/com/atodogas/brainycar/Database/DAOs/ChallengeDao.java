package com.atodogas.brainycar.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atodogas.brainycar.Database.Entities.ChallengeEntity;

import java.util.List;

@Dao
public interface ChallengeDao {
    @Query("SELECT * FROM Challenges")
    List<ChallengeEntity> getAllChallenge();

    @Query("SELECT * FROM Challenges WHERE id NOT IN (SELECT idChallenge FROM Achievements WHERE idUser = :idUser)")
    List<ChallengeEntity> getChallengesNotAchievement(int idUser);
}
