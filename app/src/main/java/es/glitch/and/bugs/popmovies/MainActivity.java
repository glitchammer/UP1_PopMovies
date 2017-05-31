package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();


    private static final String MOVIE_KEY = "CURRENT_LIST_OF_MOVIES";

    private MoviesAdapter moviesAdapter;
    private GridLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init timber
        Timber.plant(new Timber.DebugTree());
        Timber.i("timber initialized. first log statement.");

//        TheMovieDBClient movieDBClient = new TheMovieDBClient(API_KEY, this);
        TheMovieDBClient movieDBClient = new TheMovieDBClient();

        moviesAdapter = new MoviesAdapter(movieDBClient, this);
        if (savedInstanceState != null) {
            List<Movie> listOfMovies = (List<Movie>) savedInstanceState.get(MOVIE_KEY);
            moviesAdapter.setMovies(listOfMovies);
        } else {
            moviesAdapter.loadMoviesMostPopular();
        }

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
        layoutManager = new GridLayoutManager(this, 3);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setAdapter(moviesAdapter);

        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public int visibleThreshold = 5;
            public int lastVisibleItem;
            public int totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (moviesAdapter.isLoading()) return;

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {


                    Timber.d("load more.... (dx,dy): (" + dx + "," + dy + ")");
                    Toast.makeText(MainActivity.this, "Load more....", Toast.LENGTH_SHORT).show();
                    moviesAdapter.loadMoreMovies();

                }

            }

        });

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
        } else if (itemThatWasClickedId == R.id.action_topHighestRated) {
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

    public void onMovieItemSelected(ImageView imgMovie, Movie movie) {

        Intent displayMovieDetails = new Intent(this, DisplayMovieDetailsActivity.class);
        displayMovieDetails.putExtra("id", movie.id);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, imgMovie, "moviePoster");
        startActivity(displayMovieDetails, options.toBundle());

//        Intent displayMovieDetails = new Intent(this, ScrollingActivity.class);
//        startActivity(displayMovieDetails);

    }
}
