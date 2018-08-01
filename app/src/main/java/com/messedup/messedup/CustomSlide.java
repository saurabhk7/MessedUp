package com.messedup.messedup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by saurabh on 1/8/18.
 */

public class CustomSlide extends SlideFragment {
    private CheckBox checkBox;
    private TextView TermsandCondTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_custom_slide, container, false);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TermsandCondTxt = (TextView) view.findViewById(R.id.termsend);

        TextView tnctxt = (TextView) view.findViewById(R.id.termsend2); //txt is object of TextView
//        tnctxt.setMovementMethod(LinkMovementMethod.getInstance());
        tnctxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://messedup.in/terms_conditions/"));
                startActivity(browserIntent);
            }
        });
        TextView pptxt = (TextView) view.findViewById(R.id.termsend3); //txt is object of TextView
//        pptxt.setMovementMethod(LinkMovementMethod.getInstance());
        pptxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://messedup.in/PrivacyPolicy/"));
                startActivity(browserIntent);
            }
        });


        return view;

    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorBlack;
    }

    @Override
    public boolean canMoveFurther() {
        return checkBox.isChecked();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Please accept the Terms and Conditions";
    }

}
