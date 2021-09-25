package com.example.loginApp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*

class SessionManager @SuppressLint("CommitPrefEdits") constructor(  // for which activity we want to store the data.
    var context: Context
) {
    // we need three variable to create Session...
    var sharedPreferences : SharedPreferences
    var editor : SharedPreferences.Editor

    // put values in the editor by calling putString() by the "editor" object..
    fun createLoginSession(
        fullName: String?,
        username: String?,
        emailId: String?,
        password: String?,
        gender: String?,
        date: String?,
        phoneNo: String?
    ) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_FULL_NAME, fullName)
        editor.putString(KEY_USER_NAME, username)
        editor.putString(KEY_EMAIL_ID, emailId)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_GENDER, gender)
        editor.putString(KEY_DATE, date)
        editor.putString(KEY_PHONE_NO, phoneNo)
        editor.commit() // we need to commit() / apply() after the the putting all the values in sharedPreference to store all values.
    }

    fun createAddress(address: String?, state: String?, name: String?, phone: String?) {
        editor.putString(KEY_ADDRESS, address)
        editor.putString(KEY_STATE, state)
        editor.putString(KEY_ADDRESS_NAME, name)
        editor.putString(KEY_ADDRESS_PHONE, phone)
        editor.commit()
    }

    // to get the information from sharedPreference we put all values in hashMap.
    // get value by using object of sharedPreference.getString(), if no value is present we return default value.
    val userDetailFromSession: HashMap<String, String?> get() {
            // to get the information from sharedPreference we put all values in hashMap.
            // get value by using object of sharedPreference.getString(), if no value is present we return default value.
            val userdata = HashMap<String, String?>()
            userdata[KEY_FULL_NAME] = sharedPreferences.getString(KEY_FULL_NAME, "")
            userdata[KEY_USER_NAME] = sharedPreferences.getString(KEY_USER_NAME, "")
            userdata[KEY_EMAIL_ID] = sharedPreferences.getString(KEY_EMAIL_ID, "")
            userdata[KEY_PASSWORD] = sharedPreferences.getString(KEY_PASSWORD, "")
            userdata[KEY_GENDER] = sharedPreferences.getString(KEY_GENDER, "")
            userdata[KEY_DATE] = sharedPreferences.getString(KEY_DATE, "")
            userdata[KEY_PHONE_NO] = sharedPreferences.getString(KEY_PHONE_NO, "")
            userdata[KEY_ADDRESS] = sharedPreferences.getString(KEY_ADDRESS, "")
            userdata[KEY_STATE] = sharedPreferences.getString(KEY_STATE, "")
            userdata[KEY_ADDRESS_NAME] = sharedPreferences.getString(KEY_ADDRESS_NAME, "")
            userdata[KEY_ADDRESS_PHONE] = sharedPreferences.getString(KEY_ADDRESS_PHONE, "")
            return userdata
        }

    fun checkLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }

    fun logoutUserFromSession() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val IS_LOGIN = "isLoggedIn"

        // these are the key value for storing data in sharedPreferences...
        const val KEY_FULL_NAME = "fullName"
        const val KEY_USER_NAME = "username"
        const val KEY_EMAIL_ID = "emailId"
        const val KEY_PASSWORD = "password"
        const val KEY_GENDER = "gender"
        const val KEY_DATE = "date"
        const val KEY_PHONE_NO = "phoneNo"
        const val KEY_ADDRESS = "address"
        const val KEY_STATE = "state"
        const val KEY_ADDRESS_NAME = "name"
        const val KEY_ADDRESS_PHONE = "phone"
    }

    init {
        // Initialize all the variable..
        sharedPreferences = context.getSharedPreferences("userLoggedInSession", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
}