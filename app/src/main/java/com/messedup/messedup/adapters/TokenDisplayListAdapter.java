package com.messedup.messedup.adapters;

/**
 * Created by saurabh on 30/5/18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.messedup.messedup.R;

import java.util.ArrayList;


public class TokenDisplayListAdapter extends BaseAdapter {

    Context context;
    private final String [] mess_name_array;
    private final int [] tokens_to_expire_array;
    private final String [] expiry_dates;
    private final int [] images= {R.drawable.poker_chip_red_shadow};

    private final int [] total_tokens_left_array;


    public TokenDisplayListAdapter(Context context, String [] mess_name_array, int [] tokens_to_expire_array, String[] expiry_dates, int [] total_tokens_left_array){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.mess_name_array = mess_name_array;
        this.tokens_to_expire_array = tokens_to_expire_array;
        this.expiry_dates = expiry_dates;
        this.total_tokens_left_array = total_tokens_left_array;

    }

    @Override
    public int getCount() {
        return mess_name_array.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.token_list_item, parent, false);
            viewHolder.messName = (TextView) convertView.findViewById(R.id.mess_name);
            viewHolder.tokenExpiryText = (TextView) convertView.findViewById(R.id.token_expiry);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.token_icon);
            viewHolder.totalTokens = (TextView) convertView.findViewById(R.id.token_label);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.messName.setText(mess_name_array[position]);
        viewHolder.tokenExpiryText.setText(""+tokens_to_expire_array[position]+" token(s) will expire on "+expiry_dates[position]);
        viewHolder.icon.setImageResource(images[0]);
        viewHolder.totalTokens.setText(""+total_tokens_left_array[position]);

        return convertView;
    }

    private static class ViewHolder {

        TextView messName;
        TextView tokenExpiryText;
        ImageView icon;
        TextView totalTokens;

    }

}