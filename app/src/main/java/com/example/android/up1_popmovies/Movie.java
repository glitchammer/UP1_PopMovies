package com.example.android.up1_popmovies;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by dnlbh on 19/03/2017.
 */

public class Movie {

    public static HashMap<Long,Movie> MOVIES_CACHE = new HashMap<Long,Movie>();

    public final long id;

    //todo dear reviewers, i wanted to keep this simple POJO light-weight. do we always need getters and setters? I'm tired of it, isn't that oldschool practice? I come across this style more and more often
    protected String title;
    protected String releaseDate;
    protected String plotSynopsis;

    protected double voteAvg;
    protected int   voteCount;

    protected URL thumbnailUrl;
    protected URL posterUrl;

    public Movie(long id) {
        this.id = id;
        MOVIES_CACHE.put(id, this);
    }



}
