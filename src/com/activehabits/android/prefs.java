package com.activehabits.android;

import java.util.Map;
import java.lang.String;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
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
        // load shared prefs
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
        Log.i(TAG, "myMgrPrefs: " + myMgrPrefs.getAll().toString());

        //myMgrPrefs.registerOnSharedPreferenceChangeListener(mChangeAction);
        // Why use myMgrPrefs and not myGetPrefs?  application vs. real prefs?

        // load XML prefs to activity
        addPreferencesFromResource(R.xml.preferences);  //R.id.prefs
        // init action0 title & listener
        Preference x = findPreference("action0"); // activity object
        x.setOnPreferenceChangeListener(mChangeAction); // turn on listener
        x.setTitle(myMgrPrefs.getString("action0", "Mark Action"));
        //Log.i(TAG, "context: " + x.getContext());
        Log.i(TAG, "action0 Mgr: " + myMgrPrefs.getString("action0", "Mark Action"));

        // add more prefs if they exist
        Map<String, ?> bar = myMgrPrefs.getAll();
        Log.i(TAG, "myMgrPrefs: " + bar.toString());
        int len = bar.size();
//        Log.i(TAG, "len: " + len);
//        ArrayList al;
        String newAction;
        for (int i = 1; i < len ; ++i) { // i=1, don't run for action0
            newAction = "action" + i;
            Log.i(TAG, "testing: '" + newAction + "'");
            if ( bar.containsKey(newAction) ) {
                // add new pref to activity
                Log.i(TAG, "need to add: " + newAction + ", " + (String) bar.get(newAction));
//                addPreferencesFromIntent(newAction); // this queries other activities.
                //addPreferencesFromResource("action0");  //R.xml.preferences
//                Editor newEditor = myMgrPrefs.edit();
//                newEditor.putString(newAction, (String) bar.get(newAction));
//                newEditor.commit();
//                Log.i(TAG, "added with Editor"); // don't need to

                // after it's added, get the Preference object as x
//                AttributeSet as = null;
//                EditTextPreference p = myMgrPrefs.(newAction, "Mark Action"); // TODO: @string
//                EditTextPreference p = EditTextPreference(this.findViewById(R.id.prefs)); //x.getContext()
//                PreferenceScreen screen = createPreferenceHierarchy();
                PreferenceScreen newPref = getPreferenceManager().createPreferenceScreen(this);
                newPref.setKey(newAction);
                newPref.setTitle(myMgrPrefs.getString(newAction, "Mark Action"));
                newPref.setSummary("Click to change action name"); // use @string
                newPref.setOnPreferenceChangeListener(mChangeAction); // turn on listener
                getPreferenceScreen().addPreference(newPref);

//                View v = this.findViewById(R.id.prefs);
//                EditTextPreference z = new EditTextPreference(v.getContext());
//                z.setTitle(myMgrPrefs.getString(newAction, "Mark Action"));
//                z.setText(myMgrPrefs.getString(newAction, "Mark Action"));
//                v.setPadding(5, 5, 5, 5);
//                ArrayList<EditTextPreference> y = new ArrayList(1);
//                y.add(z);
//                v.addTouchables(y);

//                al = ; TODO: restart here
//                EditTextPreference p.setKey(newAction);
//                getPreferenceScreen().addPreference(p);//  .findPreference(newAction);

                Log.i(TAG, "added Pref: " + newAction + ", " + myMgrPrefs.getString(newAction, "Mark Action"));
            }
        }
        //Map<String, ?> foo = myGetPrefs.getAll();
        //Log.i(TAG, "myGetPrefs: " + foo.toString());
        //Log.i(TAG, "action1 Mgr: " + myMgrPrefs.getString("action1", "Mark Action"));
        Log.i(TAG, "myMgrPrefs: " + myMgrPrefs.getAll().toString());
    }

    private void addNewAction() {
        //SharedPreferences myPrefs = getPreferences(R.xml.preferences); // fail
        //addPreferencesFromResource(R.id.prefs); // NotFoundException
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
//        SharedPreferences myMgrPrefs = getPreferences(R.id.prefs);

        // add to shared preferences
        // Map<String, ?> bar = myMgrPrefs.getAll();
        int len = myMgrPrefs.getAll().size(); // -1 for 0 based, + 1 for new value = size
        String newAction = "action" + len;
        Log.i(TAG, "adding: " + newAction);

        Editor e = myMgrPrefs.edit();
        e.putString(newAction, "Mark Action"); // TODO: @string?
        e.commit();
        Log.i(TAG, "myMgrPrefs: " + myMgrPrefs.getAll().toString());

        // add to activity
        PreferenceScreen newPref = getPreferenceManager().createPreferenceScreen(this);
        newPref.setKey(newAction);
        newPref.setTitle("Mark Action");
        newPref.setSummary("Click to change action name"); // use @string
        newPref.setOnPreferenceChangeListener(mChangeAction); // turn on listener
        getPreferenceScreen().addPreference(newPref);
    }

    private void removeAction() {
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
//        SharedPreferences myMgrPrefs = getPreferences(R.id.prefs);
        int len = myMgrPrefs.getAll().size()-1; // 0 based
        if (len == 0) {
            // TODO: inform dialog box - can't remove last item
            return;
        }
        // TODO: confirmation dialog box

        // remove from prefs
        // Map<String, ?> bar = myMgrPrefs.getAll();
        String remPref = "action" + len;
        Log.i(TAG, "removing: " + remPref);

        Editor e = myMgrPrefs.edit();
        e.remove(remPref);
        e.commit();
        Log.i(TAG, "myMgrPrefs: " + myMgrPrefs.getAll().toString());

        // remove from activity
        final PreferenceScreen s = getPreferenceScreen();
        s.removePreference(s.findPreference(remPref));
    }

    OnPreferenceChangeListener mChangeAction = new OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference changedPref, Object newValue) {
            //SharedPreferences myPrefs = PreferenceManager
            //    .getDefaultSharedPreferences(getBaseContext());
            //SharedPreferences.Editor ed = myPrefs.edit();
            //ed.putString(changedPref.getKey(), String.valueOf(newValue));
            //Log.i(TAG, "changedPref " + changedPref.toString());
            changedPref.setTitle(String.valueOf(newValue));
            //Log.i(TAG, "newValue " + newValue.toString());
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
