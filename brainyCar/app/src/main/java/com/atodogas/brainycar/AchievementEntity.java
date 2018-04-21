package com.atodogas.brainycar;

/**
 * Created by belenvb on 21/04/2018.
 */

public class AchievementEntity {

    private String title;
    private String description;
    private Boolean isAchieve;
    private Integer level;

    public AchievementEntity(String title, String description, Boolean isAchieve, Integer level) {
        this.title = title;
        this.description = description;
        this.isAchieve = isAchieve;
        this.level = level;
    }

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

    public void setAchieve(Boolean achieve) {
        isAchieve = achieve;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
