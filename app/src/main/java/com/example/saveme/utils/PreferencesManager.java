package com.example.saveme.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager
{
    //общие константы для работы с preferences
    public static final String KEY_FIRST_LAUNCH = "firstLaunch";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASS = "pass";

    private final static String APP_MAIN_PREFERENCES = "main_preferences";

    public static SharedPreferences getPreferences( Context context )
    {
        return context.getSharedPreferences( APP_MAIN_PREFERENCES, Context.MODE_PRIVATE );
    }

    public static SharedPreferences.Editor getEditor( Context context )
    {
        return getPreferences( context ).edit();
    }
}
