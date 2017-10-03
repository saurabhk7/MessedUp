package com.messedup.messedup.adapters;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.messedup.messedup.MenuFragment;
import com.messedup.messedup.R;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.SharedPreferancesPackage.SharedPreference;
import com.messedup.messedup.mess_menu_descriptor.MenuCardView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saurabh on 24/8/17.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MenuCardView> list;
    private ViewGroup Contextparent;

    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;
    private static final int STATIC_CARD_FOOT = 2;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://messed-up-37064.appspot.com");

    SharedPreference sharedPreference;
    List<String> favorites;




    public MyAdapter(ArrayList<MenuCardView> data) {
        list = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        Contextparent=parent;
        View view;
        if(viewType==STATIC_CARD)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.static_menu_top_card, parent, false);
            HeaderViewHolder holder1 = new HeaderViewHolder(view);
            return holder1;
        }
        else if(viewType==STATIC_CARD_FOOT)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.static_menu_bottom_card, parent, false);
            FooterViewHolder holder3= new FooterViewHolder(view);
            return holder3;
        }

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        MyViewHolder holder2 = new MyViewHolder(view);
        return holder2;




    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder Viewholder, final int ViewPosition) {

        try {
            if (Viewholder instanceof MyViewHolder) {

                final int position=ViewPosition-1;

                final MyViewHolder holder = (MyViewHolder) Viewholder;

                Log.d("IN MY ADAPTER "+position, list.get(position).getMessID());

                holder.MessNameTxtView.setText(list.get(position).getMessID());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.MenuUpdatedTextView.setElevation(18f);
                }



                holder.costTextView.setText(" ₹ "+list.get(position).getGCharge());


                if (list.get(position).getStat().equals("1"))
                    holder.MenuUpdatedTextView.setVisibility(View.INVISIBLE);
                else if(list.get(position).getStat().equals("0"))
                    holder.MenuUpdatedTextView.setVisibility(View.VISIBLE);



                if (list.get(position).getOpenClose().equals("0")) {
                    holder.MessOpenBadge.setVisibility(View.INVISIBLE);
                } else {
                    holder.MessCloseBadge.setVisibility(View.INVISIBLE);

                }

                holder.CurrentObj = list.get(position);

                StorageReference imageRef = null;
                try {

                    if(!list.get(position).getSpecial().equalsIgnoreCase("null"))
                        imageRef = storageRef.child("specials").child(getSpecialName(list.get(position).getSpecial()) + ".jpg");
                    else if(!list.get(position).getSpecialExtra().equalsIgnoreCase("null"))
                        imageRef = storageRef.child("specials").child(getSpecialName(list.get(position).getSpecialExtra()) + ".jpg");

                }catch (Exception e)
                {
                    Log.e("SPECIAL IMAGE","Image not found!");
                }


                if(imageRef.getDownloadUrl().toString()!=null) {

                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            try {
                                Picasso.with(Contextparent.getContext()).load(uri).networkPolicy(NetworkPolicy.OFFLINE).into(holder.SpecialImg, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                        Picasso.with(Contextparent.getContext()).load(uri).into(holder.SpecialImg);

                                    }
                                });
                            } catch (Exception e) {
                                Log.v("E_VALUE", e.getMessage());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });

                }else
                {
                    Log.e("SPECIAL IMAGE","Image not found!");
                }


        /*holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InfoIntent=new Intent(view.getContext(),MessInfoActivity.class);
                InfoIntent.putExtra("messid",list.get(position).getMessID());
                view.getContext().startActivity(InfoIntent);

            }
        });

        holder.MenuLayout.setClickable(true);

        holder.MenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InfoIntent=new Intent(view.getContext(),MessInfoActivity.class);
                InfoIntent.putExtra("messid",list.get(position).getMessID());
                view.getContext().startActivity(InfoIntent);
            }
        });*/

/*
        holder.MessInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InfoIntent=new Intent(view.getContext(),ChooserActivity.class);
                InfoIntent.putExtra("messid",list.get(position).getMessID());


            }
        });*/
