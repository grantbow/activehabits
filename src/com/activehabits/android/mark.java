package com.activehabits.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;

public class mark extends Activity implements OnClickListener {
	private static final String TAG = "ActiveHabits.mark";
	private static FileWriter writer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set up layout and button
        setContentView(R.layout.main);
        Button logEventButton = (Button) findViewById(R.id.log_event_button);
        logEventButton.setOnClickListener((OnClickListener) this);
//        registerForContextMenu(logEventButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        // later use setPreferences(int)?

    	boolean mExternalStorageAvailable = false;
    	boolean mExternalStorageWriteable = false;
    	String state = Environment.getExternalStorageState();
    	if (Environment.MEDIA_MOUNTED.equals(state)) {
    	    // We can read and write the media
    	    mExternalStorageAvailable = mExternalStorageWriteable = true;
    	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
    	    // We can only read the media
    	    mExternalStorageAvailable = true;
    	    mExternalStorageWriteable = false;
    	} else {
    	    // Something else is wrong. It may be one of many other states, but all we need
    	    //  to know is we can neither read nor write
    	    mExternalStorageAvailable = mExternalStorageWriteable = false;
    	}

    	// begin work

    	if (mExternalStorageAvailable) {
    		// getExternalStorageDirectory()
    		// check if f1le exists
    		//read in last event from log
    		try {
    			File root = Environment.getExternalStorageDirectory();
    			Resources res = getResources();
    			String sFileName = res.getString(R.string.log_event_filename); //String sFileName = "activehabits.txt";
    			File gpxfile = new File(root,sFileName);
    			//if gpxfile.exists()
    			writer = new FileWriter(gpxfile, true); // appends
    			//else
    		}
    		catch(IOException e) {
    			e.printStackTrace();
    		}
    	} else  { // file doesn't exist - needs testing but works OK for now
    		if (mExternalStorageWriteable) {
    			// create log & notify user
    		} else {
    			// notify user no storage and no log && exit
    		}
    	  }
    }

    @Override
    public void onPause() {
    	try {
    	writer.flush();
    	writer.close();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.habit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.mark); // we are in mark so disable mark item
        // is it possible to make menu 1 x 3 instad of 2x2?
//      ((ViewGroup) menu.getItem(R.id.chart)).setPadding(1,10,10,1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//      case R.id.mark: // item removed
//          return true;
        case R.id.chart:
        	Intent myIntent = new Intent(this,chart.class);
        	startActivity(myIntent);
        	finish();
            return true;
//      case R.id.social:
//          return true;
//        case R.id.settings:
//            return true; // TODO: settings from mark
        case R.id.about:
            return true; // TODO: about from mark
        case R.id.quit: {
            finish();
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
		Calendar rightnow = Calendar.getInstance();
		Date x = rightnow.getTime();
        Integer b = x.getMinutes() * 100 / 60;

        LocationManager locator = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc = locator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null) {
          // Fall back to coarse location.
          loc = locator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        String locString;
        if (loc == null)
        	locString = "";
        else
        	locString = loc.toString();

//        Criteria c = new Criteria();
//        c.setAccuracy(Criteria.NO_REQUIREMENT);
//        Location loc = locator.getLastKnownLocation(locationManager.getBestProvider(c, true));
                       // criteria, enabledOnly - getLastKnownLocation error check?
        try {
        	if (b < 10) { // x.getTime() should be identical foo.getTimeInMillis()
        		Log.i(TAG, "mark write: event1\t" + (rightnow.getTimeInMillis() / 1000) + "\t" + x.getHours() + ".0" + b + "\t" + x.toLocaleString() + "\t" + locString);
        		writer.append("event1\t" + (rightnow.getTimeInMillis() / 1000) + "\t" + x.getHours() + ".0" + b + "\t" + x.toLocaleString() + "\t" + locString + "\n");
        	} else {
        		Log.i(TAG, "mark write: event1\t" + (rightnow.getTimeInMillis() / 1000) + "\t" + x.getHours() + "." + b + "\t" + x.toLocaleString() + "\t" + locString);
        		writer.append("event1\t" + (rightnow.getTimeInMillis() / 1000) + "\t" + x.getHours() + "." + b + "\t" + x.toLocaleString() + "\t" + locString + "\n");
        	}
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
    	finish();
	}

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu, menu);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//        // Handle item selection
//        switch (item.getItemId()) {
//        case R.id.rename:
//            renameEvent(info.id);
//            return true;
//        case R.id.remove:
//            removeEvent(info.id);
//            return true;
//        default:
//            return super.onContextItemSelected(item);
//        }
//    }

//    private void renameEvent(long id) {
//        // from Context Item
//        showDialog(R.id.dialog_rename);
//    }
//
//    private void removeEvent(long id) {
//        // from Context Item
//
//    }

//    @Override
//    protected Dialog onCreateDialog(int id) { // TODO: OnPrepareDialog
//        switch(id) {
//        case R.id.rename:
//            rn.SetText("eventname"); // TODO: FRUSTRATION
/* ALL I FUCKING WANT IS A SIMPLE DIALOG BOX LINEAR LAYOUT I
 * GUESS WITH A TITLE, AN EDITTEXT FIELD POPULATED WITH THE
 * EVENT NAME AND OK / CANCEL
 * WHY IS THIS SO FUCKING HARD
 * I MUST BE WAY TOO TIRED FOR THIS */
//            default:
//                return null;
//        }
//    }

};
