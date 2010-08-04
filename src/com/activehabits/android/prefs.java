package com.activehabits.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

public class prefs extends PreferenceActivity {
    private static final String TAG = "ActiveHabits.mark";
//    private static FileWriter writer;
//    private static final Integer MAXEVENTS = 50; // TODO: fix max # events String[] of 50

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if preferences, load
        // load default preferences
        addPreferencesFromResource(R.xml.preferences);

        Preference x = findPreference("action0");
        x.setOnPreferenceChangeListener(mChangeAction);
        //TODO: find why preference title is not restored with value
        SharedPreferences myPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
        x.setTitle(myPrefs.getString(x.getKey(), "Mark Action"));
    }

    OnPreferenceChangeListener mChangeAction = new OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference changedPref, Object newValue) {
            //SharedPreferences myPrefs = PreferenceManager
            //    .getDefaultSharedPreferences(getBaseContext());
            //SharedPreferences.Editor ed = myPrefs.edit();
            //ed.putString(changedPref.getKey(), String.valueOf(newValue));
            Log.i(TAG, "changedPref " + changedPref.toString());
            changedPref.setTitle(String.valueOf(newValue));
            Log.i(TAG, "newValue " + newValue.toString());
            return true;
        }
    };

/*    private void getPrefs() {
        // Get the xml/preferences.xml preferences
        SharedPreferences prefs = PreferenceManager
            .getDefaultSharedPreferences(getBaseContext());
        String[] actionPreference = new String[MAXEVENTS];
        actionPreference[1] = prefs.getString("editTextPref",
                            "Nothing has been entered");
        Log.i(TAG, "foo");
    } */
}
