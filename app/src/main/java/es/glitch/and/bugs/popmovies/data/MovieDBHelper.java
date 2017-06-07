package es.glitch.and.bugs.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import es.glitch.and.bugs.popmovies.data.MovieContract.MoviesEntry;

/**
 * Created by dnlbh on 10/04/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 3;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +

                // id: we can re-use the existing primary key from theMovieDB.org
                MoviesEntry._ID                  + " INTEGER PRIMARY KEY," +

                MoviesEntry.COLUMN_TITLE         + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT, " +
                MoviesEntry.COLUMN_RELEASE_DATE  + " TIMESTAMP, " +
                MoviesEntry.COLUMN_VOTE_AVG      + " REAL, " +
                MoviesEntry.COLUMN_VOTE_CNT      + " INTEGER , " +
                MoviesEntry.COLUMN_THUMBNAIL     + " TEXT, " +
                MoviesEntry.COLUMN_POSTER        + " TEXT," +
                MoviesEntry.COLUMN_BACKDROP      + " TEXT" +
                "); ";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
