package com.example.android.up1_popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        Intent intent = getIntent();
        long movieID  = intent.getExtras().getLong("id");
        Movie movie   = Movie.MOVIES_CACHE.get(movieID);




    }
}
