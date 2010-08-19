package com.activehabits.android;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
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
        Log.i(TAG, "prefs myMgrPrefs: " + myMgrPrefs.getAll().toString());

        //myMgrPrefs.registerOnSharedPreferenceChangeListener(mChangeAction);
        // Why use myMgrPrefs and not myGetPrefs?  application vs. real prefs?

        // load XML prefs to activity
        addPreferencesFromResource(R.xml.preferences);  //R.id.prefs
        // init action0 title & listener
        Preference x = findPreference("action0"); // activity object
        x.setOnPreferenceChangeListener(mChangeAction); // turn on listener
        x.setTitle(myMgrPrefs.getString("action0", getString(R.string.markaction)));

        Log.i(TAG, "action0 ChangeListener: " + x.getOnPreferenceChangeListener()); // null
        Log.i(TAG, "action0 ClickListener: " + x.getOnPreferenceClickListener()); // null

        //x.setOnClickPreferenceListener(this);

        ////Log.i(TAG, "prefs action0 Mgr: " + myMgrPrefs.getString("action0", getString(R.string.markaction)));

        // add more prefs if they exist
        Map<String, ?> bar = myMgrPrefs.getAll();
        int len = bar.size();
        Log.i(TAG, "prefs myMgrPrefs (" + len + "): " + bar.toString());
        String newAction;
        for (int i = 1; i < len ; ++i) { // i=1, don't run for action0
            newAction = "action" + i;
            if ( bar.containsKey(newAction) ) {
                // add new pref to activity
                Log.i(TAG, "prefs need to add: " + newAction + ", " + (String) bar.get(newAction));

                ////Log.i(TAG, "01");
                //Preference newPref = findPreference(newAction);
                EditTextPreference newPref = new EditTextPreference(this);
                newPref.setKey(newAction);
                newPref.setTitle((CharSequence)bar.get(newAction));
                newPref.setSummary(getString(R.string.clicktochange)); // use @string
                newPref.setOnPreferenceChangeListener(mChangeAction);//(OnPreferenceChangeListener) this); // turn on listener
                getPreferenceScreen().addPreference(newPref);
                //Preference newScreen = new EditTextPreference(this);
//////////////// WHY CAN'T I CREATE A PREFERENCE!?
                // don't need a real "EditTextPreference"? need a screen and hook up the right methods for onPreferenceClick
                //        so that the automatic dialog box pops up to edit the value and save it.
                //        which I thought would be included in the real object instead of a screen.
                //        Screens are also supposed to have a set of values inside them.
                //        not sure why I can't get the rest of the methods to work on a real pref.
                //Preference newScreen = getPreferenceManager().findPreference(newAction);
                ////Log.i(TAG, "01.1");
                // gives a null pointer exception
               //PreferenceScreen newScreen = getPreferenceManager().createPreferenceScreen(this);//.getNewPreferenceScreen(newAction);

                ////Log.i(TAG, "01.1.1");
               //newScreen.setKey(newAction);

               //newScreen.setTitle(myMgrPrefs.getString(newAction, getString(R.string.markaction)));
                ////Log.i(TAG, "01.1.2");
               //newScreen.setSummary(getString(R.string.clicktochange)); // use @string
               //newScreen.setOnPreferenceChangeListener(mChangeAction);//(OnPreferenceChangeListener) this); // turn on listener
                // NullPointerException
                ////Log.i(TAG, "01.2");
               //newScreen.setOnPreferenceClickListener(x.getOnPreferenceClickListener());// turn on listener
                ////Log.i(TAG, "01.3");
                // mClickAction // mChangeAction
                //newScreen.addPreference(newPref); // null pointer
               //getPreferenceScreen().addPreference(newScreen);
               //Log.i(TAG, "01.3");

                //Log.i(TAG, "02");
//                Log.i(TAG, "prefs newPref: " + newPref.toString());
//                Log.i(TAG, "03");
//                Log.i(TAG, "prefs screen: " + getPreferenceScreen().toString());
              //getPreferenceScreen().addPreference;
//                getPreferenceScreen().addPreference(newPref);
//                Log.i(TAG, "04");

                //newPref.setText(newAction);
                //newPref.setOnClickPreferenceListener((OnClickListener) this);

                Log.i(TAG, "prefs added: " + newAction);
            }
        }
        //Map<String, ?> foo = myGetPrefs.getAll();
        //Log.i(TAG, "myGetPrefs: " + foo.toString());
        //Log.i(TAG, "action1 Mgr: " + myMgrPrefs.getString("action1", getString(R.string.markaction)));
        //Log.i(TAG, "prefs myMgrPrefs: " + myMgrPrefs.getAll().toString());
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
        Log.i(TAG, "prefs adding: " + newAction);

        Editor e = myMgrPrefs.edit();
        e.putString(newAction, getString(R.string.markaction));
        e.commit();
        Log.i(TAG, "prefs myMgrPrefs: " + myMgrPrefs.getAll().toString());

        // add to activity
       //PreferenceScreen newPref = getPreferenceManager().createPreferenceScreen(this);
        EditTextPreference newPref = new EditTextPreference(this);
        newPref.setKey(newAction);
        newPref.setTitle(getString(R.string.markaction));
        newPref.setSummary(getString(R.string.clicktochange)); // use @string
        //newPref.setOnPreferenceClickListener(mClickAction);
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
        Log.i(TAG, "prefs removing: " + remPref);

        Editor e = myMgrPrefs.edit();
        e.remove(remPref);
        e.commit();
        //Log.i(TAG, "prefs myMgrPrefs: " + myMgrPrefs.getAll().toString());

        // remove from activity
        final PreferenceScreen s = getPreferenceScreen();
        s.removePreference(s.findPreference(remPref)); //null pointer exception
    }

    OnPreferenceChangeListener mChangeAction = new OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference changedPref, Object newValue) {
            //SharedPreferences myPrefs = PreferenceManager
            //    .getDefaultSharedPreferences(getBaseContext());
            //SharedPreferences.Editor ed = myPrefs.edit();
            //ed.putString(changedPref.getKey(), String.valueOf(newValue));
            Log.i(TAG, "prefs changedPref " + changedPref.toString());
            changedPref.setTitle(String.valueOf(newValue));
            Log.i(TAG, "prefs newValue " + newValue.toString());
            return true;
        }
    };

    OnPreferenceClickListener mClickAction = new OnPreferenceClickListener() {
        public boolean onPreferenceClick(Preference toChangePref) {
            Log.i(TAG, "prefs toChangePref " + toChangePref.toString());
            //getPreferenceScreen().getDialog();
            //TODO: mClickAction
            //changingPref.; // hmm!!!!
            //Log.i(TAG, "prefs newValue " + newValue.toString());
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
        menu.removeItem(R.id.quit); // disable quit item
        if ( menu.findItem(R.id.removeaction) == null ) {
            //TODO: prefs context menu order
            Log.i(TAG, "prefs order: " + menu.findItem(R.id.about).getOrder());
            //menu.add(Menu.FIRST, R.id.addaction, Menu.FIRST, getString(R.string.addaction)); // moved to menu
            menu.add(Menu.FIRST, R.id.removeaction, Menu.FIRST, getString(R.string.removeaction));
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
        Log.i(TAG, "prefs foo");
    } */
}
