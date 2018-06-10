package com.atodogas.brainycar.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Challenges")
public class ChallengeEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String Title;

    @NonNull
    private String Description;

    @NonNull
    private int Level;

    @NonNull
    private float Objective;

    @NonNull
    private String Operator;

    @NonNull
    private String Variable;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(@NonNull String title) {
        Title = title;
    }

    public void setDescription(@NonNull String description) {
        Description = description;
    }

    public void setLevel(@NonNull int level) {
        Level = level;
    }

    public void setObjective(@NonNull float objective) {
        Objective = objective;
    }

    public void setOperator(@NonNull String operator) {
        Operator = operator;
    }

    public void setVariable(@NonNull String variable) {
        Variable = variable;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return Title;
    }

    @NonNull
    public String getDescription() {
        return Description;
    }

    @NonNull
    public int getLevel() {
        return Level;
    }

    @NonNull
    public float getObjective() {
        return Objective;
    }

    @NonNull
    public String getOperator() {
        return Operator;
    }

    @NonNull
    public String getVariable() {
        return Variable;
    }
}
