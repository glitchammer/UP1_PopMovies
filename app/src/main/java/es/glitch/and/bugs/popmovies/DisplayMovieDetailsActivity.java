package es.glitch.and.bugs.popmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DisplayMovieDetailsActivity extends AppCompatActivity implements
        LoaderCallbacks<List<? extends Object>> {


    private static final int LOADER_REVIEWS = 10;
    private static final int LOADER_TRAILERS = 20;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout appBarLayout;

    @BindView(R.id.ivBackdrop) ImageView ivBackdrop;
    @BindView(R.id.ivMoviePoster) ImageView ivMoviePoster;
    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtReleaseDate) TextView txtReleaseDate;
    @BindView(R.id.txtRating) TextView txtRating;
    @BindView(R.id.txtSynopsis) TextView txtSynopsis;

    @BindView(R.id.containerReviews) LinearLayout containerReviews;

    private Movie movie;

//    // picasso needs a strong reference for the target
//    private Target mTargetBackdropHandler = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//            appBarLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
//        }
//
//        @Override
//        public void onBitmapFailed(Drawable errorDrawable) {
//            Timber.w("onBitmapFailed");
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//            Timber.d("onPrepareLoad: "+movie.backdropUrl);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        // get movie data
        Intent intent = getIntent();
        // this was suggested by a reviewer.
        // it does not help a thing actually, because when intent or extras are not given, my app does not work
        // can't do magic here. what do you prefer? empty screen or a crashing app? .... this belongs to the field of philosophies i guess.
        if (intent == null || intent.getExtras() == null) return;

        long movieID = intent.getExtras().getLong("id");
        movie = Movie.MOVIES_CACHE.get(movieID);

        // bind ui resources
        ButterKnife.bind(this);

        // load ui content
        Picasso.with(this)
                .load(movie.posterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(ivMoviePoster);

        Picasso.with(this)
                .load(movie.backdropUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_missing)
                .into(ivBackdrop);


//        ivMoviePoster.setImageBitmap(ImageUtils.getImage(movie.imgDataThumbnail));

        toolbar.setTitle(movie.title);
//        toolbar.setBackground();
        setSupportActionBar(toolbar);

        txtTitle.setText(movie.title);
        txtReleaseDate.setText(movie.releaseDate);
        txtRating.setText(String.format("%.1f/10 (%d votes)", movie.voteAvg, movie.voteCount));
        txtSynopsis.setText(movie.plotSynopsis);

        // load reviews
        getSupportLoaderManager().initLoader(LOADER_REVIEWS, intent.getExtras(), this).forceLoad();
        getSupportLoaderManager().initLoader(LOADER_TRAILERS, intent.getExtras(), this).forceLoad();

    }

    private void updateFavorite(MenuItem item) {
        int img = movie.isFavorite ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp;
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

        if (id == R.id.toggleFavorite) {

            movie.isFavorite = !movie.isFavorite;

            updateFavorite(item);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<? extends Object>> onCreateLoader(int id, final Bundle args) {
        if (id == LOADER_REVIEWS) {
            return new AsyncTaskLoader<List<? extends Object>>(DisplayMovieDetailsActivity.this) {
                @Override
                public List<? extends Object> loadInBackground() {
                    long movieId = args.getLong("id");
                    List<Review> reviews = null;
                    try {
                        reviews = TheMovieDBClient.loadReviews(movieId);
                    } catch (IOException e) {
                        Timber.e(e, "Cannot load review data");
                        Toast.makeText(DisplayMovieDetailsActivity.this, "Error, cannot load reviews", Toast.LENGTH_SHORT).show();
                    }
                    return reviews;
                }
            };
        } else if (id == LOADER_TRAILERS) {
            return new AsyncTaskLoader<List<? extends Object>>(DisplayMovieDetailsActivity.this) {
                @Override
                public List<? extends Object> loadInBackground() {
                    long movieId = args.getLong("id");
                    List<Trailer> trailers = null;
                    try {
                        trailers = TheMovieDBClient.loadTrailers(movieId);
                    } catch (IOException e) {
                        Timber.e(e, "Cannot load trailer data");
                        Toast.makeText(DisplayMovieDetailsActivity.this, "Error, cannot load trailers", Toast.LENGTH_SHORT).show();
                    }
                    return trailers;
                }
            };
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<? extends Object>> loader, List<? extends Object> data) {

        int loaderId = loader.getId();

        if (loaderId==LOADER_REVIEWS) {

            for (Review review: ((List<Review>) data)) {

                View view = LayoutInflater.from(this).inflate(R.layout.review, containerReviews, false);

                TextView txtAuthorName = (TextView) view.findViewById(R.id.txtAuthorName);
                TextView txtReview     = (TextView) view.findViewById(R.id.txtReview);

                txtAuthorName.setText(review.author);
                txtReview.setText(review.content);

                containerReviews.addView(view);
            }

            containerReviews.getRootView().invalidate();

        }
        else if (loaderId==LOADER_TRAILERS) {

        }

    }

    @Override
    public void onLoaderReset(Loader<List<? extends Object>> loader) {

    }

}
