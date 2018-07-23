package com.messedup.messedup.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.messedup.messedup.MainActivity;
import com.messedup.messedup.MessInfoActivity;
import com.messedup.messedup.R;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.SharedPreference;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;

import java.util.List;

/**
 * Created by saurabh on 24/8/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView MessNameTxtView, MenuUpdatedTextView, MessOpenBadge, MessCloseBadge, costTextView;
    public ListView SpecialList, MenuListView1, MenuListView2, MenuListView3;
    public LinearLayout MenuLayout;
    public ImageView OpenImg, CloseImg, SpecialImg;
    public ImageButton MessInfoBtn, ShareMenuBtn;

    public TextView menuTxtCard;
    private Context context;
    MaterialFavoriteButton favorite;

    SharedPreference sharedPreference;
    List<String> favorites;


    public View view;
    public MenuCardView CurrentObj;


    public MyViewHolder(final View v) {
        super(v);


        context = v.getContext();

        final DetailsSharedPref detailsSharedPref=new DetailsSharedPref(v.getContext());


        SpecialImg = (ImageView) v.findViewById(R.id.SpecialImgView);

        MessNameTxtView = (TextView) v.findViewById(R.id.mess_name);
        MessOpenBadge = (TextView) v.findViewById(R.id.MessOpenBadge);
        MessOpenBadge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_green_24dp, 0, 0, 0);
        MessCloseBadge = (TextView) v.findViewById(R.id.MessCloseBadge);
        MessCloseBadge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_white_24dp, 0, 0, 0);
        MenuUpdatedTextView = (TextView) v.findViewById(R.id.menu_update_status);
        MenuUpdatedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_light_bulb, 0, 0, 0);
        costTextView = (TextView) v.findViewById(R.id.costTextView);
        costTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coins, 0, 0, 0);
        favorite = (MaterialFavoriteButton) v.findViewById(R.id.favButton);

        SpecialList = (ListView) v.findViewById(R.id.SpecialListView);
        MenuListView1 = (ListView) v.findViewById(R.id.list_view_1);
        MenuListView2 = (ListView) v.findViewById(R.id.list_view_2);
        MenuListView3 = (ListView) v.findViewById(R.id.list_view_3);
        MessInfoBtn = (ImageButton) v.findViewById(R.id.MessInfoBtn);

        MenuLayout = (LinearLayout) v.findViewById(R.id.menu_layout);


        menuTxtCard = (TextView) v.findViewById(R.id.menuTxtViewCard);

        ShareMenuBtn = (ImageButton) v.findViewById(R.id.shareMenuBtn);

        view = v;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked

                String meal2=detailsSharedPref.getMealStatusSharedPrefs();

                if(isNetworkAvailable()&&!meal2.equalsIgnoreCase("OFFLINE")) {



                    Intent InfoIntent = new Intent(view.getContext(), MessInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("messobj", CurrentObj);
                    InfoIntent.putExtras(bundle);
                    view.getContext().startActivity(InfoIntent);


                }
                else
                {
                    Intent InfoIntent = new Intent(view.getContext(), MessInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("messobj", CurrentObj);
                    InfoIntent.putExtras(bundle);
                    view.getContext().startActivity(InfoIntent);
                    Toast.makeText(view.getContext(),"You are currently offline!",Toast.LENGTH_SHORT).show();
                }
            }
        });



        String meal=detailsSharedPref.getMealStatusSharedPrefs();


        if(meal.equalsIgnoreCase("OFFLINE")||meal.equals(" "))
        {
            meal="Menu";
        }


        final String finalMeal = meal;
        ShareMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Messed Up! \nMess, Menu and more!");
                    String sAux = "\nHey!\nCheckout today's "+ finalMeal +" at *" + CurrentObj.getMessID() + "* !" +
                            "\n" + CurrentObj + "\n\nTap for more: ";
                    sAux = sAux + "http://www.messedup.in/app \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    view.getContext().startActivity(Intent.createChooser(i, "Share "+CurrentObj.getMessID()+"'s Menu"+" with"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });


        /*sharedPreference = new SharedPreference();
        favorites = sharedPreference.getFavorites(v.getContext());


        favorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean isfavorite) {
                       // Toast.makeText(context,"Fav "+isfavorite+" clicked of: "+CurrentObj.getMessID(),Toast.LENGTH_SHORT).show();

                        if(isfavorite)
                        {
                            sharedPreference.addFavorite(context,
                                    CurrentObj.getMessID());
                            Toast.makeText(context,"Fav Added of: "+CurrentObj.getMessID(),Toast.LENGTH_SHORT).show();

                            MenuFragment menuFragment=new MenuFragment();

                        }
                        if(!isfavorite)
                        {
                            sharedPreference.removeFavorite(context,
                                    CurrentObj.getMessID());

                            Toast.makeText(context,"Fav Removed of: "+CurrentObj.getMessID(),Toast.LENGTH_SHORT).show();

                        }



                    }
                });


        favorite.setOnFavoriteAnimationEndListener(
                new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {
                        //
                    }
                });

*/



        /*MenuListView1.setClickable(false);
        MenuListView2.setClickable(false);
        MenuListView3.setClickable(false);*/


     /*   MessInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InfoIntent=new Intent(context,ChooserActivity.class);
                InfoIntent.putExtra("messid",getAdapterPosition());
                context.startActivity(InfoIntent);

                Toast.makeText(context,"You Clicked : "+getAdapterPosition(),Toast.LENGTH_SHORT).show();
            }
        });*/





    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView lunchdinnerTxt;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        lunchdinnerTxt = (TextView) itemView.findViewById(R.id.MealTypeTextView);

    }
}

class FooterViewHolder extends RecyclerView.ViewHolder {

    public TextView versionText, contactus;

    public FooterViewHolder(final View itemView) {
        super(itemView);

        versionText = (TextView) itemView.findViewById(R.id.VersionTxt);
        contactus = (TextView) itemView.findViewById(R.id.ContactUsTxtView);

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int REQUEST_PHONE_CALL = 1;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "917387636474"));

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(itemView.getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        itemView.getContext().startActivity(intent);
                    }
                } else {
                    itemView.getContext().startActivity(intent);
                }*/


//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:+" + "+917387636474"));
//                if (ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                itemView.getContext().startActivity(callIntent);

                String[] TO = {"help@messedup.in"};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Messed Up Support!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    itemView.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                    finish();
//                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(itemView.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

