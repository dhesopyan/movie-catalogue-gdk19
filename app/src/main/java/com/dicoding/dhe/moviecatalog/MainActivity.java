/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dicoding.dhe.moviecatalog.activity.FavoriteActivity;
import com.dicoding.dhe.moviecatalog.activity.SearchActivity;
import com.dicoding.dhe.moviecatalog.activity.SettingsActivity;
import com.dicoding.dhe.moviecatalog.adapter.SectionsPageAdapter;
import com.dicoding.dhe.moviecatalog.fragment.MovieFragment;
import com.dicoding.dhe.moviecatalog.fragment.TvFragment;
import com.dicoding.dhe.moviecatalog.helper.LocaleHelper;
import com.dicoding.dhe.moviecatalog.model.MainViewModel;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {
    String language, movie, tv, search;
    FirebaseJobDispatcher mDispatcher;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie =  getString(R.string.movie);
        tv = getString(R.string.television);
        search = getString(R.string.search);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        ViewPager mViewPager =  findViewById(R.id.container);
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
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this,lang);
        Resources resources = context.getResources();
        tv = resources.getString(R.string.television);
        search = resources.getString(R.string.search);
        movie = resources.getString(R.string.movie);
        if (getSupportActionBar() != null) {
        getSupportActionBar().setTitle(resources.getString(R.string.app_name));
        getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case (R.id.action_english):
                    if(isOnline()) {
                        if (!((String) Paper.book().read("language")).equals((String) "en")) {
                            Paper.book().write("language", "en");
                            updateView((String) Paper.book().read("language"));
                            recreate();
                        }
                        Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_LONG).show();
                    }
                    break;
                case (R.id.action_indonesia):
                    if(isOnline()) {
                        if (!((String) Paper.book().read("language")).equals((String) "in")) {
                            Paper.book().write("language", "in");
                            updateView((String) Paper.book().read("language"));
                            recreate();
                        }
                        Toast.makeText(getApplicationContext(), "Bahasa indonesia", Toast.LENGTH_LONG).show();
                    }
                    break;
                case (R.id.action_favorit):
                        Intent favoritIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                        startActivity(favoritIntent);
                    break;
                case (R.id.action_search):
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
                    break;
                case (R.id.action_settings):
                    Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;
            }
            return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        Paper.init(this);
        language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language","en");
        updateView((String)Paper.book().read("language"));

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new MovieFragment(), movie);
        adapter.addFragment(new TvFragment(), tv);
        viewPager.setAdapter(adapter);
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
                Toast.makeText(this, getText(R.string.no_internet), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}