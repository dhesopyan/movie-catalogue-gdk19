/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.adapter.MovieAdapter;
import com.dicoding.dhe.moviecatalog.model.MainViewModel;
import com.dicoding.dhe.moviecatalog.model.Movie;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMovieFragment extends Fragment {
    private RecyclerView rvCategory;
    private MovieAdapter adapter;
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    public String language, locale;

    public SearchMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_movie, container, false);

        progressBar = rootView.findViewById(R.id.progressBarSearch);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getMovies().observe(this, getMovies);

        Paper.init(getActivity());
        locale = Paper.book().read("language");
        language = locale.equals("in") ? "id" : "en";

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();

        rvCategory = rootView.findViewById(R.id.lv_list);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCategory.setAdapter(adapter);

        SearchView simpleSearchView = rootView.findViewById(R.id.search); // inititate a search view

        // perform set on query text listener event
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            // do something on text submit
                mainViewModel.getMovie(query, language);
                showLoading(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
            // do something when text changes
                return false;
            }
        });

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

}

