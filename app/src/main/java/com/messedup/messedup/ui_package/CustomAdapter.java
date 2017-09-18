package com.messedup.messedup.ui_package;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.messedup.messedup.R;

import java.util.ArrayList;

/**
 * Created by saurabh on 4/9/17.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    private ArrayList<String> college_list;
    String[] countryNames;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, ArrayList<String> college_list) {
        this.context = applicationContext;
       // this.flags = flags;
        this.college_list = college_list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return college_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
      //  ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.spinner_text_view);
        TextView title = (TextView) view.findViewById(R.id.spinner_text_title);

        names.setTextSize(16);
        title.setTextSize(9);



        //  icon.setImageResource(flags[i]);
        names.setText(college_list.get(i));
        title.setText("SELECTED AREA");
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);

        txt.setPadding(36, 36, 36, 36);
        txt.setTextSize(15);
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Medium.ttf");
        txt.setTypeface(type);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(college_list.get(position));
        txt.setTextColor(Color.parseColor("#565656"));
        if(college_list.get(position).equals("Select your Area")) {
            txt.setTextColor(Color.parseColor("#767676"));
        }
        return  txt;
    }


}