package com.activehabits.android;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class mark extends Activity implements OnClickListener {
    private static final String TAG = "ActiveHabits.mark"; // for Log.i(TAG, ...);
    private static FileWriter writer;
    private static int paddingValue = 3; // * 10 pixels for calculating button sizes
    private static int splashed = 0;
    private View contextMenuItem; // button long pressed for context menu
    private View textEntryView;   // TextEntry to update with default value of contextMenuItem

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        super.onResume();
        // load default preferences
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
        setContentView(R.layout.main);

        // prepare to add more buttons from myMgrPrefs if they exist
        Map<String, ?> bar = myMgrPrefs.getAll();
        //Log.i(TAG, "mark myMgrPrefs: " + bar.toString());
        int len = bar.size();

        // roughly each button height = screen size / 1+len
        //         subtract for padding - use self.paddingValue
        Display container = ((WindowManager)this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        //Log.i(TAG, "mark vars1: " + container.toString() );
        //Log.i(TAG, "mark vars2: " + container.getHeight());//(int) container.getHeight() );
        Integer buttonHeight;
        if (len == 0) {
            buttonHeight = (Integer) ((container.getHeight() - (10*(mark.paddingValue+1) )) / (1)); }
        else {
            buttonHeight = (Integer) ((container.getHeight() - (10*(len+mark.paddingValue) )) / (len)); }
        // set up action0 button
        Button logEventButton = (Button) findViewById(R.id.log_event_button);
        logEventButton.setText(myMgrPrefs.getString("action0", getString(R.string.markaction)));
        logEventButton.setTag("action0");
        logEventButton.setMinLines(3);
        logEventButton.setPadding(10, 10, 10, 10);
        logEventButton.setOnClickListener((OnClickListener) this);
        logEventButton.setHeight(buttonHeight);
        registerForContextMenu(logEventButton);
        
        final CharSequence setTo = logEventButton.getText();
        final CharSequence defaultSetTo = getString(R.string.markaction);
        
        //Log.i(TAG, "mark splash? " + setTo + ", " + defaultSetTo);

        if (setTo.equals(defaultSetTo) & (mark.splashed == 0)) {     // strange syntax to make it compare
            mark.splashed = 1;
        	// assume if first action is not changed from default
        	//     this is first run or help is needed so show splash
        	Intent mySplashIntent = new Intent(this,splash.class);
        	startActivityForResult(mySplashIntent,1);
        }

        // prepare to add more buttons if they exist
        String newAction;
        for (int i = 1; i < len ; ++i) { // i=1, don't run for action0
            newAction = "action" + i;
            if ( bar.containsKey(newAction) ) { // & ! (findView(newAction)) ) {
                // add new button to activity
                //Log.i(TAG, "mark need to add: " + newAction + ", " + (String) bar.get(newAction) + ", " +buttonHeight);
                createNewButton(newAction, myMgrPrefs.getString(newAction, getString(R.string.markaction)), buttonHeight);
            }
        }
        //Log.i(TAG, "mark myMgrPrefs: " + myMgrPrefs.getAll().toString());

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

    private void createNewButton(String newAction, String newActionString, Integer newButtonHeight) {
    	// add new button to activity
        Button newButton = new Button(this);
        newButton.setMinLines(3);
        newButton.setPadding(10, 10, 10, 10);
        newButton.setTag(newAction);
        newButton.setText(newActionString);
        newButton.setClickable(true);
        newButton.setLongClickable(true);
        newButton.setFocusableInTouchMode(false);
        newButton.setFocusable(true);
        newButton.setOnClickListener((OnClickListener) this);
        newButton.setHeight(newButtonHeight);
        registerForContextMenu(newButton);
        View logEventButton = findViewById(R.id.log_event_button);
        ((ViewGroup) logEventButton.getParent()).addView(newButton);

        //Log.i(TAG, "mark added: " + newAction + ", " + newActionString);
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
    public boolean onCreateOptionsMenu(Menu menu) { // bottom menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.habit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { // bottom menu
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.mark); // we are in mark so disable mark item
        // is it possible to make menu 1 x 3 instead of 2x2?
        //((ViewGroup) menu.getItem(R.id.chart)).setPadding(1,10,10,1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//      case R.id.mark: // we are mark Activity, item removed
//          return true;
        case R.id.chart:
        	Intent myChartIntent = new Intent(this,chart.class);
        	startActivity(myChartIntent);
        	finish();
            return true;
//      case R.id.social:
//          return true;
        case R.id.addaction:
        	addNewAction();
            return true;
//        case R.id.prefs:
//            Intent myPrefIntent = new Intent(this,prefs.class);
//            startActivity(myPrefIntent);
//            return true;
        case R.id.about:
            Intent myAboutIntent = new Intent(this,about.class);
            startActivity(myAboutIntent);
            return true;
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
	        // x.getTime() should be identical rightnow.getTimeInMillis()
		Integer b = x.getMinutes() * 100 / 60;

        LocationManager locator = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;
        try {
            loc = locator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                // Fall back to coarse location.
                loc = locator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//              Criteria c = new Criteria();
//              c.setAccuracy(Criteria.NO_REQUIREMENT);
//              Location loc = locator.getLastKnownLocation(locationManager.getBestProvider(c, true));
                             // criteria, enabledOnly - getLastKnownLocation error check?
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        String locString;
        if (loc == null)
        	locString = "";
        else
        	locString = loc.toString();

        CharSequence buttonText = ((Button) v).getText();

        try {
        	if (b < 10) {
        		Log.i(TAG, "mark write: "
        		        + buttonText + "\t"
        		        + (rightnow.getTimeInMillis() / 1000) + "\t"
        		        + x.getHours() + ".0" + b + "\t"
        		        + x.toLocaleString() + "\t" + locString);
        		writer.append( buttonText + "\t" +
        		        (rightnow.getTimeInMillis() / 1000) + "\t"
        		        + x.getHours() + ".0" + b + "\t"
        		        + x.toLocaleString() + "\t"
        		        + locString + "\n");
        	} else {
        		Log.i(TAG, "mark write: "
        		        + buttonText + "\t"
        		        + (rightnow.getTimeInMillis() / 1000) + "\t"
        		        + x.getHours() + "." + b + "\t"
        		        + x.toLocaleString() + "\t"
        		        + locString);
        		writer.append( buttonText + "\t"
        		        + (rightnow.getTimeInMillis() / 1000) + "\t"
        		        + x.getHours() + "." + b + "\t"
        		        + x.toLocaleString() + "\t"
        		        + locString + "\n");
        	}
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
    	finish();
	}

    private void addNewAction() {
        //SharedPreferences myPrefs = getPreferences(R.xml.preferences); // fail
        //addPreferencesFromResource(R.id.prefs); // NotFoundException
        SharedPreferences myMgrPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);

        // add to shared preferences
        // Map<String, ?> bar = myMgrPrefs.getAll();
        int len = myMgrPrefs.getAll().size(); // -1 for 0 based, + 1 for new value = size
        String newAction = "action" + len;
        //Log.i(TAG, "mark adding: " + newAction + ", " + getString(R.string.markaction));

        Editor e = myMgrPrefs.edit();
        e.putString(newAction, getString(R.string.markaction));
        e.commit();
        //Log.i(TAG, "mark myMgrPrefs: " + myMgrPrefs.getAll().toString());

        // calculate new buttonHeight
        Display container = ((WindowManager)this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        final Integer buttonHeight;
        // we are adding a button, len will be OK.
        buttonHeight = (Integer) ((container.getHeight() - (10*(mark.paddingValue) )) / (len));
    	// resize existing buttons to buttonHeight
        ViewGroup context = (ViewGroup) findViewById(R.id.log_event_button).getParent();
        Integer i;
        for (i = 0; i < len; ++i) {
        	//Log.i(TAG, "mark resizing " + i + ", " + context.getChildAt(i) + " to " + buttonHeight);
            ((Button) context.getChildAt(i)).setHeight(buttonHeight);
        }
        // add button to activity
        createNewButton(newAction, getString(R.string.markaction), buttonHeight);
        // redraw
        Intent myPrefIntent = new Intent(this,mark.class);
        startActivity(myPrefIntent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        contextMenuItem = v; // stores button context menu called from // does not need to move to onPrepareContextMenu
        SubMenu sub = menu.addSubMenu (0, R.id.playlist, 0, R.string.playlistchange );
        sub.clear();
        sub.add(1,R.id.playlistclear,0,R.string.playlistclear);

        // I sure hope the list of playlists doesn't change on me but this function is my only choice
    	//List<String> listPlay = new ArrayList<String>();
    	//List<String> listIds = new ArrayList<String>();
    	/* populate */
    	String playlistid = null;
    	String newListName = null;

    	Cursor cursor = getContentResolver().query(
    			MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
    			null, null, null, null);
    	if (cursor != null) {
    	 if (cursor.moveToFirst()) {
    	  do {
    	     playlistid = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
    	     newListName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
    	     //listPlay.add(newListName);
    	     //listIds.add(playlistid);
    	     Intent intent = new Intent();
    	     intent.putExtra("playlist", Long.parseLong(playlistid));
    	     // this is a fantastic intent trick from com/android/music/MusicUtils.java and MediaPlaybackActivity.java
    	     sub.add(2, R.id.playlistselected, Integer.parseInt(playlistid), newListName).setIntent(intent);
    	  } while (cursor.moveToNext());
    	     cursor.close();
    	 }
    	}
    	//Log.i(TAG, "playlistid " + playlistid);
    	//Log.i(TAG, "listPlay " + listPlay.toString());
    	//Log.i(TAG, "listIds " + listIds.toString());
        //p.addTouchables(listViews);

//    	// play playlist
//    	Intent intent = new Intent(Intent.ACTION_VIEW);
//    	intent.setComponent(new ComponentName("com.android.music","com.android.music.PlaylistBrowserActivity"));
//    	intent.setType(MediaStore.Audio.Playlists.CONTENT_TYPE);
//    	intent.setFlags(0x10000000); // need to understand these 3 lines
//    	//intent.putExtra(playlist, false);
//    	intent.putExtra("playlist", playlistid);
//    	startActivity(intent);
//    	// test mp3 http://download29.jamendo.com/download/track/469312/mp32/24deaf7def/Hope.mp3	
        
    }
    
// Doesn't Exist!!! WTF!?
//    @Override
//    public void onPrepareContextMenu(ContextMenu menu) {
//    	super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.renameaction:
        	showDialog(R.layout.rename);
            return true;
        case R.id.removeaction:
            showDialog(R.layout.remove);
            return true;
// moved to context submenu
//        case R.id.playlist:
//            showDialog(R.layout.playlist);
//            return true;
        case R.id.playlistclear:
        	Log.i(TAG, "playlistclear");
        	// set playlist setting to 0
        	//TODO: figure out where to store playlist data
        	return true;
        case R.id.playlistselected:
            // this is a fantastic intent trick from com/android/music/MusicUtils.java and MediaPlaybackActivity.java
        	long sel = item.getIntent().getLongExtra("playlist", 0);
        	Log.i(TAG, "sel " + sel );
        	// set playlist setting to sel
        	return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
    	//Dialog dialog;
        switch(id) {

        case R.layout.rename: //renameDialogInt: // from Context Item
        	LayoutInflater factory = LayoutInflater.from(mark.this);
        	textEntryView = factory.inflate(R.layout.rename, null);
        	
        	/* return the constructed AlertDialog */
            // TODO: can enter be intercepted during dialog text entry?
        	return new AlertDialog.Builder(mark.this)
            .setTitle(R.string.renametitle)
            .setView(textEntryView)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK */
                	EditText b = (EditText) textEntryView.findViewById(R.id.renametext);
                	final CharSequence ca;
                	ca = (CharSequence) b.getText();
                	// TODO: if result not null & ! equal to old result
                    /* change preference */
                	CharSequence newAction = (CharSequence) ((Button)contextMenuItem).getTag();
                	//final CharSequence x = ((Button)contextMenuItem).getText();
                    //Log.i(TAG, "change " + newAction + " from " + x + " to " + ca );
                    SharedPreferences myMgrPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                    //Log.i(TAG, "mark myMgrPrefs before: " + myMgrPrefs.getAll().toString() );
                    Editor e = myMgrPrefs.edit();
                    e.putString( newAction.toString(), ca.toString());
                    e.commit();
                    //Log.i(TAG, "mark myMgrPrefs  after: " + myMgrPrefs.getAll().toString());
                	
                    /* change button */
                	((Button)contextMenuItem).setText(ca);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked cancel so do nothing */
                }
            })
            //.setIcon(R.drawable.alert_dialog_icon)
            .create();
        	//(R.layout.rename);
        	//DialogInterface.setOnDismissListener(this.onDismiss());

// moved to context submenu
//        case R.layout.playlist:
//        	LayoutInflater playFactory = LayoutInflater.from(mark.this);
//        	View playView = playFactory.inflate(R.layout.playlist, null);
//
//        	/* playlist dialog */
//        	return new AlertDialog.Builder(mark.this)
//            .setTitle(R.string.playlist)
//            .setView(playView)
//            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    /* User clicked OK */
//                }
//            })
//            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    /* User clicked cancel so do nothing */
//                }
//            })
//            //.setIcon(R.drawable.alert_dialog_icon)
//            .create();

        case R.layout.remove:
        	LayoutInflater removeFactory = LayoutInflater.from(mark.this);
        	View confirmView = removeFactory.inflate(R.layout.remove, null);

        	// confirm remove dialog
        	return new AlertDialog.Builder(mark.this)
            .setTitle(R.string.removetitle)
            .setView(confirmView)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked OK */
                    /* change preferences by moving all down to fill the gap */
                	CharSequence oldAction = (CharSequence) ((Button)contextMenuItem).getTag();
                    SharedPreferences myMgrPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                    int len = myMgrPrefs.getAll().size();
                	// assume string of exactly "actionX", X<10
                	int begin = Integer.parseInt(oldAction.subSequence(6, 7).toString());
                	//Log.i(TAG, "remove " + oldAction + ", move from " + begin + " to len " + len);
                    //Log.i(TAG, "mark myMgrPrefs before: " + myMgrPrefs.getAll().toString() );
                    Editor e = myMgrPrefs.edit();
                    for (int i = begin; i < len ; ++i) {
                        String newAction = "action" + i;
                        String movedAction = "action" + (i+1);
                        e.putString( newAction, myMgrPrefs.getString(movedAction, "error") ); // error if defaults
                    }
                    e.remove("action"+(len-1));
                    e.commit();
                    //Log.i(TAG, "mark myMgrPrefs  after: " + myMgrPrefs.getAll().toString());

                    // redraw
                    Intent myRemovePrefIntent = new Intent(getBaseContext(), mark.class);
                    startActivity(myRemovePrefIntent);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    /* User clicked cancel so do nothing */
                }
            })
            //.setIcon(R.drawable.alert_dialog_icon)
            .create();

        default:
            return null;
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog d) {
        //super.onPrepareDialog(id, d);
        switch(id) {
        case R.layout.rename: //renameDialogInt: // from Context Item
        	// prepare default text of dialog box with button name
        	//Resources ress = textEntryView.getResources();
        	//Log.i(TAG, "res current button name " + ((Button)contextMenuItem).getText() ); // shows new data for R.id.renametext
            View y = d.findViewById(R.id.renametext);
            //Log.i(TAG, "res y " + y.toString());
            ((TextView) y).setText(((Button)contextMenuItem).getText());
            //Log.i(TAG, "changed");
// moved to context submenu
//        case R.layout.playlist:
//        	/* Get list of playlists */
//        	//Context.sendOrderedBroadcast(android.);
//        	
//        	//foo = MediaStore.Audio.Playlists(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI);  // Audio.Playlists(android.provider.MediaStore);// android.provider.EXTERNAL_CONTENT_URI);
//        	//String bar = foo.getContentUri();
//        	
//        	//ContentResolver cr = getContentResolver();
//        	//cr.query(android.provider.MediaStore.Playlists.CONTENT_URI, projection, selection, selectionArgs, sortOrder);//Audio.PlaylistsColumns);
//
//        	/* populate ListView */
//            ViewGroup p = (ViewGroup) findViewById(R.id.playlistlist); // p is a ListView
//
//        	List<String> listPlay = new ArrayList<String>();
//        	List<String> listIds = new ArrayList<String>();
//        	ArrayList<View> listViews = new ArrayList<View>();
//        	//String playlist = "oneshot";
//        	String playlistid = null;
//        	String newListName = null;
//        	TextView addMe = null;
//
//        	Cursor cursor = getContentResolver().query(
//        			MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
//        			null, null, null, null);
//        	if (cursor != null) {
//        	 if (cursor.moveToFirst()) {
//        	  do {
//        	     playlistid = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID));
//        	     newListName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME));
//        	     listPlay.add(newListName);
//        	     listIds.add(playlistid);
//        	     addMe = new Button(getBaseContext());
//        	     addMe.setText(newListName);
//        	     addMe.setMinLines(3);
//        	     addMe.setTag(playlistid);
//        	     addMe.setClickable(true);
//        	     addMe.setLongClickable(false);
//        	     addMe.setFocusableInTouchMode(false);
//        	     addMe.setFocusable(true);
//        	     //addMe.setOnClickListener((OnClickListener) this);
//        	        
//        	        p.addView(addMe);
//        	     listViews.add(addMe);
//        	  } while (cursor.moveToNext());
//        	     cursor.close();
//        	 }
//        	}
//        	Log.i(TAG, "playlistid " + playlistid);
//        	Log.i(TAG, "listPlay " + listPlay.toString());
//        	Log.i(TAG, "listIds " + listIds.toString());
//            //p.addTouchables(listViews);
//
////        	// play playlist
////        	Intent intent = new Intent(Intent.ACTION_VIEW);
////        	intent.setComponent(new ComponentName("com.android.music","com.android.music.PlaylistBrowserActivity"));
////        	intent.setType(MediaStore.Audio.Playlists.CONTENT_TYPE);
////        	intent.setFlags(0x10000000); // need to understand these 3 lines
////        	//intent.putExtra(playlist, false);
////        	intent.putExtra("playlist", playlistid);
////        	startActivity(intent);
////        	// test mp3 http://download29.jamendo.com/download/track/469312/mp32/24deaf7def/Hope.mp3	
        }
    }
};
