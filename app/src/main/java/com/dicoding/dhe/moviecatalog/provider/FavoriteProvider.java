/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.provider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.helper.FavoriteHelper;
import com.dicoding.dhe.moviecatalog.widget.StackWidget;

import io.paperdb.Paper;

import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.CONTENT_URI;

public class FavoriteProvider extends ContentProvider {

    private static final int FAVORITE = 100;
    private static final int FAVORITE_ID = 101;
    private static final int FAVORITE_TITLE = 102;
    private static final int FAVORITE_DELETE = 103;
    private static final int FAVORITE_LANGUAGE = 104;
    private String language;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, FavoriteColumns.TABLE_NAME, FAVORITE);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, FavoriteColumns.TABLE_NAME + "/#", FAVORITE_ID);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, FavoriteColumns.TABLE_NAME  + "/" + FavoriteColumns.COLUMN_TITLE + "/*", FAVORITE_TITLE);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, FavoriteColumns.TABLE_NAME + "/delete" + "/#", FAVORITE_DELETE);
        sUriMatcher.addURI(DatabaseContract.AUTHORITY, FavoriteColumns.TABLE_NAME + "/language", FAVORITE_LANGUAGE);
    }

    private FavoriteHelper favoriteHelper;

    @Override
    public boolean onCreate() {
        favoriteHelper = new FavoriteHelper(getContext());
        favoriteHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case FAVORITE:
                cursor = favoriteHelper.queryProvider();
                break;

            case FAVORITE_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;

            case FAVORITE_TITLE:
                cursor = favoriteHelper.queryByTypeProvider(uri.getLastPathSegment());
                break;

            case FAVORITE_DELETE:
                favoriteHelper.deleteProvider(String.valueOf(uri.getLastPathSegment()));
                cursor = null;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                ComponentName thisWidget = new ComponentName(getContext(), StackWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                Intent updates = new Intent();
                updates.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                updates.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
                getContext().sendBroadcast(updates);
                break;

            case FAVORITE_LANGUAGE:
                Paper.init(getContext());
                language = Paper.book().read("language");
                MatrixCursor extras = new MatrixCursor(new String[] { "language" });
                extras.addRow(new String[] { language });
                cursor = extras;
                break;

            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                added = favoriteHelper.insertProvider(contentValues);
                break;

            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                break;

            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                updated = favoriteHelper.updateProvider(uri.getLastPathSegment(), contentValues);
                break;

            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }
}
