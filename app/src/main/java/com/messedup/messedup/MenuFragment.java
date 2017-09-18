package com.messedup.messedup;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.messedup.messedup.SharedPreferancesPackage.SharedPrefHandler;
import com.messedup.messedup.SharedPreferancesPackage.SharedPreference;
import com.messedup.messedup.adapters.MyAdapter;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;
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
import java.util.List;
import java.util.Map;

import static com.messedup.messedup.MainActivity.college_list;
import static com.messedup.messedup.MainActivity.spinner;

public class MenuFragment extends Fragment {


    //DATABASE AND SHARED PREF OBJECTS
    DatabaseHandler databaseHandler;
    SharedPrefHandler sharedPrefHandler;

    //LOADED MENU ARRAYLIST TO POPULATE CARD VIEW
    ArrayList<HashMap<String, String>> MenuArrayList = new ArrayList<>();

    public  static ArrayList<HashMap<String,String>> AllMessInfoFromDatabaseSplash=new ArrayList<>();

    public static MenuFragment newInstance(/*ArrayList<HashMap<String,String>> temp*/) {
        MenuFragment fragment = new MenuFragment();
        //  AllMessInfoFromDatabaseSplash=temp;
//        Toast.makeText(fragment.getContext(),"inInstance",Toast.LENGTH_SHORT).show();
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


    public View intializeList(final View mPassedView) {




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
                Toast.makeText(mPassedView.getContext(), "Yours Fav: " + MessInfoObj.get(TAG_MESSID), Toast.LENGTH_SHORT).show();
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
            // LoadingDialog.dismiss();


        }
        catch (Exception e)
        {
            // onRefreshComplete("complete");
            //  Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
            return mPassedView;
        }
        onRefreshComplete("complete");
        Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mPassedView.getContext(), "Yours Fav: " + MessInfoObj.get(TAG_MESSID), Toast.LENGTH_SHORT).show();
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
        onRefreshComplete("complete");
        Toast.makeText(getActivity(), "Your Menu is Up to Date!", Toast.LENGTH_SHORT).show();
        return mPassedView;



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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //set up the view
        OnCreaterootView = inflater.inflate(R.layout.fragment_card, container, false);

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



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedArea;
                Log.d("spinner pos: ",i+" "+"" );

