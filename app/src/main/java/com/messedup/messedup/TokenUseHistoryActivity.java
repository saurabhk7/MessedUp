package com.messedup.messedup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.auth.FirebaseAuth;
import com.messedup.messedup.adapters.CustomListAdapter;
import com.messedup.messedup.adapters.TokenHistoryCustomListAdapter;
import com.messedup.messedup.connection_handlers.HttpHandler;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TokenUseHistoryActivity extends AppCompatActivity {


    ArrayList<String> messName = new ArrayList<>();
    ArrayList<String> plateType = new ArrayList<>();
    ArrayList<String> useDate = new ArrayList<>();
    ArrayList<String> TransID = new ArrayList<>();
    ArrayList<String> Offerdate = new ArrayList<>();


    View RootView;


    ProgressBar progressBar;
    DoubleBounce doubleBounce ;
    Button checktransHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_use_history);

        RootView=findViewById(R.id.activity_token_use_history);



        setTitle("Token Usage History");


        progressBar = (ProgressBar)findViewById(R.id.history_spin_kit_progress);
        doubleBounce = new DoubleBounce();

        if(isNetworkAvailable())
            new GetHistory(RootView).execute();

        checktransHistory = (Button)findViewById(R.id.check_transaction_history);

        checktransHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TokenUseHistoryActivity.this,TransactionHistoryActivity.class));
            }
        });
    }

    private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onBackPressed() {
        finish();
    }


    class GetHistory extends AsyncTask<Void, Void, Void> {

        private String TAG = MainActivity.class.getSimpleName();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat reqdFormat=new SimpleDateFormat("dd MMM");
        String nbcoll;
        String jsonStr;
        private View view;

        GetHistory(View v)
        {
            view=v;
        }



        protected void onPreExecute() {
            super.onPreExecute();
            messName.clear();
            useDate.clear();
            plateType.clear();
            TransID.clear();

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

//            nbcoll = getSharedPrefs();

//            String urlnbcoll=getURLString(nbcoll);


            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();


//            String jsonStr = sh.makeServiceCall("http://wanidipak56.000webhostapp.com/getOffers.php?NBCollege=" + NBCollege);

            String BASEURL = Constants.getBaseUrl();

            jsonStr = sh.makeServiceCall(BASEURL+"/getTokenUseHistory.php?userid="+userid);

            Log.e(TAG, "Response from url: " + jsonStr);
            try {
                if (jsonStr != null) {

                    JSONArray data = new JSONArray(jsonStr);
                    for(int i = 0; i < data.length(); i++){
                        JSONObject offer = data.getJSONObject(i);
                        messName.add(offer.getString("Name"));
                        plateType.add(offer.getString("PlateName"));
                        TransID.add("TransID: "+offer.getString("TransactionId"));
                        useDate.add(offer.getString("Date"));



                    }

                    Log.e("qwrt", plateType.toString());

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

            ListView listView0dinner = (ListView)view.findViewById(R.id.order_list_view);
            TokenHistoryCustomListAdapter adapter = new TokenHistoryCustomListAdapter(TokenUseHistoryActivity.this,messName,plateType,useDate,TransID);
            listView0dinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();

//            DatabaseHandler db=new DatabaseHandler(view.getContext());

//            db.updateOffer(nbcoll,jsonStr);

//            Log.e(nbcoll,jsonStr);

            if(progressBar.isIndeterminate())
                progressBar.setVisibility(View.INVISIBLE);


//            if (pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}


