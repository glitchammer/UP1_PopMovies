package com.example.android.up1_popmovies;

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

/**
 * Created by dnlbh on 31/03/2017.
 */

public class TheMovieDBClient {

    private static final String TAG = TheMovieDBClient.class.getName();

    public static final String SORT_POPULARITY_DESC   = "popularity.desc";
    public static final String SORT_VOTE_AVERAGE_DESC = "vote_average.desc";
    private final ConnectivityManager connectivityManager;


    private String BASE_URI = "https://api.themoviedb.org/3/discover/movie";

    private String API_KEY;

    public TheMovieDBClient(String API_KEY, ConnectivityManager connectivityManager) {
        this.API_KEY = API_KEY;
        this.connectivityManager = connectivityManager;
    }


    public List<Movie> getTopMovies(String sortBy) throws IOException {

        // build up query URI
        Uri prepareURI = Uri.parse(BASE_URI).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("sort_by", sortBy)
                .build();


        // we need to make it a locator, a URL
        URL url = null;
        try {
            url = new URL(prepareURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // check internet connection first
        //TODO for some reason this isOnline() method returns wrong results - even though it's supposed to be right practice, no?
        if (!isOnline()) throw new IOException("No internet connection");

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
                movie.thumbnailUrl  = new URL("http://image.tmdb.org/t/p/w500"     + json.getString("poster_path"));
                movie.posterUrl     = new URL("http://image.tmdb.org/t/p/w500" + json.getString("poster_path"));

                movies.add(movie);
            }


        } catch (JSONException e) {
            Log.wtf(TAG, "should never happen", e);
        }

        return movies;
    }


    public boolean isOnline() {
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




}
