package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import es.glitch.and.bugs.popmovies.data.MovieContract;
import timber.log.Timber;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieThumbnailVH> implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesAdapter.class.getName();

    private static final int LOADER_FAVORITES = 10;

    private final MainActivity mainActivity;
    private TheMovieDBClient movieDBClient;

    // list of movies - init empty list
    private List<Movie> movies = new ArrayList<Movie>();
    private int pageIndex = 1;

    private String mode;
    private boolean isLoading;


    public MoviesAdapter(TheMovieDBClient movieDBClient, MainActivity mainActivity) {
        this.movieDBClient = movieDBClient;
        this.mainActivity = mainActivity;
    }

    public void loadMoviesMostPopular() {
        setLoading(true);
        pageIndex = 1;
        setMovies(new ArrayList<Movie>());
        mode = TheMovieDBClient.MOST_POPULAR;
        new LoadMoviesTask().execute(mode, String.valueOf(pageIndex));
    }

    public void loadMoviesHighestRated() {
        setLoading(true);
        pageIndex = 1;
        setMovies(new ArrayList<Movie>());
        mode = TheMovieDBClient.HIGHEST_RATED;
        new LoadMoviesTask().execute(mode, String.valueOf(pageIndex));
    }

    public void loadMoviesFavorites() {

        setLoading(false);
        mode=null;
        setMovies(new ArrayList<Movie>());

        mainActivity.getSupportLoaderManager().initLoader(LOADER_FAVORITES, null, this).forceLoad();

    }


    public void loadMoreMovies() {
        if (mode==null) return;
        setLoading(true);
        pageIndex++;
        new LoadMoviesTask().execute(mode, String.valueOf(pageIndex));
    }



    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movie> getMovies() {
        return movies;
    }


    private void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    /**
     * @return true if movies are currently loading
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * revalidate to make sure the list of movies is in sync with the actual data.
     * Currently, only used for back navigation scenario after user returned from detail view. See scenario in the comments.
     */
    public void revalidate() {

        // scenario: user looks at her favorite movies, selects one of them (DisplayMovieDetailsActivity), removes it from favorites and clicks back button.
        //           in this case, the movie is still there until some "refresh" happens. so we need to revalidate the movies adapter
        if (mode==null) {
            // favorite movies are showing from contentprovider db.
            // reload
            loadMoviesFavorites();
        }

    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mainActivity,
                MovieContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
                );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        List<Movie> listMovies = new ArrayList<Movie>();

        for (int i=0; i<cursor.getCount(); i++) {

            cursor.moveToPosition(i);

            Movie movie = new Movie(cursor.getLong(cursor.getColumnIndex(MovieContract.MoviesEntry._ID)));

            movie.title        = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE));
            movie.thumbnailUrl = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_THUMBNAIL));
            movie.posterUrl    = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER));
            movie.backdropUrl  = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_BACKDROP));
            movie.releaseDate  = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE));
            movie.plotSynopsis = cursor.getString(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS));
            movie.voteCount    = cursor.getInt(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_CNT));
            movie.voteAvg      = cursor.getDouble(cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_VOTE_AVG));

            listMovies.add(movie);

        }

        setMovies(listMovies);
        setLoading(false);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // not to be implemented
    }

    public class LoadMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private boolean exceptionOccurred = false;
        private String mode;

        @Override
        protected List<Movie> doInBackground(String... params) {

            String sortBy = params[0];
            int pageIndex = Integer.parseInt(params[1]);

            List<Movie> movies = new ArrayList<Movie>();

            try {
                movies = movieDBClient.getTopMovies(sortBy, pageIndex);

            } catch (IOException e) {
                Log.e(TAG, "failed to connect to themoviedb.org", e);
                exceptionOccurred = true;

            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> moviesToBeAppended) {

            List<Movie> currentMovies = getMovies();
            currentMovies.addAll(moviesToBeAppended);
            setMovies(currentMovies);

            setLoading(false);

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
                    mainActivity.onMovieItemSelected(imgMovie, movie);
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
