package com.messedup.messedup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;
import com.mindorks.placeholderview.annotations.expand.SingleTop;
import com.mindorks.placeholderview.annotations.expand.Toggle;

/**
 * Created by saurabh on 29/4/18.
 */

@Parent
@SingleTop
@Layout(R.layout.feed_heading)
public class HeadingView {

    @View(R.id.headingTxt)
    private TextView headingTxt;

    @View(R.id.toggleIcon)
    private ImageView toggleIcon;

    @Toggle(R.id.toggleView)
    private LinearLayout toggleView;

    @ParentPosition
    private int mParentPosition;

    @View(R.id.tokenCountTxt)
    public TextView tokenCountTxt;


    private Context mContext;
    private String mHeading;

    public HeadingView(Context context, String heading) {
        mContext = context;
        mHeading = heading;
    }
    public HeadingView()
    {

    }
    public void updateCount(String op)
    {
//        TextView tv = (TextView)
//        int tc = Integer.parseInt(tokenCountTxt.getText().toString());
        if(op.equals("+"))
        {
            Toast.makeText(mContext,"add",Toast.LENGTH_SHORT).show();
//            tc++;
        }
        else if(op.equals("-"))
        {
//            tc--;
        }

//        tokenCountTxt.setText(tc+"");
    }

    @Resolve
    private void onResolved() {
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
        headingTxt.setText(mHeading);
    }

    @Expand
    private void onExpand(){
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp));
    }

    @Collapse
    private void onCollapse(){
        toggleIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
    }
}
