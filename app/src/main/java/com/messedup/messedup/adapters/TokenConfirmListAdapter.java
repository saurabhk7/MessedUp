package com.messedup.messedup.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.messedup.messedup.PaymentGatewayActivity;
import com.messedup.messedup.R;

import java.util.ArrayList;

/**
 * Created by saurabh on 2/6/18.
 */

public class TokenConfirmListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> leftdata;
    ArrayList<String> rightdata;





    public TokenConfirmListAdapter(Context context, ArrayList<String> leftdata, ArrayList<String> rightdata) {

        this.context=context;
        this.leftdata=leftdata;
        this.rightdata=rightdata;

    }

    @Override
    public int getCount() {
        return leftdata.size();
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


        TokenConfirmListAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new TokenConfirmListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.confirm_tokens_listview, parent, false);
            viewHolder.messplatetypetxt = (TextView) convertView.findViewById(R.id.platetypetxt);
            viewHolder.tokenqtytxt = (TextView) convertView.findViewById(R.id.qtytxt);
            viewHolder.lineView = (View)convertView.findViewById(R.id.horizontal_line_view);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TokenConfirmListAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.lineView.setVisibility(View.GONE);

        viewHolder.messplatetypetxt.setText(leftdata.get(position));

        if(rightdata.get(position).equals("messname")) {
            viewHolder.messplatetypetxt.setTextSize(18);
            viewHolder.messplatetypetxt.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolder.tokenqtytxt.setText("");
        }
        else {
            viewHolder.messplatetypetxt.setTextSize(15);
            viewHolder.tokenqtytxt.setTextSize(15);
            viewHolder.tokenqtytxt.setText(rightdata.get(position));
        }

        if(position+1<rightdata.size()) {
            if (rightdata.get(position + 1).equals("messname")) {
                viewHolder.lineView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewHolder.lineView.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    private static class ViewHolder {

        TextView messplatetypetxt;
        TextView tokenqtytxt;
        View lineView;


    }
}
