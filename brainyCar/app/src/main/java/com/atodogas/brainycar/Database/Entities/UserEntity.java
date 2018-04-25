package com.atodogas.brainycar.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Users")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String idGoogle;


    public void setId(int id) {
        this.id = id;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    public int getId() {
        return id;
    }

    public String getIdGoogle() {
        return idGoogle;
    }
}