/*
        holder.MenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent InfoIntent=new Intent(view.getContext(),MessInfoActivity.class);
                InfoIntent.putExtra("messid",list.get(position).getMessID());
                view.getContext().startActivity(InfoIntent);

               // Toast.makeText(view.getContext(),"You Clicked : "+list.get(position).getMessID(),Toast.LENGTH_SHORT).show();
            }
        });*/


                setSpecialList(holder, position);
                //setMenuLists(holder, position);
                setMenuTxt(holder,position);



                sharedPreference = new SharedPreference();
                favorites = sharedPreference.getFavorites(Contextparent.getContext());

                if(list.get(position).getFavMess().equals("true"))
                {
                    holder.favorite.setFavorite(true,false);
                }
                else if(list.get(position).getFavMess().equals("false"))
                {
                    holder.favorite.setFavorite(false,false);
                }


                holder.favorite.setOnFavoriteChangeListener(
                        new MaterialFavoriteButton.OnFavoriteChangeListener() {
                            @Override
                            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean isfavorite) {
                                // Toast.makeText(context,"Fav "+isfavorite+" clicked of: "+CurrentObj.getMessID(),Toast.LENGTH_SHORT).show();

                            }
                        });



                holder.favorite.setOnFavoriteAnimationEndListener(
                        new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                            @Override
                            public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean isfavorite) {

                                if(isfavorite)
                                {
                                    sharedPreference.addFavorite(Contextparent.getContext(),
                                            list.get(position).getMessID());
                                    Toast.makeText(Contextparent.getContext(),"Fav Added of: "+list.get(position).getMessID(),Toast.LENGTH_SHORT).show();
                                    list.get(position).setFavMess("true");
                                    Log.d("After Added","1"+sharedPreference.getFavorites(Contextparent.getContext()).toString());

                                    FirebaseMessaging.getInstance().subscribeToTopic(getTopicName(list.get(position).getMessID()));
                                    // FirebaseMessaging.getInstance().subscribeToTopic("tanmay");
                                    MenuFragment menuFragment=new MenuFragment();
                                    menuFragment.intializeList(Contextparent);

                                }
                                if(!isfavorite)
                                {
                                    sharedPreference.removeFavorite(Contextparent.getContext(),
                                            list.get(position).getMessID());
                                    list.get(position).setFavMess("false");

                                    Toast.makeText(Contextparent.getContext(),"Fav Removed of: "+list.get(position).getMessID(),Toast.LENGTH_SHORT).show();

                                    Log.d("After Removed","2"+sharedPreference.getFavorites(Contextparent.getContext()).toString());
                                    try {
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopicName(list.get(position).getMessID()));
                                        //  FirebaseMessaging.getInstance().unsubscribeFromTopic("tanmay");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        MenuFragment menuFragment=new MenuFragment();
                                        menuFragment.intializeList(Contextparent);
                                    }


                                }



                            }
                        });








            }
            else if(Viewholder instanceof HeaderViewHolder)
            {
                HeaderViewHolder holder=(HeaderViewHolder)Viewholder;

                DetailsSharedPref detailsSharedPref=new DetailsSharedPref(Contextparent.getContext());


                holder.lunchdinnerTxt.setText(detailsSharedPref.getMealStatusSharedPrefs());

                // holder.lunchdinnerTxt.setText("OFFLINE");


            }

            else if(Viewholder instanceof FooterViewHolder)
            {
                FooterViewHolder holder=(FooterViewHolder)Viewholder;

                PackageInfo pInfo = null;
                try {
                    pInfo = Contextparent.getContext().getPackageManager().getPackageInfo(Contextparent.getContext().getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                holder.versionText.setText("version: "+pInfo.versionName);


            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String getSpecialName(String special) {

        special=special.replace(" ","_");
        special=special.toLowerCase();
        special=special.trim();
        return special;
    }

    private void setMenuTxt(MyViewHolder holder, int position) {


        holder.menuTxtCard.setText(Html.fromHtml(list.get(position).getMenuCard()));

    }

    private void setMenuLists(MyViewHolder holder, int position) {
        Log.d("IN MY ADAPTER ",list.get(position).getMessID());


        ArrayList<String> items1=new ArrayList<>();
        ArrayList<String> items2=new ArrayList<>();
        ArrayList<String> items3=new ArrayList<>();

        int num1=0;
        int num2=0;
        int num3=0;


        int i=0;
        int j=0;
        int k=0;

        if(list.get(position).getVegieOne()!=null && !list.get(position).getVegieOne().equals("null")) {
            items1.add(i, list.get(position).getVegieOne());
            if(list.get(position).getVegieOne().length()>10)
                num1++;
            i++;
        }
        if(list.get(position).getVegieTwo()!=null && !list.get(position).getVegieTwo().equals("null")) {
            items1.add(i, list.get(position).getVegieTwo());
            if(list.get(position).getVegieTwo().length()>10)
                num1++;
            i++;
        }
        if(list.get(position).getVegieThree()!=null && !list.get(position).getVegieThree().equals("null")) {
            items2.add(j, list.get(position).getVegieThree());
            if(list.get(position).getVegieThree().length()>10)
                num2++;
            j++;
        }
        if(list.get(position).getRice()!=null && !list.get(position).getRice().equals("null")) {
            items2.add(j, list.get(position).getRice());
            if(list.get(position).getRice().length()>10)
                num2++;
            j++;
        }
        if(list.get(position).getRoti()!=null && !list.get(position).getRoti().equals("null")) {
            items3.add(k, list.get(position).getRoti());
            if(list.get(position).getRoti().length()>10)
                num3++;
            k++;
        }
        if(list.get(position).getOther()!=null && !list.get(position).getOther().equals("null")) {
            items3.add(k, list.get(position).getOther());
            if(list.get(position).getOther().length()>10)
                num3++;
            k++;
        }

        ArrayAdapter<String> itemsAdapter1 =
                new ArrayAdapter<String>(Contextparent.getContext(), R.layout.custom_list_view, items1);
        ArrayAdapter<String> itemsAdapter2 =
                new ArrayAdapter<String>(Contextparent.getContext(), R.layout.custom_list_view, items2);
        ArrayAdapter<String> itemsAdapter3 =
                new ArrayAdapter<String>(Contextparent.getContext(), R.layout.custom_list_view, items3);

        holder.MenuListView1.setAdapter(itemsAdapter1);
        holder.MenuListView2.setAdapter(itemsAdapter2);
        holder.MenuListView3.setAdapter(itemsAdapter3);

//UNCOMMENT THIS FOR DYNAMIC HEIGHT

        setListViewHeightBasedOnChildren(holder.MenuListView1,num1);
        setListViewHeightBasedOnChildren(holder.MenuListView2,num2);
        setListViewHeightBasedOnChildren(holder.MenuListView3,num3);


        itemsAdapter1.notifyDataSetChanged();
        itemsAdapter2.notifyDataSetChanged();
        itemsAdapter3.notifyDataSetChanged();



    }



    private void setSpecialList(MyViewHolder holder, int position) {

        ArrayList<String> Specialitems=new ArrayList<>();
        String Special1=list.get(position).getSpecial();
        String Special2=list.get(position).getSpecialExtra();

        if(Special1.equals("null"))
        {
            Special1=null;
        }
        if(Special2.equals("null"))
        {
            Special2=null;
        }

        if((Special1==null||Special1.equals("null")) && (Special2==null||Special2.equals("null")) )
        {
            //Specialitems.add("No Special Today!");
        }



        if(Special1!=null)
        {
            Specialitems.add(Special1);
        }
        if(Special2!=null)
        {
            Specialitems.add(Special2);
        }

        ArrayAdapter<String> SpecialitemsAdapter =
                new ArrayAdapter<String>(Contextparent.getContext(),R.layout.special_custom_list_view, Specialitems);


        holder.SpecialList.setAdapter(SpecialitemsAdapter);
        setListViewHeightBasedOnChildren(holder.SpecialList,0);

        SpecialitemsAdapter.notifyDataSetChanged();
    }

    private static void setListViewHeightBasedOnChildren(ListView listView , int num) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }


        Log.e("NUM","*"+num);
        // int totalHeight = 50*num;
        int totalHeight=0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();

            Log.e("HEIGHT of ","----"+listView.toString()+" * i * "+listItem.toString()+" height "+listItem.getMeasuredHeight());

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        //  Log.d("IN MY ADAPTER ",""+list.size());

        if (list == null) {
            return 2;
        }

        if (list.size() == 0) {
            //Return 2 here to show nothing
            return 2;
        }

        // Add extra view to show the footer view
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0) {
            return STATIC_CARD;
        } else if(position==list.size()+1)
        {
            return STATIC_CARD_FOOT;
        }
        else {
            return DYNAMIC_CARD;
        }
    }

    private String getTopicName(String messID) {

        messID=messID.replace(" ","_");
        messID=messID.toLowerCase();
        messID=messID.trim();
        return messID;
    }





}
