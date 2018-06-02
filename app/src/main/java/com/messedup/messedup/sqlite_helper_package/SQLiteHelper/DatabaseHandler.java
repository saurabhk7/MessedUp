package com.messedup.messedup.sqlite_helper_package.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import com.messedup.messedup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.messedup.messedup.adapters.CustomListAdapter;
import com.messedup.messedup.signin_package.GoogleSignIn;

/**
 * @author saurabh
 *
 */



public class DatabaseHandler extends SQLiteOpenHelper {


    private Context context;


    /**
     * @param context
     * @use Passing context to super constructor
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;
    }

    static String DATABASE_NAME="MessDatabase";


    public static final String MENU_TABLE_NAME="CardInfo";
    public static final String MENU_TABLE_Column_ID="NB_College" ;
    public static final String MENU_TABLE_Column_1_card_details="CardDetails";
    public static final String MENU_TABLE_Column_2_offer_details="OfferDetails";
    public static final String CREATE_TABLE_ALL_MENU
            ="CREATE TABLE IF NOT EXISTS "+MENU_TABLE_NAME+" ("+MENU_TABLE_Column_ID+" VARCHAR PRIMARY KEY, "
            +MENU_TABLE_Column_1_card_details+" VARCHAR, "+MENU_TABLE_Column_2_offer_details+" VARCHAR) ";


    public static final String MESS_INFO_TABLE_NAME="MessInfo";
    public static final String MESS_INFO_Column_ID="Mess_ID" ;
    public static final String MESS_INFO_Column_1_mess_details="MessDetails";
    public static final String MESS_INFO_Column_2_week_menu="WeeklyMenu";
    public static final String CREATE_TABLE_MESS_INFO
            ="CREATE TABLE IF NOT EXISTS "+MESS_INFO_TABLE_NAME+" ("+MESS_INFO_Column_ID+" VARCHAR, "
            +MESS_INFO_Column_1_mess_details+" VARCHAR, "+MESS_INFO_Column_2_week_menu+" VARCHAR) ";

    public static final String USER_INFO_TABLE_NAME="MessInfo";
    public static final String USER_INFO_Column_ID="User_ID" ;
    public static final String USER_INFO_Column_1_token_details="TokenDetails";
    public static final String CREATE_TABLE_USER_INFO
            ="CREATE TABLE IF NOT EXISTS "+USER_INFO_TABLE_NAME+" ("+USER_INFO_Column_ID+" VARCHAR, "
            +USER_INFO_Column_1_token_details+" VARCHAR) ";
    /**
     * @param database
     * @use Creating Offline (SQLite Tables) to save APP state for Offline Use
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_ALL_MENU);
        database.execSQL(CREATE_TABLE_MESS_INFO);
        database.execSQL(CREATE_TABLE_USER_INFO);

    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     * @use To Update the Offline Database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MENU_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MESS_INFO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+USER_INFO_TABLE_NAME);

        onCreate(db);
    }

    /**
     * Update card status against each Mess ID
     * @param nbcoll
     * @param cardjsonlist
     */
    public void updateMenuCard(String nbcoll, String cardjsonlist){
        SQLiteDatabase database = this.getWritableDatabase();

        /*String qq = "REPLACE INTO "+MENU_TABLE_NAME+" ("+MENU_TABLE_Column_ID+","+MENU_TABLE_Column_1_card_details+
                ") VALUES ('"+nbcoll+"','"+cardjsonlist+"');";
        *//*ContentValues values = new ContentValues();
        values.put(MENU_TABLE_Column_ID, nbcoll); // Contact Name
        values.put(MENU_TABLE_Column_1_card_details, cardjsonlist);
        database.insert(MENU_TABLE_NAME, null, values);*//*

        database.execSQL(qq);*/


        ContentValues initialValues = new ContentValues();
        initialValues.put(MENU_TABLE_Column_ID, nbcoll); // the execution is different if _id is 2
        initialValues.put(MENU_TABLE_Column_1_card_details, cardjsonlist);

        int id = (int) database.insertWithOnConflict(MENU_TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(MENU_TABLE_NAME, initialValues, MENU_TABLE_Column_ID+"=?", new String[] {nbcoll});  // number 1 is the _id here, update to variable for your code
        }
        database.close();
    }

