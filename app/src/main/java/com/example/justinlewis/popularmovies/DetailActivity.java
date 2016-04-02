package com.example.justinlewis.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        MovieData model = (MovieData) getIntent().getSerializableExtra("Editing");
        Bundle b = new Bundle();
        b.putSerializable("MODEL", model);

        if (savedInstanceState == null) {
            DetailActivityFragment frag = new DetailActivityFragment();
            frag.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        System.out.println("Back Button Pressed");
    }

    public static class DetailActivityFragment extends Fragment {

        MovieData model;

        public DetailActivityFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            model = (MovieData) getArguments().getSerializable("MODEL");
            //System.out.println(model.getPlot_synopsis());
            View view = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView movieTitle = (TextView) view.findViewById(R.id.movie_title);
            TextView moviePlot = (TextView) view.findViewById(R.id.plotSynopsisText);
            TextView releaseDate = (TextView) view.findViewById(R.id.movieReleaseYear);
            TextView voteAverage = (TextView) view.findViewById(R.id.movieVoteAverate);
            ImageView imageView = (ImageView) view.findViewById(R.id.moviePoster);

            voteAverage.setText("Vote average: " + model.getVote_average() + "/10");
            releaseDate.setText(model.getRelease_date());
            moviePlot.setText(model.getPlot_synopsis());
            movieTitle.setText(model.getTitle());
            Picasso.with(view.getContext()).load(model.getPoster_url())
                    .into(imageView);
            return view;
            //return inflater.inflate(R.layout.fragment_detail, container, false);
        }
    }

}
