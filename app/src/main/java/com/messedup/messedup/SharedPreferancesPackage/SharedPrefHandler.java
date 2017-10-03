package com.messedup.messedup.SharedPreferancesPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by saurabh on 15/9/17.
 */

public class SharedPrefHandler {

    private Context context;


    public SharedPrefHandler(Context context) {
        this.context = context;
    }

    public void updateSharedPrefs(String selectedArea) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("selectedarea", selectedArea);
        editor.apply();
        editor.commit();

    }

    /**
     * @return the Current Nearby College stored in the Shared Preference (if necessary)
     */
    public String getSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("selectedarea", "PICT, BVP, Katraj");
        Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        return PreStoredArea;


    }

}
