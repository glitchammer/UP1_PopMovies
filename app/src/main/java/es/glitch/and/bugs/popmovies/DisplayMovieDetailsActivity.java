package es.glitch.and.bugs.popmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.imgMoviePoster) ImageView imgMoviePoster;
    @BindView(R.id.imgAddFavorite) ImageView imgAddFavorite;
    @BindView(R.id.txtTitle)       TextView txtTitle;
    @BindView(R.id.txtReleaseDate) TextView txtReleaseDate;
    @BindView(R.id.txtRating)      TextView txtRating;
    @BindView(R.id.txtSynopsis)    TextView txtSynopsis;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        // get movie data
        Intent intent = getIntent();
        // this was suggested by a reviewer.
        // it does not help a thing actually, because when intent or extras are not given, my app does not work
        // can't do magic here. what do you prefer? empty screen or a crashing app? .... this belongs to the field of philosophies i guess.
        if (intent==null || intent.getExtras()==null) return;

        long movieID  = intent.getExtras().getLong("id");
        movie   = Movie.MOVIES_CACHE.get(movieID);

        // bind ui resources
        ButterKnife.bind(this);

        // load ui content
        Picasso.with(this)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(imgMoviePoster);

        txtTitle.setText(movie.title);
        txtReleaseDate.setText(movie.releaseDate);
        txtRating.setText(String.format("%.1f/10 (%d votes)", movie.voteAvg, movie.voteCount));
        txtSynopsis.setText(movie.plotSynopsis);
        updateImgAddFavorite();

        // control
        imgAddFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // toggle fav
                movie.isFavorite = !movie.isFavorite;
                updateImgAddFavorite();
            }
        });

    }

    private void updateImgAddFavorite() {
        int img = movie.isFavorite? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp;
        imgAddFavorite.setImageResource(img);
    }
}
