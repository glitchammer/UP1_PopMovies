package com.example.android.up1_popmovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();


    /**  replace with your own theMovieDB.org API key  */
    public static final String API_KEY = "ce73a701c02ec5881c476758e26f5169";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TheMovieDBClient movieDBClient = new TheMovieDBClient(API_KEY);

        // TODO: 01/04/2017: clarify, I don't want to pass the activity "back reference" spaghetti code...
        MoviesAdapter moviesAdapter = new MoviesAdapter(movieDBClient, this);

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
            return true;
        }
        if (itemThatWasClickedId == R.id.action_topHighestRated) {
            Context context = MainActivity.this;
            String textToShow = "Load highest rated movies";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
