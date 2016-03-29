package com.example.justinlewis.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void getMoviePosters()
    {
        FetchMovieDataTask t = new FetchMovieDataTask();
        t.execute("");
    }


    public class FetchMovieDataTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

        @Override
        protected String [] doInBackground(String...params)
        {
            if (params.length == 0)
                return null;
            String [] retval = null;
            return retval;
        }
        private String buildImageURL(String imagePath)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org/t/p")
                    .appendPath("w185")
                    .appendPath(imagePath);
            Log.v(LOG_TAG, builder.build().toString());
            return builder.build().toString();
        }

        private String getPopularMovieURL()
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendQueryParameter("popular?api_key", BuildConfig.MOVIE_API_KEY);
            Log.v(LOG_TAG, builder.build().toString());
            return builder.build().toString();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
        }

        private String readPopularMovieData() {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String popularMovieJSON = null;

            try {
                URL url = new URL(getPopularMovieURL());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                popularMovieJSON = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return popularMovieJSON;
        }
    }
}
