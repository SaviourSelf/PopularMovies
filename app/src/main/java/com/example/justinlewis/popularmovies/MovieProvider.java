package com.example.justinlewis.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Justin Lewis on 4/10/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final String DBNAME = "MOVIEDB";
    private static final String TABLE_NAME = "movieTable";

    private static final String ID_FIELD = "_ID";
    private static final String TITLE_FIELD = "title";
    private static final String VOTER_AVERAGE_FIELD = "voteAverage";
    private static final String POSTER_URL_FIELD = "posterUrl";
    private static final String RELEASE_DATE_FIELD = "releaseDate";
    private static final String PLOT_FIELD = "plot";

    static{
        sMovieQueryBuilder = new SQLiteQueryBuilder();
    }

    private static UriMatcher buildUriMatcher()
    {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Helper class that actually creates and manages the provider's underlying data repository.
     */
    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        private static final String SQL_CREATE_MAIN = "CREATE TABLE " +
                TABLE_NAME + " " +                // Table's name
                "( " +                           // The columns in the table
                ID_FIELD + " INTEGER PRIMARY KEY, " +
                TITLE_FIELD + " TEXT NOT NULL, " +
                RELEASE_DATE_FIELD + " TEXT NOT NULL, " +
                POSTER_URL_FIELD + " TEXT NOT NULL, " +
                PLOT_FIELD + " TEXT NOT NULL, " +
                VOTER_AVERAGE_FIELD + " TEXT NOT NULL" +
                ");";

        /*
         * Instantiates an open helper for the provider's SQLite data repository
         * Do not do database creation and upgrade here.
         */
        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        /*
         * Creates the data repository. This is called when the provider attempts to open the
         * repository and SQLite reports that it doesn't exist.
         */
        public void onCreate(SQLiteDatabase db) {
            // Creates the main table
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            return;
        }
    }
}
