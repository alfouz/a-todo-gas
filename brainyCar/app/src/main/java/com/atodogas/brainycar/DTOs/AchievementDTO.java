package com.atodogas.brainycar.DTOs;


public class AchievementDTO {
    private String title;
    private String description;
    private boolean isAchieve;
    private int level;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAchieve() {
        return isAchieve;
    }

    public void setAchieve(boolean achieve) {
        isAchieve = achieve;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
