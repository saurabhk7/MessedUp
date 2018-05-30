package com.messedup.messedup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.messedup.messedup.R;
import com.messedup.messedup.TokenSelectionActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by saurabh on 1/5/18.
 */

public class TokenExpandableListAdapter extends BaseExpandableListAdapter{


    Context mContext;
    List<String> messlist;
    Map<String , List<String>> platetypes;

    public TokenExpandableListAdapter(Context mContext, List<String> messlist, Map<String, List<String>> platetypes) {
        this.mContext = mContext;
        this.messlist = messlist;
        this.platetypes = platetypes;
    }

    @Override
    public int getGroupCount() {
        return messlist.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return platetypes.get(messlist.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return messlist.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return platetypes.get(messlist.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        String messname = (String)getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.feed_heading, null);

        }
        TextView messnameTxtView = (TextView)view.findViewById(R.id.headingTxt);
        messnameTxtView.setText(messname);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        String platetype = (String)getChild(i,i1);


        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.feed_item, null);

        }
        TextView messnameTxtView = (TextView)view.findViewById(R.id.titleTxt);
        messnameTxtView.setText(platetype);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
