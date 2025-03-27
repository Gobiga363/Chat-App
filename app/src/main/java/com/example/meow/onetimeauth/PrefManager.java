package com.example.meow.onetimeauth;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "MyPreferences";
    private static final String IS_FIRST_TIME = "IsFirstTime";
    private static final String USER_TYPE = "UserType";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public boolean isFirstTime() {
        return pref.getBoolean(IS_FIRST_TIME, true);
    }

    public void setFirstTime(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME, isFirstTime);
        editor.apply();
    }

    public void setUserType(String userType) {
        editor.putString(USER_TYPE, userType);
        editor.apply();
    }

    public String getUserType() {
        return pref.getString(USER_TYPE, "Unknown"); // Default to "Unknown"
    }
}

