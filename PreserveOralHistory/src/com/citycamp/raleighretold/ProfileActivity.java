package com.citycamp.raleighretold;

import android.app.Activity;
import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.about_list);
	        setContentView(R.layout.createuserprofile);
	        //TextView welcome = (TextView)findViewById(R.id.abouttext);
	        //welcome.setText(Html.fromHtml(getString(R.string.About)));

	        Log.i("inside ProfileActivity","ProfileActivity onCreate");
	        
//	        final Button button = (Button) findViewById(R.id.emailbutton);
//	         button.setOnClickListener(new View.OnClickListener() {
//	             public void onClick(View v) {
//	                 // Perform action on click
//	            	 emailMeMethod(v);
//	             }
//	         });
	        
//	        String appVersion = getVersion();
	        //String[] displayFields = new String[] {"location", "address1", "address2", "phone"};
	        //int[] displayViews = new int[] {R.id.location, R.id.address1, R.id.address2, R.id.phone};    
	        
	        String[] aboutArray = new String[] {"+chad foley, @emulsion, sardeenz@gmail.com", "Feel free to get in touch.  Tip: You can now add 'starred' bands to your calendar via your phone's Menu Option button","version 1.11"};
	        
// Citycamp	        
//			SeparatedListAdapter adapter = new SeparatedListAdapter(this);
//			
//			adapter.addSection("About", new ArrayAdapter<String>(this,R.layout.about_list, aboutArray));
//	    	ListView lv = getListView();
//	    	lv.setTextFilterEnabled(true);
			//(ListView) findViewById(android.R.id.list);
	    	//lv.setFastScrollEnabled(true);
			//lv.setAdapter(adapter);	
//Citycamp setListAdapter(adapter); 
	    }
	    
	    
//	    private String getVersion() {
//			String versionNo = ""; 
//	        PackageInfo pInfo = null; 
//	        try{ 
//	                pInfo = getPackageManager().getPackageInfo ("com.foley.hopscotch.planner",PackageManager.GET_META_DATA); 
//	        } catch (NameNotFoundException e) { 
//	                pInfo = null; 
//	        } 
//	        if(pInfo != null) 
//	                versionNo = ""+pInfo.versionCode;
//	        		return versionNo;
//		}

		public void emailMeMethod(View view) {
	    	Log.i("inside EmailMethod","insideEmailMethod");
	    }
	    
	    public void onHomeClick(View v) {
	       	Log.i("inside onHomeClick","inside onHomeClick");
	           UIUtils.goHome(this);
	       }

//	       public void onSearchClick(View v) {
//	       	Log.i("inside onSearchClick","inside onSearchClick");
//	           UIUtils.goSearch(this);
//	       }
	    
}

