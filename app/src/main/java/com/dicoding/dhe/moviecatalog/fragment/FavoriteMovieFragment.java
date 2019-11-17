/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicoding.dhe.moviecatalog.LoadNotesCallback;
import com.dicoding.dhe.moviecatalog.helper.FavoriteHelper;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.adapter.MovieFavoriteAdapter;
import com.dicoding.dhe.moviecatalog.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.dicoding.dhe.moviecatalog.helper.MappingHelper.mapCursorToArrayList;
import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.CONTENT_URI;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadNotesCallback {
    RecyclerView rvMovie;
    private MovieFavoriteAdapter movieAdapter;
    private DataObserver myObserver;


    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        rvMovie = rootView.findViewById(R.id.lv_list);
        movieAdapter = new MovieFavoriteAdapter(getActivity());
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(movieAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, getActivity());
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        new getData(getActivity(), this).execute();

        return rootView;
    }

    @Override
    public void postExecute(Cursor notes) {
        if(notes.getCount() != 0) {
            ArrayList<Movie> listNotes = mapCursorToArrayList(notes);
            if (listNotes.size() > 0) {
                movieAdapter.setListMovies(listNotes);
            } else {
                movieAdapter.setListMovies(new ArrayList<Movie>());
            }
        } else {
            movieAdapter.setListMovies(new ArrayList<Movie>());
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;


        private getData(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            FavoriteHelper favoriteHelper = new FavoriteHelper(weakContext.get());
            favoriteHelper.open();
            return favoriteHelper.queryByTypeProvider("movie");
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (LoadNotesCallback) this).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getData(getActivity(), this).execute();
    }

}