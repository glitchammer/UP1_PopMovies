package es.glitch.and.bugs.popmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dnlbh on 10/04/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "es.glitch.and.bugs.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME           = "movies";

        public static final String COLUMN_TITLE         = "title";
        public static final String COLUMN_RELEASE_DATE  = "releaseDate";
        public static final String COLUMN_PLOT_SYNOPSIS = "plotSynopsis";
        public static final String COLUMN_VOTE_AVG      = "voteAvg";
        public static final String COLUMN_VOTE_CNT      = "voteCnt";
        public static final String COLUMN_THUMBNAIL     = "thumbnail";
        public static final String COLUMN_POSTER        = "poster";

    }

}
