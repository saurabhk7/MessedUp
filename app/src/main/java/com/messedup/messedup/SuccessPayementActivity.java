package com.messedup.messedup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SuccessPayementActivity extends AppCompatActivity {


    String explainString1, explainString2, explainString3, explainString4,infotxtStr;
    TextView explainTextView1, explainTextView2, explainTextView3, explainTextView4,InfoTxtView;

    String totaltokens, amount,orderid,paymentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_payement);


        setTitle("Payment Complete");


        final KonfettiView konfettiView = (KonfettiView)findViewById(R.id.konfettiView);


        Intent successIntent = getIntent();
        Bundle extras = successIntent.getExtras();
        if (extras != null) {
            totaltokens = extras.getString("totaltokens");
            amount = extras.getString("amount");
            orderid = extras.getString("orderid");
            paymentid = extras.getString("paymentid");

            //The key argument here must match that used in the other activity
        }


        explainTextView1 = (TextView)findViewById(R.id.explaintxt1);
        explainTextView2 = (TextView)findViewById(R.id.explaintxt2);
        explainTextView3 = (TextView)findViewById(R.id.explaintxt3);
        explainTextView4 = (TextView)findViewById(R.id.explaintxt4);

        InfoTxtView = (TextView)findViewById(R.id.infotxt);

        explainString1 = "<b><font color=#424242>Payment ID:</font></b> "+paymentid;
        explainString2 = "<b><font color=#424242>Order ID:</font></b> "+orderid;
        explainString3 = "<b><font color=#424242>Total Tokens:</font></b> "+totaltokens;
        explainString4 = "<b><font color=#424242>Amount Paid:</font></b> ₹"+amount;
        infotxtStr = "invoice will be mailed to <br>your registered email address";

        explainTextView1.setText(Html.fromHtml(explainString1));
        explainTextView2.setText(Html.fromHtml(explainString2));
        explainTextView3.setText(Html.fromHtml(explainString3));
        explainTextView4.setText(Html.fromHtml(explainString4));

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


        Button completeBtn = (Button)findViewById(R.id.complete_payment_btn);

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SuccessPayementActivity.this,MainActivity.class));
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuccessPayementActivity.this,MainActivity.class));
    }
}
