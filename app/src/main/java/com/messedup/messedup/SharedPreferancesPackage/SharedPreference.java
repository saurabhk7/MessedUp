package com.messedup.messedup.SharedPreferancesPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saurabh on 7/9/17.
 * @author saurabh
 * @use to store favourite Mess of the User
 */

public class SharedPreference {

    public static final String PREFS_NAME = "MESS_APP";
    public static final String FAVORITES = "MESS_Favorite";

    public SharedPreference() {
        super();
    }

    /**
     * @param context
     * @param favorites
     * @use save the favourite mess in shared preference
     */
    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<String> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    /**
     * @param context
     * @param product
     * @use to add favourite in the arraylist of favourite Mess
     */
    public void addFavorite(Context context, String product) {
        List<String> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<String>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    /**
     * @param context
     * @param product
     * @use to remove favourite from the arraylist of favourite Mess
     */
    public void removeFavorite(Context context, String product) {
        ArrayList<String> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    /**
     * @param context
     * @return ArrayList of favourite Mess stored in the Shared preference
     */
    public ArrayList<String> getFavorites(Context context) {
        SharedPreferences settings;
        List<String> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            String[] favoriteItems = gson.fromJson(jsonFavorites,
                    String[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<String>(favorites);
        } else
            return null;

        return (ArrayList<String>) favorites;
    }


}
