package com.activehabits.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class splash extends Activity implements OnClickListener {
    //private static final String TAG = "ActiveHabits.mark";
	//private static OnClickListener mSplashListener;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG, "splash");
        setContentView(R.layout.splash);
        View v;
        v = (View)findViewById(R.id.ImageView01);
        v.setOnClickListener(this);
        v = (View)findViewById(R.id.TextView01);
        v.setOnClickListener(this);
        v = (View)findViewById(R.id.ButtonSplashOK);
        v.setOnClickListener(this);
    }
    
    public void onClick(View v) {
      // finish when anything is clicked
        finish();
    }

}