package com.messedup.messedup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.messedup.messedup.signin_package.PhoneNumberAuthentication;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* Button ContinueBtn=(Button)findViewById(R.id.ContBtn);


        ContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, PhoneNumberAuthentication.class));
            }
        });*/


       /* addSlide(new CustomSlide2());

        addSlide(new CustomSlide1());*/

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide5)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.menu)
                .title("Menu at your fingertips")
                .description("Now no need to go and check menu of every mess. Get next 7 days menu instantly!")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide6)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.offers_compressed)
                .title("Your Favourites First")
                .description("Just add your favourite mess, and get notified about their real time updates!")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide7)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.copped_cardview)
                .title("Never miss out Offers")
                .description("Get notified about nearby offers and mess updates instantly.")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide8)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.share_compressed)
                .title("Sharing is Caring")
                .description("Let your friends know where you are gonna eat today, so that you have a great company!")
                .build());


    }

    @Override
    public void onFinish() {
        super.onFinish();

        startActivity(new Intent(this,PhoneNumberAuthentication.class));

    }
}
