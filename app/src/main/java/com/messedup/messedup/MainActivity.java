package com.messedup.messedup;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.GeneralSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.SharedPrefHandler;
import com.messedup.messedup.admobs_activity.AdMobsActivity;
import com.messedup.messedup.admobs_activity.ClosingActivity;
import com.messedup.messedup.connection_handlers.HttpHandler;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;
import com.messedup.messedup.ui_package.CircleTransform;
import com.messedup.messedup.ui_package.CustomAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.support.v4.app.FragmentTransaction.TRANSIT_NONE;

public class MainActivity extends AppCompatActivity {



    public static  Spinner spinner;
    public static  ArrayList<String> college_list=new ArrayList<>();
    public static final HashSet<String> college_list2=new HashSet<>();
    public static AdRequest adRequest;
    public BottomBarTab notifs;
    private DetailsSharedPref detailsSharedPref;
    public InterstitialAd mInterstitialAd= new InterstitialAd(this);




    /*UI Attributes*/
    Toolbar toolbar;



    //DATABASE AND SHARED PREF OBJECTS
    DatabaseHandler databaseHandler;
    SharedPrefHandler sharedPrefHandler;

    //LOADED MENU ARRAYLIST TO POPULATE CARD VIEW
    // ArrayList<HashMap<String, String>> MenuArrayList = new ArrayList<>();

    //USER TO CHECK IF BACK CLICKED TWICE
    boolean doubleBackToExitPressedOnce = false;



    /**
     * @author saurabh
     * @use Main Home Screen Rendering and display fragments
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_main);


        //initializing the toolbar view
        initToolBar();
      //  loadAddinBack();
        addHitCount();


        //startActivity(new Intent(MainActivity.this, IntroActivity.class));





        detailsSharedPref=new DetailsSharedPref(this);

        if(!detailsSharedPref.getDetailsSent().equals("success"))
            getUserDetails(this);


        if(detailsSharedPref.getWalkThroughStatus().equals("notdone"))
            startIntro(this);

        // Firebase User Error Handling
        /*TextView phoneTxtView = (TextView)findViewById(R.id.PhoneNumTxtView);
        phoneTxtView.setVisibility(View.GONE);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Toast.makeText(this,"Some Error occurred, Please try again!",Toast.LENGTH_SHORT).show();
        }
*/

