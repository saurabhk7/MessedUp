package com.messedup.messedup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.transition.Fade;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.messedup.messedup.adapters.TabsPagerAdapter;
import com.messedup.messedup.connection_handlers.HttpHandler;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;
import com.messedup.messedup.ui_package.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MessInfoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private TextView toolbarTextView, LunchTimeTxt, DinnerTimeTxt,GuestTxt,MonthyTxt,AddressTxt;
    public static ArrayList<ArrayList> fullData = new ArrayList<>();
    public static HashMap<String, String> Hashmessinfo = new HashMap<>();



    private String lunchtxt,dinnertxt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_info);

        setupWindowAnimations();


        toolbar = (Toolbar) findViewById(R.id.toolbar3);

        LunchTimeTxt = (TextView) findViewById(R.id.LunchTimingTxtView);
        DinnerTimeTxt = (TextView) findViewById(R.id.DinnerTimingTxtView);
        GuestTxt=(TextView)findViewById(R.id.OneTimeCostText);
        MonthyTxt=(TextView)findViewById(R.id.MonthlyCostTxtView);
        AddressTxt=(TextView)findViewById(R.id.AddTxtView);






        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);


        String MessID;
        MenuCardView MessObj = null;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                MessID = null;
            } else {
                MessObj = (MenuCardView) extras.getSerializable("messobj");
                if (MessObj != null) {
                    MessID = MessObj.getMessID();
                    String urlname= getURLString( MessID );
                    Toast.makeText(this, "Show Info of : " + urlname, Toast.LENGTH_SHORT).show();
                    toolbarTextView.setText(MessID);
                    GetMenu obj = new GetMenu(MessInfoActivity.this,urlname);
                    obj.execute();
                    GetMessInfo Infoobj = new GetMessInfo(MessInfoActivity.this,urlname);
                    Infoobj.execute();


                }
            }
        } else {
            MessObj = (MenuCardView) savedInstanceState.getSerializable("messid");
            if (MessObj != null) {
                MessID = MessObj.getMessID();

                String urlname =getURLString( MessID );

                Toast.makeText(this, "Show Info of2 : " + urlname, Toast.LENGTH_SHORT).show();
                toolbarTextView.setText(MessID);

                GetMenu obj = new GetMenu(MessInfoActivity.this,urlname);
                obj.execute();
                GetMessInfo Infoobj = new GetMessInfo(MessInfoActivity.this,urlname);
                Infoobj.execute();




            }

        }


        final ImageView ProfilePic = (ImageView)findViewById(R.id.ProfilePicImg);




        String name= null;
        if (MessObj != null) {
            name = (MessObj.getMessID().substring(0,1)).toLowerCase();
        }

        TextDrawable drawable=TextDrawable.builder()
                .beginConfig()
                .fontSize(90)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(name, Color.parseColor("#da3340"));

        ProfilePic.setImageDrawable(drawable);

      /*  int id = getResources().getIdentifier("com.messedup.messedup:drawable/" + name, null, null);


        Picasso.with(this).load(id).transform(new CircleTransform()).into(ProfilePic);
*/




       /* GetMenu obj = new GetMenu(MessInfoActivity.this);
        obj.execute();
        GetMessInfo Infoobj = new GetMessInfo(MessInfoActivity.this);
        Infoobj.execute();
*/




        /*viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),fullData);
        tabLayout = (TabLayout) findViewById(R.id.tab);

        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);*/


    }

    private String getURLString(String messID) {


        messID=messID.replace(",","");
        messID=messID.replace(" ","%20");
        return messID;

    }


    public void loadTabs() {
        viewPager = (ViewPager) findViewById(R.id.pager);
//        Log.e("asdfssad", fullData.toString());
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fullData);
        tabLayout = (TabLayout) findViewById(R.id.tab);

        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }



    class GetMenu extends AsyncTask<Void, Void, Void> {

        private String TAG = MainActivity.class.getSimpleName();
        private ProgressDialog pDialog;
        private Context mcontext;
        private String urlMess;
//    public ArrayList<ArrayList> fullData = new ArrayList<>();

        public GetMenu(Context context,String urlname) {
            mcontext = context;
            urlMess=urlname;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//         Showing progress dialog
            pDialog = new ProgressDialog(mcontext);
            pDialog.setMessage("Getting Menu...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getMenu.php?messname=" + urlMess);
            // String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getMenu.php?messname=Anand%20Food%20Xprs");

            Log.e(TAG, "Response from url: " + jsonStr);

            try {
                if (jsonStr != null) {

                    JSONArray details = new JSONArray(jsonStr);

                    for (int i = 0; i < details.length(); i++) {
                        JSONObject m = details.getJSONObject(i);

                        String date = m.getString("date");
                        String day = m.getString("day");
                        HashMap<String, String> info = new HashMap<>();
                        info.put("date", date);
                        info.put("day", day);

                        JSONArray menu = m.getJSONArray("menu");
                        ArrayList<HashMap> m3 = new ArrayList<>();

                        HashMap<String, ArrayList> m2 = new HashMap<>();
                        for (int j = 0; j < menu.length(); j++) {
                            JSONObject m1 = menu.getJSONObject(j);

                            String Meal = m1.getString("Meal");
                            String Rice = m1.getString("Rice");
                            String VegieOne = m1.getString("VegieOne");
                            String VegieTwo = m1.getString("VegieTwo");
                            String VegieThree = m1.getString("VegieThree");
                            String Roti = m1.getString("Roti");
                            String Special = m1.getString("Special");
                            String SpecialExtra = m1.getString("SpecialExtra");
                            String Other = m1.getString("Other");

                            HashMap<String, String> menuhash = new HashMap<>();

                            menuhash.put("Meal", Meal);
                            menuhash.put("Rice", Rice);
                            menuhash.put("VegieOne", VegieOne);
                            menuhash.put("VegieTwo", VegieTwo);
                            menuhash.put("VegieThree", VegieThree);
                            menuhash.put("Roti", Roti);
                            menuhash.put("Special", Special);
                            menuhash.put("SpecialExtra", SpecialExtra);
                            menuhash.put("Other", Other);

                            m3.add(menuhash);

//                            m2.put("menu", menuhash);
                        }
                        m2.put("menu", m3);

                        ArrayList<HashMap> node = new ArrayList<>();
                        node.add(info);
                        node.add(m2);

//                    System.out.println(node);
                        fullData.add(node);
                    }
                } else {
                    Toast.makeText(mcontext, "Oops,Some Error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            Toast.makeText(mcontext, "Menu Updated", Toast.LENGTH_SHORT).show();

            loadTabs();
        }
    }


    class GetMessInfo extends AsyncTask<Void, Void, Void>{

        private String TAG = MainActivity.class.getSimpleName();
        private ProgressDialog pDialog;
        private Context mcontext;
        private String urlMess;

        public GetMessInfo(Context context,String urlname){
            mcontext = context;
            urlMess=urlname;
        }

        protected void onPreExecute() {
            super.onPreExecute();
//         Showing progress dialog
            pDialog = new ProgressDialog(mcontext);
            pDialog.setMessage("Getting the Mess Info...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();
            Log.e("url mess: ",urlMess);
            String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getMessInfo.php?messname=" + urlMess);
            // String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getMessInfo.php?messname=Anand Food Xprs");

            Log.e(TAG, "Response from url: " + jsonStr);
            try {
                if (jsonStr != null) {

                    JSONObject messinfo = new JSONObject(jsonStr);

                    Log.e("Mess InfoStr ","*"+messinfo.toString());


//                    String Name = messinfo.get("Name");

                    Hashmessinfo.put("Name", messinfo.getString("Name"));
                    Hashmessinfo.put("GuestCharge", messinfo.getString("GuestCharge"));
                    Hashmessinfo.put("LunchOpen", messinfo.getString("LunchOpen"));
                    Hashmessinfo.put("LunchClose", messinfo.getString("LunchClose"));
                    Hashmessinfo.put("DinnerOpen", messinfo.getString("DinnerOpen"));
                    Hashmessinfo.put("DinnerClose", messinfo.getString("DinnerClose"));
                    Hashmessinfo.put("MonthlyCharge", messinfo.getString("MonthlyCharge"));
                    Hashmessinfo.put("Contact", messinfo.getString("Contact"));
                    Hashmessinfo.put("Address", messinfo.getString("Address"));
                    Hashmessinfo.put("Location", messinfo.getString("Location"));
                    Hashmessinfo.put("NBCollege", messinfo.getString("NBCollege"));
                    Hashmessinfo.put("Owner", messinfo.getString("Owner"));

                } else {
                    Toast.makeText(mcontext, "Oops,Some Error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
//            loadmessinfo();

            Toast.makeText(mcontext, "Mess Info Updated", Toast.LENGTH_SHORT).show();
            SetDetails(Hashmessinfo);
            Log.e("Mess Info ","*"+Hashmessinfo.toString());
        }
    }

    private void SetDetails(HashMap<String, String> hashmessinfo) {


        loadBottomNavigationView(hashmessinfo.get("Contact"),hashmessinfo.get("Location"),hashmessinfo);
        setMessTimeDetails(hashmessinfo.get("LunchOpen"),hashmessinfo.get("LunchClose"),
                hashmessinfo.get("DinnerOpen"),hashmessinfo.get("DinnerClose"));
        setPrices(hashmessinfo.get("GuestCharge"),hashmessinfo.get("MonthlyCharge"));
        setAddress(hashmessinfo.get("Address"));


    }




    private void loadBottomNavigationView(final String num, final String loc, final HashMap<String, String> hashmessinfo) {

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_call:
//                                This is how to ask permission and make calls
                                int REQUEST_PHONE_CALL = 1;
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+91"+num));

                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (ContextCompat.checkSelfPermission(MessInfoActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(MessInfoActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                                    } else {
                                        startActivity(intent);
                                    }
                                } else {
                                    startActivity(intent);
                                }
                                break;

                            case R.id.action_locate:
                                String uri = String.format(Locale.ENGLISH, loc);//"geo:18.457542,73.850834?q=life+gym");//, latitude, longitude);
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                startActivity(intent1);
                                break;

                            case R.id.action_share:
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String shareBody = "*"+hashmessinfo.get("Name")+"*"
                                        +"\n"+"Address: "+hashmessinfo.get("Address")
                                        +"\n"+"Monthly Charge: ₹"+hashmessinfo.get("MonthlyCharge")
                                        +"\n"+"Guest Charge: ₹"+hashmessinfo.get("GuestCharge")
                                        +"\n"+"Contact: "+hashmessinfo.get("Contact")+
                                        "\n\nTap for more: "+
                                        "https://goo.gl/KiLH44 \n\n";
//                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                startActivity(sharingIntent);
                                break;
                        }
                        return false;
                    }
                });

    }

    private void setMessTimeDetails(String lotime,String lctime,String dotime,String dctime) {

        lunchtxt = "<b>Lunch: </b>"+lotime+" to "+lctime;
        dinnertxt = "<b>Dinner: </b>"+dotime+" to "+dctime;

        LunchTimeTxt.setText(Html.fromHtml(lunchtxt));
        DinnerTimeTxt.setText(Html.fromHtml(dinnertxt));


    }

    private void setPrices(String guestCharge, String monthlyCharge) {

        GuestTxt.setText("Guest Charge: ₹"+guestCharge);
        MonthyTxt.setText("Monthly Charge: ₹"+monthlyCharge);


    }

    private void setAddress(String address) {

        AddressTxt.setText(address);

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(new Explode());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
