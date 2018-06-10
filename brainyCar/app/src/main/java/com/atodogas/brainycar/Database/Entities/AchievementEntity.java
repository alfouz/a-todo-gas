package com.atodogas.brainycar.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Achievements", primaryKeys = {"idUser", "idChallenge"},
        foreignKeys = {
            @ForeignKey(    entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "idUser",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE),

            @ForeignKey(    entity = ChallengeEntity.class,
                        parentColumns = "id",
                        childColumns = "idChallenge",
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE)})
public class AchievementEntity {
    private int idUser;
    private int idChallenge;

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setIdChallenge(int idChallenge) {
        this.idChallenge = idChallenge;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdChallenge() {
        return idChallenge;
    }
}
