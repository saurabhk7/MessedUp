package com.messedup.messedup;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.ChildPosition;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;

/**
 * Created by saurabh on 29/4/18.
 */

@Layout(R.layout.feed_item)
public class InfoView {

    @ParentPosition
    private int mParentPosition;

    @ChildPosition
    private int mChildPosition;

    @View(R.id.titleTxt)
    private TextView titleTxt;

    @View(R.id.captionTxt)
    private TextView captionTxt;

    @View(R.id.timeTxt)
    private TextView timeTxt;

    @View(R.id.imageView)
    private ImageView imageView;

    @View(R.id.IncBtn)
    private Button IncBtn;

    @View(R.id.DecBtn)
    private Button DecBtn;

    @View(R.id.cntTxt)
    private TextView cntTxt;

    private Info mInfo;
    private Context mContext;

    public InfoView(Context context, Info info) {
        mContext = context;
        mInfo = info;
    }

    @Resolve
    private void onResolved() {
        titleTxt.setText(mInfo.getTitle());
        captionTxt.setText(mInfo.getCaption());
        timeTxt.setText(mInfo.getTime());
        final HeadingView mHeadingView =  new HeadingView();


        final int[] locCnt = {Integer.parseInt(cntTxt.getText().toString())};

        IncBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                int locCnt2 = Integer.parseInt(cntTxt.getText().toString());
                locCnt2++;
                cntTxt.setText(locCnt2+"");
//                mHeadingView.tokenCountTxt.setText(locCnt2+"");
//                mHeadingView.updateCount("+");
            }
        });

        DecBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                int locCnt2 = Integer.parseInt(cntTxt.getText().toString());
                locCnt2--;
                cntTxt.setText(locCnt2+"");
//                mHeadingView.updateCount("-");
            }
        });




        Glide.with(mContext).load(mInfo.getImageUrl()).into(imageView);
    }
}