package com.messedup.messedup.adapters;

/**
 * Created by saurabh on 21/9/17.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.messedup.messedup.R;

import java.util.ArrayList;

public class TokenHistoryCustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> NotifDetails;
    private final ArrayList<String> NotifSmall;
    private final ArrayList<String> TimeDetails;
    private final ArrayList<String> TransDetails;



    public TokenHistoryCustomListAdapter(Activity context, ArrayList<String> NotifDetails, ArrayList<String> NotifSmall
    , ArrayList<String> TimeDetails, ArrayList<String> TransDetails) {
        super(context, R.layout.notif_special_list_view, NotifDetails);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.NotifDetails=NotifDetails;
        this.NotifSmall=NotifSmall;
        this.TimeDetails=TimeDetails;
        this.TransDetails=TransDetails;

    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.token_use_history_listview_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.messDetailsTxt);
        ImageView imageView2 = (ImageView) rowView.findViewById(R.id.new_notif_icon_2);
        TextView extratxt = (TextView) rowView.findViewById(R.id.platetypetxt);
        TextView timeTxt = (TextView) rowView.findViewById(R.id.time_txt_view);
        TextView TransTxt = (TextView) rowView.findViewById(R.id.transid_txt_view);

        txtTitle.setText(NotifDetails.get(position));
        extratxt.setText(NotifSmall.get(position));
        TransTxt.setText(TransDetails.get(position));
        if(TimeDetails.get(position).equals("NoDate"))
         timeTxt.setText("");
        else
        timeTxt.setText(TimeDetails.get(position));

        return rowView;

    };
}