    /**
     *  /**
     * Update card status against each Mess ID
     * @param cardjsonlist
     */
    public void updateUserCard(String userid, String cardjsonlist){
        SQLiteDatabase database = this.getWritableDatabase();

        /*String qq = "REPLACE INTO "+MENU_TABLE_NAME+" ("+MENU_TABLE_Column_ID+","+MENU_TABLE_Column_1_card_details+
                ") VALUES ('"+nbcoll+"','"+cardjsonlist+"');";
        *//*ContentValues values = new ContentValues();
        values.put(MENU_TABLE_Column_ID, nbcoll); // Contact Name
        values.put(MENU_TABLE_Column_1_card_details, cardjsonlist);
        database.insert(MENU_TABLE_NAME, null, values);*//*

        database.execSQL(qq);*/


        ContentValues initialValues = new ContentValues();
        initialValues.put(USER_INFO_Column_ID, userid); // the execution is different if _id is 2
        initialValues.put(USER_INFO_Column_1_token_details, cardjsonlist);

        int id = (int) database.insertWithOnConflict(USER_INFO_TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(USER_INFO_TABLE_NAME, initialValues, USER_INFO_Column_ID+"=?", new String[] {userid});  // number 1 is the _id here, update to variable for your code
        }
        database.close();
    }
    /*
     * @param nbcoll
     * @param offer
     * @use To Update the Offer Database
     */
    public void updateOffer(String nbcoll, String offer){
        SQLiteDatabase database = this.getWritableDatabase();

        /*String qq = "REPLACE INTO "+MENU_TABLE_NAME+" ("+MENU_TABLE_Column_ID+","+MENU_TABLE_Column_1_card_details+
                ") VALUES ('"+nbcoll+"','"+cardjsonlist+"');";
        *//*ContentValues values = new ContentValues();
        values.put(MENU_TABLE_Column_ID, nbcoll); // Contact Name
        values.put(MENU_TABLE_Column_1_card_details, cardjsonlist);
        database.insert(MENU_TABLE_NAME, null, values);*//*

        database.execSQL(qq);*/


        ContentValues initialValues = new ContentValues();
        initialValues.put(MENU_TABLE_Column_ID, nbcoll); // the execution is different if _id is 2
        initialValues.put(MENU_TABLE_Column_2_offer_details, offer);

        int id = (int) database.insertWithOnConflict(MENU_TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(MENU_TABLE_NAME, initialValues, MENU_TABLE_Column_ID+"=?", new String[] {nbcoll});  // number 1 is the _id here, update to variable for your code
        }
        database.close();
    }


    public ArrayList<String> getAllNBCollege()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> nbcoll = new ArrayList<>();
        Cursor cursor = db.query(MENU_TABLE_NAME, new String[] { MENU_TABLE_Column_ID}, null,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                nbcoll.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return nbcoll;
    }


    /**
     * @param id
     * @use To get the Card Json
     */
    public ArrayList<HashMap<String, String>> getCardJson(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String json="";
        JSONObject jObj=null;
        Cursor cursor = db.query(MENU_TABLE_NAME, new String[] { MENU_TABLE_Column_ID,
                        MENU_TABLE_Column_1_card_details }, MENU_TABLE_Column_ID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor != null&& cursor.getCount()>0) {
            json = cursor.getString(1);

            Log.e("JSON DBHAND: ","********"+json);

            if(json.equals("nodata"))
            {
                return null;
            }

        }
        else
        {
            return null;
        }

        // return contact

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        cursor.close();
        return JSONtoArrayList(jObj);

    }

    /**
     * @param id
     * @use To get the Card Json
     */
    public String getOfferJson(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String json="";
        JSONObject jObj=null;
        Cursor cursor = db.query(MENU_TABLE_NAME, new String[] { MENU_TABLE_Column_ID,
                        MENU_TABLE_Column_2_offer_details}, MENU_TABLE_Column_ID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor != null&& cursor.getCount()>0) {
            json = cursor.getString(1);

            Log.e("JSON DBHAND: ","********"+json);

            /*if(!json.equals("nodata"))
            {

            }*/

        }


        return json;

    }

