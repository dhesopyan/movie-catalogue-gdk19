/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import static android.provider.BaseColumns._ID;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.getColumnInt;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.getColumnString;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_BACKDROP;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_OVERVIEW;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_POSTER;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_TITLE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_VOTE;

import static android.provider.BaseColumns._ID;

public class Movie implements Parcelable {
    private String images, imagesDetail, name, description, showtime, rating, language, runtime, popularity, type;
    private Integer movieId;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getShowtime() {
        return showtime;
    }

    public String getRating() { return rating; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public String getRuntime() { return runtime; }

    public String getPopularity() { return popularity; }

    public String getImages() {
        return images;
    }

    public String getImagesDetail() {
        return imagesDetail;
    }

    public String getType() {
        return type;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public Movie() {
    }

    public Movie(JSONObject object) {

        try {
            Integer movieid = object.getInt("id");
            String images = object.getString("poster_path");
            String imagesDetail = "https://image.tmdb.org/t/p/w342"+object.getString("backdrop_path");
            String name = object.has("title") ? object.getString("title") : object.getString("name");
            String description = object.getString("overview") ;
            String language = object.getString("original_language");
            String vote = object.getString("vote_average");
            String release = object.has("release_date") ? object.getString("release_date") : object.getString("first_air_date");
            String popularity = object.getString("popularity");
            String type = object.has("title") ? "movie" : "tv";

            this.movieId = movieid;
            this.name = name;
            this.images = images;
            this.imagesDetail = imagesDetail;
            this.showtime = release;
            this.description = description;
            this.language = language;
            this.rating = vote;
            this.runtime = release;
            this.popularity = popularity;
            this.type = type;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Movie(Parcel in) {
        movieId = in.readInt();
        name = in.readString();
        description = in.readString();
        showtime = in.readString();
        rating = in.readString();
        language= in.readString();
        runtime = in.readString();
        popularity = in.readString();
        images = in.readString();
        imagesDetail = in.readString();
        type = in.readString();
    }

    public Movie(int id, String title, String description, String date, String images) {
        this.movieId = id;
        this.name = title;
        this.description = description;
        this.showtime = date;
        this.images = images;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(showtime);
        dest.writeString(rating);
        dest.writeString(language);
        dest.writeString(runtime);
        dest.writeString(popularity);
        dest.writeString(images);
        dest.writeString(imagesDetail);
        dest.writeString(type);
    }

    public Movie(Cursor cursor) {
        this.movieId = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, COLUMN_TITLE);
        this.images = getColumnString(cursor, COLUMN_BACKDROP);
        this.imagesDetail = getColumnString(cursor, COLUMN_POSTER);
        this.runtime = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.rating = getColumnString(cursor, COLUMN_VOTE);
        this.description = getColumnString(cursor, COLUMN_OVERVIEW);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}