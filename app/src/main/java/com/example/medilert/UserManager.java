package com.example.medilert;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    
    private final SharedPreferences prefs;
    
    public UserManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }
    
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "User"); // "User" is the default value
    }
} 