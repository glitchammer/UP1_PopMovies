package es.glitch.and.bugs.popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by dnlbh on 31/03/2017.
 */

public class TheMovieDBClient {

    private static final String TAG = TheMovieDBClient.class.getName();

    public static final String MOST_POPULAR  = "/movie/popular";
    public static final String HIGHEST_RATED = "/movie/top_rated";

    public static final String PATH_REVIEWS  = "/movie/%d/reviews";
    public static final String PATH_TRAILERS = "/movie/%d/videos";


    private static final String BASE_URI = "https://api.themoviedb.org/3";

    /**
     * please declare your own theMovieDB.org API key in your local gradle.properties file as
     * THE_MOVIE_DB_API_KEY="<your key>"
     * This concept was derived from http://stackoverflow.com/questions/33134031/is-there-a-safe-way-to-manage-api-keys/34021467#34021467
     */
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;



    public List<Movie> getTopMovies(String modePath, int pageIndex) throws IOException {

        // build up query URI
        Uri prepareURI = Uri.parse(BASE_URI + modePath).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("page", Integer.toString(pageIndex))
                .build();


        // we need to make it a locator, a URL
        URL url = null;
        try {
            url = new URL(prepareURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // check internet connection first
//        //TODO for some reason this isOnline() method returns wrong results - even though it's supposed to be right practice, no?
//        if (!Utilities.isOnline(context)) throw new IOException("No internet connection");

        //
        // read from the db - make that webservice call
        //
        String searchResultsStr = IOUtils.toString(url.openStream());

        // init result list
        List<Movie> movies = new ArrayList<Movie>();

        // parse json
        try {
            JSONObject pageOne = new JSONObject(searchResultsStr);
            JSONArray results = pageOne.getJSONArray("results");

            Log.d(TAG, "no of results: " +results.length() );

            for (int i=0; i<results.length(); i++) {
                JSONObject json = results.getJSONObject(i);

                Movie movie = new Movie(json.getLong("id"));
                movie.title         = json.getString("original_title");
                movie.releaseDate   = json.getString("release_date");
                movie.plotSynopsis  = json.getString("overview");
                movie.voteAvg       = json.getDouble("vote_average");
                movie.voteCount     = json.getInt("vote_count");
                movie.thumbnailUrl  = "http://image.tmdb.org/t/p/w500" + json.getString("poster_path");
                movie.posterUrl     = "http://image.tmdb.org/t/p/w500" + json.getString("poster_path");

                movies.add(movie);
            }


        } catch (JSONException e) {
            Log.wtf(TAG, "should never happen", e);
        }

        return movies;
    }

    public static List<Review> loadReviews(long movieId) throws IOException {

        // build up query URI
        Uri prepareURI = Uri.parse(BASE_URI + String.format(PATH_REVIEWS, movieId)).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();


        // we need to make it a locator, a URL
        URL url = null;
        try {
            url = new URL(prepareURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // check internet connection first
//        //TODO for some reason this isOnline() method returns wrong results - even though it's supposed to be right practice, no?
//        if (!Utilities.isOnline(context)) throw new IOException("No internet connection");

        //
        // read from the db - make that webservice call
        //
        String reviewsJsonStr = IOUtils.toString(url.openStream());

        Timber.i("Reviews:\n"+reviewsJsonStr);

        return null;
    }

    public static List<Trailer> loadTrailers(long movieId) throws IOException {

        // build up query URI
        Uri prepareURI = Uri.parse(BASE_URI + String.format(PATH_TRAILERS, movieId)).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();


        // we need to make it a locator, a URL
        URL url = null;
        try {
            url = new URL(prepareURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // check internet connection first
//        //TODO for some reason this isOnline() method returns wrong results - even though it's supposed to be right practice, no?
//        if (!Utilities.isOnline(context)) throw new IOException("No internet connection");

        //
        // read from the db - make that webservice call
        //
        String trailersJsonStr = IOUtils.toString(url.openStream());

        Timber.i("Trailers:\n"+trailersJsonStr);

        return null;
    }

}
