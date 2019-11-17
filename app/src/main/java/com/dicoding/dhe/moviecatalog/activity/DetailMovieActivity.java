/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.dicoding.dhe.moviecatalog.helper.FavoriteHelper;
import com.dicoding.dhe.moviecatalog.model.Movie;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;
import com.dicoding.dhe.moviecatalog.provider.FavoriteColumns;
import com.dicoding.dhe.moviecatalog.util.DateTime;
import com.dicoding.dhe.moviecatalog.widget.StackWidget;

import io.paperdb.Paper;

@GlideModule
public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_LANGUAGE = "extra_language";
    TextView voteRate, originalLanguageTitle, originalLanguage, releaseDate, popularity, overview;
    Movie movie;
    String language;
    private Movie item;
    private FavoriteHelper favoriteHelper;
    private Boolean isFavorite = false;
    ImageView iv_favorite;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        voteRate = findViewById(R.id.text_rating_title);
        originalLanguageTitle = findViewById(R.id.text_language_title);
        originalLanguage = findViewById(R.id.text_language);
        releaseDate = findViewById(R.id.text_release_title);
        popularity = findViewById(R.id.text_popularity_title);
        overview = findViewById(R.id.text_overview);
        iv_favorite = findViewById(R.id.iv_favorite);

        language = getIntent().getStringExtra(EXTRA_LANGUAGE);
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        item = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (isFavorite) iv_favorite.setImageResource(R.drawable.ic_favorite);
        else iv_favorite.setImageResource(R.drawable.ic_favorite_border);

        Paper.init(this);

        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));

        ImageView imgPhoto =  findViewById(R.id.img_movie_photo);
        ImageView imgBackdop =  findViewById(R.id.iv_backdrop);
        TextView textOverview = findViewById(R.id.text_review);
        TextView textRating = findViewById(R.id.text_rating);
        TextView textLanguage = findViewById(R.id.text_language);
        TextView textRelease = findViewById(R.id.text_release);
        TextView textPopularity = findViewById(R.id.text_popularity);

        Glide.with(this).load(movie.getImagesDetail())
                .thumbnail(Glide.with(imgPhoto).load(R.drawable.placeholder))
                .fitCenter()
                .into(imgBackdop);

        Glide.with(this).load("https://image.tmdb.org/t/p/w185"+movie.getImages())
                .thumbnail(Glide.with(imgPhoto).load(R.drawable.img_load))
                .fitCenter()
                .into(imgPhoto);
        textOverview.setText(movie.getDescription());
        textRating.setText(movie.getRating());
        textLanguage.setText(movie.getLanguage());
        textRelease.setText(DateTime.getLongDate(movie.getRuntime()));
        textPopularity.setText(movie.getPopularity());
        setActionBarTitle(movie.getName());

        iv_favorite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isFavorite) FavoriteRemove();
            else FavoriteSave();
            isFavorite = !isFavorite;
            favoriteSet();
            }
        });

        loadDataSQLite();
    }

    private void setActionBarTitle(String title){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
        }  else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        voteRate.setText(resources.getText(R.string.rating));
        originalLanguageTitle.setText(resources.getText(R.string.language));
        originalLanguage.setText(resources.getText(R.string.english));
        releaseDate.setText(resources.getText(R.string.release));
        popularity.setText(resources.getText(R.string.popularity));
        overview.setText(resources.getText(R.string.overview));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    private void FavoriteSave() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        ContentValues cv = new ContentValues();
        cv.put(FavoriteColumns.COLUMN_ID, item.getMovieId());
        cv.put(FavoriteColumns.COLUMN_TITLE, item.getName());
        cv.put(FavoriteColumns.COLUMN_LANGUAGE, item.getLanguage());
        cv.put(FavoriteColumns.COLUMN_BACKDROP, item.getImagesDetail());
        cv.put(FavoriteColumns.COLUMN_POSTER, item.getImages());
        cv.put(FavoriteColumns.COLUMN_RELEASE_DATE, item.getRuntime());
        cv.put(FavoriteColumns.COLUMN_VOTE, item.getRating());
        cv.put(FavoriteColumns.COLUMN_POPULARITY, item.getPopularity());
        cv.put(FavoriteColumns.COLUMN_OVERVIEW, item.getDescription());
        cv.put(FavoriteColumns.COLUMN_TYPE, item.getType());

        favoriteHelper.insertProvider(cv);
        Toast.makeText(this, R.string.like, Toast.LENGTH_SHORT).show();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, StackWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Intent updates = new Intent();
        updates.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        updates.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        SystemClock.sleep(500);
        super.sendBroadcast(updates);
    }

    private void FavoriteRemove() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        favoriteHelper.deleteProvider(String.valueOf(item.getMovieId()));
        Toast.makeText(this, R.string.dislike, Toast.LENGTH_SHORT).show();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName thisWidget = new ComponentName(this, StackWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        Intent updates = new Intent();
        updates.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        updates.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
        super.sendBroadcast(updates);
    }

    private void loadDataSQLite() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        Cursor cursor = favoriteHelper.queryByIdProvider(String.valueOf(item.getMovieId()));

        if (cursor != null) {
            if (cursor.moveToFirst()) isFavorite = true;
            cursor.close();
        }
        favoriteSet();
    }

    private void favoriteSet() {
        if (isFavorite) iv_favorite.setImageResource(R.drawable.ic_favorite);
        else iv_favorite.setImageResource(R.drawable.ic_favorite_border);
    }

}
