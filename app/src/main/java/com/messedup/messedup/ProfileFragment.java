package com.messedup.messedup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.messedup.messedup.ui_package.CircleTransform;
import com.messedup.messedup.ui_package.SampleDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.messedup.messedup.signin_package.GoogleSignIn;

public class ProfileFragment extends Fragment {

    Button SignOutBtn;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    private TextView mNameTxtView,mEmailTxtView,mContactTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View ProfileView=inflater.inflate(R.layout.activity_profile_fragment, container, false);

        mNameTxtView=(TextView)ProfileView.findViewById(R.id.NameTxtView);
        mEmailTxtView=(TextView)ProfileView.findViewById(R.id.EmailTxtView);
        mContactTxtView=(TextView)ProfileView.findViewById(R.id.ContactTxtView);
        SignOutBtn = (Button)ProfileView.findViewById(R.id.LogOutBtn);
        ImageButton SignOutImgBtn = (ImageButton) ProfileView.findViewById(R.id.LogOUtImgBtn);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            int i=0;
            FirebaseUser CurrentUser= FirebaseAuth.getInstance().getCurrentUser();

            for (UserInfo profile : CurrentUser.getProviderData()) {

                i++;
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                Log.d("-----PROVIDER "+i+" ----- ","---- "+providerId+" ----");
                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();

                if(providerId.equals("google.com"))
                    Log.d("-----PROVIDER "+i+" ----- ",name);

                if(providerId.equals("google.com"))
                    mNameTxtView.setText(name);
                String email = profile.getEmail();
                if(providerId.equals("firebase"))
                    Log.d("-----PROVIDER "+i+" ----- "+i,email);


                if(providerId.equals("google.com")) {


                    final Uri photoUrl = profile.getPhotoUrl();
                    final ImageView ProfilePic=(ImageView)ProfileView.findViewById(R.id.ProfilePicImg);

                    Log.d("-----PROVIDER " + i + " ----- ", photoUrl.toString());
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

                Log.d("-----PROVIDER "+i+" ----- ","----------------");
            }

            mEmailTxtView.setText(CurrentUser.getEmail());
            mContactTxtView.setText(CurrentUser.getPhoneNumber());


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
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.messedup.saurabh.mess2 \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share to"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });



        return ProfileView;


    }


}
