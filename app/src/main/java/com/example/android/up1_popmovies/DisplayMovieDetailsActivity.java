package com.example.android.up1_popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        Intent intent = getIntent();
        long movieID  = intent.getExtras().getLong("id");
        Movie movie   = Movie.MOVIES_CACHE.get(movieID);

        ImageView imgMoviePoster = (ImageView) findViewById(R.id.imgMoviePoster);
        TextView txtTitle        = (TextView)  findViewById(R.id.txtTitle);
        TextView txtReleaseDate  = (TextView)  findViewById(R.id.txtReleaseDate);
        TextView txtRating       = (TextView)  findViewById(R.id.txtRating);
        TextView txtSynopsis     = (TextView)  findViewById(R.id.txtSynopsis);

        Picasso.with(this).load(movie.posterUrl.toString()).into(imgMoviePoster);

        txtTitle.setText(movie.title);
        txtReleaseDate.setText(movie.releaseDate);
        txtRating.setText(String.format("%.1f / 10, %d votes in totoal", movie.voteAvg, movie.voteCount));
        txtSynopsis.setText(movie.plotSynopsis);

    }
}
