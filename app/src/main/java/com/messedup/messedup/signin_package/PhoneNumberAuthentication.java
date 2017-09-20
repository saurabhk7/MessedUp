package com.messedup.messedup.signin_package;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.messedup.messedup.MainActivity;
import com.messedup.messedup.R;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
public class PhoneNumberAuthentication extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;

    AuthCredential GoogleSignInCredential;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent recvGoogleInt=getIntent();
        GoogleSignInCredential= (AuthCredential) recvGoogleInt.getExtras().get("GoogleCredential");
        //if (GoogleSignInCredential != null) {
            Log.e("PNA RECV CRED",GoogleSignInCredential.getProvider());



        // Toast.makeText(PhoneNumberAuthentication.this,"Signed In",Toast.LENGTH_SHORT).show();

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                    ))
                            .build(),
                    RC_SIGN_IN);

        }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        Log.d("1ad","1ff");
        if (requestCode == RC_SIGN_IN) {
            Log.d("2ad","1ff");

            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.d("3ad","1ff");

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Log.d("4ad","1ff");


                Log.d("RESULT CODE", "OK");

                Log.d("5da","1ff");

                LinkingAuthProv();



            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_STANDARD)
                            .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                            .setText("   Login Cancelled!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    startActivity(new Intent(this,GoogleSignIn.class));


                    Log.e("Login","Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_BUTTON)
                            .setButtonText("RETRY")
                            .setIconResource(Style.ICONPOSITION_LEFT, R.drawable.ic_error_outline_white_24dp)
                            .setText("   No Internet Connection!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_STANDARD)
                            .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                            .setText("   Oops, Some Error occurred!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    Log.e("Login","Unknown Error");
                    return;
                }
            }
            Log.e("Login","Unknown sign in response");
        }


    }

    public void LinkingAuthProv()
    {

        Log.e("IN LINKING AUTH CRED",GoogleSignInCredential.getProvider());
        Log.e("LINKINGAUTHCURRENTUSER","++++000000++++");

        Log.e("LINKINGAUTHCURRENTUSER","+++++"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                Log.e("user not null","999090909"+FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(GoogleSignInCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Linkingauth", "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.d("Linkingauth"," ***** "+ user.getUid());


                            FirebaseMessaging.getInstance().subscribeToTopic("allDevice");

                            startActivity(new Intent(PhoneNumberAuthentication.this,MainActivity.class));
                            finish();

                            //updateUI(user);
                        } else {
                            Log.w("Linkingauth", "linkWithCredential:failure", task.getException());
                            Toast.makeText(PhoneNumberAuthentication.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }


        @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}*/

public class PhoneNumberAuthentication extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    AuthCredential GoogleSignInCredential;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // Intent recvGoogleInt=getIntent();
       // GoogleSignInCredential= (AuthCredential) recvGoogleInt.getExtras().get("GoogleCredential");
       // Log.d("RECV CRED",GoogleSignInCredential.toString());


        mAuth=FirebaseAuth.getInstance();

        // Toast.makeText(PhoneNumberAuthentication.this,"Signed In",Toast.LENGTH_SHORT).show();

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                ))
                        .setIsSmartLockEnabled(true)
                        .build(),
                RC_SIGN_IN);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {

                Log.d("RESULT CODE", "OK");

                if(mAuth.getCurrentUser().getProviders().contains("google.com")){

                    Toast.makeText(PhoneNumberAuthentication.this, "Welcome Back!",
                            Toast.LENGTH_SHORT).show();
                    Intent gotoPhoneAuthInt = new Intent(PhoneNumberAuthentication.this, MainActivity.class);
                    // gotoPhoneAuthInt.putExtra("GoogleCredential",credential);
                    startActivity(gotoPhoneAuthInt);
                }
                else {

                    Intent gotoPhoneAuthInt = new Intent(PhoneNumberAuthentication.this, GoogleSignIn.class);
                    // gotoPhoneAuthInt.putExtra("GoogleCredential",credential);
                    startActivity(gotoPhoneAuthInt);
                }

               // LinkingAuthProv();



            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_STANDARD)
                            .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                            .setText("   Login Cancelled!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    startActivity(new Intent(this,GoogleSignIn.class));


                    Log.e("Login","Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_BUTTON)
                            .setButtonText("RETRY")
                            .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                            .setText("   No Internet Connection!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();
                    Log.e("Login","No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    SuperActivityToast.create(PhoneNumberAuthentication.this, new Style(), Style.TYPE_STANDARD)
                            .setIconResource(Style.ICONPOSITION_LEFT,R.drawable.ic_error_outline_white_24dp)
                            .setText("   Oops, Some Error occurred!")
                            .setDuration(Style.DURATION_LONG)
                            .setFrame(Style.FRAME_LOLLIPOP)
                            .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_AMBER))
                            .setAnimations(Style.ANIMATIONS_POP).show();

                    Log.e("Login","Unknown Error");
                    return;
                }
            }
            Log.e("Login","Unknown sign in response");
        }


    }

    public void LinkingAuthProv()
    {

        Log.d("IN LINKING AUTH CRED",GoogleSignInCredential.getProvider());
        Log.d("LINKINGAUTHCURRENTUSER",FirebaseAuth.getInstance().getCurrentUser().getUid());


        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(GoogleSignInCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Linkingauth", "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.d("Linkingauth", user.getUid());


                            FirebaseMessaging.getInstance().subscribeToTopic("allDevice");

                            startActivity(new Intent(PhoneNumberAuthentication.this,MainActivity.class));
                            finish();

                            //updateUI(user);
                        } else {
                            Log.w("Linkingauth", "linkWithCredential:failure", task.getException());
                            Toast.makeText(PhoneNumberAuthentication.this, "Authentication failed.",
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


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}