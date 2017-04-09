package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieThumbnailVH> {

    private static final String TAG = MoviesAdapter.class.getName();

    private final MainActivity mainActivity;
    private TheMovieDBClient movieDBClient;

    // list of movies - init empty list
    private List<Movie> movies = new ArrayList<Movie>();


    public MoviesAdapter(TheMovieDBClient movieDBClient, MainActivity mainActivity) {
        this.movieDBClient = movieDBClient;
        this.mainActivity  = mainActivity;
    }

    public void loadMoviesMostPopular() {
        setMovies(new ArrayList<Movie>());
        new LoadMoviesTask().execute(TheMovieDBClient.MOST_POPULAR);
    }

    public void loadMoviesHighestRated() {
        setMovies(new ArrayList<Movie>());
        new LoadMoviesTask().execute(TheMovieDBClient.HIGHEST_RATED);
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public class LoadMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private boolean exceptionOccurred = false;

        @Override
        protected List<Movie> doInBackground(String... params) {

            String sortBy = params[0];

            List<Movie> movies = new ArrayList<Movie>();

            try {
                movies = movieDBClient.getTopMovies(sortBy);

            } catch (IOException e) {
                Log.e(TAG, "failed to connect to themoviedb.org", e);
                exceptionOccurred = true;

            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            setMovies(movies);

            if (exceptionOccurred) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("No Connection to theMovieDB.org")
                        .setMessage("Cannot load movie data. Please check your internet connection.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //tv.setText(input.getEditableText().toString());
                                Toast.makeText(mainActivity, "Cannot connect to theMovieDB.org", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
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

        private ImageView imgMovie;
        private Movie movie;

        public MovieThumbnailVH(View itemView) {
            super(itemView);
            imgMovie = (ImageView) itemView.findViewById(R.id.img_movie);

            imgMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent displayMovieDetails = new Intent(mainActivity, DisplayMovieDetailsActivity.class);
                    displayMovieDetails.putExtra("id", movie.id);
                    mainActivity.startActivity(displayMovieDetails);
                }
            });

        }

        public void bind(Movie movie) {
            this.movie = movie;
            Picasso.with(mainActivity)
                    .load(movie.thumbnailUrl)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_missing)
                    .into(imgMovie);
        }
    }

}