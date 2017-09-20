package com.messedup.messedup.signin_package;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.messedup.messedup.MainActivity;
import com.messedup.messedup.R;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.bitmap;
import static android.R.attr.fillColor;

/*
public class GoogleSignIn extends AppCompatActivity {

    private SignInButton mGoogleBtn;
    private ActionProcessButton btnSignIn;
    private static  final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG ="GOOGLE_SIGN_IN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);

        mAuth=FirebaseAuth.getInstance();



        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    Intent gotoPhoneAuthInt=new Intent(GoogleSignIn.this,MainActivity.class);
                    startActivity(gotoPhoneAuthInt);
                }

            }
        };





        mGoogleBtn=(SignInButton)findViewById(R.id.googleBtn);
        Button mToastBtn = (Button) findViewById(R.id.ToastTestBtn);

        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        // you can display endless google like progress indicator
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);


// set progress > 0 to start progress indicator animation




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient =new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                                .setButtonText("RETRY")
                                .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                                .setText("   Oops, Google Sign In Failed!")
                                .setDuration(Style.DURATION_LONG)
                                .setFrame(Style.FRAME_LOLLIPOP)
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_POP).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSignIn.setProgress(1);
                signIn();

            }
        });

        mToastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                        .setButtonText("RETRY")
                        .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                        .setText("   Oops, Google Sign In Failed!")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                        .setAnimations(Style.ANIMATIONS_POP).show();

                btnSignIn
                        .setProgress(-1);
            }
        });







    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                if(FirebaseAuth.getInstance().getCurrentUser()==null)
                {
                    Log.e("ONACTRES", "FIREBASE USER IS NULL");

                    Log.e("Logged In as: ",account.getEmail());
                    Log.e("Logged In as: ",account.getPhotoUrl().toString());
                   // Log.e("Logged In as: ",account.));


                }
                else
                {
                    Log.d("ONACTRES", "NOT NULL FIREBASE USER IS "+FirebaseAuth.getInstance().getCurrentUser().getUid());

                }

                SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
                        .setText("Logged In as: "+account.getEmail())
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                        .setAnimations(Style.ANIMATIONS_POP).show();



                //  Toast.makeText(GoogleSignIn.this,account.getEmail(),Toast.LENGTH_SHORT).show();

                firebaseAuthWithGoogle(account);
            } else {


                SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                        .setButtonText("RETRY")
                        .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                        .setText("   Oops, Google Sign In Failed!")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                        .setAnimations(Style.ANIMATIONS_POP).show();


                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.e("firebaseAuthWithGoogle:","***" + account.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
       */
/* PhoneNumberAuthentication obj=new PhoneNumberAuthentication(credential);*//*


        Log.e("GOOGLESIGNIN", "sending cred");
        Log.e("GOOGLESIGNINCREDSENT", credential.getProvider());



        Intent gotoPhoneAuthInt=new Intent(GoogleSignIn.this,PhoneNumberAuthentication.class);
        gotoPhoneAuthInt.putExtra("GoogleCredential",credential);
        startActivity(gotoPhoneAuthInt);


        */
/*mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });*//*

    }

    private void updateUI(FirebaseUser mUser)
    {
        if(mUser==null)
        {
            Toast.makeText(GoogleSignIn.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
*/
public class GoogleSignIn extends AppCompatActivity {

    private SignInButton mGoogleBtn;
    private ActionProcessButton btnSignIn;
    private static  final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG ="GOOGLE_SIGN_IN";
    private DetailsSharedPref mDetailsSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);




/*

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {

                    Intent gotoPhoneAuthInt=new Intent(GoogleSignIn.this,MainActivity.class);
                    startActivity(gotoPhoneAuthInt);
                }

            }
        };
*/




        mDetailsSharedPref=new DetailsSharedPref(this);

        mGoogleBtn=(SignInButton)findViewById(R.id.googleBtn);
        Button mToastBtn = (Button) findViewById(R.id.ToastTestBtn);

        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        // you can display endless google like progress indicator
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);


// set progress > 0 to start progress indicator animation





        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient =new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                                .setButtonText("RETRY")
                                .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                                .setText("   Oops, Google Sign In Failed!")
                                .setDuration(Style.DURATION_LONG)
                                .setFrame(Style.FRAME_LOLLIPOP)
                                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                                .setAnimations(Style.ANIMATIONS_POP).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSignIn.setProgress(1);
                signIn();

            }
        });

        mToastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                        .setButtonText("RETRY")
                        .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                        .setText("   Oops, Google Sign In Failed!")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                        .setAnimations(Style.ANIMATIONS_POP).show();

                btnSignIn
                        .setProgress(-1);
            }
        });







    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();

                if(FirebaseAuth.getInstance().getCurrentUser()==null)
                {
                    Log.d("ONACTRES", "FIREBASE USER IS NULL");

                }
                else
                {
                    Log.d("ONACTRES", "NOT NULL FIREBASE USER IS "+FirebaseAuth.getInstance().getCurrentUser().getUid());

                }

                if (account != null) {
                    SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
                            .setText("Logged In as: "+account.getEmail())
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                    mDetailsSharedPref.updateNameSharedPrefs(account.getDisplayName());
                    mDetailsSharedPref.updateEmailSharedPrefs(account.getEmail());
                    if(account.getPhotoUrl()!=null)
                        mDetailsSharedPref.updatePhotURLSharedPrefs(account.getPhotoUrl().toString());

                }


                //  Toast.makeText(GoogleSignIn.this,account.getEmail(),Toast.LENGTH_SHORT).show();

                firebaseAuthWithGoogle(account);
            } else {


                SuperActivityToast.create(GoogleSignIn.this, new Style(), Style.TYPE_BUTTON)
                        .setButtonText("RETRY")
                        .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                        .setText("   Oops, Google Sign In Failed!")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_RED))
                        .setAnimations(Style.ANIMATIONS_POP).show();


                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
       /* PhoneNumberAuthentication obj=new PhoneNumberAuthentication(credential);*/

        Log.d("GOOGLESIGNIN", "sending cred");
        Log.d("GOOGLESIGNINCREDSENT", credential.getProvider());



        LinkingAuthProv(credential);


        /*mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });*/

        /*Intent gotoPhoneAuthInt=new Intent(GoogleSignIn.this,PhoneNumberAuthentication.class);
       // gotoPhoneAuthInt.putExtra("GoogleCredential",credential);
        startActivity(gotoPhoneAuthInt);*/

    }

    private void updateUI(FirebaseUser mUser)
    {
        if(mUser==null)
        {
            btnSignIn
                    .setProgress(-1);
            Toast.makeText(GoogleSignIn.this,"Sign In Failed",Toast.LENGTH_SHORT).show();
        }
    }

    public void LinkingAuthProv(AuthCredential credential)
    {

       /* Log.d("IN LINKING AUTH CRED",GoogleSignInCredential.getProvider());*/
        Log.d("LINKINGAUTHCURRENTUSER",FirebaseAuth.getInstance().getCurrentUser().getUid());



        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Linkingauth", "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.d("Linkingauth", user.getUid());


                            FirebaseMessaging.getInstance().subscribeToTopic("allDevice");

                            startActivity(new Intent(GoogleSignIn.this,MainActivity.class));
                            finish();

                            //updateUI(user);
                        } else {

                            btnSignIn
                                    .setProgress(-1);
                            Log.w("Linkingauth", "linkWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignIn.this, "Welcome Back!",
                                    Toast.LENGTH_SHORT).show();

                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
