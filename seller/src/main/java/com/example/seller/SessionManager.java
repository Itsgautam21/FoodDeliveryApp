package com.example.seller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionManager {

    // we need three variable to create Session...
    SharedPreferences sharedPreferences;    // It is a class for temporary data storage.
    SharedPreferences.Editor editor;    // it is the editor by which we add the values.
    Context context;        // for which activity we want to store the data.

    private static final String IS_LOGIN = "isLoggedIn";
    // these are the key value for storing data in sharedPreferences...
    public static final String KEY_FULL_NAME = "fullName";
    public static final String KEY_EMAIL_ID = "emailId";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE_NO = "phoneNo";

    public static final String KEY_ADDRESS_NAME = "name";
    public static final String KEY_ADDRESS_PHONE = "phone";
    public static final String KEY_ADDRESS_PIN = "pinCode";
    public static final String KEY_ADDRESS_STATE = "state";
    public static final String KEY_ADDRESS_CITY = "city";
    public static final String KEY_ADDRESS_HOUSE = "house";
    public static final String KEY_ADDRESS_ROAD = "road";


    public static final String KEY_SHOP_NAME = "shopName";
    public static final String KEY_SHOP_DESCRIPTION = "shopDescription";
    public static final String KEY_SHOP_OPEN = "isOpen";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        // Initialize all the variable..
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userLoggedInSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // put values in the editor by calling putString() by the "editor" object..
    public void createLoginSession(String fullName, String emailId, String password, String phoneNo) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL_ID, emailId);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHONE_NO, phoneNo);
        editor.commit(); // we need to commit() / apply() after the the putting all the values in sharedPreference to store all values.
    }
    public void createAddress(String name, String phoneNo, String pinCode, String state, String city, String houseNo, String roadName) {
        editor.putString(KEY_ADDRESS_NAME, name);
        editor.putString(KEY_ADDRESS_PHONE, phoneNo);
        editor.putString(KEY_ADDRESS_PIN, pinCode);
        editor.putString(KEY_ADDRESS_STATE, state);
        editor.putString(KEY_ADDRESS_CITY, city);
        editor.putString(KEY_ADDRESS_HOUSE, houseNo);
        editor.putString(KEY_ADDRESS_ROAD, roadName);
        editor.commit();
    }

    public void createShopInfo(String name, String description) {
        editor.putString(KEY_SHOP_NAME, name);
        editor.putString(KEY_SHOP_DESCRIPTION, description);
        editor.commit();
    }


    public HashMap<String, String> getUserDetailFromSession() {
        // to get the information from sharedPreference we put all values in hashMap.
        // get value by using object of sharedPreference.getString(), if no value is present we return default value.
        HashMap<String, String> userdata = new HashMap<>();
        userdata.put(KEY_FULL_NAME, sharedPreferences.getString(KEY_FULL_NAME,""));
        userdata.put(KEY_EMAIL_ID, sharedPreferences.getString(KEY_EMAIL_ID,""));
        userdata.put(KEY_PASSWORD, sharedPreferences.getString(KEY_PASSWORD,""));
        userdata.put(KEY_PHONE_NO, sharedPreferences.getString(KEY_PHONE_NO,""));
        return userdata;
    }
    public HashMap<String, String> getUserAddressFromSession() {
        HashMap<String, String> userAddress = new HashMap<>();
        userAddress.put(KEY_ADDRESS_NAME, sharedPreferences.getString(KEY_ADDRESS_NAME, ""));
        userAddress.put(KEY_ADDRESS_PHONE, sharedPreferences.getString(KEY_ADDRESS_PHONE, ""));
        userAddress.put(KEY_ADDRESS_PIN, sharedPreferences.getString(KEY_ADDRESS_PIN, ""));
        userAddress.put(KEY_ADDRESS_STATE, sharedPreferences.getString(KEY_ADDRESS_STATE, ""));
        userAddress.put(KEY_ADDRESS_CITY, sharedPreferences.getString(KEY_ADDRESS_CITY, ""));
        userAddress.put(KEY_ADDRESS_HOUSE, sharedPreferences.getString(KEY_ADDRESS_HOUSE, ""));
        userAddress.put(KEY_ADDRESS_ROAD, sharedPreferences.getString(KEY_ADDRESS_ROAD, ""));
        return userAddress;
    }
    public HashMap<String, String> getUserShopInfoFromSession() {
        HashMap<String, String> userShopInfo = new HashMap<>();
        userShopInfo.put(KEY_SHOP_NAME, sharedPreferences.getString(KEY_SHOP_NAME, ""));
        userShopInfo.put(KEY_SHOP_DESCRIPTION, sharedPreferences.getString(KEY_SHOP_DESCRIPTION, ""));
        userShopInfo.put(KEY_SHOP_OPEN, sharedPreferences.getString(KEY_SHOP_OPEN, ""));
        return userShopInfo;
    }

    public boolean checkLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public boolean checkOpen() {
        return sharedPreferences.getBoolean(KEY_SHOP_OPEN, false);
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}
