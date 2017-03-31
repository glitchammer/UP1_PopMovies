package com.example.android.up1_popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

        MoviesAdapter moviesAdapter = new MoviesAdapter(movieDBClient);

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);

//        rvMovies.setLayoutManager(new GridLayoutManager(this));



    }
}
