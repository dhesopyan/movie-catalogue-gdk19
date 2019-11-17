/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.adapter.SectionsPageAdapter;
import com.dicoding.dhe.moviecatalog.fragment.SearchMovieFragment;
import com.dicoding.dhe.moviecatalog.fragment.SearchTvFragment;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;

import io.paperdb.Paper;

public class SearchActivity extends AppCompatActivity {
    String language, movie, tv, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        movie =  getString(R.string.movie);
        tv = getString(R.string.television);

        ViewPager mViewPager =  findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#BBBBBB"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(mViewPager);

        Paper.init(this);

        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));

    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        tv = resources.getString(R.string.television);
        movie = resources.getString(R.string.movie);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resources.getString(R.string.search));
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    private void setupViewPager(ViewPager viewPager) {
        Paper.init(this);
        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");
        updateView((String)Paper.book().read("language"));

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchMovieFragment(), movie);
        adapter.addFragment(new SearchTvFragment(), tv);
        viewPager.setAdapter(adapter);
    }

}
