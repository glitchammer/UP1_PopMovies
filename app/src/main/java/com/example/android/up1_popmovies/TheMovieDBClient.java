package com.example.android.up1_popmovies;

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


    private String BASE_URI = "https://api.themoviedb.org/3/discover/movie";

    private String API_KEY;

    public TheMovieDBClient(String API_KEY) {
        this.API_KEY = API_KEY;
    }


    public List<Movie> getTopMovies(String sort) throws IOException {

        // build up query URI
        Uri prepareURI = Uri.parse(BASE_URI).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("sort_by", sort)
                .build();


        // we need to make it a locator, a URL
        URL url = null;
        try {
            url = new URL(prepareURI.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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







}
