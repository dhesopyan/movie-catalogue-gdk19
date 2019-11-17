/*
 *  * Created by ade supyan abdul aziz.
 *  * Copyright (c) 2019. All rights reserved.
 *  * Last modified 25/08/19 21:54 PM.
 */

package com.dicoding.dhe.moviecatalog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.dicoding.dhe.moviecatalog.activity.DetailMovieActivity;
import com.dicoding.dhe.moviecatalog.listener.CustomOnItemClickListener;
import com.dicoding.dhe.moviecatalog.model.Movie;
import com.dicoding.dhe.moviecatalog.R;
import com.dicoding.dhe.moviecatalog.util.DateTime;

import java.util.ArrayList;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> mData = new ArrayList<>();
    private Context context;
    private Activity activity;
    private String locale;

    public MovieAdapter(Activity activity) {
        this.activity = activity;
    }

    /**
     * Gunakan method ini jika semua datanya akan diganti
     *
     * @param items kumpulan data baru yang akan mengganti semua data yang sudah ada
     */

    public void setData(Context context, ArrayList<Movie> items) {
        this.context = context;
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int position) {
        locale = (String) Paper.book().read("language");
        movieViewHolder.bind(mData.get(position));
        movieViewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                makeText(activity, mData.get(position).getName(), LENGTH_SHORT).show();
                Intent movieIntent = new Intent(activity, DetailMovieActivity.class);
                movieIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE, mData.get(position));
                movieIntent.putExtra(DetailMovieActivity.EXTRA_LANGUAGE, locale);
                activity.startActivity(movieIntent);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @GlideModule
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

        void bind(Movie movieItems){
            Glide.with(imgPhoto).load("https://image.tmdb.org/t/p/w92"+movieItems.getImages())
                    .thumbnail(Glide.with(imgPhoto).load(R.drawable.img_load))
                    .fitCenter()
                    .into(imgPhoto);
            tvName.setText(movieItems.getName());
            tvRemarks.setText(movieItems.getDescription());
            tvShowtime.setText(DateTime.getShortDate(movieItems.getShowtime()));
        }
    }
}