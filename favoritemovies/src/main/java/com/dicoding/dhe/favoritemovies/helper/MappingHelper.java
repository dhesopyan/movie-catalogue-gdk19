/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.helper;

import android.database.Cursor;

import com.dicoding.dhe.favoritemovies.entity.MovieItem;

import java.util.ArrayList;

import static com.dicoding.dhe.favoritemovies.database.FavoriteColumns.*;

public class MappingHelper {

    public static ArrayList<MovieItem> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<MovieItem> notesList = new ArrayList<>();

        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(COLUMN_ID));
            String title = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String description = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_OVERVIEW));
            String date = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE));
            String images = notesCursor.getString(notesCursor.getColumnIndexOrThrow(COLUMN_POSTER));
            notesList.add(new MovieItem(id, title, description, date, images));
        }

        return notesList;
    }
}
