package com.messedup.messedup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dd.CircularProgressButton;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;

public class SelectBreakfastActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TextView toolbarTextView;

    public  String MessID;
    public MenuCardView MessObj = null;
    private int mShortAnimationDuration;
    public CircularProgressButton circularProgressButton1;
    public CircularProgressButton circularProgressButton2;
    public CircularProgressButton circularProgressButton3;
    public CircularProgressButton circularProgressButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_breakfast);



        toolbar = (Toolbar) findViewById(R.id.toolbar3);

        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        circularProgressButton1=(CircularProgressButton)findViewById(R.id.AnimImInBtn1);
        circularProgressButton2=(CircularProgressButton)findViewById(R.id.AnimImInBtn2);
        circularProgressButton3=(CircularProgressButton)findViewById(R.id.AnimImInBtn3);
        circularProgressButton4=(CircularProgressButton)findViewById(R.id.AnimImInBtn4);


        Intent intent = getIntent();
        MessID = intent.getExtras().getString("messid");
        toolbarTextView.setText(MessID);

        final ImageView ProfilePic = (ImageView)findViewById(R.id.ProfilePicImg);




        String name= null;
        if (MessID != null) {
            name = (MessID.substring(0,1)).toLowerCase();
            TextDrawable drawable=TextDrawable.builder()
                    .beginConfig()
                    .fontSize(90)
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(name, Color.parseColor("#da3340"));

            ProfilePic.setImageDrawable(drawable);
        }

        circularProgressButton1.setIndeterminateProgressMode(true); // turn on indeterminate progress
        circularProgressButton2.setIndeterminateProgressMode(true); // turn on indeterminate progress
        circularProgressButton3.setIndeterminateProgressMode(true); // turn on indeterminate progress
        circularProgressButton4.setIndeterminateProgressMode(true); // turn on indeterminate progress


        circularProgressButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*/
                circularProgressButton1.setProgress(50);
//                try {
//                    Thread.sleep(5000);
//                    circularProgressButton1.setProgress(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circularProgressButton1.setProgress(100);
                        circularProgressButton1.setClickable(false);
                        circularProgressButton2.setClickable(false);
                        circularProgressButton3.setClickable(false);
                        circularProgressButton4.setClickable(false);



                    }
                },3000);

            }
        });
        circularProgressButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*/
                circularProgressButton2.setProgress(50);
//                try {
//                    Thread.sleep(5000);
//                    circularProgressButton2.setProgress(100);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circularProgressButton2.setProgress(100);
                        circularProgressButton1.setClickable(false);
                        circularProgressButton2.setClickable(false);
                        circularProgressButton3.setClickable(false);
                        circularProgressButton4.setClickable(false);

                    }
                },3000);

            }
        });
        circularProgressButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*/
                circularProgressButton3.setProgress(50);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      circularProgressButton3.setProgress(100);
                        circularProgressButton1.setClickable(false);
                        circularProgressButton2.setClickable(false);
                        circularProgressButton3.setClickable(false);
                        circularProgressButton4.setClickable(false);

                    }
                },3000);
            }
//

        });
        circularProgressButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*/
                circularProgressButton4.setProgress(50);
//                try {
//                    Thread.sleep(5000);
//                    circularProgressButton4.setProgress(100);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circularProgressButton4.setProgress(100);
                        circularProgressButton1.setClickable(false);
                        circularProgressButton2.setClickable(false);
                        circularProgressButton3.setClickable(false);
                        circularProgressButton4.setClickable(false);

                    }
                },3000);

            }
        });




        // Toast.makeText(this, "Show Info
    }
    private String getURLString(String messID) {


        messID=messID.replace(",","");
        messID=messID.replace(" ","%20");
        return messID;

    }


}

