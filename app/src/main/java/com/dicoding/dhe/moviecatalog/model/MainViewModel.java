/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.SystemClock;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dicoding.dhe.moviecatalog.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class MainViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<Movie>> listMovie = new MutableLiveData<>();
    private String BASE_URL = "https://api.themoviedb.org/";
    private String BASE_URL2 = "https://private-5d1b4-themoviedbmock.apiary-mock.com/";
    private int i = 0;

    public ArrayList<Movie> setMovie(final String language) {

        final ArrayList<Movie> listItems = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"3/discover/movie?api_key="+API_KEY+"&language="+language+"&sort_by=popularity.desc&include_adult=false&include_video=true&page=1")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray list = response.getJSONArray("results");

                            for (int i = 0; i < list.length(); i++) {
                                JSONObject movie = list.getJSONObject(i);
                                Movie movieItems = new Movie(movie);
                                listItems.add(movieItems);
                            }
                            listMovie.postValue(listItems);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        if(i < 6){
                        SystemClock.sleep(200);
                            if(i>=3 && i<6) {
                                BASE_URL = BASE_URL2;
                            }
                        setMovie(language);
                        }
                        i++;
                    }
                });
        return listItems;
    }

    public ArrayList<Movie> setTv(final String language) {
        final ArrayList<Movie> listItems = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"3/discover/tv?api_key="+API_KEY+"&language="+language+"&sort_by=popularity.desc&include_adult=false&include_video=true&page=1")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray list = response.getJSONArray("results");

                            for (int i = 0; i < list.length(); i++) {
                                JSONObject moview = list.getJSONObject(i);
                                Movie movieItems = new Movie(moview);
                                listItems.add(movieItems);
                            }
                            listMovie.postValue(listItems);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        if(i < 6){
                            SystemClock.sleep(200);
                            if(i>=3 && i<6) {
                                BASE_URL = BASE_URL2;
                            }
                            setTv(language);
                        }
                        i++;
                    }
                });
        return listItems;
    }

    public ArrayList<Movie> getMovie(final String keyword, final String language) {
        final ArrayList<Movie> listItems = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"3/search/movie?api_key={"+API_KEY+"}&language="+language+"&query={"+keyword+"}")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray list = response.getJSONArray("results");

                            for (int i = 0; i < list.length(); i++) {
                                JSONObject movie = list.getJSONObject(i);
                                Movie movieItems = new Movie(movie);
                                listItems.add(movieItems);
                            }
                            listMovie.postValue(listItems);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        if(i < 6){
                            SystemClock.sleep(200);
                            if(i>=3 && i<6) {
                                BASE_URL = BASE_URL2;
                            }
                            getMovie(keyword,language);
                        }
                        i++;
                    }
                });
        return listItems;
    }

    public ArrayList<Movie> getTv(final String keyword, final String language) {
        final ArrayList<Movie> listItems = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"3/search/tv?api_key={"+API_KEY+"}&language="+language+"&query={"+keyword+"}")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JSONArray list = response.getJSONArray("results");

                            for (int i = 0; i < list.length(); i++) {
                                JSONObject movie = list.getJSONObject(i);
                                Movie movieItems = new Movie(movie);
                                listItems.add(movieItems);
                            }
                            listMovie.postValue(listItems);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror
                        if(i < 6){
                            SystemClock.sleep(200);
                            if(i>=3 && i<6) {
                                BASE_URL = BASE_URL2;
                            }
                            getTv(keyword,language);
                        }
                        i++;
                    }
                });
        return listItems;
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return listMovie;
    }
}
