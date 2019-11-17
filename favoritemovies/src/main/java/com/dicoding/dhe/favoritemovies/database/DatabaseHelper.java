/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_BACKDROP;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_ID;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_LANGUAGE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_OVERVIEW;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_POPULARITY;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_POSTER;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_TITLE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.COLUMN_VOTE;
import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "catalogue_movie";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_MOVIE = "create table " + TABLE_NAME + " (" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_TITLE + " text not null, " +
                COLUMN_BACKDROP + " text not null, " +
                COLUMN_POSTER + " text not null, " +
                COLUMN_RELEASE_DATE + " text not null, " +
                COLUMN_VOTE + " text not null, " +
                COLUMN_OVERVIEW + " text not null, " +
                COLUMN_LANGUAGE + " text not null, " +
                COLUMN_POPULARITY + " text not null " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE MOVIE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
