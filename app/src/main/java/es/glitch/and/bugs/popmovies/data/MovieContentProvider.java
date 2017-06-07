package es.glitch.and.bugs.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MovieContentProvider extends ContentProvider {


    public static final int CODE_MOVIES         = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, CODE_MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", CODE_MOVIES_WITH_ID);
        return uriMatcher;
    }

    private MovieDBHelper movieDBHelper;

    public MovieContentProvider() {
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDBHelper = new MovieDBHelper(context);
        return true;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return movieDBHelper.getWritableDatabase().delete(
                MovieContract.MoviesEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        movieDBHelper.getWritableDatabase().insert(
                MovieContract.MoviesEntry.TABLE_NAME,
                null,
                values
        );

        return MovieContract.MoviesEntry.buildMovieUri(Long.parseLong(""+values.get(MovieContract.MoviesEntry._ID)));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES_WITH_ID: {

                String movieId = uri.getLastPathSegment();

                String[] selectionArguments = new String[]{movieId};

                cursor = movieDBHelper.getReadableDatabase().query(

                        MovieContract.MoviesEntry.TABLE_NAME,
                        projection,
                        MovieContract.MoviesEntry._ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_MOVIES: {
                cursor = movieDBHelper.getReadableDatabase().query(
                        MovieContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // no use case in this project
        throw new UnsupportedOperationException("Not yet implemented, no use case for this in the project.");
    }
}
