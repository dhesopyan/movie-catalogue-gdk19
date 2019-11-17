/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dicoding.dhe.moviecatalog.activity.FavoriteDetailActivity;
import com.dicoding.dhe.moviecatalog.listener.CustomOnItemClickListener;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.model.Movie;
import com.dicoding.dhe.moviecatalog.util.DateTime;

import java.util.ArrayList;

import static com.dicoding.dhe.moviecatalog.provider.DatabaseContract.CONTENT_URI;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.MovieViewHolder> {
    private final ArrayList<Movie> listMovies = new ArrayList<>();
    private Activity activity;
    Context context;

    public MovieFavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Movie> getListMovies() {
        return listMovies;
    }

    public void setListMovies(ArrayList<Movie> listMovies) {
        this.listMovies.clear();
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.bind(listMovies.get(position));
        movieViewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FavoriteDetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + getListMovies().get(position).getMovieId());
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount()  {
        return listMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, overview, showtime;
        ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_name);
            overview = itemView.findViewById(R.id.txt_description);
            imageView = itemView.findViewById(R.id.img_photo);
            showtime = itemView.findViewById(R.id.txt_showtime);
        }

        void bind(Movie movieItems) {
            Glide.with(imageView).load("https://image.tmdb.org/t/p/w92" + movieItems.getImages())
                    .thumbnail(Glide.with(imageView).load(R.drawable.img_load))
                    .fitCenter()
                    .into(imageView);
            title.setText(movieItems.getName());
            overview.setText(movieItems.getDescription());
            showtime.setText(DateTime.getShortDate(movieItems.getShowtime()));
        }
    }
}
