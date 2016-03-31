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
import android.widget.Toast;

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

    public static class DetailActivityFragment extends Fragment {

        MovieData model;

        public DetailActivityFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            model = (MovieData) getArguments().getSerializable("MODEL");
            System.out.println(model.getPlot_synopsis());
            return inflater.inflate(R.layout.fragment_detail, container, false);
        }
    }

}
