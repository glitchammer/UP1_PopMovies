package com.example.android.up1_popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Parcelable;
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

    /**  replace with your own theMovieDB.org API key  */
    public static final String API_KEY    = "ce73a701c02ec5881c476758e26f5169";

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
