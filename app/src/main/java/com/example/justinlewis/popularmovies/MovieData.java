package com.example.justinlewis.popularmovies;

import java.io.Serializable;

/**
 * Created by Justin Lewis on 3/30/2016.
 */
public class MovieData implements Serializable {

    private String title, release_date, poster_url, vote_average, plot_synopsis;

    public MovieData(String title, String release_date, String poster_url, String vote_average, String plot_synopsis)
    {
        this.title = title;
        this.release_date = release_date; //Just the year
        this.poster_url = poster_url;
        this.vote_average = vote_average;
        this.plot_synopsis = plot_synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }
}
