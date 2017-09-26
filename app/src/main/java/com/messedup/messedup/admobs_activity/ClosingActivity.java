package com.messedup.messedup.admobs_activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.messedup.messedup.R;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;

public class ClosingActivity extends AppCompatActivity {


    TextView ClosingMsgTxt;
    static int TIMEOUT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_closing);

        ClosingMsgTxt=(TextView)findViewById(R.id.ClosingTxtView);
        FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        DetailsSharedPref dobj=new DetailsSharedPref(this);

        for (UserInfo profile : CurrentUser.getProviderData()) {

            String providerId = profile.getProviderId();
            Log.e("PROVDEL ",providerId);

            if (providerId.equals("google.com")) {
                String nm = profile.getDisplayName();
                Log.e("NAAAMMMEE ",nm);
                ClosingMsgTxt.setText("Goodbye, "+nm+"\n\nEnjoy Your Meal Today!");
                dobj.updateEmailSharedPrefs(nm);
            }

        }



        if(!dobj.getNameSharedPrefs().equals("NAME")) {
            String name;
            name = dobj.getNameSharedPrefs();
            ClosingMsgTxt.setText("Goodbye, " + name + "\n\nEnjoy Your Meal Today!");
        }


            Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                appExit();
            }
        }, TIMEOUT);

    }


    public void appExit () {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //System.exit(0);

    }
}
