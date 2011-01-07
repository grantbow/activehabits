package com.activehabits.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class help extends Activity implements OnClickListener {
    private static final String TAG = "AH.help";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "help");
        setContentView(R.layout.help);
        View v;
        v = (View)findViewById(R.id.ImageView02);
        v.setOnClickListener(this);
        v = (View)findViewById(R.id.TextView02);
        v.setOnClickListener(this);
        v = (View)findViewById(R.id.ButtonHelpOK);
        v.setOnClickListener(this);
    }
    
    public void onClick(View v) {
      // finish when the anything is clicked
        finish();
    }

}