                if(adapterView.getItemAtPosition(i)!=null) {
                    selectedArea = adapterView.getItemAtPosition(i).toString();
                }
                else {
                    selectedArea = college_list.get(i);
                    Log.d("spinner pos: ","found null using list "+selectedArea);
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
                    MenuArrayList = databaseHandler.getCardJson(sharedPrefHandler.getSharedPrefs());
                    if (MenuArrayList == null) {
                        Toast.makeText(OnCreaterootView.getContext(), "Please Check Your Connection and Refresh", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Total Menu : ", MenuArrayList.toString());


                        Toast.makeText(OnCreaterootView.getContext(), MenuArrayList.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

                initiateRefresh(view);
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

        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        //  new DummyBackgroundTask().execute();

        new LoadAllMess(view).execute();
    }

    public void initiateRefresh(View view,String area) {
        Log.i("IN INITIATE REFRESH", "initiateRefresh");

        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        //  new DummyBackgroundTask().execute();

        new LoadAllMess(view,area).execute();
    }

    private void onRefreshComplete(String url) {
        Log.i("REFRESH COMPLETE", "onRefreshComplete"+url);

        // Remove all items from the ListAdapter, and then replace them with the new items


        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }






    /**
     * Created by saurabh on 24/8/17.
     */

    public class LoadAllMess extends AsyncTask<String , Void ,String> {

        private  View mPassedView;
        private String MessArea;
        JSONObject jObj = null;
        String json = "";

        LoadAllMess(View PassedView)
        {
            mPassedView=PassedView;
            String preselectArea= getSharedPrefs();
            MessArea=preselectArea;
        }

        LoadAllMess(View view,String area)
        {
            mPassedView=view;

            MessArea=area;
        }


        // products JSONArray
        JSONArray mess = null;
        JSONArray mess2 = null;

        //JSONParser jParser = new JSONParser();
        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";
        private String url_mess_menu="https://wanidipak56.000webhostapp.com/receivemenu.php";
        //ArrayList<HashMap<String, String>> messList;

        // JSON Node names
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

//TODO: give lunch and dinner open/close and open/close status


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AllMessInfoFromDatabase.clear();
           /* pDialog = new ProgressDialog(getActivity().getApplicationContext());
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(String... args) {

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                //constants
                URL url = new URL("https://wanidipak56.000webhostapp.com/t.php");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("college", MessArea);

                String message = jsonObject.toString();

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout( 10000 /*milliseconds*/ );
                conn.setConnectTimeout( 15000 /* milliseconds */ );
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
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }  finally {
                //clean up
                try {
                    os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                conn.disconnect();
            }
            return null;


            /*final String TAG_SUCCESS = "success";
              final String TAG_MESSINFO = "messinfo";
              final String TAG_MESSID = "messid";
              final String TAG_NAME = "name";
              final String TAG_RICE = "rice";
              final String TAG_ROTI = "roti";
              final String TAG_SPECIAL = "special";
              final String TAG_SPECIAL_EXTRA = "specialextra";
              final String TAG_VEGIE1 = "vegieone";
              final String TAG_VEGIE2 = "vegietwo";
              final String TAG_VEGIE3 = "vegiethree";
              final String TAG_OTHER = "other";*/
            /*List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            //  JSONObject json_obj_all = jParser.makeHttpRequest(url_all_products, "GET", params);
            JSONObject json_obj_menu = jParser.makeHttpRequest(url_mess_menu, "GET", params);

            // Check your log cat for JSON reponse
            //   Log.d("All Products: ", json_obj_all.toString());

            try {
                // Checking for SUCCESS TAG
                //     int success1 = json_obj_all.getInt(TAG_SUCCESS);


                int success2 = json_obj_menu.getInt(TAG_SUCCESS);

/*

                if (success1 == 1) {
                    // products found
                    // Getting Array of Products
                    mess = json_obj_all.getJSONArray(TAG_MESSINFO);

                    // looping through All Products
                    for (int i = 0; i < mess.length(); i++) {
                        JSONObject c = mess.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_MESSID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_MESSID, id);
                        map.put(TAG_NAME, name);

                        Log.d("Dipak: Mess Id ", id);
                        Log.d("Dipak: Mess Name ", name);

                        // adding HashList to ArrayList
                       // AllMessInfoFromDatabase.add(map);
                    }
                } else {
                    // no products found
                    Log.d("Dipak: ", "No Mess found!");
                }


                if(success2==1){
                    // products found
                    // Getting Array of Products
                    mess2 = json_obj_menu.getJSONArray(TAG_MESSINFO);

                    // looping through All Products
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


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_MESSID, id+" "+MessArea);
                        map.put(TAG_RICE, rice);
                        map.put(TAG_VEGIE1, vegie1);
                        map.put(TAG_VEGIE2, vegie2);
                        map.put(TAG_VEGIE3, vegie3);
                        map.put(TAG_ROTI, roti);
                        map.put(TAG_SPECIAL, special);
                        map.put(TAG_SPECIAL_EXTRA, special_extra);
                        map.put(TAG_OTHER, other);


                        Log.d("inDoinBackground: ID", map.toString());


                        // adding HashList to ArrayList
                        AllMessInfoFromDatabase.add(map);
                    }
                    Log.d("inDoinBackground: ID", AllMessInfoFromDatabase.toString());




                } else {
                    // no products found
                    Log.d("Dipak: ", "No Mess found!");
                }

            }
            catch (Exception e)
            {



                   /* SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_STANDARD)
                            .setText("Oops! No Internet Connection!")
                            .setDuration(Style.DURATION_MEDIUM)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                e.printStackTrace();
            }



            return null;*/
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
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


                        Log.d("inDoinBackground: ID", map.toString());


                        // adding HashList to ArrayList
                        AllMessInfoFromDatabase.add(map);
                    }


                }
                else
                {
                    Toast.makeText(mPassedView.getContext(),"Oops,Error Updating Mess Menus",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            View v=intializeList(mPassedView);
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread

        }

    }


    class DummyBackgroundTask extends AsyncTask<String, String, String> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected String doInBackground(String... args) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;

            // Return a new random list of cheeses
        }

        @Override
        protected void onPostExecute(String file_url) {

            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread
            onRefreshComplete(file_url);
        }


    }








}