package com.atodogas.brainycar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by belenvb on 21/04/2018.
 */

public class BugEntity implements Parcelable {
    private String title;
    private String description;

    public BugEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public BugEntity(Parcel in) {
        title = in.readString();
        description = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
    }

    public static final Creator<BugEntity> CREATOR = new Creator<BugEntity>() {
        @Override
        public BugEntity createFromParcel(Parcel in) {
            return new BugEntity(in);
        }

        @Override
        public BugEntity[] newArray(int size) {
            return new BugEntity[size];
        }
    };
}
