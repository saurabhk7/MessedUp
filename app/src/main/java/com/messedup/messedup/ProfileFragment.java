package com.messedup.messedup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;
import com.messedup.messedup.ui_package.CircleTransform;
import com.messedup.messedup.ui_package.SampleDialogFragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.messedup.messedup.signin_package.GoogleSignIn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment {

    Button ImInBtn;
    String email;
    DetailsSharedPref mDetailsSharedPref;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;
    public CircularProgressButton circularProgressButton;


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
        ImageButton SignOutImgBtn = (ImageButton) ProfileView.findViewById(R.id.LogOUtImgBtn);
        ImageButton RateAppBtn = (ImageButton) ProfileView.findViewById(R.id.rateAppBtn);

       circularProgressButton=(CircularProgressButton)ProfileView.findViewById(R.id.AnimImInBtn);




        TapToExpandTxt = (TextView) ProfileView.findViewById(R.id.tap_to_expand_badge);
        TapToExpandTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_zoom_out_map_white_24dp, 0, 0, 0);

        mDetailsSharedPref = new DetailsSharedPref(ProfileView.getContext());



        if(mDetailsSharedPref.getImInStatus().equals("notdone")) //TODO: finalise this after testing
        {
            circularProgressButton.setIndeterminateProgressMode(true); // turn on indeterminate progress

            circularProgressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*
                Toast.makeText(ProfileView.getContext(),"Thank You!",Toast.LENGTH_SHORT).show();
*/
                    circularProgressButton.setProgress(50);
                   /* new Handler().postDelayed(new Runnable() {
                        public void run() {

*/
                            addImIn(ProfileView);


/*

                        }
                    }, 3000);
*/

                }
            });

        }
        else
        {

            circularProgressButton.setProgress(100);

          //  Toast.makeText(ProfileView.getContext(),"Thank You, Already Noted!",Toast.LENGTH_SHORT).show();

        }

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
        //SignOut Dialog will open
        SignOutImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleDialogFragment fragment
                        = SampleDialogFragment.newInstance(5,10.0f,true,false);
                fragment.show(getActivity().getFragmentManager(), "blur_sample");
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
                    sAux = sAux + "https://goo.gl/rseyyY \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share Messed Up App to"));
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



        final View thumb1View = ProfileView.findViewById(R.id.thumb_button_1);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(ProfileView.getContext(),"Clicked!",Toast.LENGTH_SHORT).show();
                zoomImageFromThumb(thumb1View, R.drawable.info_latest,ProfileView);
            }
        });

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);





        return ProfileView;


    }

    private void addImIn(View view) {

        HitCount hitCount=new HitCount(view);

        hitCount.execute("http://wanidipak56.000webhostapp.com/imincount.php");

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
        expandedImageView.setImageResource(imageResId);

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
