/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.adapter.SectionsPageAdapter;
import com.dicoding.dhe.moviecatalog.fragment.FavoriteMovieFragment;
import com.dicoding.dhe.moviecatalog.fragment.FavoriteTvFragment;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;

import io.paperdb.Paper;

public class FavoriteActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        mViewPager =  findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setSelectedTabIndicatorHeight((int) (2 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#BBBBBB"), Color.parseColor("#ffffff"));
        tabLayout.setupWithViewPager(mViewPager);

        Paper.init(this);

        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");

        updateView((String)Paper.book().read("language"));

        setActionBarTitle(getString(R.string.favorite));
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(resources.getString(R.string.app_name));
            getSupportActionBar().setElevation(0);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavoriteMovieFragment(), getString(R.string.movie));
        adapter.addFragment(new FavoriteTvFragment(), getString(R.string.television));
        viewPager.setAdapter(adapter);
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

}
