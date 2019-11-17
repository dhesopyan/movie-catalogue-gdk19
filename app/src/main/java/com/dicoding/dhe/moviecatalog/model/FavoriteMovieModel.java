/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.model;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.getColumnDouble;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.getColumnInt;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.getColumnString;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_BACKDROP;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_LANGUAGE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_OVERVIEW;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_POPULARITY;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_POSTER;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_TITLE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_VOTE;

public class FavoriteMovieModel {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("language")
    private String language;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("popularity")
    private int popularity;

    @SerializedName("overview")
    private String overview;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public FavoriteMovieModel(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.language = getColumnString(cursor, COLUMN_LANGUAGE);
        this.backdropPath = getColumnString(cursor, COLUMN_BACKDROP);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER);
        this.releaseDate = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.voteAverage = getColumnDouble(cursor, COLUMN_VOTE);
        this.popularity = getColumnInt(cursor, COLUMN_POPULARITY);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",language = '" + language + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        "overview = '" + overview + '\'' +
                        "}";
    }
}