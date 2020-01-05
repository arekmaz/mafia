package com.arekmaz.mafia;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences getAppScopeSharedPreferences() {
        return getSharedPreferences(getString(R.string.app_shared_prefs_file_name), Context.MODE_PRIVATE);
    }

    protected void writePref(String key, String value) {
        SharedPreferences sharedPref = getAppScopeSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    protected String getAllPrefs() {
        SharedPreferences sharedPref = getAppScopeSharedPreferences();
        return sharedPref.getAll().toString();
    }

    protected String getPref(String key) {
        return getPref(key, "");
    }

    protected String getPref(String key, String defaultVal) {
        SharedPreferences sharedPref = getAppScopeSharedPreferences();
        return sharedPref.getString(key, defaultVal);
    }

    protected  <A extends Activity> void startActivity(Class<A> activityClass) {
        startActivity(activityClass, null);
    }

    protected  <A extends Activity> void startActivity(Class<A> activityClass, Bundle extras) {
        Intent intent = new Intent(this, activityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        super.startActivity(intent);
    }
}
