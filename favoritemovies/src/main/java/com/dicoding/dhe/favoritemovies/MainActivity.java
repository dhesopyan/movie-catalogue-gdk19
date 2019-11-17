/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.dicoding.dhe.favoritemovies.adapter.SectionsPageAdapter;
import com.dicoding.dhe.favoritemovies.fragment.MovieFragment;
import com.dicoding.dhe.favoritemovies.fragment.TvFragment;
import com.dicoding.dhe.favoritemovies.helper.LocaleHelper;

import io.paperdb.Paper;

import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    String movie, tv, str;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movie =  getString(R.string.movie);
        tv = getString(R.string.television);

        ViewPager mViewPager =  findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#BBBBBB"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(mViewPager);

        Paper.init(this);
        setLanguage();
    }

    private void setupViewPager(ViewPager viewPager) {
        Paper.init(this);
        setLanguage();
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), movie);
        adapter.addFragment(new TvFragment(), tv);
        viewPager.setAdapter(adapter);
    }

    private void setLanguage() {
        cursor = this.getContentResolver().query(Uri.parse(CONTENT_URI + "/" + "language"),null,null, null,null);
        if (cursor.moveToFirst()) {
            str = cursor.getString(cursor.getColumnIndex("language"));
        }
        Paper.book().write("language",str);
        updateView((String)Paper.book().read("language"));
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        tv = resources.getString(R.string.television);
        movie = resources.getString(R.string.movie);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resources.getString(R.string.app_name));
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = this.getContentResolver().query(Uri.parse(CONTENT_URI + "/" + "language"),null,null, null,null);
        if (cursor.moveToFirst()) {
            str = cursor.getString(cursor.getColumnIndex("language"));
            if(!str.equals(Paper.book().read("language"))) {
                Paper.book().write("language",str);
                updateView((String)Paper.book().read("language"));
                this.recreate();
            }
        }
    }

}
