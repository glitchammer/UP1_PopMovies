package es.glitch.and.bugs.popmovies;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayMovieDetailsActivity extends AppCompatActivity {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.imgMoviePoster) ImageView imgMoviePoster;
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
                .into(imgMoviePoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        appBarLayout.setBackground(imgMoviePoster.getDrawable());
                    }
                    @Override
                    public void onError() {
                        // ignore
                    }
                });
//        imgMoviePoster.setImageBitmap(ImageUtils.getImage(movie.imgDataThumbnail));

        toolbar.setTitle(movie.title);
//        toolbar.setBackground();
        setSupportActionBar(toolbar);

        txtTitle.setText(movie.title);
        txtReleaseDate.setText(movie.releaseDate);
        txtRating.setText(String.format("%.1f/10 (%d votes)", movie.voteAvg, movie.voteCount));
        txtSynopsis.setText(movie.plotSynopsis);

    }

    private void updateFavorite(MenuItem item) {
        int img = movie.isFavorite? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp;
        item.setIcon(img);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateFavorite(menu.findItem(R.id.toggleFavorite));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id==R.id.toggleFavorite) {

            movie.isFavorite = !movie.isFavorite;

            updateFavorite(item);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
