package com.example.justinlewis.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
        //Share information
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    public static class DetailActivityFragment extends Fragment {

        MovieData model;

        public DetailActivityFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            model = (MovieData) getArguments().getParcelable("MODEL");
            View view = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView movieTitle = (TextView) view.findViewById(R.id.movie_title);
            TextView moviePlot = (TextView) view.findViewById(R.id.plotSynopsisText);
            TextView releaseDate = (TextView) view.findViewById(R.id.movieReleaseYear);
            TextView voteAverage = (TextView) view.findViewById(R.id.movieVoteAverate);
            ImageView imageView = (ImageView) view.findViewById(R.id.moviePoster);

            voteAverage.setText("Vote average: " + model.getVote_average() + "/10");
            releaseDate.setText(model.getRelease_date());
            moviePlot.setText(model.getPlot_synopsis());
            //moviePlot.setText(model.getReviewObject()[0].getContent());
            movieTitle.setText(model.getTitle());
            Picasso.with(view.getContext()).load(model.getPoster_url())
                    .into(imageView);
            return view;
        }
    }

}
