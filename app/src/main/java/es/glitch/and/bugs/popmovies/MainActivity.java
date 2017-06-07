package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();


    private static final String CURRENT_LIST_OF_MOVIES = "CURRENT_LIST_OF_MOVIES";
    private static final String CURRENT_MODE = "MODE";

    private MoviesAdapter moviesAdapter;
    private GridLayoutManager layoutManager;

    @BindView(R.id.rv_movies) RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init timber
        Timber.plant(new Timber.DebugTree());
        Timber.i("timber initialized. first log statement.");

        // stetho
        Stetho.Initializer initStetho = Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build();
        Stetho.initialize(initStetho);

        ButterKnife.bind(this);

//        TabHost.TabContentFactory tcf = new TabHost.TabContentFactory(){
//            @Override
//            public View createTabContent(String tag) {
//                return rvMovies;
//            }
//        };
//
//        TabHost.TabSpec tabSpecMostPopular  = tabHost.newTabSpec("mostPopular").setIndicator("Most Popular").setContent(tcf);
//        TabHost.TabSpec tabSpecHighestRated = tabHost.newTabSpec("highestRated").setIndicator("Highest Rated").setContent(tcf);
//        TabHost.TabSpec tabSpecFavorites    = tabHost.newTabSpec("favorites").setIndicator("Favorites").setContent(tcf);
//
//        tabHost.setup();
//
//        tabHost.addTab(tabSpecMostPopular);
//        tabHost.addTab(tabSpecHighestRated);
//        tabHost.addTab(tabSpecFavorites);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Adding Tabs
//        for (String tab_name : tabs) {
//            actionBar.addTab(actionBar.newTab().setText(tab_name)
//                    .setTabListener(this));
//        }

//        TheMovieDBClient movieDBClient = new TheMovieDBClient(API_KEY, this);
        TheMovieDBClient movieDBClient = new TheMovieDBClient();

        moviesAdapter = new MoviesAdapter(movieDBClient, this);
        if (savedInstanceState != null) {
            List<Movie> listOfMovies = (List<Movie>) savedInstanceState.get(CURRENT_LIST_OF_MOVIES);
            moviesAdapter.setMovies(listOfMovies);
            moviesAdapter.setMode(savedInstanceState.getString(CURRENT_MODE));
        } else {
            moviesAdapter.loadMoviesMostPopular();
        }


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
//                    Toast.makeText(MainActivity.this, "Load more....", Toast.LENGTH_SHORT).show();
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

        int itemId = item.getItemId();
        Context context = MainActivity.this;

        if (itemId == R.id.action_topMostPopular) {
            Toast.makeText(context, "Load most popular movies", Toast.LENGTH_SHORT).show();
            moviesAdapter.loadMoviesMostPopular();
            return true;
        } else if (itemId == R.id.action_topHighestRated) {
            Toast.makeText(context, "Load highest rated movies", Toast.LENGTH_SHORT).show();
            moviesAdapter.loadMoviesHighestRated();
            return true;
        } else if (itemId == R.id.action_favorites) {
            Toast.makeText(context, "Load favorite movies", Toast.LENGTH_SHORT).show();
            moviesAdapter.loadMoviesFavorites();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(CURRENT_LIST_OF_MOVIES, (ArrayList<? extends Parcelable>) moviesAdapter.getMovies());
        outState.putString(CURRENT_MODE, moviesAdapter.getMode());
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

    @Override
    protected void onResume() {
        super.onResume();

        // scenario: user looks at her favorite movies, selects one of them (DisplayMovieDetailsActivity), removes it from favorites and clicks back button.
        //           in this case, the movie is still there until some "refresh" happens. so we need to revalidate the movies adapter

        moviesAdapter.revalidate();

    }
}
