package com.messedup.messedup.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.messedup.messedup.R;
import com.messedup.messedup.SharedPreferancesPackage.SharedPreference;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;

import java.util.List;

/**
 * Created by saurabh on 24/8/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView MessNameTxtView,MenuUpdatedTextView,MessOpenBadge,MessCloseBadge,costTextView;
    public ListView SpecialList,MenuListView1,MenuListView2,MenuListView3;
    public LinearLayout MenuLayout;
    public ImageView OpenImg,CloseImg,SpecialImg;
    public ImageButton MessInfoBtn,ShareMenuBtn;
    private Context context;
    MaterialFavoriteButton favorite;

    SharedPreference sharedPreference;
    List<String> favorites;


    public View view;
    public MenuCardView CurrentObj;


    public MyViewHolder(final View v) {
        super(v);


        context = v.getContext();

        SpecialImg=(ImageView)v.findViewById(R.id.SpecialImgView);

        MessNameTxtView = (TextView) v.findViewById(R.id.mess_name);
        MessOpenBadge = (TextView) v.findViewById(R.id.MessOpenBadge);
        MessOpenBadge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_green_24dp, 0, 0, 0);
        MessCloseBadge = (TextView) v.findViewById(R.id.MessCloseBadge);
        MessCloseBadge.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_access_time_white_24dp, 0, 0, 0);
        MenuUpdatedTextView = (TextView) v.findViewById(R.id.menu_update_status);
        MenuUpdatedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all_white_24dp, 0, 0, 0);
        costTextView = (TextView) v.findViewById(R.id.costTextView);
        costTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coins, 0, 0, 0);
        favorite= (MaterialFavoriteButton)v.findViewById(R.id.favButton);

        SpecialList = (ListView) v.findViewById(R.id.SpecialListView);
        MenuListView1 = (ListView) v.findViewById(R.id.list_view_1);
        MenuListView2 = (ListView) v.findViewById(R.id.list_view_2);
        MenuListView3 = (ListView) v.findViewById(R.id.list_view_3);
        MessInfoBtn = (ImageButton) v.findViewById(R.id.MessInfoBtn);

        MenuLayout = (LinearLayout) v.findViewById(R.id.menu_layout);

        OpenImg = (ImageView) v.findViewById(R.id.MessOpenIcon);
        CloseImg = (ImageView) v.findViewById(R.id.MessCloseIcon);

        ShareMenuBtn=(ImageButton)v.findViewById(R.id.shareMenuBtn);

        view = v;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // item clicked
              //  Intent InfoIntent = new Intent(view.getContext(), MessInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("messobj", CurrentObj);
              //  InfoIntent.putExtras(bundle);
             //   view.getContext().startActivity(InfoIntent);
            }
        });


        ShareMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Messed Up! \nMess, Menu and more!");
                    String sAux = "\nHey!\nCheckout today's Lunch at *"+CurrentObj.getMessID()+"* !"+
                            "\n" +CurrentObj+"\n\nTap for more: ";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.messedup.saurabh.mess2 \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    view.getContext().startActivity(Intent.createChooser(i, "Share to"));
                } catch(Exception e) {
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
}

class HeaderViewHolder extends RecyclerView.ViewHolder {
    public HeaderViewHolder(View itemView) {
        super(itemView);

    }
}

class FooterViewHolder extends RecyclerView.ViewHolder {
    public FooterViewHolder(View itemView) {
        super(itemView);

    }
}

