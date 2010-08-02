package com.activehabits.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class chart extends Activity {

	private static final String TAG = "ActiveHabits.chart";
	
	private static final Integer MAXEVENTS = 100; // TODO: fix max # events String[] of 50

	/* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        // not sure how to change some values from .../src/achart/doc/org/achartengine/chart/TimeChart.html
        // we created an Intent so the actual class is instantiated inside the other Activity
    	Intent draw = ChartFactory.getTimeChartIntent(getApplicationContext(), getDataset(), getRenderer(), "string");
    	try { startActivity(draw); }
    	catch (ActivityNotFoundException e) { e.printStackTrace(); }
        catch (NullPointerException e) { e.printStackTrace(); }
    	Log.i(TAG, "chart drawn");

        // TODO: org.achartengine menu
    	
    	finish(); // finishes chart activity, leaves the new org.achartengine* activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.removeItem(R.id.chart); // we are in chart so disable chart menu item
        inflater.inflate(R.menu.habit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.chart);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        //use setPreferences(int)?
//        showDialog(R.id.dialog_choose_chart);
    }

    @Override
    protected Dialog onCreateDialog(int id) { // TODO: chart OnPrepareDialog
        switch(id) {
        case R.id.dialog_choose_chart:
            // Respond to anything from this dialog by drawing right now.
        	CharSequence[] items = new CharSequence[] {"all"}; // now predifined
                // will populate with habit names
        	return new AlertDialog.Builder(chart.this)
        	    .setTitle( R.string.chart )
        	    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int whichButton) {
    	        	    finish();
    	            }
    	        }
        	    ).setItems(items, new DialogInterface.OnClickListener() {
    	                public void onClick(DialogInterface dialog, int whichButton) {
    	                	Intent draw = ChartFactory.getTimeChartIntent(getApplicationContext(), getDataset(), getRenderer(), null);
    	                	try {
    	                		startActivity(draw);
    	                	}
    	                	catch (ActivityNotFoundException e) {
    	                		e.printStackTrace();
    	                	}
    	                	Log.i(TAG, "drawn chart");
    	                		

//                            finish();
//    	                	if whichButton == 1
//    	        	            x;
//    	        	        else
//    	        	        	;
                        }
        	        }
        	    ).create();
//        	.setOnCancelListener(arg0)
//        	.setOnKeyListener(arg0)
//          .setCancelable(false)
            default:
            return null;
        }
    }
    
    private XYMultipleSeriesDataset getDataset() {
        Integer l = 0; // lines of data, zero based
        String[] eventName = new String[MAXEVENTS];
        String[] eventSec = new String[MAXEVENTS];
        String[] eventHour = new String[MAXEVENTS];
        String[] eventMore = new String[MAXEVENTS];
        // assume data on SDcard exists and is good
        // read data from SDcard
        try {
            final File root = Environment.getExternalStorageDirectory();
            final Resources res = getResources();
            final String sFileName = res.getString(R.string.log_event_filename);
            final File gpxfile = new File(root,sFileName);
            BufferedReader buf = new BufferedReader(new FileReader(gpxfile)); // appends
            String[] temp = new String[10];
            String x = buf.readLine();
            while ( x.length() > 0 ) {
                //Log.i(TAG, "buf.readline() " + x); // USEFUL
                if ( ! x.startsWith("#") ) { // if not a comment
                    Log.i(TAG, "chart read: " + x);
                    temp = x.split("\t", 4); // Max 4 strings split on tabs, perfect
                    //Log.i(TAG, "eventName " + temp[0]);
                    eventName[l] = temp[0];
                    //Log.i(TAG, "eventSec " + temp[1]);
                    eventSec[l] = temp[1];
                    //Log.i(TAG, "eventHour " + temp[2]);
                    eventHour[l] = temp[2];
                    //Log.i(TAG, "eventMore " + temp[3]);
                    eventMore[l] = temp[3];
                    l += 1; // only count lines of data
                }
                x = buf.readLine(); // next line
            }
        }
        catch (NullPointerException e) { // for arrays - normal end of the loop!
            //e.printStackTrace();
        } catch (FileNotFoundException e) { // for New File...
            e.printStackTrace();
        } catch (IOException e) { // for problems with reading buf
            e.printStackTrace();
        }
        l -= 1; // that wasn't data

        // Got Data!
        // currently assumes data is in chronological order
        // X values are seconds (not milliseconds) from (int) eventSec[0] to eventSec[l]
        // Y values are hours from eventHour[0] to eventHour[l]
        // X range varies with start and end times - ideally add buffer on both sides
        // Y range is 0 - 24 hours in the day from TOP to BOTTOM like a Calendar.
        // right now each eventName is the same - this will change
        
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = l;
        // long minXvalue = eventSec[0] * 1000; // Seconds to Milliseconds
        // Random r = new Random();
        for (int i = 0; i < 1; i++) { // only one series until eventNames change
          TimeSeries series = new TimeSeries("Marked Events"); // eventName
          for (int k = 0; k < nr; k++) {
            series.add(new Date(Long.parseLong(eventSec[k])*1000), Double.parseDouble(eventHour[k]));
          }
          //Log.i(TAG, "series" + series.toString());
          dataset.addSeries(series);
        }
        //Log.i(TAG, "ready to ship dataset");
        return dataset;
      }

    private XYMultipleSeriesRenderer getRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.GREEN);
        r.setPointStyle(PointStyle.TRIANGLE);
        r.setLineWidth((float) 0.0);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setLabelsColor(Color.LTGRAY);
        renderer.setLabelsTextSize(10);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setXLabels(3); // TODO: setXLabels sucks, need to addTextLabels at specific points manually
        renderer.setYLabels(3);
        renderer.setYAxisMin(0.0);
        renderer.setYAxisMax(24.0);
        renderer.setLegendTextSize(14);
        renderer.setDisplayChartValues(true);
        renderer.setShowGrid(true);
        Log.i(TAG, "label text name " + renderer.getTextTypefaceName());
        Log.i(TAG, "label text style " + renderer.getTextTypefaceStyle());
        renderer.setChartValuesTextSize(18);
        //Log.i(TAG, "renderer " + renderer.toString());
        return renderer;
      } 
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.mark:
        	Intent myIntent = new Intent(this, mark.class);
        	startActivity(myIntent);
            return true;
//      case R.id.chart: // item removed
//          return true;
//      case R.id.social:
//          return true;
        case R.id.settings:
            return true; // TODO: settings from chart
        case R.id.about:
            return true; // TODO: about from chart
        case R.id.quit: {
            finish();
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

};
