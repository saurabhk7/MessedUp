package com.messedup.messedup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messedup.messedup.TokenPackage.TokenWebViewClient;
import com.messedup.messedup.adapters.TokenExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TokenSelectionActivity extends AppCompatActivity {



    /*private ExpandablePlaceHolderView mExpandableView;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.new_token_selection);

        mContext = this.getApplicationContext();
        mExpandableView = (ExpandablePlaceHolderView)findViewById(R.id.expandableView);
        for(Feed feed : Utils.loadFeeds(this.getApplicationContext())){
            mExpandableView.addView(new HeadingView(mContext, feed.getHeading()));
            for(Info info : feed.getInfoList()){
                mExpandableView.addView(new InfoView(mContext, info));
            }
        }


    }*/


    /*UI Attributes*/
    //**
 /*   Toolbar toolbar;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static ArrayList<String> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_token_selection);

        myOnClickListener = new MyOnClickListener(this);

        initToolBar();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();

        data.add("Mess 1");
        data.add("Mess 2");
        data.add("Mess 3");
        data.add("Mess 4");
        data.add("Mess 5");



        adapter = new CustomTokenAdapter(data);
        recyclerView.setAdapter(adapter);

    }


    public void initToolBar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Select your tokens");
        setSupportActionBar(toolbar);

    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
*/
/*

    Toolbar toolbar;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private static ArrayList<String> data;


    ExpandableListView expandableListView;
    List<String> messlist;
    Map<String , List<String>> platetypes;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.expandable_listview_token_selection);
        initToolBar();

        expandableListView = (ExpandableListView) findViewById(R.id.TokenExpandableView);

        fillData();


        listAdapter = new TokenExpandableListAdapter(this, messlist, platetypes);

        expandableListView.setAdapter(listAdapter);

    }

    private void fillData() {

        messlist = new ArrayList<>();

        messlist.add("Mess 1");
        messlist.add("Mess 2");
        messlist.add("Mess 3");
        messlist.add("Mess 4");
        messlist.add("Mess 5");

        List<String> Mess1 = new ArrayList<>();
        Mess1.add("Unlimited");
        Mess1.add("Limited");

        List<String> Mess2 = new ArrayList<>();
        Mess2.add("Unlimited");
        Mess2.add("Limited");

        List<String> Mess3 = new ArrayList<>();

        Mess3.add("Unlimited");

        List<String> Mess4 = new ArrayList<>();
        Mess4.add("Unlimited");
        Mess4.add("Limited");
        Mess4.add("Non-Veg");

        List<String> Mess5 = new ArrayList<>();
        Mess5.add("Unlimited");
        Mess5.add("Limited");

    }

    public void initToolBar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Select your tokens");
        setSupportActionBar(toolbar);

    }
*/

    Toolbar toolbar;

//    private static RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private static RecyclerView recyclerView;
//    static View.OnClickListener myOnClickListener;
//    private static ArrayList<Integer> removedItems;
//    private static ArrayList<String> data;





    ExpandableListView expandableListView;
    List<String> messlist;
    Map<String , List<String>> platetypes;
    ExpandableListAdapter listAdapter;
    private ProgressBar mProgressBar;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.webview_token_selection);
        initToolBar();

        try {
            mProgressBar = (ProgressBar) findViewById(R.id.pb);



            String BASEURL = Constants.getBaseUrl();

            String urltemp = BASEURL+"/offerlogic.php";
            webView = (WebView) findViewById(R.id.TokenSelectWebview);
            ImageView errimg = (ImageView) findViewById(R.id.ErrorImgView);
            TextView errtext = (TextView) findViewById(R.id.ErrorTxtView);
            //Specify the URL you want to display//
            TokenWebViewClient tokenWebViewClient = new TokenWebViewClient(webView,errimg,errtext);

            webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(tokenWebViewClient);


            webView.setWebChromeClient(new WebChromeClient() {
                /*
                    public void onProgressChanged (WebView view, int newProgress)
                        Tell the host application the current progress of loading a page.

                    Parameters
                        view : The WebView that initiated the callback.
                        newProgress : Current page loading progress, represented by an integer
                            between 0 and 100.
                */
                public void onProgressChanged(WebView view, int newProgress) {
                    // Update the progress bar with page loading progress


                    Log.d("Progress: ", newProgress + "");
                    if (newProgress == 100) {
                        // Hide the progressbar
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    }
                }


            });


            //Obtain the WebSettings object//



            WebSettings webSettings = webView.getSettings();



//Call setJavaScriptEnabled(true)//
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setDisplayZoomControls(false);

            webView.loadUrl(urltemp);
            webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");


        }
        catch (Exception e)
        {
            Log.d("ExceptionCaught","*****");
        }


    }
    public void initToolBar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Select your tokens");
        setSupportActionBar(toolbar);

    }

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Want to go back?")
                .setContentText("Your current order will be lost")
                .setCancelText("No")
                .setConfirmText("Yes, go back")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {

                        sDialog.cancel();
//                        startActivity(new Intent(TokenSelectionActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();


        /*new AlertDialog.Builder(this)
                .setTitle("Really want to go back?")
                .setMessage("Your current order will be lost")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        PaymentGatewayActivity.super.onBackPressed();
                        startActivity(new Intent(TokenSelectionActivity.this, MainActivity.class));

                    }
                }).create().show();*/
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {

        webView.setVisibility(View.VISIBLE);
        super.onResume();
    }
}

class MyJavaScriptInterface {

    private Context ctx;
    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        Intent intent = new Intent(ctx, PaymentGatewayActivity.class);
        intent.putExtra("jsonString",html);
        ctx.startActivity(intent);
        Log.d("&*&*&*&*&",html);

    }



}