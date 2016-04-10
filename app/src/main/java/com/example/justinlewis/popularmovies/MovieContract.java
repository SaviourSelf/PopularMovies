package com.example.justinlewis.popularmovies;

import android.net.Uri;

import java.net.URI;

/**
 * Created by Justin Lewis on 4/10/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.justinlewis.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static Uri buildMovieUri(String movieID)
    {
        return BASE_CONTENT_URI.buildUpon().appendPath(movieID).build();
    }

}
