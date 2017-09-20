package com.messedup.messedup.SharedPreferancesPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by saurabh on 20/9/17.
 */

public class GeneralSharedPref {


    private Context context;


    public GeneralSharedPref(Context context) {
        this.context = context;
    }

    public void updateFromSharedPref(String from) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fromstatus", from);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF from : ",from);

    }


    /**
     * @return the email stored in the Shared Preference (if necessary)
     */
    public String getFromSharedPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("fromstatus", "splash");
        // Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF fromret: ",PreStoredArea);

        return PreStoredArea;

    }


}
