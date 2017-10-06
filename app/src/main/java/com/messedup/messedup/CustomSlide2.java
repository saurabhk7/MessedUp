/*
package com.messedup.messedup;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import agency.tango.materialintroscreen.SlideFragment;


public class CustomSlide2 extends SlideFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_slide2, container, false);
        VideoView videoView = (VideoView)view.findViewById(R.id.videoView2);
        videoView.setVideoURI(Uri.parse("android.resource://com.messedup.messedup/" + R.raw.vi2_compressed));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        return view;
    }

    public int backgroundColor() {
        return R.color.colorSlide5;
    }

    public int buttonsColor() {
        return R.color.colorBlack;
    }
}
*/
