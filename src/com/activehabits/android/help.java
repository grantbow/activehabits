/*Copyright 2011 Grant Bowman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

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
