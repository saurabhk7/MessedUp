package com.messedup.messedup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsx.rateme.RateMeDialogTimer;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.GeneralSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.SharedPrefHandler;
import com.messedup.messedup.SharedPreferancesPackage.SharedPreference;
import com.messedup.messedup.adapters.MyAdapter;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;
import com.messedup.messedup.ui_package.RateUsDialogFragment;
import com.messedup.messedup.ui_package.SampleDialogFragment;
import com.mobapphome.mahandroidupdater.tools.MAHUpdaterController;

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
import java.util.List;
import java.util.Map;

import static com.messedup.messedup.MainActivity.college_list;
import static com.messedup.messedup.MainActivity.spinner;

public class MenuFragment extends Fragment {


    //DATABASE AND SHARED PREF OBJECTS
    DatabaseHandler databaseHandler;
    public SharedPrefHandler sharedPrefHandler;
    public DatabaseHandler db;



    //LOADED MENU ARRAYLIST TO POPULATE CARD VIEW
    ArrayList<HashMap<String, String>> MenuArrayList = new ArrayList<>();

    public  static ArrayList<HashMap<String,String>> AllMessInfoFromDatabaseSplash=new ArrayList<>();

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }
    ArrayList<String> MessNameList=new ArrayList<>();
    public  static ArrayList<HashMap<String,String>> AllMessInfoFromDatabase=new ArrayList<>();

    public static ArrayList<MenuCardView> AllMessMenu = new ArrayList<>();
    RecyclerView MyRecyclerView;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSINFO = "messinfo";
    private static final String TAG_MESSID = "messid";
    private static final String TAG_NAME = "name";
    private static final String TAG_RICE = "rice";
    private static final String TAG_ROTI = "roti";
    private static final String TAG_SPECIAL = "special";
    private static final String TAG_SPECIAL_EXTRA = "specialextra";
    private static final String TAG_VEGIE1 = "vegieone";
    private static final String TAG_VEGIE2 = "vegietwo";
    private static final String TAG_VEGIE3 = "vegiethree";
    private static final String TAG_OTHER = "other";
    private static final String TAG_GCHARGE = "gcharge";
    private static final String TAG_STATUS = "status";
    private static final String TAG_OPENTIME = "opentime";
    private static final String TAG_CLOSETIME = "closetime";
    private static final String TAG_OPENCLOSE="openstatus";
    private static boolean POPULATED_FLAG=false;
    //  SpotsDialog LoadingDialog ;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private  View OnCreaterootView;
    private Toolbar toolbarFrag;
    private static boolean FROM_SPLASH_SCREEN=true;


    //  private HashMap<String ,String> MenuHashMap=new HashMap<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       /* initializeHashMaps();*/ // this will get deleted later as the hasmap will
        // be retrieved from the database ..  then uncomment the initilizeList method!

        // intializeList();  uncomment this later
    }

    private void initializeHashMaps(View PassedView) {

        new LoadAllMess(PassedView).execute();

/*




        HashMap<String,String>Mess1=new HashMap<>();
        Mess1.put("messid","Anand Food Xprs"); //here there will be ,messid but change it to MessName
        Mess1.put("rice","Jeera Rice");
        Mess1.put("vegieone","Methi");
        Mess1.put("vegietwo","Baingan");
        Mess1.put("vegiethree",null);
        Mess1.put("roti","Roti");
        Mess1.put("special","Gulab Jamun");
        Mess1.put("specialextra",null);
        Mess1.put("other","achar");
        HashMap<String,String>Mess2=new HashMap<>();
        Mess2.put("messid","Kwality Mess");//here there will be ,messid but change it to MessName
        Mess2.put("rice","Rice");
        Mess2.put("vegieone","Bhindi");
        Mess2.put("vegietwo","Daal Tadka");
        Mess2.put("vegiethree","Aloo Gravy");
        Mess2.put("roti","Roti");
        Mess2.put("special","Kheer");
        Mess2.put("specialextra","Raita");
        Mess2.put("other","ButterMilk");
        HashMap<String,String>Mess3=new HashMap<>();
        Mess3.put("messid","Gujrati Mess");//here there will be ,messid but change it to MessName
        Mess3.put("rice","Masala Rice");
        Mess3.put("vegieone",null);
        Mess3.put("vegietwo",null);
        Mess3.put("vegiethree",null);
        Mess3.put("roti","Paratha");
        Mess3.put("special",null);
        Mess3.put("specialextra",null);
        Mess3.put("other","achar");
        HashMap<String,String>Mess4=new HashMap<>();
        Mess4.put("messid","PICT College Mess");//here there will be ,messid but change it to MessName
        Mess4.put("rice","Rice");
        Mess4.put("vegieone","Mutter Gravy");
        Mess4.put("vegietwo","Mix Veg.");
        Mess4.put("vegiethree",null);
        Mess4.put("roti","Roti");
        Mess4.put("special",null);
        Mess4.put("specialextra",null);
        Mess4.put("other","Dahi");
        HashMap<String,String>Mess5=new HashMap<>();
        Mess5.put("messid","Navruchi Veg.");//here there will be ,messid but change it to MessName
        Mess5.put("rice","Rice");
        Mess5.put("vegieone","Paneer Masala Makhanwala");
        Mess5.put("vegietwo","Daal");
        Mess5.put("vegiethree","Aloo Fry");
        Mess5.put("roti","Roti / Chapati");
        Mess5.put("special","Kheer");
        Mess5.put("specialextra",null);
        Mess5.put("other","ButterMilk");
        AllMessInfoFromDatabase.add(Mess1);
        AllMessInfoFromDatabase.add(Mess2);
        AllMessInfoFromDatabase.add(Mess3);
        AllMessInfoFromDatabase.add(Mess4);
        AllMessInfoFromDatabase.add(Mess5);
*/




    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {







        //set up the view
        OnCreaterootView = inflater.inflate(R.layout.fragment_card, container, false);

/*
        if(isNetworkAvailable())
        {
            String BASEURL = Constants.getBaseUrl();

            String url = BASEURL+"checkVersion.php";

            MAHUpdaterController.init(getActivity(), url);
        }
*/


        //  getUserDetails2(OnCreaterootView);

        //  startIntro(OnCreaterootView);

        //DATABASE HANDLER OBJECT
        db=new DatabaseHandler(OnCreaterootView.getContext());

        db.getAll();

        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) OnCreaterootView.findViewById(R.id.swiperefresh);
        // BEGIN_INCLUDE (change_colors) //TODO: See the changing colors of loading circle
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
       /* mSwipeRefreshLayout.setColorScheme(
                Color.BLUE,Color.CYAN,Color.GREEN,Color.RED);*/
        // END_INCLUDE (change_colors)

        // LoadingDialog=new SpotsDialog(getActivity(), R.style.Custom);

        if (POPULATED_FLAG) {
            re_initilializeHashMaps(OnCreaterootView);
        }

        spinner=(Spinner)getActivity().findViewById(R.id.categorySpinner);


        final TextView hiddenTextView = new TextView(getContext());
        hiddenTextView.setVisibility(View.GONE);


        final GeneralSharedPref gobj=new GeneralSharedPref(OnCreaterootView.getContext());




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedArea = "PICT, BVP, Katraj";
                Log.d("spinner pos: ",i+" "+"" );

                if(adapterView.getItemAtPosition(i)!=null) {
                    selectedArea = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    try {
                        selectedArea = college_list.get(i);
                        Log.d("spinner pos: ", "found null using list " + selectedArea);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("SPINNER EXCEPTION","**");
                    }
                }

                try {


                    // Toast.makeText(OnCreaterootView.getContext(), "Selected College: " + selectedArea, Toast.LENGTH_SHORT).show();
                    Log.e("Selected College"," "+selectedArea);
                    String preselectArea = getSharedPrefs();
                    // Toast.makeText(OnCreaterootView.getContext(), "SHARED PREF College: " + preselectArea, Toast.LENGTH_SHORT).show();
                    Log.e("Preselected College", " " + preselectArea);

                    //SUBSCRIBE TO THE NEARBY COLLEGE TOPIC
                    updateSharedPrefs(selectedArea);
                    String preselectArea2 = getSharedPrefs();
                    FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(preselectArea2));

                    // Toast.makeText(OnCreaterootView.getContext(), "After Updating SHARED PREF College: " + preselectArea2, Toast.LENGTH_SHORT).show();
                    Log.e("After Updating College", " " + preselectArea2);


                    //Setting Up Database and Shared Pref Objects to retrieve the Stored Menu
                    databaseHandler = new DatabaseHandler(OnCreaterootView.getContext());
                    sharedPrefHandler = new SharedPrefHandler(OnCreaterootView.getContext());

                    //Populating the Menu ArrayList

                    if(gobj.getFromSharedPref().equals("splash")) {

                        Log.e("FROM: ","splash");
                        MenuArrayList = databaseHandler.getCardJson(sharedPrefHandler.getSharedPrefs());
                        gobj.updateFromSharedPref("splashdone");

                        if (MenuArrayList == null) {
                            Toast.makeText(OnCreaterootView.getContext(), "Please Check Your Connection and Refresh", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Total Menu : ", MenuArrayList.toString());



                            intializeList(OnCreaterootView);
                            //initiateRefresh(OnCreaterootView);
                            //   Toast.makeText(OnCreaterootView.getContext(), MenuArrayList.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else  if(gobj.getFromSharedPref().equals("splashdone")&&!isNetworkAvailable())
                    {
                        MenuArrayList = databaseHandler.getCardJson(sharedPrefHandler.getSharedPrefs());
                        gobj.updateFromSharedPref("splashdone");

                        if (MenuArrayList == null) {
                            final DetailsSharedPref dsp=new DetailsSharedPref(OnCreaterootView.getContext());
                            dsp.updateMealStatusSharedPref("OFFLINE");
                            Toast.makeText(OnCreaterootView.getContext(), "Please Check Your Connection and Refresh", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Total Menu : ", MenuArrayList.toString());

                            intializeList(OnCreaterootView);
                            //initiateRefresh(OnCreaterootView);
                            //   Toast.makeText(OnCreaterootView.getContext(), MenuArrayList.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        initiateRefresh(OnCreaterootView);
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        return OnCreaterootView;
    }

    private void showRateUsDialog(View onCreaterootView) {

        RateUsDialogFragment fragment
                = RateUsDialogFragment.newInstance(5,10.0f,true,false);
        fragment.show(getActivity().getFragmentManager(), "blur_sample");

    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if(isNetworkAvailable()) {

            String BASEURL = Constants.getBaseUrl();

            String url = BASEURL + "checkVersion.php";

            MAHUpdaterController.init(getActivity(), url);
        }
*/
        // BEGIN_INCLUDE (setup_refreshlistener)
        /**
         * Implement {@link SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("IN ON REFRESH", "onRefresh called from SwipeRefreshLayout");
                /*if(isNetworkAvailable()) {

                    String BASEURL = Constants.getBaseUrl();

                    String url = BASEURL + "checkVersion.php";

                    MAHUpdaterController.init(getActivity(), url);
                }
*/
                if(isNetworkAvailable())
                    initiateRefresh(view);
                else
                {
                    Toast.makeText(view.getContext(),"Oops, No network available!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }
// END_INCLUDE (setup_views)

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initiateRefresh(View view) {
        Log.i("IN INITIATE REFRESH", "initiateRefresh");

        final DetailsSharedPref dsp=new DetailsSharedPref(view.getContext());
        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        //  new DummyBackgroundTask().execute();

        final LoadAllMess lam= new LoadAllMess(view);
        lam.execute();
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

                    DatabaseHandler dbhandler=new DatabaseHandler(OnCreaterootView.getContext());
                    SharedPrefHandler shrprefhndlr=new SharedPrefHandler(OnCreaterootView.getContext());

                    final GeneralSharedPref gobj=new GeneralSharedPref(OnCreaterootView.getContext());
                    MenuArrayList = dbhandler.getCardJson(shrprefhndlr.getSharedPrefs());
                    gobj.updateFromSharedPref("splashdone");

                    if (MenuArrayList == null) {
                        dsp.updateMealStatusSharedPref("OFFLINE");

                        Toast.makeText(OnCreaterootView.getContext(), "Please Check Your Connection and Refresh", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Total Menu : ", MenuArrayList.toString());
                        intializeList(OnCreaterootView);
                        //initiateRefresh(OnCreaterootView);
                        //   Toast.makeText(OnCreaterootView.getContext(), MenuArrayList.toString(), Toast.LENGTH_LONG).show();
                    }


                }

            }
        }, 9000);



    }

    public void initiateRefresh(View view,String area) {
        Log.i("IN INITIATE REFRESH", "initiateRefresh");

        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        //  new DummyBackgroundTask().execute();

        new LoadAllMess(view,area).execute();
    }

    private void onRefreshComplete(String url,View v) {
        Log.e("REFRESH COMPLETE", "onRefreshComplete"+url);

        // Remove all items from the ListAdapter, and then replace them with the new items

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);

        // Stop the refreshing indicator

        try {
            if (mSwipeRefreshLayout != null) {
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);

                afterRefresh();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getUserDetails2(View view) {

        String Username,UserID,UserEmail,UserContact;



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int i = 0;
            FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

            Log.e("USERID: ",""+CurrentUser.getUid());


            for (UserInfo profile : CurrentUser.getProviderData()) {

                i++;
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider

                // Name, email address, and profile photo Url



                if (providerId.equals("google.com")) {
                    Username = profile.getDisplayName();
                    Log.e("NAME: ",Username);

                }



                if (providerId.equals("firebase")) {

                    UserEmail = profile.getEmail();
                    Log.e("EMAIL: ",UserEmail);

                }



            }


            UserContact=CurrentUser.getPhoneNumber();
            Log.e("PHONE: ",""+UserContact);

            UserID=CurrentUser.getUid();
            Log.e("UID: ",""+UserID);


        }



    }

    private void afterRefresh() {

        RateMeDialogTimer.onStart(OnCreaterootView.getContext());

        DetailsSharedPref dsp=new DetailsSharedPref(OnCreaterootView.getContext());
        if (RateMeDialogTimer.shouldShowRateDialog(OnCreaterootView.getContext(), 20, 7)&&!dsp.getRateStatus().equals("notshow")) {
            // show the dialog with the code above

            showRateUsDialog(OnCreaterootView);
        }

    }


    /**
     * Created by saurabh on 24/8/17.
     */

    class LoadAllMess extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context thiscontext;
        private String MessArea;
        ProgressDialog progressDialog;
        private DetailsSharedPref dspobj;
        private int myversion;


        JSONObject jObj = null;
        String json = "";
        // JSON Node names





        LoadAllMess(View PassedView) {
            mPassedView = PassedView;
            MessArea = getSharedPrefs();
            thiscontext=PassedView.getContext();
            dspobj=new DetailsSharedPref(thiscontext);

        }

        LoadAllMess(View view, String area) {
            mPassedView = view;
            thiscontext=view.getContext();
            MessArea = area;
            dspobj=new DetailsSharedPref(thiscontext);

        }

        LoadAllMess(Context cont) {
            thiscontext = cont;
            String preselectArea = getSharedPrefs();
            MessArea = preselectArea;

            dspobj=new DetailsSharedPref(thiscontext);

            Log.i("SPLASH SCREEN SHARED", preselectArea);
        }


/*        final String TAG_SUCCESS = thiscontext.getString(R.string.TAG_SUCCESS);
        final String TAG_MESSINFO = thiscontext.getString(R.string.TAG_MESSINFO);
        final String TAG_MESSID =thiscontext.getString(R.string.TAG_MESSID);
        final String TAG_NAME = thiscontext.getString(R.string.TAG_NAME);
        final String TAG_RICE = thiscontext.getString(R.string.TAG_RICE);
        final String TAG_ROTI = thiscontext.getString(R.string.TAG_ROTI);
        final String TAG_SPECIAL = thiscontext.getString(R.string.TAG_SPECIAL);
        final String TAG_SPECIAL_EXTRA = thiscontext.getString(R.string.TAG_SPECIAL_EXTRA);
        final String TAG_VEGIE1 = thiscontext.getString(R.string.TAG_VEGIE1);
        final String TAG_VEGIE2 = thiscontext.getString(R.string.TAG_VEGIE2);
        final String TAG_VEGIE3 = thiscontext.getString(R.string.TAG_VEGIE3);
        final String TAG_OTHER = thiscontext.getString(R.string.TAG_OTHER);
        final String TAG_GCHARGE = thiscontext.getString(R.string.TAG_GCHARGE);
        final String TAG_STATUS = thiscontext.getString(R.string.TAG_STATUS);
        final String TAG_OPENTIME = thiscontext.getString(R.string.TAG_OPENTIME);
        final String TAG_CLOSETIME = thiscontext.getString(R.string.TAG_CLOSETIME);*/
        /**
         * @use clear the initial Hashmap
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(thiscontext);
            progressDialog.setMessage("Loading your Menu!");
            progressDialog.show();
            AllMessInfoFromDatabaseSplash.clear();

            try {
                PackageInfo pInfo = thiscontext.getPackageManager().getPackageInfo(thiscontext.getPackageName(), 0);
                myversion = pInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
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
                String url_mess_menu = thiscontext.getString(R.string.url_mess_menu);

                URL url = new URL(url_mess_menu);
                JSONObject jsonObject = new JSONObject();


                jsonObject.put("college", MessArea);
                jsonObject.put("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());



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

                            db.getAll();
                            db.updateMenuCard(MessArea, json);
                            Log.e("JSON UPDATED: ",json);
                        }
                        else
                        {
                            db.getAll();
                            db.updateMenuCard(MessArea,"nodata");
                            Log.e("%%%%%","NOT SUCCESS IN MENUFRAG");
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
                progressDialog.dismiss();

                dspobj.updateMealStatusSharedPref(jObj.getString("meal"));

                int success = jObj.getInt("success");
                int  minversion = jObj.getInt("version");
                minversion++;

                Log.e("MINVERSTEST","MY VERSION: "+myversion+" MIN VERSION: "+minversion);


                if (success == 1&& minversion<=myversion) {

                    Log.e("MINVERSTEST","SPLASH SATISFIED");

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
                        String openclose = c.getString(TAG_OPENCLOSE).trim();

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
                        map.put(TAG_OPENCLOSE,openclose);


                        Log.d("inDoinBackground: ID", "``````````````````````" + map.toString());


                        // adding HashList to ArrayList
                        AllMessInfoFromDatabaseSplash.add(map);
                    }


                    Log.i("IN MENUFRAG SCREEN ", "``````````````````````" + AllMessInfoFromDatabaseSplash.toString());

                    intializeList(mPassedView);
                    // close this activity
                    //contextFinal..finish();

                }
                else if (success != 1 && minversion<=myversion)
                {
                    Log.i("SPLASH SCREEN SHARED", "ERROR");


                    Toast.makeText(thiscontext,"No data found",Toast.LENGTH_SHORT).show();
                    updateSharedPrefs(thiscontext.getString(R.string.pict));
                    onRefreshComplete("complete",mPassedView);
                }
                else if (minversion>myversion)
                {
                    startActivity(new Intent(getActivity(),UpdateVersionActivity.class));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }





    }


    public View intializeList(final View mPassedView) {

        //  Log.d("In initialize List", AllMessInfoFromDatabase.toString());

        DatabaseHandler databaseHandler1=new DatabaseHandler(mPassedView.getContext());
        SharedPrefHandler sharedPrefHandler1 = new SharedPrefHandler(mPassedView.getContext());

        AllMessInfoFromDatabase=databaseHandler1.getCardJson(sharedPrefHandler1.getSharedPrefs());
        Log.e("In##initialize List", AllMessInfoFromDatabase.toString());

        AllMessMenu.clear();

        for (HashMap<String, String> MessInfoObj : AllMessInfoFromDatabase) {
            MenuCardView MessMenuObj = new MenuCardView();

            Log.d("In initialize List 2", MessInfoObj.toString());

              /*If a product exists in shared preferences then set heart_red drawable
         * and set a tag*/
            if (checkFavoriteItem(MessInfoObj.get(TAG_MESSID), mPassedView)) {

                try { String temp= getTopicName(MessInfoObj.get(TAG_MESSID));
                    Log.e("topic: ",temp);

                    FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    // FirebaseMessaging.getInstance().subscribeToTopic("tanmay");
                }
                catch (Exception e)
                {
                    Log.e("getTopicName"," "+MessInfoObj.get(TAG_MESSID));
                }
                //  Toast.makeText(mPassedView.getContext(), "Yours Fav: " + MessInfoObj.get(TAG_MESSID), Toast.LENGTH_SHORT).show();
                MessMenuObj.setFavMess("true");

            } else {
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    //  FirebaseMessaging.getInstance().unsubscribeFromTopic("tanmay");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MessMenuObj.setFavMess("false");
                }

                MessMenuObj.setFavMess("false");
            }


            for (Map.Entry<String, String> entry : MessInfoObj.entrySet()) {

                switch (entry.getKey()) {
                    case TAG_MESSID:
                        MessMenuObj.setMessID(entry.getValue());
                        break;
                    case TAG_RICE:
                        MessMenuObj.setRice(entry.getValue());
                        break;
                    case TAG_VEGIE1:
                        MessMenuObj.setVegieOne(entry.getValue());
                        break;
                    case TAG_VEGIE2:
                        MessMenuObj.setVegieTwo(entry.getValue());
                        break;
                    case TAG_VEGIE3:
                        MessMenuObj.setVegieThree(entry.getValue());
                        break;
                    case TAG_ROTI:
                        MessMenuObj.setRoti(entry.getValue());
                        break;
                    case TAG_SPECIAL:
                        MessMenuObj.setSpecial(entry.getValue());
                        break;
                    case TAG_SPECIAL_EXTRA:
                        MessMenuObj.setSpecialExtra(entry.getValue());
                        break;
                    case TAG_OTHER:
                        MessMenuObj.setOther(entry.getValue());
                        break;
                    case TAG_GCHARGE:
                        MessMenuObj.setGCharge(entry.getValue());
                        break;
                    case TAG_OPENTIME:
                        MessMenuObj.setOTime(entry.getValue());
                        break;
                    case TAG_CLOSETIME:
                        MessMenuObj.setCTime(entry.getValue());
                        break;
                    case TAG_STATUS:
                        MessMenuObj.setStat(entry.getValue());
                        break;
                    case TAG_OPENCLOSE:
                        MessMenuObj.setOpenClose(entry.getValue());
                        break;


                }


            }

            if (MessMenuObj.getFavMess().equals("true")) {
                FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                // FirebaseMessaging.getInstance().subscribeToTopic("tanmay");

                Log.i("MessFavtrue ", MessMenuObj.getMessID());
                AllMessMenu.add(0, MessMenuObj);
            } else if (MessMenuObj.getFavMess().equals("false")) {
                try {
                    Log.e("----REMOVING-----","removing "+MessInfoObj.get(TAG_MESSID));
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    //  FirebaseMessaging.getInstance().unsubscribeFromTopic("tanmay");

                } catch (Exception e) {
                    Log.e("----REMOVING-----","removing ");
                    e.printStackTrace();
                } finally {
                    Log.i("MessFavfalse ", MessMenuObj.getMessID());

                    AllMessMenu.add(MessMenuObj);
                }


            }




            Log.d("ALL MESS MENU : ", AllMessMenu.toString());


        }

        MyRecyclerView = (RecyclerView) mPassedView.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (AllMessMenu.size()>0&&MyRecyclerView != null) {

            MyRecyclerView.setAdapter(new MyAdapter(AllMessMenu));
        }
        else
        {
            ImageView ErrorImg=(ImageView)mPassedView.findViewById(R.id.ErrorImgView);
            TextView ErrotTxt=(TextView)mPassedView.findViewById(R.id.ErrorTxtView);

            ErrorImg.setVisibility(View.VISIBLE);
            ErrotTxt.setVisibility(View.VISIBLE);
        }
       /* if(AllMessMenu.size() ==0 || MyRecyclerView==null)
        {
            ImageView ErrorImg=(ImageView)mPassedView.findViewById(R.id.ErrorImgView);
            TextView ErrotTxt=(TextView)mPassedView.findViewById(R.id.ErrorTxtView);

            ErrorImg.setVisibility(View.VISIBLE);
            ErrotTxt.setVisibility(View.VISIBLE);



        }
        else
        {
            ImageView ErrorImg=(ImageView)mPassedView.findViewById(R.id.ErrorImgView);
            TextView ErrotTxt=(TextView)mPassedView.findViewById(R.id.ErrorTxtView);

            ErrorImg.setVisibility(View.INVISIBLE);
            ErrotTxt.setVisibility(View.INVISIBLE);
        }*/
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        POPULATED_FLAG = true;

        try {
            // LoadingDialog.dismiss();


        }
        catch (Exception e)
        {
            //onRefreshComplete("complete",mPassedView);
            //  Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
            return mPassedView;
        }
        onRefreshComplete("complete",mPassedView);
        Toast.makeText(mPassedView.getContext(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
        return mPassedView;



    }

    public View intializeList(final View mPassedView ,ArrayList<HashMap<String,String>> AllMessInfoFromDatabaseTemp) {


        AllMessInfoFromDatabase=AllMessInfoFromDatabaseTemp;

        int count = 0;
        Log.d("In initialize List", AllMessInfoFromDatabase.toString());

        AllMessMenu.clear();

        for (HashMap<String, String> MessInfoObj : AllMessInfoFromDatabase) {
            MenuCardView MessMenuObj = new MenuCardView();

            Log.d("In initialize List 2", MessInfoObj.toString());

              /*If a product exists in shared preferences then set heart_red drawable
         * and set a tag*/
            if (checkFavoriteItem(MessInfoObj.get(TAG_MESSID), mPassedView)) {

                try { String temp= getTopicName(MessInfoObj.get(TAG_MESSID));
                    Log.e("topic: ",temp);

                    FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    // FirebaseMessaging.getInstance().subscribeToTopic("tanmay");
                }
                catch (Exception e)
                {
                    Log.e("getTopicName"," "+MessInfoObj.get(TAG_MESSID));
                }
                // Toast.makeText(mPassedView.getContext(), "Yours Fav: " + MessInfoObj.get(TAG_MESSID), Toast.LENGTH_SHORT).show();
                MessMenuObj.setFavMess("true");

            } else {
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    //  FirebaseMessaging.getInstance().unsubscribeFromTopic("tanmay");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MessMenuObj.setFavMess("false");
                }

                MessMenuObj.setFavMess("false");
            }


            for (Map.Entry<String, String> entry : MessInfoObj.entrySet()) {

                switch (entry.getKey()) {
                    case TAG_MESSID:
                        MessMenuObj.setMessID(entry.getValue());
                        break;
                    case TAG_RICE:
                        MessMenuObj.setRice(entry.getValue());
                        break;
                    case TAG_VEGIE1:
                        MessMenuObj.setVegieOne(entry.getValue());
                        break;
                    case TAG_VEGIE2:
                        MessMenuObj.setVegieTwo(entry.getValue());
                        break;
                    case TAG_VEGIE3:
                        MessMenuObj.setVegieThree(entry.getValue());
                        break;
                    case TAG_ROTI:
                        MessMenuObj.setRoti(entry.getValue());
                        break;
                    case TAG_SPECIAL:
                        MessMenuObj.setSpecial(entry.getValue());
                        break;
                    case TAG_SPECIAL_EXTRA:
                        MessMenuObj.setSpecialExtra(entry.getValue());
                        break;
                    case TAG_OTHER:
                        MessMenuObj.setOther(entry.getValue());
                        break;
                    case TAG_GCHARGE:
                        MessMenuObj.setGCharge(entry.getValue());
                        break;
                    case TAG_OPENTIME:
                        MessMenuObj.setOTime(entry.getValue());
                        break;
                    case TAG_CLOSETIME:
                        MessMenuObj.setCTime(entry.getValue());
                        break;
                    case TAG_STATUS:
                        MessMenuObj.setStat(entry.getValue());
                        break;


                }


            }

            if (MessMenuObj.getFavMess().equals("true")) {
                FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                // FirebaseMessaging.getInstance().subscribeToTopic("tanmay");

                Log.i("MessFavtrue ", MessMenuObj.getMessID());
                AllMessMenu.add(0, MessMenuObj);
            } else if (MessMenuObj.getFavMess().equals("false")) {
                try {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(MessInfoObj.get(TAG_MESSID)));
                    //  FirebaseMessaging.getInstance().unsubscribeFromTopic("tanmay");

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Log.i("MessFavfalse ", MessMenuObj.getMessID());

                    AllMessMenu.add(MessMenuObj);
                }


            }




            Log.d("ALL MESS MENU : ", AllMessMenu.toString());


        }

        MyRecyclerView = (RecyclerView) mPassedView.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (AllMessMenu.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(AllMessMenu));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);
        POPULATED_FLAG = true;

        try {
            //  LoadingDialog.dismiss();


        }
        catch (Exception e)
        {
            // onRefreshComplete("complete");
            //  Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
            return mPassedView;
        }
        onRefreshComplete("complete",mPassedView);
        Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
        return mPassedView;



    }



    private void re_initilializeHashMaps(View mPassedView)
    {
        MyRecyclerView = (RecyclerView) mPassedView.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (AllMessMenu.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(AllMessMenu));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);


        POPULATED_FLAG=true;


    }

    //TODO:===========================CHECKS AND SMALL SUPPORT METHODS==============================
    /*Checks whether a particular product exists in SharedPreferences*/
    public boolean checkFavoriteItem(String checkProduct , View passedView) {

        SharedPreference sharedPreference= new SharedPreference();
        boolean check = false;
        List<String> favorites = sharedPreference.getFavorites(passedView.getContext());
        if (favorites != null) {
            for (String product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }


    private void updateSharedPrefs(String selectedArea) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedarea",selectedArea);
        editor.apply();
        editor.commit();

    }

    private String getSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String PreStoredArea=preferences.getString("selectedarea", "Select your Area");
        Log.d("IN SHARED PREFs","GOT STRING "+PreStoredArea);
        return PreStoredArea;


    }

    private String getTopicName(String messID) {

        try {
            messID=messID.replace(",","_");
            messID = messID.replace(" ", "_");
            messID = messID.toLowerCase();
            messID = messID.trim();
        }
        catch (Exception e)
        {
            Log.i("error","Error in topicname");
        }

        return messID;
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //TODO:=========================================================================================

    private void startIntro(View v)
    {
        TapTargetSequence sequence= new TapTargetSequence(getActivity())
                .targets(
                       /* TapTarget.forView(v.findViewById(R.id.shareMenuBtn), "Your College Area", "Tap to select your area!\n(If not found we are soon coming there:P)")
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

                        TapTarget.forView(v.findViewById(R.id.), "Menu of your selected area", "Menu of the Messes around your area!")
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

                        TapTarget.forView(v.findViewById(R.id.tab_notifs), "Notifications and Announcements", "You'll get all the updates regarding the Messes around your area!")
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
                                .targetRadius(70)  ,                // Specify the target radius (in dp)*/
                        TapTarget.forView(v.findViewById(R.id.shareMenuBtn), "Your Profile", "Your details ! \n(Which you already know :P)")
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

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }


                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });

        sequence.start();

    }


}