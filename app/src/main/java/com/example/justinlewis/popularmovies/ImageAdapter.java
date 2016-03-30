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
    private List<String> posterUrls;
    public String LOG_TAG = ImageAdapter.class.getSimpleName();

    public ImageAdapter(Context c, List<String> myList) {
        super();
        mContext = c;
        this.posterUrls = myList;
        posterUrls.add("http://image.tmdb.org/t/p/w185/dlIPGXPxXQTp9kFrRzn0RsfUelx.jpg");
    }

    @Override
    public int getCount() {
        System.out.println("Returning: " + posterUrls.size());
        return posterUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return posterUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 6;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Calling getView");
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(this.mContext).load(posterUrls.get(position))
                .into(imageView);

        return imageView;
    }
}
