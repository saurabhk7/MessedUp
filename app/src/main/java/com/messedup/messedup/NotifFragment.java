package com.messedup.messedup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.messedup.messedup.adapters.CarouselViewPagerAdapter;
import com.messedup.messedup.adapters.CustomListAdapter;
import com.messedup.messedup.adapters.OfferPageAdapter;
import com.messedup.messedup.connection_handlers.HttpHandler;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotifFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifFragment extends Fragment  {

    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> Description = new ArrayList<>();
    ArrayList<String> Offerdate = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Integer> OfferImageArray = new ArrayList<Integer>();
    private static ViewPager mPager;
    private static int currentPage = 0;

    private ArrayList<String> OfferImageUrlArray = new ArrayList<>();
    private String[] imageUrls;

    private static boolean  OFFER_ARRAY_LOADED = false;

    private static int tot_off = 0;

    private OnFragmentInteractionListener mListener;

//    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 3000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2500; // time in milliseconds between successive task executions.


    ProgressBar progressBar;
    DoubleBounce doubleBounce ;
    FoldingCube foldingCube;


    public NotifFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifFragment newInstance(String param1, String param2) {
        NotifFragment fragment = new NotifFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View NotifView= inflater.inflate(R.layout.fragment_notif, container, false);



        progressBar = (ProgressBar)NotifView.findViewById(R.id.spin_kit_progress);
        doubleBounce = new DoubleBounce();
        foldingCube = new FoldingCube();





//       if(tot_off==0)
//        initOffers(NotifView);
        if(isNetworkAvailable())
            new GetOfferCarousel(getContext(),NotifView).execute();





        HashMap<String,String> NotifMap=new HashMap<>();


        if(isNetworkAvailable())
            new GetOffers(NotifView).execute();
        else
        {
            Name.clear();
            Offerdate.clear();
            Description.clear();

            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat reqdFormat=new SimpleDateFormat("dd MMM");

            DatabaseHandler db=new DatabaseHandler(NotifView.getContext());

            String nbcollt = getSharedPrefs();

            String jsonStr = db.getOfferJson(nbcollt);

            try {
                if (jsonStr != null) {

                    JSONArray data = new JSONArray(jsonStr);
                    for(int i = 0; i < data.length(); i++){
                        JSONObject offer = data.getJSONObject(i);
                        Name.add(offer.getString("Name"));
                        Description.add(offer.getString("Description"));
                        //Offerdate.add(offer.getString("Offerdate"));


                        try {
                            Date d =simpleDateFormat.parse(offer.getString("Offerdate"));
                            String req=reqdFormat.format(d);
                            Log.e(req,"--- "+req);
                            Offerdate.add(req);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.e("qwrt", Description.toString());

                } else {
//                    Toast.makeText(mcontext, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ListView listView0dinner = (ListView)NotifView.findViewById(R.id.notif_list_view);
            CustomListAdapter adapter = new CustomListAdapter(getActivity(),Name,Description,Offerdate);
            listView0dinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            progressBar.setVisibility(View.INVISIBLE);

        }

       /* list.add(0,"Gujrati Mess");
        list.add(1,"Anand Food Xprs");
        list.add(2,"PICT College Mess");
        list.add(3,"Gujrati Mess");
        list.add(4,"Kwality Mess");

        small_list.add(0,"Special Aamras on 28th September 2017");
        small_list.add(1,"Something else on 24th September 2017");
        small_list.add(2,"New offer starts from on 22th September 2017");
        small_list.add(3,"Special Kheer on 25th September 2017");
        small_list.add(4,"New Paneer Thaali with cost just 20Rs. extra");

        time_list.add(0,"28th Sept");
        time_list.add(1,"24th Sept");
        time_list.add(2,"22th Sept");
        time_list.add(3,"25th Sept");
        time_list.add(4,"NoDate");*/


      /*  for(int i=0;i<list.size();i++)
        {
            NotifMap.put(list.get(i),small_list.get(i));
        }*/




        TextView aboutustxt=(TextView)NotifView.findViewById(R.id.ContactUsTxtView);

        aboutustxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotifView.getContext(),AboutUsActivity.class));
            }
        });

        return NotifView;
    }


