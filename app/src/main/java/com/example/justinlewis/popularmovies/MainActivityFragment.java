package com.example.justinlewis.popularmovies;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private ArrayAdapter<String> mPosterAdapter;
    private List<String> posterUrls;
    private ImageAdapter images;
    private GridView gridview;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posterUrls = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        getMoviePosters();

        gridview = (GridView) rootView.findViewById(R.id.picture_gridview);
        images = new ImageAdapter(this.getActivity(), posterUrls);
        gridview.setAdapter(images);
        gridview.setVisibility(GridView.VISIBLE);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return gridview;
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
            String json = readPopularMovieData();

            //Contains URL to posters
            String [] posters = null;
            try {
                posters = getMoviePosters(json);
            } catch (JSONException e)
            {
                Log.e(LOG_TAG, "Error getting JSON");
            }
            return posters;
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
            //Log.v(LOG_TAG, builder.build().toString());
            return builder.build().toString();
        }

        private String getPopularMovieURL()
        {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("popular")
                    .appendQueryParameter("api_key", BuildConfig.MOVIE_API_KEY);
            //Log.v(LOG_TAG, builder.build().toString());
            return builder.build().toString();
        }

        private String [] getMoviePosters(String jsonString) throws JSONException
        {
            JSONObject fullJson = new JSONObject(jsonString);
            JSONArray array = fullJson.getJSONArray("results");
            String [] retVal = new String [array.length()];
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject o = array.getJSONObject(i);
                retVal[i] = buildImageURL(o.getString("poster_path"));
                //Log.v(LOG_TAG, retVal[i]);
            }
            return retVal;
        }


        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            posterUrls.clear();
            for (String s : strings)
                posterUrls.add(s);
            Log.v(LOG_TAG, "Strings length: " + posterUrls.size());
            images.notifyDataSetChanged();
            Log.v(LOG_TAG, "Notify sent! -- results: " + images.getCount());
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
