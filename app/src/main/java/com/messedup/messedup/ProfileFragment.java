package com.messedup.messedup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.adapters.TokenDisplayListAdapter;
import com.messedup.messedup.firebase_messaging.MyFirebaseMessagingService;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;
import com.messedup.messedup.sqlite_helper_package.SQLiteHelper.DatabaseHandler;
import com.messedup.messedup.ui_package.CircleTransform;
import com.messedup.messedup.ui_package.SampleDialogFragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.messedup.messedup.signin_package.GoogleSignIn;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProfileFragment extends Fragment {

    Button ImInBtn;
    String contactnum, username, email,refcode = "SAURABHK734";
    DetailsSharedPref mDetailsSharedPref;
    String Uid;

    String explainString1, explainString2, explainString3, explainString4;

    TextView explainTextView1, explainTextView2, explainTextView3, explainTextView4;
    View explainLayout;

    ListView lView;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;
    DoubleBounce doubleBounce ;

   // TextView refTxtView;


    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    public CircularProgressButton circularProgressButton;

    private TextView refTxtView, refInfoTxtView;


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;

    }

    private TextView mNameTxtView,mEmailTxtView,mContactTxtView,TapToExpandTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View ProfileView = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        mDetailsSharedPref =new DetailsSharedPref(ProfileView.getContext());

        mNameTxtView = (TextView) ProfileView.findViewById(R.id.NameTxtView);
        mEmailTxtView = (TextView) ProfileView.findViewById(R.id.EmailTxtView);
        mContactTxtView = (TextView) ProfileView.findViewById(R.id.ContactTxtView);
