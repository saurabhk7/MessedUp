package com.messedup.messedup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cdflynn.android.library.checkview.CheckView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TokenUseSuccess extends AppCompatActivity {

    String explainString1, explainString2, explainString3, headingTxtexplainString4,infotxtStr;
    TextView explainTextView1, explainTextView2, explainTextView3, headingTxtexplainTextView4,InfoTxtView;

    String totaltokens, messname,timeuse,dateuse;

    CheckView mCheckView;

    Button doneGoBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_use_success);


        setTitle("Token Usage Summary");


        final KonfettiView konfettiView = (KonfettiView)findViewById(R.id.konfettiView);
        mCheckView = (CheckView)findViewById(R.id.check_anim);

        doneGoBackBtn = (Button)findViewById(R.id.complete_token_btn);

        Intent successIntent = getIntent();
        Bundle extras = successIntent.getExtras();
        if (extras != null) {
            totaltokens = extras.getString("totaltokensleft");
            messname = extras.getString("messname");
            timeuse = extras.getString("timeused");
            dateuse = extras.getString("dateused");

            //The key argument here must match that used in the other activity
        }


        explainTextView1 = (TextView)findViewById(R.id.explaintxt1);
        explainTextView2 = (TextView)findViewById(R.id.explaintxt2);
        explainTextView3 = (TextView)findViewById(R.id.explaintxt3);
        headingTxtexplainTextView4 = (TextView)findViewById(R.id.headingTxt);

        InfoTxtView = (TextView)findViewById(R.id.infotxt);

        explainString1 = "<b><font color=#424242>Time Used:</font></b> "+timeuse;
        explainString2 = "<b><font color=#424242>Date Used:</font></b> "+dateuse;
        explainString3 = "<b><font color=#424242>Total Tokens Left:</font></b> "+totaltokens;
        headingTxtexplainString4 = messname;
        infotxtStr = "Show this to the mess owner and<br><b>Get Confirmation</b>";

        explainTextView1.setText(Html.fromHtml(explainString1));
        explainTextView2.setText(Html.fromHtml(explainString2));
        explainTextView3.setText(Html.fromHtml(explainString3));
        headingTxtexplainTextView4.setText(Html.fromHtml(headingTxtexplainString4));

        InfoTxtView.setText(Html.fromHtml(infotxtStr));

        konfettiView.build()
                .addColors(Color.RED, Color.GREEN, Color.CYAN, Color.BLUE, Color.YELLOW)
                .setDirection(359.0,270.0)
                .setSpeed(1f, 15f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2500L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(10, 4))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                mCheckView.check();

            }
        };


        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1500, 1500);

        doneGoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
