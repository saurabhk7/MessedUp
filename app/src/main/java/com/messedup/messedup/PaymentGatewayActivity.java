package com.messedup.messedup;

import android.content.Context;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.messedup.messedup.SharedPreferancesPackage.DetailsSharedPref;
import com.messedup.messedup.adapters.TokenConfirmListAdapter;
import com.messedup.messedup.signin_package.PhoneNumberAuthentication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentGatewayActivity extends AppCompatActivity {

    TextView jsontxtview,promoInfoTxt;
    EditText refCodeTxtView;
    Button CompletePayBtn,applyPromoBtn;

    Context mContext;

    CircularProgressButton mCircularProgressButton;

    String totaltokens;
    String finalDisccost="";

    boolean PROMOCODE_APPLIED = false;
    String PROMOCODE_CODE = "NOPROMO";
    String PROMOCODE_FUID = "NOPROMO";

    String validfrom, validtill, totaldays;

    String email,phone,buyername, amount, purpose = "Messed Up Mess Tokens",userid;
    DetailsSharedPref mDetailsSharedPref;

    String token_purpose="";
    String current_mess="";
    Boolean is_first = true;

    ArrayList<String> leftdata = new ArrayList<>();
    ArrayList<String> rightdata = new ArrayList<>();

    TextView sundayStaticTxt, sundayDynamictxt;

    int totalSundays=0;

    InstapayListener listener;


//    ArrayList<String> playtetypeStr = new ArrayList<>();
//    ArrayList<String> qtyStr = new ArrayList<>();

    LinkedHashMap<String, String> FinalMapToDisplay = new LinkedHashMap<>();

    LinkedHashMap<String, String> FinalMapToPass = new LinkedHashMap<>();
    String stringToAdd="";

    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {

        Log.e("failed callPay:","paycalled");


        final Activity activity = this;

        this.amount=amount;
        this.purpose=purpose;

        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
       pay.put("send_sms", true);
      pay.put("send_email", true);
 } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);

    }



    
    private void initListener() {
        Log.e("failed callPay:","lsitenerinit");
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {

                Log.e("PROMOCODEPASS: ",PROMOCODE_CODE);


                if(FinalMapToPass.size()!=0) {

                    FinalMapToPass.put("promocodeused", PROMOCODE_CODE);

                    FinalMapToPass.put("frienduid", PROMOCODE_FUID);

                    FinalMapToPass.put("uid", userid);

                    FinalMapToPass.put("totaltokens", totaltokens);
                }

                for (String finalkey : FinalMapToPass.keySet()) {
                    String value = FinalMapToPass.get(finalkey);
                    System.out.println("FinalMapToPass2**:   " + finalkey + "   :   " + value);
                }


                if(amount!=null) {
                    new PostTxnData(response,amount,token_purpose, FinalMapToPass).execute();
                    amount =null;

                //TODO: write async task to send the hashmap details to server

                Toast.makeText(getApplicationContext(), "Congratulations! Payment Successful", Toast.LENGTH_LONG)
                        .show();
                }
                else
                {
                    Log.e("Instamojo failed","Amount null");
                }

            }

            @Override
            public void onFailure(int code, String reason) {
                listener = null;


                if(amount!=null) {
                Log.e("Instamojo failed","Code: "+code+"reason: "+reason);
                Toast.makeText(getApplicationContext(), "Oops, something went wrong", Toast.LENGTH_SHORT)
                        .show();

                    new PostTxnFailData(reason, amount, token_purpose, code).execute();
                    amount =null;
                }
                else
                {
                    Log.e("Instamojo failed","Amount null");
                }


            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payement);
        // Call the function callInstamojo to start payment here


        sundayStaticTxt = (TextView)findViewById(R.id.sundaypricetxtstatic);
        sundayDynamictxt = (TextView)findViewById(R.id.sundaypricetxtdyanamic);


        FinalMapToDisplay = new LinkedHashMap<>();
        FinalMapToPass = new LinkedHashMap<>();

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mContext=this;

        mCircularProgressButton = (CircularProgressButton)findViewById(R.id.AnimApplyPromoBtn);

        applyPromoBtn =(Button)findViewById(R.id.promocode_btn);
        refCodeTxtView =(EditText)findViewById(R.id.refCodeEditText) ;
        promoInfoTxt = (TextView)findViewById(R.id.promoinfotxt);

        mDetailsSharedPref =new DetailsSharedPref(this);

        email = mDetailsSharedPref.getEmailSharedPrefs().trim();
        buyername = mDetailsSharedPref.getNameSharedPrefs().trim();
        phone = mDetailsSharedPref.getPhoneSharedPrefs().trim().substring(3,13);

        Log.e("DETAILS:",email+ " " + buyername + " " + phone);

        setTitle("Review your order");

        Intent getIntent = getIntent();

        jsontxtview = (TextView) findViewById(R.id.jsontxt);

        if(getIntent.hasExtra("jsonString")) {
            String json = getIntent.getExtras().getString("jsonString");
            jsontxtview.setText("THE TOKEN ORDER JSON IS: " + json);
            try {
//                HashMap<String,String> hm = toHashMap(json);
                Log.e("JSON :: ","YOUR JSON IS: "+json);
                JsonHelper helper = new JsonHelper();
                JSONObject jObject = new JSONObject(json);
                Map<String,Object> hm = helper.toMap(jObject);
                for (String name: hm.keySet()){

                    String key =name;
                    String value = hm.get(key).toString();

//                    System.out.println("Key: " + key + " Value: " + value);


                    if(key.equals("Plates"))
                    {
//                        System.out.println("blah Plates ");
                        value="{Plates: "+value+"}";
                        JSONObject PlatejObject = new JSONObject(value);
//                        Map<String,Object> phm  = JsonHelper.toMap(PlatejObject);
//
//                        System.out.println("In Plate Map: "+key + " " + phm.size());

                        List list1=new ArrayList<String>();
                        JSONArray array=PlatejObject.getJSONArray("Plates");
                        for(int i=0;i<array.length();i++)
                        {
                            list1.add(array.getJSONObject(i).toString());
                            System.out.println("blah list: "+array.getJSONObject(i).toString());
                        }
                        Iterator itr=list1.iterator();
                        while(itr.hasNext())
                        {
//                            String str1=itr.next().e+"\n";

                            Map<String,Object> tempmap = helper.toMap(new JSONObject(itr.next().toString()));
                            System.out.println("blah map: "+tempmap.keySet());
                            FinalMapToDisplay.put(tempmap.keySet().toArray()[0].toString(),"MESSNAME");

                            String[] plate_array = tempmap.values().toString().split(",");
                            for(int i =0;i<plate_array.length;i++) {
                                String testtemp = plate_array[i].replaceAll("\\p{P}","");
                                String test= testtemp.replaceAll(" ","");
                                String[] platemap = test.split("=");
                                System.out.println("blah: " +"type: "+platemap[0]+" qty: "+platemap[1]);
                                FinalMapToDisplay.put(tempmap.keySet().toArray()[0].toString()+":"+platemap[0].replaceAll("(.)([A-Z])", "$1 $2"),platemap[1]);
                            }
//                            JSONArray array=PlatejObject.getJSONArray();

                        }
                    }
                    else {

                        FinalMapToDisplay.put(key,value);
                    }
                }



//                initTable(hm);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            initTable(FinalMapToDisplay);


        }
        else
        {
            jsontxtview.setText("PLEASE WAIT ... ");
        }



    }


    public void initTable(LinkedHashMap<String, String> hm) {
//        TableLayout ll = (TableLayout) findViewById(R.id.confirmTable);
//        ll.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
//
//        ll.setBackgroundColor(200);



        float disccost = 0;
        float origcost = 0;
        float discount = 0;
        int discpercent = 0;
        int w = 0;
        String Offer = "";

        int i = 0;

        for (String finalkey : FinalMapToDisplay.keySet()) {

            String key = finalkey;
            String value = FinalMapToDisplay.get(finalkey);
            System.out.println("FINALMAP:   " + key + "   :   " + value);


            if (key.equals("Offer_Details")) {

            } else if (key.equals("Discounted_Cost")) {
                disccost = Float.parseFloat(value);
            } else if (key.equals("Original_Cost")) {
                origcost = Float.parseFloat(value);

            } else if (key.equals("Offer")) {

            } else if (key.equals("Total_Tokens")) {


                TextView totalTokensDynTxt = (TextView) findViewById(R.id.totaltokensdynamic);

                totalTokensDynTxt.setText(value);

                totaltokens = value;

               /* TableRow row= new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);
                TextView platetype = new TextView(this);
                TextView qty = new TextView(this);
                platetype.setText("TOTAL TOKENS");
                platetype.setTextColor(getResources().getColor(R.color.app_green));
                qty.setText(value);
                qty.setTextColor(getResources().getColor(R.color.app_green));
                qty.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                platetype.setTextSize(18);
                qty.setTextSize(18);

                row.addView(platetype);
                row.addView(qty);
                ll.addView(row,i);
                i++;
*/
//                addSpace(ll,this,i);
//                i++;addSpace(ll,this,i);
//                i++;addSpace(ll,this,i);
//                i++;addSpace(ll,this,i);
//                i++;
            } else if (value.equals("MESSNAME")) {

 /*               TableRow row= new TableRow(this);
                row.setBackground(ContextCompat.getDrawable(this,R.drawable.border));
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView messname = new TextView(this);
                messname.setTextSize(20);
//                messname.setBackground(ContextCompat.getDrawable(this,R.drawable.border));

                messname.setTextColor(getResources().getColor(R.color.colorPrimary));*/
                key = key.replaceAll("_", " ");
//                messname.setText(key);

                stringToAdd=key;

                leftdata.add(key);
                rightdata.add("messname");

/*                row.addView(messname);
//                System.out.println("ROW: "+ll.getMeasuredWidth());
                w=row.getWidth();
                ll.addView(row,i);
                System.out.println("ROW: "+ll.getMeasuredWidth());
                i++;*/
            }

            else if(key.equals("ValidFrom"))
            {
                Log.e("Validity From:",value);
                validfrom = value;
            }
            else if(key.equals("ValidTill"))
            {
                Log.e("Validity Till:",value);
                validtill = value;
            }
            else if(key.equals("NoOfDays"))
            {
                Log.e("Validity totaldays:",value);

                totaldays = value;
            }
            else if(key.equals("SundayExtraCharge"))
            {
                Log.e("SUNDAY: ","KEYERROR: "+key+" VALUE: "+value);

                int costSunday = Integer.parseInt(value);

                int extraSunday = totalSundays*costSunday;

                sundayDynamictxt.setText("+₹"+extraSunday);


            }

            else if(key.equals("NoOfSundays"))
            {
                Log.e("NO OF SUNDAY: ","KEYERROR: "+key+" VALUE: "+value);
                totalSundays = Integer.parseInt(value);
                sundayStaticTxt.setText(value+" sundays extra charge");

                if(totalSundays==0)
                {
                    sundayStaticTxt.setVisibility(View.GONE);
                    sundayDynamictxt.setVisibility(View.GONE);
                }
                else
                {
                    sundayStaticTxt.setVisibility(View.VISIBLE);
                    sundayDynamictxt.setVisibility(View.VISIBLE);
                }
            }

            else {

                if (!value.equals("0")) {
/*                    TableRow row = new TableRow(this);
                    row.setBackground(ContextCompat.getDrawable(this,R.drawable.border));
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(lp);
                    TextView platetype = new TextView(this);
                    platetype.setBackground(ContextCompat.getDrawable(this,R.drawable.border));

                    TextView qty = new TextView(this);

                    qty.setGravity(Layout.DIR_RIGHT_TO_LEFT);
                    qty.setBackground(ContextCompat.getDrawable(this,R.drawable.border));
                    qty.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

                    qty.setTextSize(15);
                    platetype.setTextSize(15);
                    platetype.setText(key.split(":")[1]+" tokens");

                    qty.setText(value);*/

                    Log.e("KEYSPLIT: ","KEYERROR: "+key);

                    String keySplit = key.split(":")[1];

                    String valuearr[] = value.split("t");

                    leftdata.add(keySplit + " tokens "+"("+valuearr[0]+"x₹"+valuearr[1]+")");
                    rightdata.add("₹"+valuearr[2]);

                    FinalMapToPass.put(stringToAdd+"#"+keySplit,valuearr[0]);

                    if(is_first)
                        token_purpose+=(valuearr[0]+" "+keySplit+" "+stringToAdd+" token");
                    else
                        token_purpose+=(", "+valuearr[0]+" "+keySplit+" "+stringToAdd+" token");

                    is_first=false;


                    /*System.out.println("row: "+w+"platetype: "+platetype.getWidth());
                    row.addView(platetype);
                    row.addView(qty);
                    ll.addView(row, i);
                    i++;*/
                }
            }


        }

//        for (String finalkey : FinalMapToPass.keySet()) {
//            String value = FinalMapToPass.get(finalkey);
//            System.out.println("FINALMAPTOPASS:   " + finalkey + "   :   " + value);
//        }



        TextView validityTxtView = (TextView)findViewById(R.id.validityDynamic);

        String validityString = "Token Validity: <b><font color=#424242>"+validfrom+"</font></b> to <b><font color=#424242>"+validtill+"</font></b> - <b><font color=#424242>("+totaldays+" Days)</font></b>";
//        validityTxtView.setText("Tokens Valid from "+validfrom+" till "+validtill);
        validityTxtView.setText(Html.fromHtml(validityString));

        System.out.println("TABLE:");
        discount = origcost - disccost;
        discpercent = (int) ((discount / origcost) * 100);
        System.out.println("TABLE:" + disccost + " " + origcost + " " + discount + " ");
        String[] keyarr = {"ORIGINAL PRICE", "OFFER PRICE", "YOU SAVE"};
        String[] valarr = {"₹" + origcost + "", "₹" + disccost + "", "₹" + discount + " (" + discpercent + "%)"};

        TextView oricostxtxview = (TextView) findViewById(R.id.oripricetxtdyanamic);
        oricostxtxview.setText("₹" + origcost);

        oricostxtxview.setPaintFlags(oricostxtxview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        TextView discostxtview = (TextView) findViewById(R.id.offerpricetxtdyanamic);
        discostxtview.setText("₹" + disccost);

        TextView yousavetxtview = (TextView) findViewById(R.id.yousavetxtdyanamic);
        yousavetxtview.setText("₹" + discount + " (" + discpercent + "%)");


        CompletePayBtn = (Button) findViewById(R.id.complete_payment_btn);
        CompletePayBtn.setText("COMPLETE PAYMENT ₹" + disccost);




        finalDisccost = disccost + "";

        //int pertokencost= (int) (Float.parseFloat(finalDisccost)/Integer.parseInt(totaltokens));

        final float finalDisccost1 = disccost;


        CompletePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                finish();


                //TODO: uncomment this later
                callInstamojoPay(email, phone, finalDisccost1 +"",totaltokens+" "+purpose, buyername);
            }
        });


