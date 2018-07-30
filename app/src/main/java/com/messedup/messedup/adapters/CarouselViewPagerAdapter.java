package com.messedup.messedup.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by saurabh on 6/6/18.
 */

public class CarouselViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] imageUrls;

    public CarouselViewPagerAdapter(Context context, String[] imageUrls)
    {
        this.context=context;
        this.imageUrls = imageUrls;
    }



    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load(imageUrls[position])
                .resize(600,200)
                .centerInside()
                .into(imageView);

        container.addView(imageView);


    return imageView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}