    private ArrayList<HashMap<String, String>> JSONtoArrayList(JSONObject jObj) {




        ArrayList<HashMap<String, String>> MenuArrayList = new ArrayList<>();


        final String TAG_SUCCESS = context.getString(R.string.TAG_SUCCESS);
        final String TAG_MESSINFO = context.getString(R.string.TAG_MESSINFO);
        final String TAG_MESSID =context.getString(R.string.TAG_MESSID);
        final String TAG_NAME = context.getString(R.string.TAG_NAME);
        final String TAG_RICE = context.getString(R.string.TAG_RICE);
        final String TAG_ROTI = context.getString(R.string.TAG_ROTI);
        final String TAG_SPECIAL = context.getString(R.string.TAG_SPECIAL);
        final String TAG_SPECIAL_EXTRA = context.getString(R.string.TAG_SPECIAL_EXTRA);
        final String TAG_VEGIE1 = context.getString(R.string.TAG_VEGIE1);
        final String TAG_VEGIE2 = context.getString(R.string.TAG_VEGIE2);
        final String TAG_VEGIE3 = context.getString(R.string.TAG_VEGIE3);
        final String TAG_OTHER = context.getString(R.string.TAG_OTHER);
        final String TAG_GCHARGE = context.getString(R.string.TAG_GCHARGE);
        final String TAG_STATUS = context.getString(R.string.TAG_STATUS);
        final String TAG_OPENTIME = context.getString(R.string.TAG_OPENTIME);
        final String TAG_CLOSETIME = context.getString(R.string.TAG_CLOSETIME);
        final String TAG_OPENCLOSE="openstatus";



        try {


            int success = jObj.getInt("success");
            if (success == 1) {
                JSONArray mess2 = jObj.getJSONArray("messinfo");

                for (int i = 0; i < mess2.length(); i++) {
                    JSONObject c = mess2.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_MESSID).trim();
                    String rice = c.getString(TAG_RICE).trim();
                    String vegie1 = c.getString(TAG_VEGIE1).trim();
                    String vegie2 = c.getString(TAG_VEGIE2).trim();
                    String vegie3 = c.getString(TAG_VEGIE3).trim();
                    String roti = c.getString(TAG_ROTI).trim();
                    String special = c.getString(TAG_SPECIAL).trim();
                    String special_extra = c.getString(TAG_SPECIAL_EXTRA).trim();
                    String other = c.getString(TAG_OTHER).trim();
                    String openclose=c.getString(TAG_OPENCLOSE).trim();

                    String gcharge = c.getString(TAG_GCHARGE).trim();
                    String otime = c.getString(TAG_OPENTIME).trim();
                    String ctime = c.getString(TAG_CLOSETIME).trim();

                        /*String otime = c.getString(TAG_OPENTIME).trim();
                        String ctime = c.getString(TAG_CLOSETIME).trim();*/

                    String stat = c.getString(TAG_STATUS).trim();


                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_MESSID, id);
                    map.put(TAG_RICE, rice);
                    map.put(TAG_VEGIE1, vegie1);
                    map.put(TAG_VEGIE2, vegie2);
                    map.put(TAG_VEGIE3, vegie3);
                    map.put(TAG_ROTI, roti);
                    map.put(TAG_SPECIAL, special);
                    map.put(TAG_SPECIAL_EXTRA, special_extra);
                    map.put(TAG_OTHER, other);
                    map.put(TAG_OPENCLOSE,openclose);

                    map.put(TAG_GCHARGE, gcharge);
                    map.put(TAG_OPENTIME, otime);
                    map.put(TAG_CLOSETIME, ctime);
                    map.put(TAG_STATUS, stat);


                    Log.d("inDoinBackground: ID", "``````````````````````" + map.toString());


                    // adding HashList to ArrayList
                    MenuArrayList.add(map);
                }


                Log.i("IN DATABASE HANDLER", "``````````````````````" + MenuArrayList.toString());

                // Toast.makeText(context, "11111Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();


                // close this activity
                //context..finish();

            } else {
                Log.i("DATA SHARED", "ERROR");

                //Toast.makeText(context, "Oops,Error Updating Mess Menus", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return MenuArrayList;

    }

