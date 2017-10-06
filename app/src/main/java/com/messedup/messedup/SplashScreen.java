package com.messedup.messedup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.GeneralSharedPref;
import com.messedup.messedup.connection_check.ConnectionManager;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.messedup.messedup.signin_package.GoogleSignIn;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;
import com.rampo.updatechecker.notice.Notice;
import com.rampo.updatechecker.store.Store;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author saurabh
 * @use First Loading Screen ( Thread Used to load data in the background )
 */
public class SplashScreen extends AppCompatActivity implements UpdateCheckerResult {


    private static final String COMP_UPDATE_KEY = "compulsary_update";
    private static final String STORE_VERSION_KEY = "store_version_code";



    public ConnectionManager connectionManager;
    LoadAllMess lam;
    public DatabaseHandler db;

    int SPLASH_TIME_OUT;
    public ArrayList<HashMap<String, String>> AllMessInfoFromDatabaseSplash = new ArrayList<>();
    private static Context thiscontext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DetailsSharedPref dspobj2;
    public FirebaseRemoteConfig mFirebaseRemoteConfig;

    UpdateChecker checker = new UpdateChecker(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Splash screen timer



        thiscontext=getBaseContext();
        lam = new LoadAllMess(thiscontext);
        db=new DatabaseHandler(thiscontext);
        dspobj2=new DetailsSharedPref(thiscontext);

        checker.setStore(Store.GOOGLE_PLAY);
        checker.setSuccessfulChecksRequired(1);
        checker.setNotice(Notice.DIALOG);
        checker.start();




        mAuth=FirebaseAuth.getInstance();

        checkupdate(this);


        //   ForceUpdateChecker.with(this).onUpdateNeeded(this).check();


        /*mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);


        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        fetchWelcome();

*/




        ShimmerFrameLayout container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        Log.e("sdf", String.valueOf(container.getDropoff()));
//        container.setBaseAlpha((float) 0.59);
//        container.setIntensity((float) 0.1);
        container.setDropoff((float) 0.6);
        container.startShimmerAnimation();




        connectionManager=new ConnectionManager(this);

        if(connectionManager.isNetworkAvailable()) {

            SPLASH_TIME_OUT=7000;

        }
        else
        {
            SPLASH_TIME_OUT=3000;
        }

        if(connectionManager.isNetworkAvailable())
        {


            lam = new LoadAllMess(thiscontext);
            lam.execute();



            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    if(lam.getStatus() == AsyncTask.Status.RUNNING)
                    {
                        lam.cancel(true);
                        Toast.makeText(SplashScreen.this, "Slow Internet :/", Toast.LENGTH_SHORT).show();
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if(currentUser!=null)
                        {
                            GeneralSharedPref gobj=new GeneralSharedPref(thiscontext);

                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            gobj.updateFromSharedPref("splash");
                            dspobj2.updateMealStatusSharedPref("OFFLINE");

                            Toast.makeText(getBaseContext(), "Oops,Error Updating Mess Menu", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                            finish();
                        }
                        else {

                            GeneralSharedPref gobj=new GeneralSharedPref(thiscontext);
                            dspobj2.updateMealStatusSharedPref("OFFLINE");
                            gobj.updateFromSharedPref("splash");

                            if(dspobj2.getIntroDone().equals("notdone")) {
                               // Intent i = new Intent(SplashScreen.this, PhoneNumberAuthentication.class);
                                Intent i = new Intent(SplashScreen.this, IntroActivity.class);

                                startActivity(i);
                                finish();                                //startActivity(i);
                               // finish();

                            }
                            else
                            {
                                /*Intent i = new Intent(SplashScreen.this, PhoneNumberAuthentication.class);*/
                                Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                                startActivity(i);
                                finish();

                            }

                        }
                        // close this activity
                        finish();
                        //  Toast.makeText(getApplicationContext(),"could not load mess",Toast.LENGTH_LONG);
                    }


                }
            }, SPLASH_TIME_OUT);


        }
        else {



            dspobj2.updateMealStatusSharedPref("OFFLINE");
            SuperActivityToast.create(SplashScreen.this, new Style(), Style.TYPE_BUTTON)
                    .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                    .setText("Oops, No Network Available!")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_LOLLIPOP)
                    .setColor(Color.parseColor("#cb202d"))
                    .setAnimations(Style.ANIMATIONS_POP).show();


            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {

                    GeneralSharedPref gobj=new GeneralSharedPref(thiscontext);

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser!=null)
                    {

                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        gobj.updateFromSharedPref("splash");
                        dspobj2.updateMealStatusSharedPref("OFFLINE");
                        Toast.makeText(getBaseContext(), "Oops,Error Updating Mess Menu", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
                    }
                    else {

                        dspobj2.updateMealStatusSharedPref("OFFLINE");
                        gobj.updateFromSharedPref("splash");

                        if(dspobj2.getIntroDone().equals("notdone")) {
                            // Intent i = new Intent(SplashScreen.this, PhoneNumberAuthentication.class);
                            Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                            gobj.updateFromSharedPref("splash");
                            startActivity(i);
                            finish();                                //startActivity(i);
                            // finish();

                        }
                        else
                        {
                                /*Intent i = new Intent(SplashScreen.this, PhoneNumberAuthentication.class);*/
                            Intent i = new Intent(SplashScreen.this, IntroActivity.class);
                            gobj.updateFromSharedPref("splash");
                            dspobj2.updateMealStatusSharedPref("OFFLINE");
                            startActivity(i);
                            finish();

                        }

                    }

/*

                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    gobj.updateFromSharedPref("splash");
                    dspobj2.updateMealStatusSharedPref("OFFLINE");

                    startActivity(i);
                    finish();
*/


                }
            },SPLASH_TIME_OUT);





        }
    }

    private void checkupdate(Activity thiscontext) {



    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void foundUpdateAndShowIt(String versionDonwloadable) {

    }

    @Override
    public void foundUpdateAndDontShowIt(String versionDonwloadable) {

    }

    @Override
    public void returnUpToDate(String versionDonwloadable) {

    }

    @Override
    public void returnMultipleApksPublished() {

    }

    @Override
    public void returnNetworkError() {

    }

    @Override
    public void returnAppUnpublished() {

        Log.e("UpdateChecker", "App is not published " + "CurrentVersion" + mVersionInstalled());
        Toast.makeText(getApplicationContext(), "App is not published", Toast.LENGTH_SHORT).show();

    }

    private String mVersionInstalled() {

        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }

    @Override
    public void returnStoreError() {

    }


    /**
     * @author dipak
     * @use to Load the Mess Menu Data in the Background
     */


    class LoadAllMess extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context contextFinal;
        private String MessArea;
        JSONObject jObj = null;
        String json = "";
        private  DetailsSharedPref dspobj;





        LoadAllMess(View PassedView) {
            mPassedView = PassedView;
            String preselectArea = getSharedPrefs();
            MessArea = preselectArea;
        }

        LoadAllMess(View view, String area) {
            mPassedView = view;

            MessArea = area;
        }

        LoadAllMess(Context cont) {
            contextFinal = cont;
            String preselectArea = getSharedPrefs();
            MessArea = preselectArea;
            dspobj=new DetailsSharedPref(contextFinal);

            Log.i("SPLASH SCREEN SHARED", preselectArea);
        }


        // products JSONArray

        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";
        private String url_mess_menu = thiscontext.getString(R.string.url_mess_menu);
        //ArrayList<HashMap<String, String>> messList;

        // JSON Node names
        private  final String TAG_SUCCESS = thiscontext.getString(R.string.TAG_SUCCESS);
        private  final String TAG_MESSINFO = thiscontext.getString(R.string.TAG_MESSINFO);
        private  final String TAG_MESSID =thiscontext.getString(R.string.TAG_MESSID);
        private  final String TAG_NAME = thiscontext.getString(R.string.TAG_NAME);
        private  final String TAG_RICE = thiscontext.getString(R.string.TAG_RICE);
        private  final String TAG_ROTI = thiscontext.getString(R.string.TAG_ROTI);
        private  final String TAG_SPECIAL = thiscontext.getString(R.string.TAG_SPECIAL);
        private  final String TAG_SPECIAL_EXTRA = thiscontext.getString(R.string.TAG_SPECIAL_EXTRA);
        private  final String TAG_VEGIE1 = thiscontext.getString(R.string.TAG_VEGIE1);
        private  final String TAG_VEGIE2 = thiscontext.getString(R.string.TAG_VEGIE2);
        private  final String TAG_VEGIE3 = thiscontext.getString(R.string.TAG_VEGIE3);
        private  final String TAG_OTHER = thiscontext.getString(R.string.TAG_OTHER);
        private  final String TAG_GCHARGE = thiscontext.getString(R.string.TAG_GCHARGE);
        private  final String TAG_STATUS = thiscontext.getString(R.string.TAG_STATUS);
        private  final String TAG_OPENTIME = thiscontext.getString(R.string.TAG_OPENTIME);
        private  final String TAG_CLOSETIME = thiscontext.getString(R.string.TAG_CLOSETIME);
        private final String TAG_OPENCLOSE="openstatus";


        /**
         * @use clear the initial Hashmap
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AllMessInfoFromDatabaseSplash.clear();
           /* pDialog = new ProgressDialog(getActivity().getApplicationContext());
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
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


                jsonObject.put("college", MessArea);





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

                    try {
                        jObj = new JSONObject(json);
                        int success = jObj.getInt("success");
                        if (success == 1) {

                            db.updateMenuCard(MessArea, json);
                        }
                        else
                        {
                            Log.e("%%%%%","NOT SUCCESS IN SPLASH");
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }



                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                //String contentAsString = readIt(is,len);
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



            try {

                int success = jObj.getInt("success");
                if (success == 1) {
                    dspobj.updateMealStatusSharedPref(jObj.getString("meal"));


                    JSONArray mess2 = jObj.getJSONArray("messinfo");

                    for (int i = 0; i < mess2.length(); i++) {
                        JSONObject c = mess2.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_MESSID).trim();
                        String rice = c.getString(TAG_RICE).trim();
                        String vegie1 = c.getString(TAG_VEGIE1).trim();
                        String vegie2 = c.getString(TAG_VEGIE2).trim();
                        String vegie3 = c.getString(TAG_VEGIE3).trim();
                        String roti = c.getString(TAG_ROTI).trim();
                        String special = c.getString(TAG_SPECIAL).trim();
                        String special_extra = c.getString(TAG_SPECIAL_EXTRA).trim();
                        String other = c.getString(TAG_OTHER).trim();

                        String gcharge = c.getString(TAG_GCHARGE).trim();
                        String otime = c.getString(TAG_OPENTIME).trim();
                        String ctime = c.getString(TAG_CLOSETIME).trim();
                        String openclose = c.getString(TAG_OPENCLOSE).trim();


                        /*String otime = c.getString(TAG_OPENTIME).trim();
                        String ctime = c.getString(TAG_CLOSETIME).trim();*/

                        String stat = c.getString(TAG_STATUS).trim();


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_MESSID, id);
                        map.put(TAG_RICE, rice);
                        map.put(TAG_VEGIE1, vegie1);
                        map.put(TAG_VEGIE2, vegie2);
                        map.put(TAG_VEGIE3, vegie3);
                        map.put(TAG_ROTI, roti);
                        map.put(TAG_SPECIAL, special);
                        map.put(TAG_SPECIAL_EXTRA, special_extra);
                        map.put(TAG_OTHER, other);

                        map.put(TAG_GCHARGE, gcharge);
                        map.put(TAG_OPENTIME, otime);
                        map.put(TAG_CLOSETIME, ctime);
                        map.put(TAG_STATUS, stat);
                        map.put(TAG_OPENCLOSE,openclose);


                        Log.d("inDoinBackground: ID", "``````````````````````" + map.toString());


                        // adding HashList to ArrayList
                        AllMessInfoFromDatabaseSplash.add(map);
                    }


                    Log.i("IN SPLASH SCREEN ", "``````````````````````" + AllMessInfoFromDatabaseSplash.toString());


                    GeneralSharedPref gobj=new GeneralSharedPref(contextFinal);
                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser!=null)
                    {
                        Toast.makeText(contextFinal, "Menu Updated Successfully", Toast.LENGTH_SHORT).show();
                        gobj.updateFromSharedPref("splash");
                        Intent i = new Intent(contextFinal, MainActivity.class);

                        startActivity(i);
                      //  contextFinal.startActivity(i);
                        finish();
                    }
                    else {

                        if(dspobj2.getIntroDone().equals("notdone")) {
                          //  Toast.makeText(contextFinal, "21111Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                            gobj.updateFromSharedPref("splash");
/*
                            Intent i = new Intent(contextFinal, PhoneNumberAuthentication.class);
*/

                            Intent i = new Intent(SplashScreen.this, IntroActivity.class);

                            startActivity(i);
                           // contextFinal.startActivity(i);
                            finish();                           // startActivity(i);
                           // contextFinal.startActivity(i);
                           // finish();
                        }
                        else
                        {
                          //  Toast.makeText(contextFinal, "11111Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                            gobj.updateFromSharedPref("splash");
/*
                            Intent i = new Intent(contextFinal, PhoneNumberAuthentication.class);
*/
                            Intent i = new Intent(SplashScreen.this, IntroActivity.class);

                            startActivity(i);
                           // contextFinal.startActivity(i);
                            finish();
                        }


                    }



                    // close this activity
                    //contextFinal..finish();

                }
                else
                {
                    Log.i("SPLASH SCREEN SHARED", "ERROR");
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    GeneralSharedPref gobj=new GeneralSharedPref(contextFinal);

                    if(currentUser!=null)
                    {
                        Toast.makeText(contextFinal, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                        gobj.updateFromSharedPref("splash");

                        Intent i = new Intent(contextFinal, MainActivity.class);
                        startActivity(i);
                      //  contextFinal.startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(contextFinal, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                        gobj.updateFromSharedPref("splash");

/*
                        Intent i = new Intent(contextFinal, PhoneNumberAuthentication.class);
*/

                        Intent i = new Intent(SplashScreen.this, IntroActivity.class);

                        startActivity(i);
                      //  contextFinal.startActivity(i);
                        finish();
                    }
                    updateSharedPrefs(thiscontext.getString(R.string.pict));
                    dspobj.updateMealStatusSharedPref("OFFLINE");

                    Toast.makeText(contextFinal, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //   View v=intializeList(mPassedView);
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread

        }


        /**
         * @param selectedArea:String of NEarby College to be updated
         * @use to update the Nearby College in the Shared Preference (if necessary)
         */
        private void updateSharedPrefs(String selectedArea) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextFinal);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("selectedarea", selectedArea);
            editor.apply();
            editor.commit();

        }

        /**
         * @return the Current Nearby College stored in the Shared Preference (if necessary)
         */
        private String getSharedPrefs() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextFinal);
            String PreStoredArea = preferences.getString("selectedarea", "PICT, BVP, Katraj");
            Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
            return PreStoredArea;


        }



    }


    private void fetchWelcome() {
        Toast.makeText(this,mFirebaseRemoteConfig.getString(COMP_UPDATE_KEY),Toast.LENGTH_SHORT).show();

        Log.e("1","1"+mFirebaseRemoteConfig.getString(COMP_UPDATE_KEY));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashScreen.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(SplashScreen.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        displayDialog();
                    }
                });
        // [END fetch_config_with_callback]
    }


    private void displayDialog() {
        // [START get_config_values]
        String welcomeMessage = mFirebaseRemoteConfig.getString(STORE_VERSION_KEY);
        // [END get_config_values]
        if (mFirebaseRemoteConfig.getBoolean(COMP_UPDATE_KEY)) {
            Toast.makeText(this,"!"+welcomeMessage,Toast.LENGTH_SHORT).show();
            Log.e("2","2"+welcomeMessage);
        } else {
            Log.e("3","3"+welcomeMessage);
        }
    }




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}


