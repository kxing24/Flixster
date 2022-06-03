package com.codepath.kathyxing.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.kathyxing.flixster.MovieDetailsActivity;
import com.codepath.kathyxing.flixster.R;
import com.codepath.kathyxing.flixster.databinding.ItemMovieBinding;
import com.codepath.kathyxing.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    /*
    private class ViewHolder extends RecyclerView.ViewHolder {
        ItemMovieBinding binding;
        public ViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    */

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageButton expandButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            expandButton = itemView.findViewById(R.id.expandButton);
            tvTitle.setOnClickListener(this);
            expandButton.setOnClickListener(this);
            tvOverview.setVisibility(View.GONE);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int placeholderId;
            // if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageUrl = back drop image
                imageUrl = movie.getBackdropPath();
                placeholderId = R.drawable.backdrop_placeholder;
            }
            else {
                // else imageUrl = poster image
                imageUrl = movie.getPosterPath();
                placeholderId = R.drawable.movie_placeholder;
            }

            int radius = 30;

            Glide.with(context).load(imageUrl).transform(new RoundedCorners(radius)).placeholder(placeholderId).into(ivPoster);
        }

        // when the user clicks on a row, show MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View v) {

            if(v == expandButton) {
                if(tvOverview.getVisibility() == View.GONE) {
                    expandButton.setRotation(180);
                    tvOverview.setVisibility(View.VISIBLE);
                }
                else {
                    expandButton.setRotation(0);
                    tvOverview.setVisibility(View.GONE);
                }
            }
            if (v == tvTitle) {
                // gets item position
                int position = getAbsoluteAdapterPosition();
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the movie at the position
                    Movie movie = movies.get(position);
                    // create intent for the new activity
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    // serialize the movie using parceler, use its short name as a key
                    intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                    // show the activity
                    context.startActivity(intent);
                }
            }

        }
    }
}
