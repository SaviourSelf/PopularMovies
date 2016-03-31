package com.example.justinlewis.popularmovies;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Justin Lewis on 3/29/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<MovieData> movieList;
    public String LOG_TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Context c, List<MovieData> myList) {
        super();
        mContext = c;
        this.movieList = myList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movieList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(this.mContext).load(movieList.get(position).getPoster_url())
                .into(imageView);

        return imageView;
    }
}
