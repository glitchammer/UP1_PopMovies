package com.example.android.up1_popmovies;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class Movie {

    public static HashMap<Long,Movie> MOVIES_CACHE = new HashMap<Long,Movie>();

    public final long id;

    protected String title;
    protected String releaseDate;
    protected String plotSynopsis;

    protected double voteAvg;
    protected int   voteCount;

    protected URL posterUrl;

    public Movie(long id) {
        this.id = id;
        MOVIES_CACHE.put(id, this);
    }



}
