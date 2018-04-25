package com.atodogas.brainycar.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Cars", foreignKeys = @ForeignKey(  entity = UserEntity.class,
                                    parentColumns = "id",
                                    childColumns = "idUser",
                                    onUpdate = ForeignKey.CASCADE,
                                    onDelete = ForeignKey.CASCADE))

public class CarEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idUser;

    @NonNull
    private String Model;

    @NonNull
    private int FuelType;

    @NonNull
    private int Kms;

    @NonNull
    private float AVGFuelConsumption;

    @NonNull
    private float AVGSpeed;


    public void setId(int id) {
        this.id = id;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setFuelType(int fuelType) {
        FuelType = fuelType;
    }

    public void setKms(int kms) {
        Kms = kms;
    }

    public void setAVGFuelConsumption(float AVGFuelConsumption) {
        this.AVGFuelConsumption = AVGFuelConsumption;
    }

    public void setAVGSpeed(float AVGSpeed) {
        this.AVGSpeed = AVGSpeed;
    }

    public int getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getModel() {
        return Model;
    }

    public int getFuelType() {
        return FuelType;
    }

    public int getKms() {
        return Kms;
    }

    public float getAVGFuelConsumption() {
        return AVGFuelConsumption;
    }

    public float getAVGSpeed() {
        return AVGSpeed;
    }
}