/*

    private void initOffers(View notifView) {

        ArrayList<Integer> OfferImageArray = new ArrayList<Integer>();

        final int[] currentPage = {0};

        for(int i=0;i<4;i++) {
            if (i % 2 == 0)
                OfferImageArray.add(R.drawable.offer_p4);
            else
                OfferImageArray.add(R.drawable.offer_p3);

        }
        final ViewPager mPager = (ViewPager) notifView.findViewById(R.id.pager);
        mPager.setAdapter(new OfferPageAdapter(getContext(),OfferImageArray));
        final CircleIndicator indicator = (CircleIndicator) notifView.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        OFFER_ARRAY_LOADED=true;
        tot_off++;

        Log.e("totoff : ",""+tot_off);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                Log.e("curpage: ", currentPage[0] +"");
                if (currentPage[0] == 4) {

                    currentPage[0] = 0;
                }
                mPager.setCurrentItem(currentPage[0], true);
                currentPage[0]++;
            }
        };

//        handler.post(Update);

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 2500);

    }

*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    class GetOffers extends AsyncTask<Void, Void, Void> {

        private String TAG = MainActivity.class.getSimpleName();
        private ProgressDialog pDialog;

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat reqdFormat=new SimpleDateFormat("dd MMM");
        String nbcoll;
        String jsonStr;
        private View view;
        //        private Context mcontext;


//        public GetOffers(Context context){
//            mcontext = context;
//        }

        GetOffers(View v)
        {
            view=v;
        }



        protected void onPreExecute() {
            super.onPreExecute();
            Name.clear();
            Offerdate.clear();
            Description.clear();

            progressBar.setIndeterminateDrawable(doubleBounce);
            progressBar.setVisibility(View.VISIBLE);

//         Showing progress dialog
//            pDialog = new ProgressDialog(mcontext);
//            pDialog.setMessage("Getting the Mess Info...");
//            pDialog.setCancelable(false);
//            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            nbcoll = getSharedPrefs();

            String urlnbcoll=getURLString(nbcoll);


//            String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getOffers.php?NBCollege=" + NBCollege);

            String BASEURL = Constants.getBaseUrl();

            jsonStr = sh.makeServiceCall(BASEURL+"/getOffers.php?NBCollege="+urlnbcoll);

            Log.e(TAG, "Response from url: " + jsonStr);
            try {
                if (jsonStr != null) {

                    JSONArray data = new JSONArray(jsonStr);
                    for(int i = 0; i < data.length(); i++){
                        JSONObject offer = data.getJSONObject(i);
                        Name.add(offer.getString("Name"));
                        Description.add(offer.getString("Description"));
                        //Offerdate.add(offer.getString("Offerdate"));


                        try {
                            Date d =simpleDateFormat.parse(offer.getString("Offerdate"));
                            String req=reqdFormat.format(d);
                            Log.e(req,"--- "+req);
                            Offerdate.add(req);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.e("qwrt", Description.toString());

                } else {
//                    Toast.makeText(mcontext, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ListView listView0dinner = (ListView)view.findViewById(R.id.notif_list_view);
            CustomListAdapter adapter = new CustomListAdapter(getActivity(),Name,Description,Offerdate);
            listView0dinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            DatabaseHandler db=new DatabaseHandler(view.getContext());

            db.updateOffer(nbcoll,jsonStr);

//            Log.e(nbcoll,jsonStr);


            if(progressBar.isIndeterminate())
                progressBar.setVisibility(View.INVISIBLE);


//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
        }
    }


    public class GetOfferCarousel extends AsyncTask<String , Void ,String> {


        private Context mContext;
        JSONArray jsonResponseArray = null;
        private View mView;
        private int arrlength = 0;

        JSONObject jObj = null;
        String json = "";


        public GetOfferCarousel(Context applicationContext, View view) {

            mContext = applicationContext;
            mView =view;
        }

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

                String BASEURL = Constants.getBaseUrl();

                URL url = new URL(BASEURL+"/getOfferCarousel.php");

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");

                //do something with response
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
                    json = sb.toString().replace("\\","");

//                    Log.e("CarouselMyTry: ", json);

                    jsonResponseArray = new JSONArray(json);


                    try {
                        jObj = new JSONObject(json);
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
            } catch (IOException e) {
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
         *
         * @use Stores the Downloaded JSON into ArrayList of HashMaps
         **/
        protected void onPostExecute(String file_url) {

            if (jsonResponseArray!=null) {


                JSONArray userinfo = jsonResponseArray;
                Log.e("CarouselMyTry1: ", userinfo.toString());

                arrlength =userinfo.length();

                imageUrls = new String[arrlength];

                for (int i = 0; i < arrlength; i++) {

                    try {
                        Object c = userinfo.get(i);

                        Log.e("CarouselMyTry2: ", c.toString());

                        OfferImageUrlArray.add(c.toString());
                        imageUrls[i] = c.toString();


                    } catch (JSONException e) {

                        Log.e("CarouselMyTry3: ", "Caught");
                        e.printStackTrace();
                    }

                }

                final ViewPager mPager = (ViewPager) mView.findViewById(R.id.pager);
                final CarouselViewPagerAdapter carouselViewPagerAdapter = new CarouselViewPagerAdapter(mContext,imageUrls);
                mPager.setAdapter(carouselViewPagerAdapter);
                final CircleIndicator indicator = (CircleIndicator) mView.findViewById(R.id.indicator);
                indicator.setViewPager(mPager);


                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == arrlength) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage, true);
                        currentPage++;
                    }

//                    public void run() {
//
//                        Log.e("curpage: ", currentPage[0] +"");
//                        if (currentPage[0] == 4) {
//
//                            currentPage[0] = 0;
//                        }
//                        mPager.setCurrentItem(currentPage[0], true);
//                        currentPage[0]++;
//                    }
                };

                handler.post(Update);

                timer = new Timer(); // This will create a new Thread
                timer .schedule(new TimerTask() { // task to be scheduled

                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, DELAY_MS, PERIOD_MS);

                mPager.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "http://www.messedup.in";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

            }

        }


    }

            private String getSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String PreStoredArea=preferences.getString("selectedarea", "Select your Area");
        Log.d("IN SHARED PREFs","GOT STRING "+PreStoredArea);
        return PreStoredArea;


    }
    private String getURLString(String nmcoll) {


        nmcoll=nmcoll.replace(" ","%20");
        return nmcoll;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}



