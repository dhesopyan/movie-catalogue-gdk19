/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.favoritemovies.adapter;

import android.app.Activity;
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
import com.dicoding.dhe.favoritemovies.listener.CustomOnItemClickListener;
import com.dicoding.dhe.favoritemovies.activity.DetailActivity;
import com.dicoding.dhe.favoritemovies.R;
import com.dicoding.dhe.favoritemovies.entity.MovieItem;

import java.util.ArrayList;

import static com.dicoding.dhe.favoritemovies.provider.DatabaseContract.CONTENT_URI;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder> {
    private final ArrayList<MovieItem> listMovies = new ArrayList<>();
    private Activity activity;

    public FavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<MovieItem> getListMovies() {
        return listMovies;
    }

    public void setListMovies(ArrayList<MovieItem> listMovies) {
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
                Intent intent = new Intent(activity, DetailActivity.class);
                Uri uri = Uri.parse(CONTENT_URI + "/" + getListMovies().get(position).getId());
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvRemarks, tvShowtime;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            tvName = itemView.findViewById(R.id.txt_name);
            tvRemarks = itemView.findViewById(R.id.txt_description);
            tvShowtime = itemView.findViewById(R.id.txt_showtime);
        }

        void bind(MovieItem movieItems){
            Glide.with(imgPhoto).load("https://image.tmdb.org/t/p/w92"+movieItems.getImages())
                    .thumbnail(Glide.with(imgPhoto).load(R.drawable.placeholder))
                    .fitCenter()
                    .into(imgPhoto);
            tvName.setText(movieItems.getTitle());
            tvRemarks.setText(movieItems.getDescription());
            tvShowtime.setText(movieItems.getDate());
        }
    }
}
