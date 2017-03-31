package com.example.android.up1_popmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieThumbnailVH> {

    private static final String TAG = MoviesAdapter.class.getName();

    private TheMovieDBClient movieDBClient;

    // list of movies - init empty list
    private List<Movie> movies = new ArrayList<Movie>();


    public MoviesAdapter(TheMovieDBClient movieDBClient) {
        this.movieDBClient = movieDBClient;
        updateMovies();
    }

    public void updateMovies() {
        new LoadMoviesTask().execute(TheMovieDBClient.SORT_POPULARITY_DESC);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }



    public class LoadMoviesTask extends AsyncTask<String, Void, List<Movie>> {


        @Override
        protected List<Movie> doInBackground(String... params) {

            String sort = params[0];

            List<Movie> movies = new ArrayList<Movie>();

            try {
                movies = movieDBClient.getTopMovies(sort);

            } catch (IOException e) {

                //TODO display message
                Log.e(TAG, "failed to connect to themoviedb.org", e);
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

        }
    }


    @Override
    public MovieThumbnailVH onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_thumbnail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MovieThumbnailVH viewHolder = new MovieThumbnailVH(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieThumbnailVH holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieThumbnailVH extends RecyclerView.ViewHolder {

        ImageView imgMovie;

        public MovieThumbnailVH(View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);
        }


        public void bind(Movie movie) {
        }
    }


}
