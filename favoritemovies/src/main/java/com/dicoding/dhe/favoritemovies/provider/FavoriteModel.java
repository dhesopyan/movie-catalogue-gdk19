/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.provider;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import static android.provider.BaseColumns._ID;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_BACKDROP;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_LANGUAGE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_OVERVIEW;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_POPULARITY;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_POSTER;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_TITLE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_VOTE;
import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.getColumnDouble;
import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.getColumnInt;
import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.getColumnString;

public class FavoriteModel {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("overview")
    private String overview;

    @SerializedName("language")
    private String language;

    @SerializedName("popularity")
    private double popularity;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
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

    public String getOverview() {
        return overview;
    }

    public String getLanguage() {
        return language;
    }

    public double getPopularity() {
        return popularity;
    }

    public FavoriteModel(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.backdropPath = getColumnString(cursor, COLUMN_BACKDROP);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER);
        this.releaseDate = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.voteAverage = getColumnDouble(cursor, COLUMN_VOTE);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
        this.language = getColumnString(cursor, COLUMN_LANGUAGE);
        this.popularity = getColumnDouble(cursor, COLUMN_POPULARITY);
    }

    @Override
    public String toString() {
        return
                "ResultsItem{" +
                        ",id = '" + id + '\'' +
                        ",title = '" + title + '\'' +
                        ",backdrop_path = '" + backdropPath + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",overview = '" + overview + '\'' +
                        ",language = '" + language + '\'' +
                        "popularity = '" + popularity + '\'' +
                        "}";
    }
}