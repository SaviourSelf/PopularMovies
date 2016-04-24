package com.example.justinlewis.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

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

public class DetailActivity extends ActionBarActivity {


    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieData model = (MovieData) getIntent().getParcelableExtra("Editing");
        Bundle b = new Bundle();
        b.putParcelable("MODEL", model);

        if (savedInstanceState == null) {
            DetailActivityFragment frag = new DetailActivityFragment();
            frag.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Share information -- handled in fragment.
        int id = item.getItemId();
        //System.out.println("Felt that in class");
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public static class DetailActivityFragment extends Fragment {

        MovieData model;
        TextView movieTitle;
        TextView moviePlot;
        TextView releaseDate;
        TextView voteAverage;
        ImageView imageView;

        @Override
        public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
            // Do something that differs the Activity's menu here
            super.onCreateOptionsMenu(menu, inflater);
            int icon = android.R.drawable.star_big_on;
            if (!model.getFavorite().equals("no")) {
                menu.getItem(1).setIcon(icon);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            //Share information
            int id = item.getItemId();
            //System.out.println("Felt that in fragment");
            if (id == R.id.menu_item_share) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out " + model.getTitle() +
                        " and its trailer: " + model.getTrailerObject()[0]);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            }
            if (id == R.id.menu_favorite_this)
            {
                //System.out.println("Updating favorite status.");
                if (model.getFavorite().equals("no")) {
                    model.setFavorite("yes");
                    item.setIcon(android.R.drawable.star_big_on);
                    updateDBFavorite("yes");
                } else {
                    model.setFavorite("no");
                    item.setIcon(android.R.drawable.star_big_off);
                    updateDBFavorite("no");
                }
            }
            return false;
        }

        private void updateDBFavorite(String str)
        {
            FetchMovieTrailerAndReviewTask t = new FetchMovieTrailerAndReviewTask();
            t.execute(str);
        }

        public DetailActivityFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            model = (MovieData) getArguments().getParcelable("MODEL");

            getReviewsAndTrailers();
            View view = inflater.inflate(R.layout.fragment_detail, container, false);
            movieTitle = (TextView) view.findViewById(R.id.movie_title);
            moviePlot = (TextView) view.findViewById(R.id.plotSynopsisText);
            releaseDate = (TextView) view.findViewById(R.id.movieReleaseYear);
            voteAverage = (TextView) view.findViewById(R.id.movieVoteAverate);
            imageView = (ImageView) view.findViewById(R.id.moviePoster);

            voteAverage.setText("Vote average: " + model.getVote_average() + "/10");
            releaseDate.setText(model.getRelease_date());
            moviePlot.setText(model.getPlot_synopsis());
            movieTitle.setText(model.getTitle());

            Picasso.with(view.getContext()).load(model.getPoster_url())
                    .into(imageView);

            return view;
        }
        public void getReviewsAndTrailers()
        {
            FetchMovieTrailerAndReviewTask t = new FetchMovieTrailerAndReviewTask();
            t.execute("ReviewsAndTrailers");
        }

        //Start subclass

        public class FetchMovieTrailerAndReviewTask extends AsyncTask<String, Void, MovieData> {

            private boolean forFavorites = false;

            @Override
            protected MovieData doInBackground(String... v) {
                if (!v[0].equals("ReviewsAndTrailers"))
                {
                    forFavorites = true;
                    updateDBFavoriteBackground(v[0]);
                    return model;
                }
                String a,b;
                a= buildTrailersOrReviewsURL(model.getId() + "", "videos");
                b= buildTrailersOrReviewsURL(model.getId() + "", "reviews");
                ReviewObject [] r = null; //getReviewsFromUrl(b);
                TrailerObject [] t = null; //getVideosFromUrl(a);
                try {
                    r = getReviewsFromUrl(b);
                    t = getVideosFromUrl(a);
                    updateDB(r,t);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                model.setReviewObject(r);
                model.setTrailerObject(t);
                return model;
            }
            @Override
            protected void onPostExecute(MovieData model) {
                super.onPostExecute(model);
                if (forFavorites == true)
                    return;
                TrailerObject [] trailers = model.getTrailerObject();
                ReviewObject [] reviews = model.getReviewObject();

                String text = moviePlot.getText() + "\n\n\nTrailers:\n\n";

                for (TrailerObject t : trailers)
                {
                    text = text + t.trailerName + "\n" + t.trailerUrl + "\n\n";
                }

                for (ReviewObject r : reviews)
                {
                    text = text + r.getContent()+ "\n";
                }
                moviePlot.setText(text);
                //System.out.println(text);

            }

            private void updateDBFavoriteBackground(String str)
            {
                ContentValues values = new ContentValues();
                values.put(MovieProvider.FAVORITE_FIELD, str);
                //int ret = getContext().getContentResolver().update(MovieProvider.CONTENT_URI, values, MovieProvider.ID_FIELD, new String[] {model.getId() + ""});
                int ret = getContext().getContentResolver().update(MovieProvider.CONTENT_URI, values, null, null);
                System.out.println("DB UPDATE FAVORITE RETURN: " + ret);
            }


            private void updateDB(ReviewObject [] reviewObjects, TrailerObject [] trailerObjects)
            {
                ContentValues values = new ContentValues();
                values.put(MovieProvider.REVIEW_FIELD, packReviews(reviewObjects));
                values.put(MovieProvider.TRAILER_FIELD, packTrailers(trailerObjects));

                int ret = getContext().getContentResolver().update(MovieProvider.CONTENT_URI, values, model.getId() + "", null);
                System.out.println("DB UPDATE REVIEW RETURN: " + ret);
            }


            private String buildTrailersOrReviewsURL(String movieId, String videosOrReviews)
            {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(movieId)
                        .appendPath(videosOrReviews)
                        .appendQueryParameter("api_key", BuildConfig.MOVIE_API_KEY);

                //System.out.println(builder.build().toString());
                return builder.build().toString();
            }

            private TrailerObject [] getVideosFromUrl(String url) throws JSONException
            {
                String data = readPopularMovieData(url);
                JSONObject fullJson = new JSONObject(data);
                JSONArray array = fullJson.getJSONArray("results");
                TrailerObject [] retVal = new TrailerObject [array.length()];
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    String key = o.getString("key");
                    key = "https://www.youtube.com/watch?v=" + key;
                    retVal[i] = new TrailerObject(
                            o.getString("name"),
                            key
                    );
                }
                return retVal;
            }

            private ReviewObject [] getReviewsFromUrl(String url) throws JSONException
            {
                String data = readPopularMovieData(url);
                JSONObject fullJson = new JSONObject(data);
                JSONArray array = fullJson.getJSONArray("results");
                ReviewObject [] retVal = new ReviewObject [array.length()];
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject o = array.getJSONObject(i);
                    retVal[i] = new ReviewObject(
                            o.getString("id"),
                            o.getString("content"),
                            o.getString("author"),
                            o.getString("url")
                    );
                }
                return retVal;
            }


            private ReviewObject [] unpackReviews(byte [] blob)
            {
                String json = new String(blob);
                Gson gson = new Gson();
                return gson.fromJson(json, new TypeToken<ArrayList<ReviewObject>>() {
                }.getType());
            }


            private String packReviews(ReviewObject [] r)
            {
                Gson gson = new Gson();
                return gson.toJson(r);
            }

            private String packTrailers(TrailerObject [] r)
            {
                Gson gson = new Gson();
                return gson.toJson(r);
            }

            private String readPopularMovieData(String movieUrl) {

                movieUrl = movieUrl.trim();
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
}
