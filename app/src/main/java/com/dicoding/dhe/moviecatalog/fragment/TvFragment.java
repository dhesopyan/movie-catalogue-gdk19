/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.dicoding.dhe.moviecatalog.model.MainViewModel;
import com.dicoding.dhe.moviecatalog.model.Movie;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.adapter.MovieAdapter;

import java.util.ArrayList;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {
        private RecyclerView rvCategory;
        private MovieAdapter adapter;
        private ArrayList<Movie> movies = new ArrayList<>();
        private MainViewModel mainViewModel;
        private ProgressBar progressBar;
        public String language, locale;

        public TvFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tv, container, false);

            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBarTv);
            mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            mainViewModel.getMovies().observe(this, getMovies);

            Paper.init(getActivity());
            locale = (String) Paper.book().read("language");
            language = locale.equals("in") ? "id" : "en";

            adapter = new MovieAdapter(getActivity());
            adapter.notifyDataSetChanged();

            rvCategory = rootView.findViewById(R.id.lv_list);
            rvCategory.setHasFixedSize(true);
            rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCategory.setAdapter(adapter);

            showLoading(true);
            return rootView;
        }

        public Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movieItems) {
                if (movieItems != null) {
                    adapter.setData(getContext(),movieItems);
                    showLoading(false);
                }
            }
        };

        private void showLoading(Boolean state) {
            if (state) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }

    private BroadcastReceiver networkStateReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            doSomethingOnNetworkChange(ni);
        }

        private void doSomethingOnNetworkChange(NetworkInfo ni) {
            if(ni == null) {
                showLoading(false);
            } else {
                showLoading(true);
            }
            mainViewModel.setTv(language);
            if(!movies.isEmpty()) {
                showLoading(false);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

}
