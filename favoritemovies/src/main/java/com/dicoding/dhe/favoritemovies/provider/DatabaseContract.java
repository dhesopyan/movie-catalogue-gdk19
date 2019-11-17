/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.provider;

import android.database.Cursor;
import android.net.Uri;

import com.dicoding.dhe.favoritemovies.database.FavoriteColumns;

public class DatabaseContract {

    public static final String AUTHORITY_MOVIE = "com.dicoding.dhe.moviecatalog";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY_MOVIE)
            .appendPath(FavoriteColumns.TABLE_NAME)
            .build();

    public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme("content")
            .authority(AUTHORITY_MOVIE)
            .appendPath(FavoriteColumns.TABLE_NAME)
            .appendPath("title")
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }

}
