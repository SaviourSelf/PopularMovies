package com.example.justinlewis.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Justin Lewis on 3/30/2016.
 */
public class MovieData implements Parcelable {

    private String title, release_date, poster_url, vote_average, plot_synopsis, favorite, source;
    private int id;
    private ReviewObject [] reviewObject;
    private TrailerObject [] trailerObject;

    public MovieData(String id, String title, String release_date, String poster_url, String vote_average, String plot_synopsis, String source, String favorite)
    {
        this(id,title,release_date,poster_url,vote_average,plot_synopsis,source,null, null, favorite);
    }


    public MovieData(String id, String title, String release_date, String poster_url, String vote_average, String plot_synopsis, String source, ReviewObject [] r, TrailerObject [] t, String favorite)
    {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.release_date = release_date; //Just the year
        this.poster_url = poster_url;
        this.vote_average = vote_average;
        this.plot_synopsis = plot_synopsis;
        this.favorite = favorite;
        this.source = source;
        this.reviewObject = r;
        this.trailerObject = t;
    }

    /*
     * Derived from writeToParcel method.
     */
    public MovieData(Parcel p)
    {
        id = Integer.parseInt(p.readString());
        title = p.readString();
        release_date = p.readString();
        poster_url = p.readString();
        vote_average = p.readString();
        plot_synopsis = p.readString();
        favorite = p.readString();
        source = p.readString();
        reviewObject = p.createTypedArray(ReviewObject.CREATOR);
        trailerObject = p.createTypedArray(TrailerObject.CREATOR);
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){

        @Override
        public MovieData createFromParcel(Parcel source){
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size){
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /*
     * These strings are read in this order in the MovieData(Parcel p) constructor.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id + "");
        dest.writeString(title);
        dest.writeString(release_date);
        dest.writeString(poster_url);
        dest.writeString(vote_average);
        dest.writeString(plot_synopsis);
        dest.writeString(favorite);
        dest.writeString(source);
        dest.writeTypedArray(reviewObject,0);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {return this.id;}

    public void setID(String id) {this.id = Integer.parseInt(id);}

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

    public String getFavorite() { return favorite; }

    public void setFavorite(String fav) {this.favorite = fav; }

    public String getSource() { return source; }

    public void setSource(String s) { this.source = s; }

    public ReviewObject [] getReviewObject() { return reviewObject; }

    public void setReviewObject(ReviewObject [] r) {this.reviewObject = r;}

    public TrailerObject [] getTrailerObject() { return trailerObject; }

    public void setTrailerObject(TrailerObject [] r) {this.trailerObject = r;}
}
