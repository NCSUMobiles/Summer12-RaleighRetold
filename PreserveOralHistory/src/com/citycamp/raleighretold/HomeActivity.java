package com.citycamp.raleighretold;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Front-door {@link Activity} that displays high-level features the schedule
 * application offers to users.
 */
public class HomeActivity extends Activity implements OnSharedPreferenceChangeListener {
	private static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences settings;
	public String spinnerID = "spinnerID";
	private long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homemenu);

        settings = getSharedPreferences(PREFS_NAME, 1);
        settings.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        
    	Log.i("HomeActivity onResume......","HomeActivity onResume......");
        super.onResume();
    }

    /** Handle "refresh" title-bar action. */
//    public void onRefreshClick(View v) {
//        // trigger off background sync
//        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
//        intent.putExtra(SyncService.EXTRA_STATUS_RECEIVER, mState.mReceiver);
//        startService(intent);
//
//        reloadNowPlaying(true);
//    }

    /** Handle "schedule" action. */
    public void onScheduleClick(View v) {
        // Launch overall conference schedule
        startActivity(new Intent(this, TellStoryActivity.class));
    }
    
    /** Handle "share" action. */
    public void onShareClick(View v) {
        // Launch overall conference schedule
    	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://post/2710898_123018201171661?owner=2710898")));
    }

    /** Handle "map" action. */
    public void onMapClick(View v) {
        // Uncomment below to use the Java-based map (depricated to allow server-side maps)
        // startActivity(new Intent(this, StoryMapActivity.class));
    	 startActivity(new Intent(this, WebMapActivityJSInterface.class));
    }
    
    /** Handle "venue" action. */
    public void onAboutClick(View v) {
        // Launch list of sessions user has starred
        startActivity(new Intent(this, AboutActivity.class));
    }

    /*
     * Handle date spinner selection
     */
    public class MyOnItemSelectedListener implements OnItemSelectedListener {
		@Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		/*
		 * When a spinner item is selected, add spinner selection (id) to shared preferences file	
		 */
	      settings = getSharedPreferences(PREFS_NAME, 1);
	      SharedPreferences.Editor editor = settings.edit();     
	      editor.putLong(spinnerID, id);
	      editor.commit();			
		        
    	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub	
				id = settings.getLong(spinnerID, id);
				Log.i("onNothingSelected in HomeActivity id = ", ""+id);

				//drawUI();
		}
	
    }
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		id = settings.getLong(spinnerID, id);
		Log.i("onSharedPreferenceChanged in HomeActivity id = ", ""+id);
		// TODO Auto-generated method stub
		
	}

 

    
}
