/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.helper;

import android.database.Cursor;

import com.dicoding.dhe.moviecatalog.model.Movie;

import java.util.ArrayList;

import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_ID;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_OVERVIEW;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_POSTER;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.dicoding.dhe.moviecatalog.provider.FavoriteColumns.COLUMN_TITLE;

public class MappingHelper {

    public static ArrayList<Movie> mapCursorToArrayList(Cursor moviesCursor) {
        ArrayList<Movie> movieList = new ArrayList<>();

        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(COLUMN_ID));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String description = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(COLUMN_OVERVIEW));
            String date = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE));
            String images = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(COLUMN_POSTER));
            movieList.add(new Movie(id, title, description, date, images));
        }

        return movieList;
    }
}
