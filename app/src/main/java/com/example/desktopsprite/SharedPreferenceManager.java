package com.example.desktopsprite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharedPreferenceManager {
    SharedPreferences sp;
    public SharedPreferences.Editor editor;
    public Context context;
    public static final Integer PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String UID = "UID";
    public static final String PID = "PID";
    public static final String PET = "PET_NAME";
    public static final String PET_SHOW = "IS_SHOW_PET";
    public static final String WEATHER = "IS_SHOW_WEATHER";
    public static final String CLOCK = "IS_CLOCK";

    public SharedPreferenceManager (Context context) {
        this.context = context;
        sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public boolean isLogin() {
        return sp.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    public HashMap<String, String> getUser() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sp.getString(NAME, "railgun"));
        user.put(EMAIL, sp.getString(EMAIL, null));
        user.put(UID, sp.getString(UID, null));

        return user;
    }

    public String getPet() {
        return sp.getString(PET, "Baby");
    }

    public boolean isShowPet() {
        return sp.getBoolean(PET_SHOW, false);
    }

    public void logout() {
        editor.putBoolean(LOGIN, false);
        editor.apply();
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public void savePet(String name) {
        editor.putString(PET, name);
        editor.apply();
    }

    public void togglePetShowState(boolean flag) {
        editor.putBoolean(PET_SHOW, flag);
        editor.apply();
    }

    public boolean getWeatherState() {
        return sp.getBoolean(WEATHER, false);
    }

    public void setWeatherState(boolean flag) {
        editor.putBoolean(WEATHER, flag);
        editor.apply();
    }

    public boolean getClockState() {
        return sp.getBoolean(CLOCK, false);
    }

    public void setClockState(boolean flag) {
        editor.putBoolean(CLOCK, flag);
        editor.apply();
    }

    public void saveUser(String name, String email) {
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.apply();
    }


    public void saveUser(String name, String email, String uid) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(UID, uid);
        editor.apply();
    }
}