//        TextView totalTokensDynTxt = (TextView) findViewById(R.id.totaltokensdynamic);
//
//        totalTokensDynTxt.setText(totaltokens+" (₹"+pertokencost+"/token)");


        initListView(leftdata,rightdata);



        applyPromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refcode = refCodeTxtView.getText().toString();
                new ApplyPromo(getApplicationContext(),refcode).execute();

            }
        });

        mCircularProgressButton.setIndeterminateProgressMode(true);

        mCircularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent succesIntent=new Intent(PaymentGatewayActivity.this, SuccessPayementActivity.class);
//
//                succesIntent.putExtra("totaltokens",totaltokens+"");
//                succesIntent.putExtra("amount",finalDisccost+"");
//
//                startActivity(succesIntent); //TODO: cut paste it to instamojo activity

                String refcode = refCodeTxtView.getText().toString(); //TODO: uncomment this
                if(refcode.length()==0)
                    Toast.makeText(mContext,"Enter the Referral code",Toast.LENGTH_SHORT).show();
                else
                    new ApplyPromo(getApplicationContext(),refcode).execute();


            }
        });


        Log.e("token_purpose","PURPOSE: "+token_purpose);
    }

    private void initListView(ArrayList<String> leftdata, ArrayList<String> rightdata) {

        ListView lView = (ListView)findViewById(R.id.plateTypeListView);

        TokenConfirmListAdapter lAdapter = new TokenConfirmListAdapter(this, leftdata, rightdata);

        lView.setAdapter(lAdapter);


    }

        /*System.out.println("TABLE:");

        for (int j = 0; j <3; j++) {

            System.out.print("TABLE: "+valarr[j]);
            TableRow row= new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            lp.topMargin=10;
            row.setLayoutParams(lp);
            TextView platetype = new TextView(this);
            TextView qty = new TextView(this);
            platetype.setText(keyarr[j]);
            qty.setText(valarr[j]);
            qty.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

            if(j==2)
            {
                platetype.setTextColor(getResources().getColor(R.color.app_green));
                qty.setTextColor(getResources().getColor(R.color.app_green));

                platetype.setTextSize(20);
                qty.setTextSize(20);
            }
            else
            {
                platetype.setTextSize(18);
                qty.setTextSize(18);
            }

            if(keyarr[j].equals("ORIGINAL PRICE"))
            {
                qty.setPaintFlags(qty.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                platetype.setTextSize(15);
                qty.setTextSize(15);
            }

            row.addView(platetype);
            row.addView(qty);
            ll.addView(row,i);
            i++;
        }


        addSpace(ll,this,i);
        i++;


        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);
        TextView platetype = new TextView(this);

        EditText code = new EditText(this);
        code.setTextSize(15);
        platetype.setText("APPLY CODE");
//        platetype.setBackgroundColor(ContextCompat.getColor(this,R.color.app_green));
        platetype.setBackground(ContextCompat.getDrawable(this,R.drawable.button_back));
        platetype.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            platetype.setElevation(10);
        }
//        platetype.setTextSize(15);
//        platetype.setHeight(10);
        code.setBackground(ContextCompat.getDrawable(this,R.drawable.border));

        code.setHint("Enter Promo Code");
            platetype.setTextColor(getResources().getColor(R.color.white));
            code.setTextColor(getResources().getColor(R.color.app_green));



        row.addView(code);
        row.addView(platetype);
        ll.addView(row,i);
        i++;

    }

    private void addSpace(TableLayout ll, PaymentGatewayActivity paymentGatewayActivity, int i) {

        TableRow row1= new TableRow(this);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        row1.setLayoutParams(lp1);
        TextView platetype1 = new TextView(this);

        EditText code1 = new EditText(this);
//        code1.setWidth(105);
        platetype1.setText("APPLY CODE");
//        platetype.setBackgroundColor(ContextCompat.getColor(this,R.color.app_green));
        platetype1.setBackground(ContextCompat.getDrawable(this,R.drawable.button_back));
        platetype1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            platetype1.setElevation(5);
        }
//        platetype.setTextSize(15);
//        platetype.setHeight(10);
        code1.setBackground(ContextCompat.getDrawable(this,R.drawable.border));

        code1.setHint("Enter Promo Code");
        platetype1.setTextColor(getResources().getColor(R.color.white));
        code1.setTextColor(getResources().getColor(R.color.app_green));



        platetype1.setVisibility(View.INVISIBLE);
        code1.setVisibility(View.INVISIBLE);

        row1.addView(platetype1);
        row1.addView(code1);

        ll.addView(row1,i);

    }
*/

    @Override
    public void onBackPressed() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Want to go back?")
                .setContentText("Your current order will be lost")
                .setCancelText("No")
                .setConfirmText("Yes, go back")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {

//                        FinalMapToPass.clear();
//                        FinalMapToDisplay.clear();
//                        amount="";

                    Log.e("Destoyed","************");


                        sDialog.cancel();
//                        startActivity(new Intent(PaymentGatewayActivity.this, TokenSelectionActivity.class));
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

        /*new AlertDialog.Builder(this)
                .setTitle("Really want to go back?")
                .setMessage("Your current order will be lost")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
//                        PaymentGatewayActivity.super.onBackPressed();
                        startActivity(new Intent(PaymentGatewayActivity.this, TokenSelectionActivity.class));

                    }
                }).create().show();*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public class ApplyPromo extends AsyncTask<String , Void ,String> {


        private Context mContext;
        private String referalcode;

        JSONObject jObj = null;
        String json = "";


        public ApplyPromo(Context applicationContext, String refcode) {

            referalcode = refcode;
            mContext = applicationContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mCircularProgressButton.setProgress(50);
        }

        /**
         * @param args
         * @use to download the latest menu in the background
         */
        protected String doInBackground(String... args) {

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                //constants


                String BASEURL = Constants.getBaseUrl();

                URL url = new URL(BASEURL+"/checkReferal.php?userid=" + userid +
                        "&referalcode=" + referalcode + "&tokens=" + totaltokens);

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");

                //do something with response
                is = conn.getInputStream();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();

                    Log.e("MyTry: ", json);


                    try {
                        jObj = new JSONObject(json);
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }


                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // try parse the string to a JSON object
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
                //String contentAsString = readIt(is,len);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;


        }

        /**
         * After completing background task Dismiss the progress dialog
         *
         * @use Stores the Downloaded JSON into ArrayList of HashMaps
         **/
        protected void onPostExecute(String file_url) {

//            mCircularProgressButton.setProgress(100);

            String success = null;
            try {
                success = jObj.getString("success");

                if (success.equals("true")) {

                    PROMOCODE_APPLIED=true;
                    PROMOCODE_CODE = referalcode;

                    refCodeTxtView.setBackground(getResources().getDrawable(R.drawable.border_green));

                    mCircularProgressButton.setProgress(100);

                    String info = jObj.getString("information");
                    promoInfoTxt.setTextColor(getResources().getColor(R.color.app_green));
                    promoInfoTxt.setText("Sucessfully applied "+info);

                    String frienduid = jObj.getString("userid");

                    PROMOCODE_FUID = frienduid;

                    Toast.makeText(mContext,"Referral Code successfully applied!",Toast.LENGTH_SHORT).show();

                } else {

                    PROMOCODE_APPLIED=false;
                    PROMOCODE_CODE = "NOPROMO";
                    PROMOCODE_FUID = "NOPROMO";

                    refCodeTxtView.setBackground(getResources().getDrawable(R.drawable.border_red));

                    mCircularProgressButton.setProgress(0);
//                    refCodeTxtView.setError("",getResources().getDrawable(R.drawable.ic_error_outline_red_24dp));
                    String info = jObj.getString("information");
                    promoInfoTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
                    promoInfoTxt.setText(info);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class PostTxnData extends AsyncTask<String , Void ,String> {


        JSONObject jObj = null;
        String json = "";
        String response;
        String amount;
        String purpose;
        LinkedHashMap<String,String> detailsMap = new LinkedHashMap<>();

        LinkedHashMap<String, String> finalMapToPass = new LinkedHashMap<>();

        // products JSONArray

        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";

        String BASEURL = Constants.getBaseUrl();

        private String url_mess_menu = BASEURL+"/postTxn.php";
        //ArrayList<HashMap<String, String>> messList;

        PostTxnData(String response, String amount, String purpose, LinkedHashMap<String, String> finalMapToPass) {
            this.response = response;
            this.amount = amount;
            this.purpose = purpose;
            this.finalMapToPass = finalMapToPass;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {



            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(url_mess_menu);
                JSONObject jsonObject = new JSONObject();

                JSONArray tokendata = new JSONArray();
                JSONObject promo = new JSONObject();

                if(finalMapToPass.size()>0) {
                    for (LinkedHashMap.Entry<String, String> entry : finalMapToPass.entrySet()) {
                        if (entry.getKey().contains("#")) {
                            JSONObject jo = new JSONObject();
                            jo.put("name", entry.getKey());
                            jo.put("count", entry.getValue());
                            tokendata.put(jo);
                        } else {
                            promo.put(entry.getKey(), entry.getValue());
                        }
                    }

                    //JSONObject tokendata = new JSONObject(finalMapToPass);


                    //Get Response in JSON
                    StringTokenizer st = new StringTokenizer(response, ":=");
                    while (st.hasMoreTokens()) {

                        String keyStr = st.nextToken().toLowerCase();
                        String valueStr = st.nextToken();
                        jsonObject.put(keyStr, valueStr);

                        detailsMap.put(keyStr, valueStr);

                        Log.e("PTXN", "++" + detailsMap);
                    }

                    Log.e("PTXN", "***" + userid);
                    Log.e("PTXN", "***" + amount);
                    Log.e("PTXN", "***" + purpose);

                    jsonObject.put("userid", userid);//change the variables accordingly
                    jsonObject.put("amount", amount);//Every variable in string format
                    jsonObject.put("validfrom", validfrom);//Every variable in string format
                    jsonObject.put("validtill", validtill);//Every variable in string format
                    jsonObject.put("purpose", token_purpose);
                    jsonObject.put("tokendata", tokendata);
                    jsonObject.put("promo", promo);


                    String message = jsonObject.toString();

                    Log.e("FinalMapToPass2**", "sending.. " + message);

                    Log.e("PTXN", "***" + message);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /*milliseconds*/);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();


                    //do somehting with response
                    is = conn.getInputStream();

                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        json = sb.toString();

                        Log.e("PTXN", "json: " + json);

                        try {
                            jObj = new JSONObject(json);
                            int success = jObj.getInt("success");
                            if (success == 1) {

                                Log.e("PostTxnData", "Succesful");
                            } else {
                                Log.e("PostTxnData", "Error");
                            }
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }


                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;


        }

        protected void onPostExecute(String file_url) {

//            for (String finalkey : FinalMapToPass.keySet()) {
//                String value = FinalMapToPass.get(finalkey);
//                System.out.println("FINALMAPTOPASS:   " + finalkey + "   :   " + value);
//            }

            if (finalMapToPass.size() > 0) {

                Log.e("DONE", "DONE");


                Intent succesIntent = new Intent(PaymentGatewayActivity.this, SuccessPayementActivity.class);

                succesIntent.putExtra("totaltokens", totaltokens + "");
                succesIntent.putExtra("amount", finalDisccost + "");
                succesIntent.putExtra("orderid", detailsMap.get("orderid") + "");
                succesIntent.putExtra("paymentid", detailsMap.get("paymentid") + "");

                Log.e("PTXN", "totaltokens " + totaltokens + "");
                Log.e("PTXN", "amount " + finalDisccost + "");
                Log.e("PTXN", "orderid " + detailsMap.get("orderid") + "");
                Log.e("PTXN", "paymentid " + detailsMap.get("paymentid") + "");


                FinalMapToDisplay = new LinkedHashMap<>();
                FinalMapToPass = new LinkedHashMap<>();
                detailsMap = new LinkedHashMap<>();

                startActivity(succesIntent);

                //NExt Intent to "Transaction Succesful Activity"
                //Intent i = new Intent(SplashScreen.this, IntroActivity.class);

                //  startActivity(i);
                finish();

            }

        }

    }

    class PostTxnFailData extends AsyncTask<String , Void ,String> {


        JSONObject jObj = null;
        String json = "";
        String reason;
        String amount;
        String purpose;
        String code;
        // products JSONArray

        //  private String url_all_products = "https://wanidipak56.000webhostapp.com/receiveall.php";

        String BASEURL = Constants.getBaseUrl();

        private String url_mess_menu = BASEURL+"/postFailedTxn.php";
        //ArrayList<HashMap<String, String>> messList;

        PostTxnFailData(String reason, String amount, String purpose, int code) {
            this.reason = reason;
            this.amount = amount;
            this.purpose = purpose;
            this.code = code+"";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {



            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;


            try {
                URL url = new URL(url_mess_menu);
                JSONObject jsonObject = new JSONObject();

                Log.e("FAILPTXN", "***" + userid);
                Log.e("FAILPTXN", "***" + amount);
                Log.e("FAILPTXN", "***" + purpose);
                Log.e("FAILPTXN", "***" + code);
                Log.e("FAILPTXN", "***" + purpose);


                try {
                    jsonObject.put("userid", userid);//change the variables accordingly
                    jsonObject.put("purpose", token_purpose);
                    jsonObject.put("amount", amount);//Every variable in string format
                    jsonObject.put("code", code);//Every variable in string format
                    jsonObject.put("reason", reason);//Every variable in string format

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                String message = jsonObject.toString();

                Log.e("FAILEDSENDING", "sending.. " + message);

//                Log.e("FAILEDPTXN", "***" + message);

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();


                    //do somehting with response
                    is = conn.getInputStream();

                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        is.close();
                        json = sb.toString();

                        Log.e("PTXN", "json: " + json);

                        try {
                            jObj = new JSONObject(json);
                            int success = jObj.getInt("success");
                            if (success == 1) {

                                Log.e("PostTxnData", "Succesful");
                            } else {
                                Log.e("PostTxnData", "Error");
                            }
                        } catch (JSONException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }


                    } catch (Exception e) {
                        Log.e("Buffer Error", "Error converting result " + e.toString());
                    }
                } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;


        }

        protected void onPostExecute(String file_url) {

//            for (String finalkey : FinalMapToPass.keySet()) {
//                String value = FinalMapToPass.get(finalkey);
//                System.out.println("FINALMAPTOPASS:   " + finalkey + "   :   " + value);
//            }



            Toast.makeText(getApplicationContext(), "Transaction Failed: " + reason+", please try again!", Toast.LENGTH_LONG)
                    .show();
            Log.e("Instamojo failed2","Code: "+code+"reason: "+reason);

            listener=null;

//            Intent i = new Intent(PaymentGatewayActivity.this,MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("frombuytokens", "true");
//            i.putExtras(bundle);
//            //i.putExtra("messid", MessID);
//            startActivity(i);


        }

        }




    class sendData extends AsyncTask<String , Void ,String> {


        JSONObject jObj = null;
        String json = "";

        LinkedHashMap<String, String> detailsMap = new LinkedHashMap<>();

        String BASEURL = Constants.getBaseUrl();

        private String url_mess_menu = BASEURL + "/postTxn.php";
        //ArrayList<HashMap<String, String>> messList;

        sendData(LinkedHashMap<String, String> dm) {

            this.detailsMap = dm;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(url_mess_menu);
                JSONObject jsonObject = new JSONObject();

                //Get Response in JSON


                String message = jsonObject.toString();


                Log.e("PTXN", "***" + message);

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();


                //do somehting with response
                is = conn.getInputStream();

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    json = sb.toString();

                    Log.e("PTXN", "json: " + json);

                    try {
                        jObj = new JSONObject(json);
                        int success = jObj.getInt("success");
                        if (success == 1) {

                            Log.e("PostTxnData", "Succesful");
                        } else {
                            Log.e("PostTxnData", "Error");
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }


                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //clean up
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;


        }

        protected void onPostExecute(String file_url) {

//            for (String finalkey : detailsMap.keySet()) {
//                String value = detailsMap.get(finalkey);
//                System.out.println("detailsMap:   " + finalkey + "   :   " + value);
//            }


        }


    }
}







