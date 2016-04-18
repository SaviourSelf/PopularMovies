package com.example.justinlewis.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Justin Lewis on 4/17/2016.
 */
public class TrailerObject implements Parcelable {

    String trailerUrl, trailerName;

    public TrailerObject(Parcel p)
    {
        trailerName = p.readString();
        trailerUrl = p.readString();
    }

    public TrailerObject(String name, String url) {
        this.trailerName = name;
        this.trailerUrl = url;
    }

    public static final Parcelable.Creator<TrailerObject> CREATOR = new Parcelable.Creator<TrailerObject>(){

        @Override
        public TrailerObject createFromParcel(Parcel source){
            return new TrailerObject(source);
        }

        @Override
        public TrailerObject[] newArray(int size){
            return new TrailerObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerName);
        dest.writeString(trailerUrl);
    }
}
