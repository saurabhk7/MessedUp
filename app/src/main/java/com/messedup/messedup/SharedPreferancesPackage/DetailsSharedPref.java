package com.messedup.messedup.SharedPreferancesPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by saurabh on 19/9/17.
 */

public class DetailsSharedPref {


    private Context context;


    public DetailsSharedPref(Context context) {
        this.context = context;
    }

    public void updateIntroDone(String status) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("intro", status);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF intro : ",status);

    }

    public void updateImInStatus(String status) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("iamin", status);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF iamin : ",status);

    }


    public void updateAdLoadStatus(String status) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("adload", status);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF adload : ",status);

    }


    public void updateDetailsSent(String status) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("details", status);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF details : ",status);

    }

    public void updateWalkThrough(String status) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("walk", status);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF walk : ",status);

    }

    public void updateRate(String rate) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("rate", rate);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF rate : ",rate);

    }

    public void updateMealStatusSharedPref(String mealtype) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("openstatus", mealtype);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF OPENStatus : ",mealtype);

    }

    public void updateNameSharedPrefs(String name) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", name);
        editor.apply();
        editor.commit();

        Log.e("SHRDPRF Name : ",name);

    }
    public void updateEmailSharedPrefs(String email) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("useremail", email);
        editor.apply();
        editor.commit();
        Log.e("SHRDPRF Email : ",email);


    }

    public void updatePhoneSharedPrefs(String phone) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphone", phone);
        editor.apply();
        editor.commit();
        Log.e("SHRDPRF Phone : ",phone);


    }

    public void updatePhotURLSharedPrefs(String url) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphotourl", url);
        editor.apply();
        editor.commit();
        Log.e("SHRDPRF PhotoURL : ",url);

    }

    /**
     * @return the email stored in the Shared Preference (if necessary)
     */
    public String getNameSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("username", "NAME");
       // Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF namereturned: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getEmailSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("useremail", "EMAIL");
       // Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF emailreturned: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getPhoneSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("userphone", "PHONE");
        // Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF phonereturned: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getPhotoURLSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("userphotourl", "URL");
      //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF urlreturned: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getMealStatusSharedPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("openstatus", " ");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF openstatusret: ",PreStoredArea);

        return PreStoredArea;

    }


    public String getWalkThroughStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("walk", "notdone");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF walk: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getRateStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("rate", "show");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF rate: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getDetailsSent() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("details", "notsuccess");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF detailsret: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getIntroDone() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("intro", "notdone");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF intro: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getAdLoadStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("adload", "notloaded");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF adload: ",PreStoredArea);

        return PreStoredArea;

    }

    public String getImInStatus() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String PreStoredArea = preferences.getString("iamin", "notdone");
        //  Log.d("IN SHARED PREFs", "GOT STRING " + PreStoredArea);
        Log.e("SHRDPRF iamin: ",PreStoredArea);

        return PreStoredArea;

    }
}
