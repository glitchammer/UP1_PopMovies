package com.example.android.up1_popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import com.example.android.up1_popmovies.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    /**
     * please declare your own theMovieDB.org API key in your local gradle.properties file as
     * THE_MOVIE_DB_API_KEY="<your key>"
     * This concept was derived from http://stackoverflow.com/questions/33134031/is-there-a-safe-way-to-manage-api-keys/34021467#34021467
     */
    public static final String API_KEY    = BuildConfig.THE_MOVIE_DB_API_KEY;

    private static final String MOVIE_KEY = "CURRENT_LIST_OF_MOVIES";

    private MoviesAdapter moviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TheMovieDBClient movieDBClient = new TheMovieDBClient(API_KEY, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

        moviesAdapter = new MoviesAdapter(movieDBClient, this);
        if (savedInstanceState != null)
        {
            List<Movie> listOfMovies = (List<Movie>) savedInstanceState.get(MOVIE_KEY);
            moviesAdapter.setMovies(listOfMovies);
        } else {
            moviesAdapter.loadMoviesMostPopular();
        }

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);

        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));

        rvMovies.setAdapter(moviesAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_topMostPopular) {
            Context context = MainActivity.this;
            String textToShow = "Load most popular movies";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            moviesAdapter.loadMoviesMostPopular();
            return true;
        }
        else if (itemThatWasClickedId == R.id.action_topHighestRated) {
            Context context = MainActivity.this;
            String textToShow = "Load highest rated movies";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            moviesAdapter.loadMoviesHighestRated();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_KEY, (ArrayList<? extends Parcelable>) moviesAdapter.getMovies());
    }
}
