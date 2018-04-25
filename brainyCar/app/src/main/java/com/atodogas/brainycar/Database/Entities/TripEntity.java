package com.atodogas.brainycar.Database.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Trips", foreignKeys = @ForeignKey(  entity = CarEntity.class,
                                    parentColumns = "id",
                                    childColumns = "idCar",
                                    onUpdate = ForeignKey.CASCADE,
                                    onDelete = ForeignKey.CASCADE))
public class TripEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idCar;

    private String StartPlace;

    private String EndPlace;

    private int Duration;


    public void setId(int id) {
        this.id = id;
    }

    public void setIdCar(int idCar) {
        this.idCar = idCar;
    }

    public void setStartPlace(String startPlace) {
        StartPlace = startPlace;
    }

    public void setEndPlace(String endPlace) {
        EndPlace = endPlace;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public int getId() {
        return id;
    }

    public int getIdCar() {
        return idCar;
    }

    public String getStartPlace() {
        return StartPlace;
    }

    public String getEndPlace() {
        return EndPlace;
    }

    public int getDuration() {
        return Duration;
    }
}
