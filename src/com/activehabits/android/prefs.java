package com.activehabits.android;

import java.util.Map;
import java.lang.String;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class prefs extends PreferenceActivity {
    private static final String TAG = "ActiveHabits.mark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load XML prefs to activity
        addPreferencesFromResource(R.xml.preferences);  //R.id.prefs
        Preference x = findPreference("action0"); // activity object
        x.setOnPreferenceChangeListener(mChangeAction); // turn on listener

        // set title of first pref to the value
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
        x.setTitle(myMgrPrefs.getString("action0", "Mark Action"));
        Log.i(TAG, "action0 Mgr: " + myMgrPrefs.getString("action0", "Mark Action"));
        // Why use myMgrPrefs and not myGetPrefs?  application vs. real prefs?

        // add more prefs if they exist
        Map<String, ?> bar = myMgrPrefs.getAll();
        Log.i(TAG, "myMgrPrefs: " + bar.toString());
        int len = bar.size();
        //Log.i(TAG, "go: action" + 1);
        String newAction;
        for (int i = 1; i < len ; ++i) { // i=1, don't run for action0
            newAction = "action" + i;
            Log.i(TAG, "testing: '" + newAction + "'");
            if ( bar.containsKey(newAction) ) {
                // add new pref to activity
                Log.i(TAG, "need to add: " + newAction);
//                addPreferencesFromIntent(newAction); // this is the key
                // after it's added, get the Preference object as x
//                x.setOnPreferenceChangeListener(mChangeAction); // turn on listener
//                x.setTitle(myMgrPrefs.getString(newAction, "Mark Action"));
            }
        }
        //SharedPreferences myGetPrefs = getPreferences(R.id.prefs);
        //Map<String, ?> foo = myGetPrefs.getAll();
        //Log.i(TAG, "myGetPrefs: " + foo.toString());
        //Log.i(TAG, "action1 Mgr: " + myMgrPrefs.getString("action1", "Mark Action"));
    }

    private void addNewAction() {
        //SharedPreferences myPrefs = getPreferences(R.xml.preferences); // fail
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);

        Map<String, ?> bar = myMgrPrefs.getAll();
        Log.i(TAG, "myMgrPrefs: " + bar.toString());
        int len = bar.size();
        Log.i(TAG, "myMgrPrefs: " + bar.toString());
        Log.i(TAG, "adding: action" + (len+1));

        //Log.i(TAG, "onCreate myPrefs: " + myMrgrPrefs.toString());
        Editor e = myMgrPrefs.edit();
        e.putString("action" + (len + 1), "Mark Action");
        e.commit();
//        addPreferencesFromResource(R.id.prefs); // NotFoundException

//        SharedPreferences myPrefs = PreferenceManager
//        .getDefaultSharedPreferences(this);
    }

    private void removeAction() {
        //SharedPreferences myPrefs = getPreferences(R.xml.preferences); // fail
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);

        Map<String, ?> bar = myMgrPrefs.getAll();
        int len = bar.size();
        Log.i(TAG, "removing: action" + (len));
        // TODO: confirmation dialog box

        Editor e = myMgrPrefs.edit();
        e.remove("action" + len);
        e.commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.habit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.mark);  // disable mark  item
        menu.removeItem(R.id.chart); // disable chart item
        menu.removeItem(R.id.prefs); // we are in prefs so disable prefs item
        menu.removeItem(R.id.quit); // disable chart item
        if ( menu.findItem(R.id.addaction) == null ) {
            Log.i(TAG, "order: " + menu.findItem(R.id.about).getOrder());
            menu.add(Menu.FIRST, R.id.addaction, Menu.FIRST, getString(R.string.addaction));
            menu.add(Menu.FIRST, R.id.removeaction, Menu.FIRST, getString(R.string.removeaction));
            // order-
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//        case R.id.mark:
//            Intent myMarkIntent = new Intent(this,mark.class);
//            startActivity(myMarkIntent);
//            finish();
//            return true;
//        case R.id.chart:
//            Intent myChartIntent = new Intent(this,chart.class);
//            startActivity(myChartIntent);
//            finish();
//            return true;
//      case R.id.social:
//          return true;
//        case R.id.prefs:
//            Intent myPrefIntent = new Intent(this,prefs.class);
//            startActivity(myPrefIntent);
//            return true;
        case R.id.addaction: {
            addNewAction();
            return true;
        }
        case R.id.removeaction: {
            removeAction();
            return true;
        }
        case R.id.about:
            return true; // TODO: about from prefs
//        case R.id.quit: {
//            finish();
//            return true;
//        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

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
