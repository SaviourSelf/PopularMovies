package com.example.justinlewis.popularmovies;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Justin Lewis on 3/29/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String [] posterUrls;
    public String LOG_TAG = ImageAdapter.class.getSimpleName();
    private GridView gv;

    public ImageAdapter(Context c, String [] posterUrls, GridView gridView) {
        mContext = c;
        if (posterUrls != null)
            this.posterUrls = posterUrls;
        else
            this.posterUrls = new String [] {"http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"};
        this.gv = gridView;
    }

    public void setPosters(String [] posters)
    {
        this.posterUrls = posters;
    }

    @Override
    public int getCount() {
        return posterUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return posterUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
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

        Picasso.with(this.mContext).load(posterUrls[position])
                .into(imageView);

        return imageView;
    }
}
