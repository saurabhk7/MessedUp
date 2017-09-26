package com.messedup.messedup;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by saurabh on 25/9/17.
 */

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Raleway-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        //....

    }



}
