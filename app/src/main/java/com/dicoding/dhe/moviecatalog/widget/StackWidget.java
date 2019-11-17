/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.helper.FavoriteHelper;

import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class StackWidget extends AppWidgetProvider {
    public static final String TOAST_ACTION = "com.dicoding.dhe.moviecatalog.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.dicoding.dhe.moviecatalog.EXTRA_ITEM";
    public static final String EXTRA_ID = "com.dicoding.dhe.moviecatalog.EXTRA_ID";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        FavoriteHelper favoriteHelper = new FavoriteHelper(context);
        favoriteHelper.open();

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, StackWidget.class);
        toastIntent.setAction(StackWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            String viewTitle = intent.getStringExtra(EXTRA_ITEM);

            Intent i = new Intent();
            i.setClassName("com.dicoding.dhe.moviecatalog", "com.dicoding.dhe.moviecatalog.activity.FavoriteDetailActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.parse(CONTENT_URI + "/" + intent.getIntExtra(EXTRA_ID, 0));
            i.setData(uri);
            context.startActivity(i);
            Toast.makeText(context, viewTitle, Toast.LENGTH_SHORT).show();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
            super.onReceive(context,intent);
        }

        super.onReceive(context, intent);
    }
}

