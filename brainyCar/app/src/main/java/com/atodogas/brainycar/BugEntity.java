package com.atodogas.brainycar;

/**
 * Created by belenvb on 21/04/2018.
 */

public class BugEntity {
    private String title;
    private String description;

    public BugEntity(String title, String description) {
        this.title = title;
        this.description = description;
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
}
