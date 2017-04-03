package com.example.android.up1_popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.imgMoviePoster) ImageView imgMoviePoster;
    @BindView(R.id.txtTitle)       TextView txtTitle;
    @BindView(R.id.txtReleaseDate) TextView txtReleaseDate;
    @BindView(R.id.txtRating)      TextView txtRating;
    @BindView(R.id.txtSynopsis)    TextView txtSynopsis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        Intent intent = getIntent();

        // this was suggested by a reviewer.
        // it does not help a thing actually, because when intent or extras are not given, my app does not work
        // can't do magic here. what do you prefer? empty screen or a crashing app? .... this belongs to the field of philosophies i guess.
        if (intent==null || intent.getExtras()==null) return;

        long movieID  = intent.getExtras().getLong("id");
        Movie movie   = Movie.MOVIES_CACHE.get(movieID);

        ButterKnife.bind(this);

        Picasso.with(this)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(imgMoviePoster);

        txtTitle.setText(movie.title);
        txtReleaseDate.setText(movie.releaseDate);
        txtRating.setText(String.format("%.1f/10 (%d votes)", movie.voteAvg, movie.voteCount));
        txtSynopsis.setText(movie.plotSynopsis);

    }
}
