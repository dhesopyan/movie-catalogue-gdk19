/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.activity;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dicoding.dhe.favoritemovies.R;
import com.dicoding.dhe.favoritemovies.database.FavoriteHelper;
import com.dicoding.dhe.favoritemovies.helper.LocaleHelper;
import com.dicoding.dhe.favoritemovies.provider.FavoriteModel;
import com.dicoding.dhe.favoritemovies.util.DateTime;

import io.paperdb.Paper;

import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {
    ImageView iv_backdrop, iv_poster;
    TextView tv_release_date, tv_vote, tv_overview, popularity, language;
    ImageView iv_favorite;

    FavoriteHelper favoriteHelper;

    private FavoriteModel item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        iv_backdrop = findViewById(R.id.iv_backdrop);
        iv_poster = findViewById(R.id.iv_poster);
        tv_release_date = findViewById(R.id.text_release);
        tv_vote = findViewById(R.id.text_rating);
        tv_overview = findViewById(R.id.text_overview);
        popularity = findViewById(R.id.text_popularity);
        language = findViewById(R.id.text_language);
        iv_favorite = findViewById(R.id.iv_favorite);

        Paper.init(this);
        updateView((String)Paper.book().read("language"));


        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) item = new FavoriteModel(cursor);
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
                Toast.makeText(DetailActivity.this,getString(R.string.dislike)+ ' ' + item.getTitle(),Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void storeData() {
        if (item == null) return;

        Glide.with(this)
                .load(item.getBackdropPath())
                .thumbnail(Glide.with(this).load(R.drawable.placeholder))
                .into(iv_backdrop);

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                .thumbnail(Glide.with(this).load(R.drawable.placeholder))
                .into(iv_poster);

        tv_release_date.setText(DateTime.getLongDate(item.getReleaseDate()));
        tv_vote.setText(String.valueOf(item.getVoteAverage()));
        tv_overview.setText(item.getOverview());
        popularity.setText(String.valueOf(item.getPopularity()));
        language.setText(item.getLanguage());
    }

    private void FavoriteRemove() {
        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        this.getContentResolver().query(
                Uri.parse(CONTENT_URI + "/delete" + "/" + String.valueOf(item.getId())), null,null,null,null
        );
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        tv_vote.setText(resources.getText(R.string.rating));
        language.setText(resources.getText(R.string.language));
        tv_release_date.setText(resources.getText(R.string.release));
        popularity.setText(resources.getText(R.string.popularity));
        tv_overview.setText(resources.getText(R.string.overview));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resources.getString(R.string.app_name));
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

}
