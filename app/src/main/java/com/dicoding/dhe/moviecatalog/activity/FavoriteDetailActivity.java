/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.helper.FavoriteHelper;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;
import com.dicoding.dhe.moviecatalog.model.FavoriteMovieModel;
import com.dicoding.dhe.moviecatalog.util.DateTime;
import com.dicoding.dhe.moviecatalog.widget.StackWidget;

import io.paperdb.Paper;

@GlideModule
    public class FavoriteDetailActivity extends AppCompatActivity {
        TextView voteRate, originalLanguageTitle, releaseDate, popularity, overview;
        String language;
        ImageView iv_favorite;
        private FavoriteMovieModel item;
        FavoriteHelper favoriteHelper;
        Context context;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favorite_detail);

            voteRate = findViewById(R.id.text_rating_title);
            originalLanguageTitle = findViewById(R.id.text_language_title);
            releaseDate = findViewById(R.id.text_release_title);
            popularity = findViewById(R.id.text_popularity_title);
            overview = findViewById(R.id.text_overview);

            iv_favorite = findViewById(R.id.iv_favorite);

            Paper.init(this);

            language = Paper.book().read("language");
            if(language == null)
                Paper.book().write("language","en");

            Uri uri = getIntent().getData();

            if (uri != null) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) item = new FavoriteMovieModel(cursor);
                    cursor.close();
                }
            }

            storeData();

            iv_favorite.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FavoriteRemove();
                    Toast.makeText(FavoriteDetailActivity.this,getString(R.string.delete)+ ' ' + item.getTitle(),Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        private void storeData() {
            if (item == null) return;

            ImageView imgPhoto =  findViewById(R.id.img_movie_photo);
            ImageView imgBackdop =  findViewById(R.id.iv_backdrop);
            TextView textOverview = findViewById(R.id.text_review);
            TextView textRating = findViewById(R.id.text_rating);
            TextView textShowtime = findViewById(R.id.text_release);
            TextView textPopularity = findViewById(R.id.text_popularity);
            TextView textLanguage = findViewById(R.id.text_language);

            Glide.with(this).load("http://image.tmdb.org/t/p/w342/" + item.getPosterPath())
                    .thumbnail(Glide.with(imgPhoto).load(R.drawable.placeholder))
                    .fitCenter()
                    .into(imgBackdop);
            Glide.with(this).load("http://image.tmdb.org/t/p/w185/" + item.getPosterPath())
                    .thumbnail(Glide.with(imgPhoto).load(R.drawable.img_load))
                    .fitCenter()
                    .into(imgPhoto);
            textOverview.setText(item.getOverview());
            textRating.setText(String.valueOf(item.getVoteAverage()));
            textShowtime.setText(DateTime.getLongDate(item.getReleaseDate()));
            textPopularity.setText(String.valueOf(item.getPopularity()));
            textLanguage.setText(item.getLanguage());
            setActionBarTitle(item.getTitle());
        }

        private void FavoriteRemove() {
            if(!String.valueOf(item.getId()).isEmpty()) {
                favoriteHelper = new FavoriteHelper(this);
                favoriteHelper.open();
                favoriteHelper.deleteProvider(String.valueOf(item.getId()));
                Toast.makeText(this, R.string.dislike, Toast.LENGTH_SHORT).show();
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            ComponentName thisWidget = new ComponentName(this, StackWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            Intent updates = new Intent();
            updates.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            updates.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);
            super.sendBroadcast(updates);
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

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
        }

    }
