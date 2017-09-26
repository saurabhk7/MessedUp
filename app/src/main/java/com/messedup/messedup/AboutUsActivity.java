package com.messedup.messedup;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.messedup.messedup.ui_package.CircleTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutUsActivity extends AppCompatActivity {

    TextView name1,name2,name3,name4;
    TextView email1,email2,email3,email4;
    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        TextView toolbarTextView = (TextView) findViewById(R.id.toolbar_title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        versionText=(TextView)findViewById(R.id.VersionAbtTxt);

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionText.setText("version: "+pInfo.versionName);



        SetDetails1(this,"Dipak Wani","wanidipak56@gmail.com","https://wanidipak56.000webhostapp.com/Photos/Dipak.png");
        SetDetails2(this,"Saurabh Kshirsagar","saurabhkshirsagar35@gmail.com","https://wanidipak56.000webhostapp.com/Photos/Saurabh.png");
        SetDetails3(this,"Tanmay Singhal","tanmaysinghal98@gmail.com","https://wanidipak56.000webhostapp.com/Photos/Tanmay.jpg");
        SetDetails4(this,"Yash Karwa","ypkarwa@gmail.com","https://wanidipak56.000webhostapp.com/Photos/Yash.png");


    }

    private void SetDetails1(final Context c, String s, String s1, String s2) {

        final Uri photoUrl = Uri.parse(s2);
        final ImageView ProfilePic = (ImageView)findViewById(R.id.profile_pic_1);

        // Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
        try {
            Picasso.with(c).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(c).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                }
            });
        } catch (Exception e) {
            Log.v("E_VALUE", e.getMessage());
        }

        name1=(TextView)findViewById(R.id.name_text_1);
        name1.setText(s);
        email1=(TextView)findViewById(R.id.email_1);
        email1.setText(s1);


    }

    private void SetDetails2(final Context c, String s, String s1, String s2) {

        final Uri photoUrl = Uri.parse(s2);
        final ImageView ProfilePic = (ImageView)findViewById(R.id.profile_pic_2);

        // Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
        try {
            Picasso.with(c).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(c).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                }
            });
        } catch (Exception e) {
            Log.v("E_VALUE", e.getMessage());
        }

        name2=(TextView)findViewById(R.id.name_text_2);
        name2.setText(s);
        email2=(TextView)findViewById(R.id.email_2);
        email2.setText(s1);


    }

    private void SetDetails3(final Context c, String s, String s1, String s2) {

        final Uri photoUrl = Uri.parse(s2);
        final ImageView ProfilePic = (ImageView)findViewById(R.id.profile_pic_3);

        // Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
        try {
            Picasso.with(c).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(c).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                }
            });
        } catch (Exception e) {
            Log.v("E_VALUE", e.getMessage());
        }

        name3=(TextView)findViewById(R.id.name_text_3);
        name3.setText(s);
        email3=(TextView)findViewById(R.id.email_3);
        email3.setText(s1);


    }

    private void SetDetails4(final Context c, String s, String s1, String s2) {

        final Uri photoUrl = Uri.parse(s2);
        final ImageView ProfilePic = (ImageView)findViewById(R.id.profile_pic_4);

        // Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
        try {
            Picasso.with(c).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(c).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                }
            });
        } catch (Exception e) {
            Log.v("E_VALUE", e.getMessage());
        }

        name4=(TextView)findViewById(R.id.name_text_4);
        name4.setText(s);
        email4=(TextView)findViewById(R.id.email_4);
        email4.setText(s1);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}