/*
        ImInBtn = (Button) ProfileView.findViewById(R.id.ImInButton);
*/


        lView = (ListView) ProfileView.findViewById(R.id.tokenDisplayListView);

        explainLayout = ProfileView.findViewById(R.id.TokenExplainLayout);

        explainTextView1 = (TextView)ProfileView.findViewById(R.id.explaintxt1);
        explainTextView2 = (TextView)ProfileView.findViewById(R.id.explaintxt2);
        explainTextView3 = (TextView)ProfileView.findViewById(R.id.explaintxt3);
        explainTextView4 = (TextView)ProfileView.findViewById(R.id.explaintxt4);

        explainString1 = "1. Click <b><font color=#59c614>\"BUY TOKENS\"</font></b> to purchase  <font color=#59c614>any number of tokens</font> of the Messes you wish <br><b>(with No minimum tokens)</b>";
        explainString2 = "2. <b><font color=#59c614>Pay</font></b> according to your selected tokens with our <b><font color=#59c614>secure payment gateway</font></b> and <b><font color=#59c614>avail exciting offers</font></b>";
        explainString3 = "3. <b><font color=#59c614>Decide your mess</font></b> by comparing the daily updated and weekly predicted menu";
        explainString4 = "4. <b><font color=#59c614>Enjoy your meal</font></b> at your favourite mess everyday";

        explainTextView1.setText(Html.fromHtml(explainString1));
        explainTextView2.setText(Html.fromHtml(explainString2));
        explainTextView3.setText(Html.fromHtml(explainString3));
        explainTextView4.setText(Html.fromHtml(explainString4));


        ImageButton SignOutImgBtn = (ImageButton) ProfileView.findViewById(R.id.LogOUtImgBtn);
        ImageButton RateAppBtn = (ImageButton) ProfileView.findViewById(R.id.rateAppBtn);

        ImageButton viewHistoryBtn = (ImageButton) ProfileView.findViewById(R.id.viewHistoryBtn);

        refTxtView = (TextView) ProfileView.findViewById(R.id.ReferralTxtView);


        circularProgressButton=(CircularProgressButton)ProfileView.findViewById(R.id.AnimImInBtn);

        progressBar = (ProgressBar)ProfileView.findViewById(R.id.spin_kit_progress);
        doubleBounce = new DoubleBounce();

        refInfoTxtView = (TextView) ProfileView.findViewById(R.id.ReferralInfoTxtView);


        TapToExpandTxt = (TextView) ProfileView.findViewById(R.id.tap_to_expand_badge);
        TapToExpandTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_zoom_out_map_white_24dp, 0, 0, 0);

        mDetailsSharedPref = new DetailsSharedPref(ProfileView.getContext());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DetailsSharedPref dspobj2=new DetailsSharedPref(ProfileView.getContext());
        final String status = dspobj2.getMealStatusSharedPrefs();

        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        new UserTokenInfo(ProfileView).execute();
        new ReferalCodeText(ProfileView,uid).execute();


        if(status.equals("OFFLINE")) {
            circularProgressButton.setProgress(-1);
            circularProgressButton.setClickable(false);
        }




        /*if(mDetailsSharedPref.getImInStatus().equals("notdone")) //TODO: finalise this after testing
        {
            circularProgressButton.setIndeterminateProgressMode(true); // turn on indeterminate progress

            circularProgressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
*//*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*//*
                    circularProgressButton.setProgress(50);
                   *//* new Handler().postDelayed(new Runnable() {
                        public void run() {

*//*
                            addImIn(ProfileView);


*//*

                        }
                    }, 3000);
*//*

                }
            });

        }
        else
        {

            circularProgressButton.setProgress(100);

          //  Toast.makeText(ProfileView.getContext(),"Thank You, Already Noted!",Toast.LENGTH_SHORT).show();

        }
*/


        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(status.equals("OFFLINE")) {
                    circularProgressButton.setProgress(-1);
                    circularProgressButton.setClickable(false);
                }
                else {

                    Intent toSelectTokenAct = new Intent(getActivity(), TokenSelectionActivity.class);
                    startActivity(toSelectTokenAct);
                }

        }
    });



        /*ImInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileView.getContext(),"Clicked! Button",Toast.LENGTH_SHORT).show();

            }
        });


*/



        // End to 0.1f if you desire 90% fade animation
        final Animation fadeOut = new AlphaAnimation(1.0f, 0.5f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(3000);



        fadeOut.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start fadeIn when fadeOut ends (repeat)
                TapToExpandTxt.setAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        TapToExpandTxt.startAnimation(fadeOut);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            int i = 0;
            FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();


            for (UserInfo profile : CurrentUser.getProviderData()) {

                i++;
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                Log.d("-----PROVIDER " + i + " ----- ", "---- " + providerId + " ----");
                // UID specific to the provider

                // Name, email address, and profile photo Url



                if (providerId.equals("google.com")) {
                    String nm = profile.getDisplayName();
                    Log.e("EMAIL: ","Name got from "+providerId+" :"+nm);
                    mDetailsSharedPref.updateNameSharedPrefs(nm);
                    mNameTxtView.setText(nm);
                    username = nm;
                }



                if (providerId.equals("firebase")) {

                    email = profile.getEmail();
                    Log.e("EMAIL: ","Email got from "+providerId+" :"+email);
                    mDetailsSharedPref.updateEmailSharedPrefs(email);
                    mEmailTxtView.setText(email);
                }


                if (providerId.equals("google.com")) {


                    final Uri photoUrl = profile.getPhotoUrl();
                    final ImageView ProfilePic = (ImageView) ProfileView.findViewById(R.id.ProfilePicImg);

                    Log.e("EMAIL: ","PhotoUrl got from "+providerId+" :"+photoUrl);
                    try {
                        Picasso.with(inflater.getContext()).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(inflater.getContext()).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                            }
                        });
                    } catch (Exception e) {
                        Log.v("E_VALUE", e.getMessage());
                    }
                }

            }


            mContactTxtView.setText(CurrentUser.getPhoneNumber());

            contactnum=CurrentUser.getPhoneNumber();

            mDetailsSharedPref.updatePhoneSharedPrefs(CurrentUser.getPhoneNumber());

            //refcode = username.substring(0,3)+contactnum.substring(contactnum.length()-3,contactnum.length());

            //refTxtView.setText(refcode.toLowerCase());


        }


        if(!mDetailsSharedPref.getEmailSharedPrefs().equals("EMAIL"))
            mEmailTxtView.setText(mDetailsSharedPref.getEmailSharedPrefs());
        if(!mDetailsSharedPref.getNameSharedPrefs().equals("NAME"))
            mNameTxtView.setText(mDetailsSharedPref.getNameSharedPrefs());

        String ImgUrl=mDetailsSharedPref.getPhotoURLSharedPrefs();

        if (!ImgUrl.equals("URL"))
        {
            final Uri photoUrl = Uri.parse(ImgUrl);
            final ImageView ProfilePic = (ImageView) ProfileView.findViewById(R.id.ProfilePicImg);

            // Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
            try {
                Picasso.with(inflater.getContext()).load(photoUrl).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(ProfilePic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(inflater.getContext()).load(photoUrl).transform(new CircleTransform()).into(ProfilePic);

                    }
                });
            } catch (Exception e) {
                Log.v("E_VALUE", e.getMessage());
            }
            //  setProfileImage(ProfileView);

        }

        mAuth = FirebaseAuth.getInstance();
        //SignOut Dialog will open
        SignOutImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SampleDialogFragment fragment
