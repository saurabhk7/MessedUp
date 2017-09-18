package com.messedup.messedup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.messedup.messedup.connection_check.ConnectionManager;
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

/**
 * @author saurabh
 * @use First Loading Screen ( Thread Used to load data in the background )
 */
public class SplashScreen extends AppCompatActivity {

    public ConnectionManager connectionManager;
    LoadAllMess lam;
    public DatabaseHandler db;

    int SPLASH_TIME_OUT;
    public ArrayList<HashMap<String, String>> AllMessInfoFromDatabaseSplash = new ArrayList<>();
    private static Context thiscontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Splash screen timer

        thiscontext=getBaseContext();
        lam = new LoadAllMess(thiscontext);
        db=new DatabaseHandler(thiscontext);

        connectionManager=new ConnectionManager(this);
        if(connectionManager.isNetworkAvailable())
        {


            lam = new LoadAllMess(thiscontext);
            lam.execute();
            SPLASH_TIME_OUT = 10000;
        }
        else {
            SuperActivityToast.create(SplashScreen.this, new Style(), Style.TYPE_BUTTON)
                    .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                    .setText("Oops, No Network Available!")
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_LOLLIPOP)
                    .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                    .setAnimations(Style.ANIMATIONS_POP).show();
            SPLASH_TIME_OUT = 2000;
        }
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                //

                if(lam.getStatus() == AsyncTask.Status.FINISHED)
                {

                }

                else
                {
                    Intent i = new Intent(SplashScreen.this, GoogleSignIn.class);
                    startActivity(i);
                    Toast.makeText(getBaseContext(),"Oops,Error Updating Mess Menuhkfkfs",Toast.LENGTH_SHORT).show();

                    // close this activity
                    finish();
                    //  Toast.makeText(getApplicationContext(),"could not load mess",Toast.LENGTH_LONG);
                }

            }
        }, SPLASH_TIME_OUT);
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

                    db.updateMenuCard(MessArea,json);


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


                        Log.d("inDoinBackground: ID", "``````````````````````" + map.toString());


                        // adding HashList to ArrayList
                        AllMessInfoFromDatabaseSplash.add(map);
                    }


                    Log.i("IN SPLASH SCREEN ", "``````````````````````" + AllMessInfoFromDatabaseSplash.toString());

                    Toast.makeText(contextFinal, "11111Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(contextFinal, GoogleSignIn.class);
                    startActivity(i);
                    contextFinal.startActivity(i);
                    finish();
                    // close this activity
                    //contextFinal..finish();

                } else {
                    Log.i("SPLASH SCREEN SHARED", "ERROR");

                    updateSharedPrefs(thiscontext.getString(R.string.pict));
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
            String PreStoredArea = preferences.getString("selectedarea", "PICT, Dhankawadi");
            Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
            return PreStoredArea;


        }



    }
}