    /**
     * @param id
     * @use To get the Offer Json
     */
    public String getOffer(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String json="";
        Cursor cursor = db.query(MENU_TABLE_NAME, new String[] { MENU_TABLE_Column_ID,
                        MENU_TABLE_Column_2_offer_details}, MENU_TABLE_Column_ID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        json = cursor.getString(1);

        // return contact
        return json;
    }


    /**
     * @param id
     * @param info
     * @use To Update the Mess Info in Database
     */
    public void updateMessInfo(String id, String info){


        /*
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
        initialValues.put(MENU_TABLE_Column_ID, nbcoll); // the execution is different if _id is 2
        initialValues.put(MENU_TABLE_Column_2_offer_details, offer);

        int id = (int) database.insertWithOnConflict(MENU_TABLE_NAME, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(MENU_TABLE_NAME, initialValues, MENU_TABLE_Column_ID+"=?", new String[] {nbcoll});  // number 1 is the _id here, update to variable for your code
        }
        database.close();
         */
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESS_INFO_Column_ID, id); // Contact Name
        values.put(MESS_INFO_Column_2_week_menu, info);


        int fromDBid = (int) database.insertWithOnConflict(MESS_INFO_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (fromDBid == -1) {
            database.update(MESS_INFO_TABLE_NAME, values, MESS_INFO_Column_ID+"=?", new String[] {id});  // number 1 is the _id here, update to variable for your code
        }

        database.close();
    }


    public HashMap<String, String> getMessInfo(String messID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String json="";
        JSONObject jObj=null;
        Cursor cursor = db.query(MESS_INFO_TABLE_NAME, new String[] { MESS_INFO_Column_ID,
                        MESS_INFO_Column_2_week_menu }, MESS_INFO_Column_ID + "=?",
                new String[] { messID }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor != null&& cursor.getCount()>0) {
            json = cursor.getString(1);

            Log.e("JSON DBHAND: ","********"+json);

            if(json.equals("nodata"))
            {
                return null;
            }

        }
        else
        {
            return null;
        }

        // return contact

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        cursor.close();
        return JSONtoArrayList_Info(jObj);

    }

    private HashMap<String,String> JSONtoArrayList_Info(JSONObject messinfo) {


        HashMap<String,String> Hashmessinfo=new HashMap<>();

        try {

        Hashmessinfo.put("Name", messinfo.getString("Name"));
        Hashmessinfo.put("GuestCharge", messinfo.getString("GuestCharge"));
        Hashmessinfo.put("LunchOpen", messinfo.getString("LunchOpen"));
        Hashmessinfo.put("LunchClose", messinfo.getString("LunchClose"));
        Hashmessinfo.put("DinnerOpen", messinfo.getString("DinnerOpen"));
        Hashmessinfo.put("DinnerClose", messinfo.getString("DinnerClose"));
        Hashmessinfo.put("MonthlyCharge", messinfo.getString("MonthlyCharge"));
        Hashmessinfo.put("Contact", messinfo.getString("Contact"));
        Hashmessinfo.put("Address", messinfo.getString("Address"));
        Hashmessinfo.put("Location", messinfo.getString("Location"));
        Hashmessinfo.put("NBCollege", messinfo.getString("NBCollege"));

            Hashmessinfo.put("Owner", messinfo.getString("Owner"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  Hashmessinfo;
    }


    /**
     * @param id
     * @param menu
     * @use To Update the Week Menu in Database
     */
    public void updateWeekMenu(String id, String menu){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MESS_INFO_Column_ID, id); // Contact Name
        values.put(MESS_INFO_Column_2_week_menu, menu);
        database.insert(MENU_TABLE_NAME, null, values);
        database.close();
    }

    public void getAll()
    {
        String selectQuery = "SELECT * FROM " + MENU_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Contact contact = new Contact();
                String id = cursor.getString(0);

                Log.i("cursor",cursor.getString(0)+cursor.getString(1)+cursor.getString(2));


            } while (cursor.moveToNext());
        }

    }
}