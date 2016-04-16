package com.example.justinlewis.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private List<MovieData> movieList;
    private ImageAdapter images;
    private GridView gridview;

    public final String POPULAR_URL = "popular";
    public final String TOP_RATED_URL = "top_rated";
    private String lastChosen = POPULAR_URL;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.setRetainInstance(true);

        movieList = new ArrayList<MovieData>();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getMoviePosters(LoadPreferences());
        gridview = (GridView) rootView.findViewById(R.id.picture_gridview);
        images = new ImageAdapter(this.getActivity(), movieList);
        gridview.setAdapter(images);
        gridview.setVisibility(GridView.VISIBLE);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                MovieData data = (MovieData) images.getItem(position);
                SavePreferences();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("Editing", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        return gridview;
    }

    private void SavePreferences()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("LASTCHOSEN", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("lastChosen", lastChosen); // value to store
        editor.commit();
    }

    private String LoadPreferences()
    {
        SharedPreferences preferences = getContext().getSharedPreferences("LASTCHOSEN", 0);
        lastChosen = preferences.getString("lastChosen", lastChosen);
        return lastChosen;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String s = LoadPreferences();
        if (s.equals(TOP_RATED_URL))
            getActivity().setTitle(getString(R.string.main_activity_title_top_rated));
        if (s.equals(POPULAR_URL))
            getActivity().setTitle(getString(R.string.main_activity_title_popular));
        getMoviePosters(s);
    }

    public void getMoviePosters(String params)
    {
        FetchMovieDataTask t = new FetchMovieDataTask();
        t.execute(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_popular_movies)
        {
            getMoviePosters(POPULAR_URL);
            lastChosen = POPULAR_URL;
            SavePreferences();
            getActivity().setTitle(getString(R.string.main_activity_title_popular));
            return true;
        }
        if (id == R.id.action_highest_rated_movies)
        {
            getMoviePosters(TOP_RATED_URL);
            lastChosen = TOP_RATED_URL;
            SavePreferences();
            getActivity().setTitle(getString(R.string.main_activity_title_top_rated));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Start subclass

    public class FetchMovieDataTask extends AsyncTask<String, Void, MovieData[]> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

        @Override
        protected MovieData [] doInBackground(String...params)
        {
            if (params.length == 0)
                return null;

            String movieUrl = getPopularMovieURL(params[0]);
            String json = readPopularMovieData(movieUrl);

            MovieData [] movieData = null;

            try {
                movieData = getMoviePosters(json);
            } catch (JSONException e)
            {
                Log.e(LOG_TAG, "Error getting JSON");
            }
            return movieData;
        }
        private String buildImageURL(String imagePath)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath(imagePath.substring(1));
            return builder.build().toString();
        }

        private String getPopularMovieURL(String popOrRated)
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(popOrRated)
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_API_KEY);

            System.out.println(builder.build().toString());
            return builder.build().toString();
        }

        private MovieData [] getMoviePosters(String jsonString) throws JSONException
        {

            JSONObject fullJson = new JSONObject(jsonString);
            JSONArray array = fullJson.getJSONArray("results");
            MovieData [] retVal = new MovieData [array.length()];
            for (int i = 0; i < array.length(); i++)
            {
                ContentValues values = new ContentValues();
                JSONObject o = array.getJSONObject(i);
                retVal[i] = new MovieData(
                        o.getString("id"),
                        o.getString("title"),                      // Title
                        o.getString("release_date"),               // Release Date
                        buildImageURL(o.getString("poster_path")), // Poster Url
                        o.getString("vote_average"),               // Vote average
                        o.getString("overview"));                  // Plot
                Log.v(LOG_TAG, "ID: " + retVal[i].getId());

                //If it doesn't exist in the DB, create it in the DB.
                Cursor cursor;
                cursor = getContext().getContentResolver().query(
                        MovieProvider.CONTENT_URI,
                        null,
                        MovieProvider.ID_FIELD + " =?",
                        new String [] {retVal[i].getId() + ""},
                        null
                );
                if (cursor.getCount() > 0) {
                    values.put(MovieProvider.ID_FIELD, retVal[i].getId());
                    values.put(MovieProvider.PLOT_FIELD, retVal[i].getPlot_synopsis());
                    values.put(MovieProvider.POSTER_URL_FIELD, retVal[i].getPoster_url());
                    values.put(MovieProvider.RELEASE_DATE_FIELD, retVal[i].getRelease_date());
                    values.put(MovieProvider.TITLE_FIELD, retVal[i].getTitle());
                    values.put(MovieProvider.VOTER_AVERAGE_FIELD, retVal[i].getVote_average());
                    Uri uri = getContext().getContentResolver().insert(MovieProvider.CONTENT_URI, values);
                }
                cursor.close();
            }
            return retVal;
        }


        @Override
        protected void onPostExecute(MovieData[] capturedList) {
            super.onPostExecute(capturedList);

            movieList.clear();
            for (MovieData s : capturedList)
                movieList.add(s);
            images.notifyDataSetChanged();
        }

        private String readPopularMovieData(String movieUrl) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String popularMovieJSON = null;

            try {
                URL url = new URL(movieUrl);

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
