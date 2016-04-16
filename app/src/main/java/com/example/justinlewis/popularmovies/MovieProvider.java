package com.example.justinlewis.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Justin Lewis on 4/10/2016.
 */
public class MovieProvider extends ContentProvider {

    private final String LOG_TAG = MovieProvider.class.getSimpleName();
    static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    public static final Uri CONTENT_URI = MovieContract.BASE_CONTENT_URI;
    private MainDatabaseHelper mOpenHelper;
    private SQLiteDatabase db;

    private static final String DBNAME = "MOVIEDB";
    public static final String TABLE_NAME = "movieTable";

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
        mOpenHelper = new MainDatabaseHelper(getContext());
        db = mOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        if (uri.getPathSegments().size() > 20)
            qb.appendWhere( ID_FIELD + "=" + uri.getPathSegments().get(1));
        /*
        switch (sUriMatcher.match(uri)) {
            case STUDENTS:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;

            case ID_FIELD:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        */

        if (sortOrder == null || sortOrder == ""){
            sortOrder = TITLE_FIELD;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(	TABLE_NAME, "", values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        Log.v(LOG_TAG, "Failed to add a record into " + uri);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = db.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = db.update(TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
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
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
            onCreate(db);
        }
    }
}
