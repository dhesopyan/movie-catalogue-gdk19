/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.dhe.moviecatalog.provider.FavoriteColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "catalogue_movie";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_MOVIE = "create table " + FavoriteColumns.TABLE_NAME + " (" +
                FavoriteColumns.COLUMN_ID + " integer primary key autoincrement, " +
                FavoriteColumns.COLUMN_TITLE + " text not null, " +
                FavoriteColumns.COLUMN_LANGUAGE + " text not null, " +
                FavoriteColumns.COLUMN_BACKDROP + " text not null, " +
                FavoriteColumns.COLUMN_POSTER + " text not null, " +
                FavoriteColumns.COLUMN_RELEASE_DATE + " text not null, " +
                FavoriteColumns.COLUMN_VOTE + " text not null, " +
                FavoriteColumns.COLUMN_POPULARITY + " text not null, " +
                FavoriteColumns.COLUMN_OVERVIEW + " text not null, " +
                FavoriteColumns.COLUMN_TYPE + " text not null " +
                ");";

        sqLiteDatabase.execSQL(CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE MOVIE IF EXISTS " + FavoriteColumns.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
