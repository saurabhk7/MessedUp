package com.messedup.messedup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.ArrayList;

public class CustomTokenAdapter extends RecyclerView.Adapter<CustomTokenAdapter.MyViewHolder> {

    private ArrayList<String> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.mess_name);

            final ElegantNumberButton elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.number_button2);
            elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    Toast.makeText(itemView.getContext(), "oldValue: "+oldValue+"newValue: "+ newValue, Toast.LENGTH_SHORT).show();
                }
            });

//            IncDecCircular incdec =(IncDecCircular) itemView.findViewById(R.id.incdec);
//            incdec.setConfiguration(LinearLayout.VERTICAL,IncDecCircular.TYPE_INTEGER,
//                   IncDecCircular.DECREMENT,  IncDecCircular.INCREMENT);
//            incdec.setupValues(0,100,1,0);
//
//            incdec.enableLongPress(true,true,150);
        }
    }

    public CustomTokenAdapter(ArrayList<String> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.token_selection_card_item, parent, false);

//        view.setOnClickListener(TokenSelectionActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition));

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}