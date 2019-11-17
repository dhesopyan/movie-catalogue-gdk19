/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
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

import com.dicoding.dhe.favoritemovies.R;
import com.dicoding.dhe.favoritemovies.adapter.FavoriteAdapter;
import com.dicoding.dhe.favoritemovies.database.FavoriteColumns;
import com.dicoding.dhe.favoritemovies.database.FavoriteHelper;
import com.dicoding.dhe.favoritemovies.entity.MovieItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.dicoding.dhe.favoritemovies.helper.MappingHelper.mapCursorToArrayList;
import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.CONTENT_URI;
import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.CONTENT_URI_TV;

public class TvFragment extends Fragment implements LoadNotesCallback {

    private FavoriteAdapter consumerAdapter;
    private DataObserver myObserver;
    private FavoriteHelper favoriteHelper;

    public TvFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        RecyclerView rvNotes = rootView.findViewById(R.id.lv_list);
        consumerAdapter = new FavoriteAdapter(getActivity());
        rvNotes.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotes.setHasFixedSize(true);
        rvNotes.setAdapter(consumerAdapter);
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
        ArrayList<MovieItem> listNotes = mapCursorToArrayList(notes);
        if (listNotes.size() > 0) {
            consumerAdapter.setListMovies(listNotes);
        } else {
            consumerAdapter.setListMovies(new ArrayList<MovieItem>());
        }
    }

    private class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadNotesCallback> weakCallback;


        private getData(Context context, LoadNotesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(Uri.parse(CONTENT_URI_TV + "/" + "tv"),null,FavoriteColumns.COLUMN_TITLE + " = ?",new String[]{"The Lion King"},null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }

    }

    class DataObserver extends ContentObserver {

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

    public void onResume() {
        super.onResume();
        new TvFragment.getData(getActivity(), this).execute();
    }

}
