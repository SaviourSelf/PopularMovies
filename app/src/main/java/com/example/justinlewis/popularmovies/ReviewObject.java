package com.example.justinlewis.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Justin Lewis on 4/16/2016.
 */
public class ReviewObject implements Parcelable {

    private String id, content, author, url;

    //https://www.youtube.com/watch?v=C5mNSbS9hi8

    public ReviewObject(String id, String content, String author, String url)
    {
        this.id = id;
        this.content = content;
        this.author = author;
        this.url = url;
    }

    public ReviewObject(Parcel p)
    {
        id = p.readString();
        content = p.readString();
        author = p.readString();
        url = p.readString();
    }

    public static final Parcelable.Creator<ReviewObject> CREATOR = new Parcelable.Creator<ReviewObject>(){

        @Override
        public ReviewObject createFromParcel(Parcel source){
            return new ReviewObject(source);
        }

        @Override
        public ReviewObject[] newArray(int size){
            return new ReviewObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(author);
        dest.writeString(url);
    }
}
