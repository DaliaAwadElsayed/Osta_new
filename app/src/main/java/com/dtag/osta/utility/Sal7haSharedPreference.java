package com.dtag.osta.utility;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dtag.osta.network.ResponseModel.Model.user.Registeration;

public class Sal7haSharedPreference {
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String IMAGE_KEY = "image_pref";
    private static final String KEY_EMAIL = "email";
    private static final String TOKEN = "token_pref";
    private static final String UID = "user_id_pref";
    private static final String LANG_KEY = "chosen_lang_key";
    private static final String ROLE = "user";
    private static final String ADDRESS = "address";
    private static final String DEVICE_TOKEN = "device_token";
    private static final String CITY = "city";
    private static final String AREA = "area";
    private static final String LONG = "long";
    private static final String LAT = "lat";


    private static Application application;

    public Sal7haSharedPreference() {

    }

    /**
     * @param language 0 for english and 1 for arabic
     */
    public static void changeLanguage(Context context, int language) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANG_KEY, language == 0 ? "en" : "ar").apply();
    }

    /**
     * save the user data into shared preference manager (APPLICATION TO APP PREFERENCE MANAGER)
     *
     * @param context  the context to access the shared preference
     * @param userData the user data from the login response
     */
    public static void saveUserData(Context context, Registeration userData, int id) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(UID, id)
                .putString(ADDRESS, userData.getAddress())
                .putString(ROLE, userData.getRole())
                .putInt(CITY, userData.getCityId())
                .putString(LONG, userData.getLongitude())
                .putString(LAT, userData.getLatitude())
//                .putInt(AREA, userData.getArea().getId())
                .putString(TOKEN, userData.getToken())
                .putBoolean(IS_LOGGED_IN, true)
                .apply();
    }


    public static void setRole(Context context, String role) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(ROLE, role)
                .apply();
    }

    /**
     * @return the user Role
     */
    public static String getRole(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ROLE, "");
    }

    /**
     * Add the device token
     */
    public static void setTokenDevice(Context context, String tokenDevice) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(DEVICE_TOKEN, tokenDevice)
                .apply();
    }

    /**
     * @return the device token
     */
    public static String getTokenDevice(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(DEVICE_TOKEN, "");
    }

    /**
     * @return the user token
     */
    public static String getToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(TOKEN, "");
    }

    /**
     * @return the user City
     */
    public static int getCity(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(CITY, 0);
    }

    /**
     * @return the user Area
     */
    public static int getArea(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(AREA, 0);
    }

    /**
     * @return the user Address
     */
    public static String getAddress(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(ADDRESS, "");
    }

    /**
     * @return the user Long
     */
    public static String getLong(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LONG, "");
    }

    /**
     * @return the user Lat
     */
    public static String getLat(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LAT, "");
    }


    /**
     * @return the user id
     */
    public static int getUid(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(UID, 0);
    }

    /**
     * @return the login state if the user logged in will return true, else will return false
     */
    public static boolean isLoggedIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * use this to clear the shared preference (when the user log out the account)
     */
    public static void clearSharedPreference(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void saveAgentImage(Context context, String imageUrl) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(IMAGE_KEY, imageUrl)
                .apply();
    }

    public static String getAgentImage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(IMAGE_KEY, "");
    }

    public static int getSelectedLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANG_KEY, "").equals("en") ? 0 : 1;
    }

    public static String getSelectedLanguageValue(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANG_KEY, "en");
    }

    /**
     * get Activity
     */
    public static void setActivity(Application activity) {
        application = activity;
    }

    public static Application getActivity() {
        return application;
    }


    private static SharedPreferences getDefaultSharedPreference(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(context) != null)
            return PreferenceManager.getDefaultSharedPreferences(context);
        else
            return null;
    }

    public static void setSelectedLanguageId(Context mContext, String id) {
        final SharedPreferences prefs = getDefaultSharedPreference(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("app_language_id", id);
        editor.apply();
    }

    public static String getSelectedLanguageId(Context mContext) {
        return getDefaultSharedPreference(mContext)
                .getString("app_language_id", "");
    }

    public enum Role {
        USER("user"), AGENT("agent"), NONE("none");
        public String Role;

        Role(String role) {
            Role = role;
        }
    }

}