        //Setting Up Bottombar
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_menu); //sets the initial tab as menu tab
        notifs = bottomBar.getTabWithId(R.id.tab_notifs);
        int notifcount; //TODO: get the no. of offers count from count(*) off offers offline table
        notifcount=3;   //temp value
        notifs.setBadgeCount(0);


        int REQUEST_PHONE_CALL = 1;
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }


        final int[] PREVIOUS_TAB = {R.id.tab_menu};
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes final int tabId) {
                Fragment selectedFragment = null;
                switch (tabId) {
                    case R.id.tab_notifs:
                        spinner = (Spinner) findViewById(R.id.categorySpinner);
                        spinner.animate().alpha(0.0f).setDuration(500)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        spinner.setVisibility(View.GONE);
                                        TextView toolup=(TextView)toolbar.findViewById(R.id.spinner_text_title);
                                        TextView tooldown=(TextView)toolbar.findViewById(R.id.spinner_text_view);

                                        toolup.setVisibility(View.VISIBLE);
                                        tooldown.setVisibility(View.VISIBLE);

                                        toolup.setText("SELECTED AREA");

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        String PreStoredArea = preferences.getString("selectedarea", getApplicationContext().getString(R.string.pict));
                                        tooldown.setText(PreStoredArea);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });


                        //  spinner.setVisibility(View.INVISIBLE);

                        selectedFragment = NotifFragment.newInstance();
                        replaceFragmentWithAnimationtoRight(selectedFragment, "tag");
                        notifs.removeBadge();
                        PREVIOUS_TAB[0] =R.id.tab_notifs;
                        break;
                    case R.id.tab_menu:
                        spinner = (Spinner) findViewById(R.id.categorySpinner);
                        spinner.animate().alpha(1.0f).setDuration(0)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        TextView toolup=(TextView)toolbar.findViewById(R.id.spinner_text_title);
                                        TextView tooldown=(TextView)toolbar.findViewById(R.id.spinner_text_view);

                                        toolup.setVisibility(View.GONE);
                                        tooldown.setVisibility(View.GONE);
                                        spinner.setVisibility(View.VISIBLE);

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });

                        //  spinner.setVisibility(View.VISIBLE);
                        selectedFragment = MenuFragment.newInstance();
                        ;
                        if(PREVIOUS_TAB[0]==R.id.tab_profile)
                            replaceFragmentWithAnimationtoRight(selectedFragment, "tag");
                        else
                            replaceFragmentWithAnimationtoLeft(selectedFragment, "tag");

                        PREVIOUS_TAB[0] =R.id.tab_menu;
                        break;
                    case R.id.tab_profile:
                        spinner = (Spinner) findViewById(R.id.categorySpinner);
                        spinner.animate().alpha(0.0f).setDuration(500)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animator) {
                                        spinner.setVisibility(View.GONE);
                                        TextView toolup=(TextView)toolbar.findViewById(R.id.spinner_text_title);
                                        TextView tooldown=(TextView)toolbar.findViewById(R.id.spinner_text_view);

                                        toolup.setVisibility(View.VISIBLE);
                                        tooldown.setVisibility(View.VISIBLE);

                                        toolup.setText("SELECTED AREA");


                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        String PreStoredArea = preferences.getString("selectedarea", getApplicationContext().getString(R.string.pict));
                                        tooldown.setText(PreStoredArea);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animator) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animator) {

                                    }
                                });

                        // spinner.setVisibility(View.INVISIBLE);
                        selectedFragment = ProfileFragment.newInstance();
                        replaceFragmentWithAnimationtoLeft(selectedFragment, "tag");
                        PREVIOUS_TAB[0] =R.id.tab_profile;
                        break;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();


            }
        });


        //Initialize the first fragment
        Fragment FirstFrag = MenuFragment.newInstance();

        replaceFragmentWithAnimationtoRight(FirstFrag, "tag");




    }

    private void addHitCount() {

        HitCount hitCount=new HitCount();

        hitCount.execute("http://wanidipak56.000webhostapp.com/updateHitCount.php");

    }




    public class HitCount extends AsyncTask<String , Void ,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    Log.v("CatalogClient", "OK");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    private void getUserDetails(Context context) {

        String Username = null,UserID,UserEmail=null,UserContact;



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int i = 0;
            FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();



            for (UserInfo profile : CurrentUser.getProviderData()) {

                i++;
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider

                // Name, email address, and profile photo Url



                if (providerId.equals("google.com")) {
                    Username = profile.getDisplayName();

                }



                if (providerId.equals("firebase")) {

                    UserEmail = profile.getEmail();

                }



            }


            UserContact=CurrentUser.getPhoneNumber();

            UserID=CurrentUser.getUid();

            sendUserDetails(UserID,Username,UserEmail,UserContact,context);

        }






    }

    public void sendUserDetails(String UserID,String Username,String UserEmail,String UserContact ,Context context)
    {
        Log.e("UID: ",""+UserID);

        Log.e("NAME: ",Username);

        Log.e("EMAIL: ",UserEmail);

        Log.e("PHONE: ",""+UserContact);


        SendUserDetails sendUserDetails=new SendUserDetails(UserID,Username,UserEmail,UserContact,context);


        sendUserDetails.execute();

    }

    private void loadAddinBack() {

        DetailsSharedPref dsp3=new DetailsSharedPref(this);
        dsp3.updateAdLoadStatus("notloaded");

        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        if(isNetworkAvailable()) {
           // LoadAd mLoadAd=new LoadAd(this);
           // mLoadAd.execute();

        }



    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent adIntent=new Intent(this, AdMobsActivity.class);

            if(isNetworkAvailable()) {
               // showInterstitial();

                appExit();
            }
            else
                appExit();

            //     this.finish(); // finish activity
            //  return;

            // moveTaskToBack(true);

        }


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back again to exit!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else
        {
            appExit();
            Log.e("AD NOT LOADED","NOT LOADED");
        }
    }



    /**
     * @use initializes the toolbar view
     */
    public void initToolBar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    public void replaceFragmentWithAnimationtoLeft(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(TRANSIT_NONE);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void replaceFragmentWithAnimationtoRight(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(TRANSIT_NONE);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        final ProgressDialog pDialogGetNB;


        Log.e("BEFORE CLEAR","**"+college_list.toString());

        college_list.clear();

        Log.e("AFTER CLEAR","**"+college_list.toString());


        if(isNetworkAvailable()) {


            Toast.makeText(this,"Searching messes in your Area!",Toast.LENGTH_SHORT).show();


            int SPLASH_TIME_OUT=6000;
            final GetNBCollege getNBCollege=new GetNBCollege(this);
            getNBCollege.execute();

            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {

                    if(getNBCollege.getStatus() == AsyncTask.Status.RUNNING)
                    {
                        getNBCollege.cancel(true);
                        Toast.makeText(MainActivity.this, "Slow Internet :/", Toast.LENGTH_LONG).show();

                        DetailsSharedPref dspobj2=new DetailsSharedPref(getApplicationContext());
                        dspobj2.updateMealStatusSharedPref("OFFLINE");


                        DatabaseHandler mdDatabaseHandleroffline=new DatabaseHandler(getApplicationContext());

                        college_list= mdDatabaseHandleroffline.getAllNBCollege();

                        Log.e("SIZE:","+"+college_list.size());

                        college_list=removeDup(college_list);


                        if(college_list.size()==0)
                        {


                            ImageView ErrorImg=(ImageView)findViewById(R.id.ErrorImgView);
                            TextView ErrotTxt=(TextView)findViewById(R.id.ErrorTxtView);

                            ErrorImg.setVisibility(View.VISIBLE);
                            ErrotTxt.setVisibility(View.VISIBLE);
                            /*college_list.add(getBaseContext().getString(R.string.pict));

                            DatabaseHandler mDatabaseHandleraddDummy=new DatabaseHandler(getBaseContext());



                            String json = "{\"messinfo\":[{\"messid\":\"Please Check you connection!\",\"rice\":null,\"vegieone\":null,\"vegietwo\":null,\"vegiethree\":null,\"roti\":null,\"special\":null,\"specialextra\":null,\"other\":null,\"opentime\":\"19:00\",\"closetime\":\"21:30\",\"openstatus\":\"0\",\"gcharge\":null,\"status\":\"0\"},{\"messid\":\"Anand Food Xprs\",\"rice\":\"Rice\",\"vegieone\":\"Kutte\",\"vegietwo\":\"Free Account\",\"null\":\"null\",\"roti\":\"null\",\"special\":\"\",\"specialextra\":\"null\",\"other\":\"null\",\"opentime\":\"null\",\"closetime\":\"null\",\"openstatus\":\"0\",\"gcharge\":\"null\",\"status\":\"0\"}],\"meal\":\"offline\",\"success\":1}";
                            mDatabaseHandleraddDummy.updateMenuCard(getBaseContext().getString(R.string.pict),json);

                            Log.e("updated dummy: ","+"+json);*/

                        }
                        else if((college_list.size()==1&&college_list.contains("Select your Area")))
                        {
                            ImageView ErrorImg=(ImageView)findViewById(R.id.ErrorImgView);
                            TextView ErrotTxt=(TextView)findViewById(R.id.ErrorTxtView);

                            ErrorImg.setVisibility(View.VISIBLE);
                            ErrotTxt.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ImageView ErrorImg=(ImageView)findViewById(R.id.ErrorImgView);
                            TextView ErrotTxt=(TextView)findViewById(R.id.ErrorTxtView);

                            ErrorImg.setVisibility(View.INVISIBLE);
                            ErrotTxt.setVisibility(View.INVISIBLE);
                        }

                        //TODO: take college list from spinner


                        Log.e("ADDED NO INTER","**"+college_list.toString());

                        Collections.sort(college_list);

                        // college_list.add(0,"Select your Area");


                        //SET THE SPINNER
                        spinner = (Spinner) findViewById(R.id.categorySpinner);
                        spinner.setAdapter(null);
                        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), college_list);
                        spinner.setAdapter(customAdapter);

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String PreStoredArea = preferences.getString("selectedarea", getApplicationContext().getString(R.string.pict));
                        Log.d("MAINACTIVTY SHARED PREF", "GOT STRING " + PreStoredArea);

                        int ind = college_list.indexOf(PreStoredArea);

                        //INITIALIZE THE SPINNER POSITION
                        spinner.setSelection(ind);

                    }


                }
            }, SPLASH_TIME_OUT);




        }
        else {


            DatabaseHandler mdDatabaseHandleroffline=new DatabaseHandler(this);

            college_list= mdDatabaseHandleroffline.getAllNBCollege();


            /*//TODO: take college list from spinner
            college_list.add(this.getString(R.string.pict));
            college_list.add(this.getString(R.string.bvp));*/

            college_list=removeDup(college_list);

            Log.e("ADDED NO INTER","**"+college_list.toString());


       /* college_list.add(this.getString(R.string.sinhagad));
        college_list.add(this.getString(R.string.skn));
        college_list.add(this.getString(R.string.vit));
        college_list.add(this.getString(R.string.cummins));*/
            Collections.sort(college_list);

            // college_list.add(0,"Select your Area");


            //SET THE SPINNER
            spinner = (Spinner) findViewById(R.id.categorySpinner);
            spinner.setAdapter(null);
            spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), college_list);
            spinner.setAdapter(customAdapter);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String PreStoredArea = preferences.getString("selectedarea", this.getString(R.string.pict));
            Log.d("MAINACTIVTY SHARED PREF", "GOT STRING " + PreStoredArea);

            int ind = college_list.indexOf(PreStoredArea);

            //INITIALIZE THE SPINNER POSITION
            spinner.setSelection(ind);

        }
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Toast.makeText(MainActivity.this, "Search Mess", Toast.LENGTH_SHORT).show();

                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        // Get the MenuItem for the action item

        // Assign the listener to that action item




        return true;
    }

    private ArrayList<String> removeDup(ArrayList<String> college_list) {

        ArrayList<String> nonDupList = new ArrayList<String>();

        Iterator<String> dupIter = college_list.iterator();
        while(dupIter.hasNext())
        {
            String dupWord = dupIter.next();
            if(nonDupList.contains(dupWord))
            {
                dupIter.remove();
            }else
            {
                nonDupList.add(dupWord);
            }
        }

        return nonDupList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            /*case R.id.filtersorticon:
                Toast.makeText(MainActivity.this, "Set The Filter Options", Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void appExit () {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    class GetNBCollege extends AsyncTask<Void, Void, Void> {

        private String TAG = MainActivity.class.getSimpleName();
        private ProgressDialog pDialog;
        private Context mcontext;

        GetNBCollege(Context context)
        {
            mcontext=context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
//         Showing progress dialog
//        pDialog = new ProgressDialog(mcontext);
//        pDialog.setMessage("Getting the Near By Colleges...");
//        pDialog.setCancelable(false);
//        pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getNBCollege.php");

            Log.e(TAG, "Response from url: " + jsonStr);
            try {
                if (jsonStr != null) {

                    JSONObject Colleges = new JSONObject(jsonStr);

                    JSONArray colleges = Colleges.getJSONArray("NBCollege");

                    for(int i=0;i<colleges.length();i++)
                    {
                        Log.e("college "+i, colleges.getString(i));
                        college_list.add(colleges.getString(i));
                    }

                    Log.e("ADDED WITH INTER","**"+college_list.toString());

                } else {
//                Toast.makeText(mcontext, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//        if (pDialog.isShowing()) {


            college_list=removeDup(college_list);

            Collections.sort(college_list);

            // college_list.add(0,"Select your Area");




            //SET THE SPINNER
            spinner=(Spinner)findViewById(R.id.categorySpinner);
            spinner.setAdapter(null);
            spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


            CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),college_list);
            spinner.setAdapter(customAdapter);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
            String PreStoredArea=preferences.getString("selectedarea",  mcontext.getString(R.string.pict));
            Log.d("MAINACTIVTY SHARED PREF","GOT STRING "+PreStoredArea);

            int ind=college_list.indexOf(PreStoredArea);

            //INITIALIZE THE SPINNER POSITION
            spinner.setSelection(ind);

           /* if(pDialogGetNB.isShowing())
                pDialogGetNB.dismiss();
*/


//            pDialog.dismiss();
//        }
        }
    }

    class LoadAd extends AsyncTask<Void, Void, Void> {

        private String TAG = MainActivity.class.getSimpleName();
        private Context mcontext;

        LoadAd(Context context)
        {
            mcontext=context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    // Check the LogCat to get your test device ID
                    .addTestDevice("5C57F9C1972E25B91E244E6898A7A78B")
                    .build();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                    DetailsSharedPref dsp3=new DetailsSharedPref(mcontext);
                    dsp3.updateAdLoadStatus("loaded");
                  //  Toast.makeText(getApplicationContext(), "Ad is loaded! in back", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClosed() {


                  //  Toast.makeText(getApplicationContext(), "Ad is closed! in back", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,ClosingActivity.class));
                }


                @Override
                public void onAdFailedToLoad(int errorCode) {
                  //  Toast.makeText(getApplicationContext(), "Ad failed to load! error code: in back" + errorCode, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,ClosingActivity.class));

                }

                @Override
                public void onAdLeftApplication() {
                  //  Toast.makeText(getApplicationContext(), "Ad left application! in back", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                  //  Toast.makeText(getApplicationContext(), "Ad is opened! in back", Toast.LENGTH_SHORT).show();
                }
            });
            // Toast.makeText(mcontext,"Ad Loaded in back",Toast.LENGTH_SHORT).show();


        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    private void startIntro(final Context mContext)
    {


        TapTargetSequence sequence= new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.spinner_text_title), "Your College Area", "Tap to select your area!\n(If not found we are soon coming there:P)")
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.transparentcol)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.blue)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                // .icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(135) ,                 // Specify the target radius (in dp),

                        TapTarget.forView(findViewById(R.id.tab_menu), "Menu of your selected area", "Menu of the Messes around your area!")
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.blue)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                // .icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(70)  ,                // Specify the target radius (in dp)

                        TapTarget.forView(findViewById(R.id.tab_notifs), "Notifications and Announcements", "You'll get all the updates regarding the Messes around your area!")
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.blue)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                // .icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(70)  ,                // Specify the target radius (in dp)
                        TapTarget.forView(findViewById(R.id.tab_profile), "Your Profile", "Your details ! \n(Which you already know :P)")
                                .outerCircleColor(R.color.colorPrimary)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.blue)  // Specify the color of the description text
                                .textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                // .icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(70)                 // Specify the target radius (in dp),


                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {

                        DetailsSharedPref dsp=new DetailsSharedPref(mContext);

                        dsp.updateWalkThrough("done");
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }


                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo

                        DetailsSharedPref dsp=new DetailsSharedPref(mContext);

                        dsp.updateWalkThrough("done");
                    }
                });

        sequence.start();

    }


    class SendUserDetails extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context thiscontext;
        String uid,username,useremail,usercontact;

        String json = "";

        SendUserDetails(String s1,String s2,String s3,String s4,Context context)
        {

            uid=s1;
            username=s2;
            useremail=s3;
            usercontact=s4;

            thiscontext=context;


        }


        // products JSONArray

        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";
        private String url_mess_menu = "https://wanidipak56.000webhostapp.com/postUserInfo.php";
        //ArrayList<HashMap<String, String>> messList;


        /**
         * @use clear the initial Hashmap
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        /**
         * @param args
         * @use to download the latest menu in the background
         */
        protected String doInBackground(String... args) {

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                //constants
                URL url = new URL(url_mess_menu);
                JSONObject jsonObject = new JSONObject();


                Log.e("!!!UID: ",uid);

                Log.e("!!!NAME: ",username);

                Log.e("!!!EMAIL: ",useremail);

                Log.e("!!!PHONE: ",usercontact);


                jsonObject.put("userid", uid);
                jsonObject.put("name", username);
                jsonObject.put("email", useremail);
                jsonObject.put("phone", usercontact);



                String message = jsonObject.toString();

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();


                //do somehting with response
                is = conn.getInputStream();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();



                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                Log.i("nnnReply",json);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;


        }

        /**
         * After completing background task Dismiss the progress dialog
         * @use Stores the Downloaded JSON into ArrayList of HashMaps
         **/
        protected void onPostExecute(String file_url) {


            if (json.equals("success")) {

                DetailsSharedPref dsp=new DetailsSharedPref(thiscontext);

               // Toast.makeText(thiscontext,json,Toast.LENGTH_SHORT).show();

                dsp.updateDetailsSent("success");

            }

        }





    }



}
