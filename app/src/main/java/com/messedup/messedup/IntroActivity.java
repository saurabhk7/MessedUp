package com.messedup.messedup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.messedup.messedup.signin_package.PhoneNumberAuthentication;

import java.util.regex.Pattern;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
                .image(R.drawable.menu_main_comp)
                .title("Menu at your fingertips")
                .description("Now no need to go and check menu of every mess. Get next 7 days menu instantly!")
                .build());
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide6)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.cardview_edit)
                .title("Your Favourites First")
                .description("Just add your favourite mess, and get notified about their real time updates!")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide7)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.offers_compressed_new)
                .title("Never miss out Offers")
                .description("Get notified about nearby offers and mess updates instantly.")
                .build());


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorSlide8)
                .buttonsColor(R.color.colorBlack)
//                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
//                        .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(R.drawable.share_compressed_new)
                .title("Sharing is Caring")
                .description("Share everyday menu with your friends and enjoy your meal together!")
                .build());

        addSlide(new CustomSlide());


    }

    @Override
    public void onFinish() {
        super.onFinish();

        startActivity(new Intent(this,PhoneNumberAuthentication.class));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        if (intent.getScheme().equals("terms")) {
            Log.e("FOUND","TERMS");
            Toast.makeText(this,"Terms",Toast.LENGTH_SHORT).show();
        }
        else  if (intent.getScheme().equals("privacy")) {
            Log.e("FOUND","TERMS");
            Toast.makeText(this,"Privacy",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("FOUND","NONE");
        }
    }*/
}


/*
public static class CustomSlide extends SlideFragment {
    private CheckBox checkBox;
    private TextView TermsandCondTxt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_custom_slide, container, false);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        TermsandCondTxt = (TextView) view.findViewById(R.id.termsend);

        TextView tnctxt= (TextView) view.findViewById(R.id.termsend2); //txt is object of TextView
//        tnctxt.setMovementMethod(LinkMovementMethod.getInstance());
        tnctxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://messedup.in/terms_conditions/"));
                startActivity(browserIntent);
            }
        });
        TextView pptxt= (TextView) view.findViewById(R.id.termsend3); //txt is object of TextView
//        pptxt.setMovementMethod(LinkMovementMethod.getInstance());
        pptxt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://messedup.in/PrivacyPolicy/"));
                startActivity(browserIntent);
            }
        });

        */
/*String termsAndConditions = "Terms and Conditions";
        String privacyPolicy = "Privacy Policy";

        TermsandCondTxt.setText("I agree to Messed Up's "+termsAndConditions+" and "+privacyPolicy);
        TermsandCondTxt.setMovementMethod(LinkMovementMethod.getInstance());

        Pattern termsAndConditionsMatcher = Pattern.compile(termsAndConditions);
        Linkify.addLinks(TermsandCondTxt, termsAndConditionsMatcher, "terms:");

        Pattern privacyPolicyMatcher = Pattern.compile(privacyPolicy);
        Linkify.addLinks(TermsandCondTxt, privacyPolicyMatcher, "privacy:");
*//*


//        customTextView(TermsandCondTxt);
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

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "I agree to Messed Up's ");
        spanTxt.append("Term and Conditions");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                Toast.makeText(getApplicationContext(), "Terms of services Clicked",
//                        Toast.LENGTH_SHORT).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://messedup.in/terms_conditions/"));
                startActivity(browserIntent);
            }
        }, spanTxt.length() - "Term and Conditions".length(), spanTxt.length(), 0);
        spanTxt.append(" and");
        spanTxt.setSpan(new ForegroundColorSpan(Color.WHITE), 32, spanTxt.length(), 0);
        spanTxt.append(" Privacy Policy");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://messedup.in/PrivacyPolicy/"));
                startActivity(browserIntent);
//                Toast.makeText(getApplicationContext(), "Privacy Policy Clicked",
//                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - " Privacy Policy".length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }




}*/
