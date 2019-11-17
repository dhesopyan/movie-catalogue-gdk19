/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieItem implements Parcelable {
    private int id;
    private String title, description, date, images;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.images);
    }

    public MovieItem(int id, String title, String description, String date, String images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.images = images;
    }

    private MovieItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.images = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override

        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }


        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}