//                        = SampleDialogFragment.newInstance(5,10.0f,true,false);
//                fragment.show(getActivity().getFragmentManager(), "blur_sample");

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout?")
                        .setContentText("Are you sure?")
                        .setCancelText("Cancel")
                        .setConfirmText("Yes, logout!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {

                                sDialog.cancel();
                                Toast.makeText(getContext(),"User Signed Out",Toast.LENGTH_SHORT).show();

                                mAuth.signOut();

                                startActivity(new Intent(getActivity(), PhoneNumberAuthentication.class) );

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();

            }
        });





        ImageButton shareBtn=(ImageButton)ProfileView.findViewById(R.id.shareAppBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Messed Up! \nMess, Menu and more!");
                    String sAux = "\nHey!\nCheckout and Download Messed Up! on Google Play. Download and " +
                            "get Mess Menu Updates!\n\n";
                    sAux = sAux + "http://www.messedup.in/app \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share Messed Up App to"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });


        ImageButton shareRefBtn=(ImageButton)ProfileView.findViewById(R.id.shareRefBtn);
        shareRefBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Messed Up! \nMess, Menu and more!");
                    String sAux = "\nHey!\nCheckout Messed Up on Google Play and " +
                            "use *'"+refcode.toLowerCase()+"'* as your referral code during payment to get *FLAT ₹51 Paytm Cashback* on your first transaction! \n\n";
                    sAux = sAux + "http://www.messedup.in/app \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share your referral code with"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });


        RateAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Your feedback is appreciated!",Toast.LENGTH_SHORT).show();

                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        viewHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!status.equals("OFFLINE")) {
                    startActivity(new Intent(getActivity(), TokenUseHistoryActivity.class));
                }
                else
                {
                    Toast.makeText(getContext(),"Oops, you are currently offline", Toast.LENGTH_SHORT).show();
                }
            }
        });



        final View thumb1View = ProfileView.findViewById(R.id.thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(ProfileView.getContext(),"Clicked!",Toast.LENGTH_SHORT).show();
                //zoomImageFromThumb(thumb1View, R.drawable.info_latest_2,ProfileView);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);



       /* final String[] messname = {"Kwality Mess", "PICT College", "Navruchi", "Gujrati Mess"};

        int[] tokentoexpire = {5,10,12,7};

        final int[] totaltokens = {10, 23, 12, 7};

        String[] expirydates = {"10/5/2018","13/5/2018","13/5/2018","10/5/2018"};


        ListView lView = (ListView) ProfileView.findViewById(R.id.tokenDisplayListView);

        TokenDisplayListAdapter lAdapter = new TokenDisplayListAdapter(getContext(), messname, tokentoexpire, expirydates, totaltokens);

        lView.setAdapter(lAdapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                //Toast.makeText(getContext(), messname[i]+" "+totaltokens[i], Toast.LENGTH_SHORT).show();

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Use "+messname[i]+" token!")
                        .setCancelText("No")
                        .setConfirmText("Yes,use it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {

                                               sendNotification("Enjoy Your Meal at "+messname[i],"At 12:30 p.m. " +
                                                "on May 31 2018 | "+(totaltokens[i]-1)+" tokens left");


                                String s = "\n" +
                                        "Time Used: 12:30 p.m.\n\n" +
                                        "Date Used: May 31 2018\n\n"+(totaltokens[i]-1)+" tokens left of "+messname[i]+"\n";


                                sDialog

                                        .setTitleText("Enjoy your meal")
                                        .setContentText("\n" +
                                                "Time Used: 12:30 p.m.\n\n" +
                                                "Date Used: May 31 2018\n\n"+(totaltokens[i]-1)+" tokens left of "+messname[i]+"\n")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);




                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {

                                       sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }
                                };


                                Timer swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, 1500, 1500);

                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();


            }
        });
*/

        return ProfileView;


    }


    private void sendNotification(String title, String message) {

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.ic_spoonfork_notif)
                .setColor(Color.parseColor("#FFcb202d"))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager)getActivity().
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(getRequestCode(), notificationBuilder.build());



    }

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }


    private void addImIn(View view) {

        HitCount hitCount=new HitCount(view);

        String BASEURL = Constants.getBaseUrl();

        hitCount.execute(BASEURL+"/imincount.php");

    }


    class UserTokenInfo extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context contextFinal;
        private String MessArea;
        JSONArray jsonResponseArray = null;
        String json = "";
        public DatabaseHandler db;

        public UserTokenInfo(View profileView) {

            mPassedView = profileView;
            contextFinal = mPassedView.getContext();
        }


        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";


        /**
         * @use clear the initial Hashmap
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setIndeterminateDrawable(doubleBounce);
            progressBar.setVisibility(View.VISIBLE);

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


                String BASEURL = Constants.getBaseUrl();

                Log.e("BASEURL: ", BASEURL + " : " + Uid);

                URL url = new URL(BASEURL + "getUserTokenInfo.php?userid=" + Uid);


                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();


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
                        Log.e("ProfDebug", json);

                        jsonResponseArray = new JSONArray(json);

                        Log.e("ProfDebugafter", jsonResponseArray.toString());

                        db.updateUserCard(Uid, json);


                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }


                } catch (Exception e) {
                    Log.e("Buffer Error1", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
//                try {
//                    jObj = new JSONObject(json);
//                } catch (JSONException e) {
//                    Log.e("JSON Parser", "Error parsing data " + e.toString());
//                }
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


            try {

                if (jsonResponseArray != null) {


                    //TODO: add response to set string of offer cashback from server


                    JSONArray userinfo = jsonResponseArray;

                    if (userinfo.length() < 1)
//                    if(true)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("Length:", "ZERO");
                        explainLayout.setVisibility(View.VISIBLE);
                        lView.setVisibility(View.GONE);

                    } else {

                        explainLayout.setVisibility(View.GONE);
                        lView.setVisibility(View.VISIBLE);

                        final String[] messname = new String[userinfo.length()];// = {"Kwality Mess", "PICT College", "Navruchi", "Gujrati Mess"};

                        int[] tokentoexpire = new int[userinfo.length()];// = {5,10,12,7};

                        final int[] totaltokens = new int[userinfo.length()];// = {10, 23, 12, 7};

                        String[] expirydates = new String[userinfo.length()];// = {"10/5/2018","13/5/2018","13/5/2018","10/5/2018"};

                        for (int i = 0; i < userinfo.length(); i++) {
                            JSONObject c = userinfo.getJSONObject(i);

                            // Storing each json item in variable
                            String count = c.getString("Count").trim();
                            String validity = c.getString("Validity").trim();
                            String name = c.getString("Name").trim();
                            String plateName = c.getString("PlateName").trim();


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<>();

                            // adding each child node to HashMap key => value
                            map.put("Count", count);
                            map.put("Validty", validity);
                            map.put("Name", name);
                            map.put("PlateName", plateName);

                            messname[i] = name + "-" + plateName;
                            tokentoexpire[i] = Integer.parseInt(count);
                            totaltokens[i] = Integer.parseInt(count);
                            expirydates[i] = validity.split(" ")[0];

                            Log.d("ProfFragUserToken: ID", "``````````````````````" + map.toString());


                            // adding HashList to ArrayList
                        }


                        TokenDisplayListAdapter lAdapter = new TokenDisplayListAdapter(getContext(), messname, tokentoexpire, expirydates, totaltokens);

                        lView.setAdapter(lAdapter);

                        progressBar.setVisibility(View.INVISIBLE);

                        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                                //Toast.makeText(getContext(), messname[i]+" "+totaltokens[i], Toast.LENGTH_SHORT).show();

                                final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog
                                        .setTitleText("Confirm meal")
                                        .setContentText("Use " + messname[i] + " token!")
                                        .setCancelText("Cancel")
                                        .setConfirmText("Yes,use it!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(final SweetAlertDialog sDialog) {


                                                //call asynch task

                                                new UseToken(messname[i], totaltokens[i], sDialog, sweetAlertDialog, mPassedView).execute();

//                                                sDialog.cancel();

//                                                SweetAlertDialog pDialog = new SweetAlertDialog(contextFinal, SweetAlertDialog.PROGRESS_TYPE);
                                                sDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                sDialog.setTitleText("Please wait ...");
                                                sDialog.setCancelable(false);
                                                sDialog.show();

                                            /*sendNotification("Enjoy Your Meal at "+messname[i],"At 12:30 p.m. " +
                                                    "on May 31 2018 | "+(totaltokens[i]-1)+" tokens left");


                                            String s = "\n" +
                                                    "Time Used: 12:30 p.m.\n\n" +
                                                    "Date Used: May 31 2018\n\n"+(totaltokens[i]-1)+" tokens left of "+messname[i]+"\n";


                                            sDialog

                                                    .setTitleText("Enjoy your meal")
                                                    .setContentText("\n" +
                                                            "Time Used: 12:30 p.m.\n\n" +
                                                            "Date Used: May 31 2018\n\n"+(totaltokens[i]-1)+" tokens left of "+messname[i]+"\n")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);




                                            final Handler handler = new Handler();
                                            final Runnable Update = new Runnable() {
                                                public void run() {

                                                    sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                }
                                            };


                                            Timer swipeTimer = new Timer();
                                            swipeTimer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    handler.post(Update);
                                                }
                                            }, 1500, 1500);
*/
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();


                            }
                        });


                    }
                    // close this activity
                    //contextFinal..finish();

                } else {

                    /////////If response is null : No tokens bought by user
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //   View v=intializeList(mPassedView);
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread

        }

    }


    class UseToken extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context contextFinal;
        private SweetAlertDialog sweetAlertDialog;
        private SweetAlertDialog currDialog;
        private int totaltokensleft;
        private String MessName;
        private String PlateType;
        JSONObject jObj = null;
        String json = "";

        public UseToken(String s, int totaltoken, SweetAlertDialog sDialog, SweetAlertDialog cursweetAlertDialog, View mPassedView) {

            MessName = s.split("-")[0];
            PlateType = s.split("-")[1];
            this.sweetAlertDialog =sDialog;
            totaltokensleft=totaltoken-1;
            currDialog = cursweetAlertDialog;
            this.mPassedView=mPassedView;
            contextFinal=mPassedView.getContext();

        }


        /**
         * @use clear the initial Hashmap
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            currDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
            currDialog.setTitleText("Just a sec...");
            currDialog.setContentText("Getting your plate ready...");
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

                URL url = new URL(BASEURL+"useToken.php");
                JSONObject jsonObject = new JSONObject();


                jsonObject.put("messname", MessName);
                jsonObject.put("platename", PlateType);
                jsonObject.put("userid", Uid);


                String message = jsonObject.toString();


                Log.e("AFTRTOK","mssg: "+message);

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
                    Log.e("AFTRTOK","json: "+json);
                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }



                } catch (Exception e) {
                    Log.e("Buffer Error2", "Error converting result " + e.toString());
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

                Log.e("AFTRTOK errorr: ", json);

                String success = jObj.getString("success");
                if (success.equals("true")) {
                    String date = jObj.getString("date");
                    String time = jObj.getString("time");
                    String transid = jObj.getString("transid");

                    try {
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        final Date dateObj = sdf.parse(time);
                        System.out.println(dateObj);
                        System.out.println("timenew"+new SimpleDateFormat("K:mm a").format(dateObj));
                    } catch (final ParseException e) {
                        e.printStackTrace();
                    }


                    sendNotification("Enjoy Your Meal at "+MessName,"At "+time+
                            " on "+date+" | TransID: "+transid);


                    String s = "\n" +
                            "Time Used: "+time+"\n\n" +
                            "Date Used: "+date+"\n\n"+(totaltokensleft)+" tokens left of "+MessName+"\n";


                    Intent succesIntent=new Intent(mPassedView.getContext(), TokenUseSuccess.class);

                    succesIntent.putExtra("totaltokensleft",totaltokensleft+"");
                    succesIntent.putExtra("messname",MessName+"");
                    succesIntent.putExtra("timeused",time+"");
                    succesIntent.putExtra("dateused",date+"");
                    succesIntent.putExtra("shortuid",refcode+"");
                    succesIntent.putExtra("transacid",transid+"");

                   Log.e("AFTRTOK", "totaltokensleft "+totaltokensleft+"");
                    Log.e("AFTRTOK", "messname "+MessName+"");
                    Log.e("AFTRTOK", "timeused "+time+"");
                    Log.e("AFTRTOK", "dateused "+date+"");
                    Log.e("AFTRTOK", "shortuid "+refcode+"");

                    startActivity(succesIntent);

                   /* currDialog

                            .setTitleText("Enjoy your meal")
                            .setContentText(s)
                            .setConfirmText("OK")
                            .setConfirmClickListener(null)
                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    new UserTokenInfo(mPassedView).execute();


                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {

                            currDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    };


                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 1500, 1500);

*/
                }
                else
                {


                    String msg = "Something went wrong";
                    try{
                        msg = jObj.getString("message");
                    }
                    catch (Exception e)
                    {
                        msg = "Something went wrong";
                    }
                    Toast.makeText(contextFinal,msg+", please try again later!",Toast.LENGTH_LONG).show();

                    /*
                    currDialog

                            .setTitleText("Oops!")
                            .setContentText("Insufficient tokens!")
                            .setConfirmText("Buy more!")

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    startActivity(new Intent(getContext(),TokenSelectionActivity.class));
                                }
                            })
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);


*/




                }


            } catch (Exception e) {

                Toast.makeText(contextFinal,"Oops something went wrong, please try again later!",Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }

            //   View v=intializeList(mPassedView);
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread

        }



    }


    class ReferalCodeText extends AsyncTask<String , Void ,String> {


        private View mPassedView;
        private Context contextFinal;
        private String MessArea,uid;
        JSONArray jsonResponseArray = null;
        String json = "";
        public DatabaseHandler db;

        public ReferalCodeText(View profileView, String uid) {

            mPassedView = profileView;
            this.uid= uid;
        }


        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";


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


                String BASEURL = Constants.getBaseUrl();

                Log.e("BASEURL: ", BASEURL+" : "+Uid);

                URL url = new URL(BASEURL+"getReferralText.php?userid="+Uid);


                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();



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
                    Log.e("Buffer Error3", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
//                try {
//                    jObj = new JSONObject(json);
//                } catch (JSONException e) {
//                    Log.e("JSON Parser", "Error parsing data " + e.toString());
//                }
                //String contentAsString = readIt(is,len);
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
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


            try {

                JSONObject jobj = new JSONObject(json);
                String mssgtoDisplay = jobj.getString("message");
                refcode = jobj.getString("ReferralCode");
                Log.e("PROMO", "PROMOCEODE" + mssgtoDisplay);
                Log.e("PROMO", "PROMOCEODE" + refcode);

                if(mssgtoDisplay.length()>0)
                    refInfoTxtView.setText(mssgtoDisplay);
                if(refcode.length()>0)
                    refTxtView.setText(refcode);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //   View v=intializeList(mPassedView);
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread


        }
    }






    public class HitCount extends AsyncTask<String , Void ,String> {


        private View mView;

/*
        public CircularProgressButton circularProgressButton=(CircularProgressButton)mView.findViewById(R.id.AnimImInBtn);
*/




        public HitCount(View c)
        {
            mView=c;
        }

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



            DetailsSharedPref mdetails=new DetailsSharedPref(mView.getContext());

            mdetails.updateImInStatus("done");
            circularProgressButton.setProgress(100); // turn off indeterminate progress

        }
    }



    private void zoomImageFromThumb(final View thumbView, int imageResId,View profileview) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView)profileview.findViewById(
                R.id.expanded_image);
       // expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        profileview.findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X,
                                startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,View.Y,
                                        startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(200);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }





}
