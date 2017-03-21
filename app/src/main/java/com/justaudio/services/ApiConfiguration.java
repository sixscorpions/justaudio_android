package com.justaudio.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by ${VIDYA}
 */

public class ApiConfiguration {


    final static String SERVER_NOT_RESPONDING = "We are unable to connect the internet. " + "Please check your connection and try again.";
    public final static String ERROR_RESPONSE_CODE = "We could not process your request at this time. Please try again later.";


    private static final String APP_PREF = "APP_PREF";
    public static final String PREF_KEY_AUTH_TOKEN = "PREF_KEY_AUTH_TOKEN";

    public static void setAuthToken(Context context, String key, String value) {
        try {
            if (context != null) {
                SharedPreferences appInstallInfoSharedPref = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
                appInstallInfoEditor.putString(key, value);
                appInstallInfoEditor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAuthToken(Context context, String key) {
        try {
            SharedPreferences authPreference = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
            return authPreference.getString(key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static final int REST_GET_MOVIES_CODE = 1;
    public static final int REST_GET_MOVIE_DETAILS_CODE = 2;
    public static final int REST_ADD_TO_FAVORITES_CODE = 3;
    public static final int REST_DELETE_FROM_FAVORITES_CODE = 4;
    public static final int REST_GET_FEV_LIST_CODE = 5;


}
