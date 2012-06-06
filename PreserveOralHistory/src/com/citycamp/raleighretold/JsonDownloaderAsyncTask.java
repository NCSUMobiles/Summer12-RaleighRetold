package com.citycamp.raleighretold;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class JsonDownloaderAsyncTask {
	
	static ProgressDialog dialog;
	String address = "no address available";
	
	
	public void download(Context context, String... parms) {
		DownloadJsonTask task = new DownloadJsonTask(context);            
        dialog = ProgressDialog.show(context, "", 
                "Loading Data. Please wait...", true);            
        task.execute(parms[0],parms[1]);        
    }	
}

	class DownloadJsonTask extends AsyncTask<String, Void, ArrayList<Development>> {
	
		private final Context context;
		LocationManager locationManager;
		LocationListener locationListener;
		
		public DownloadJsonTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected ArrayList<Development> doInBackground(String... parms) {			
	        //return RestClient.loadDevelopmentsFromNetwork(parms[0],parms[1]);
	        return RestClient.showMyLocation(parms[0],parms[1]);
	    }
		
		
		
		@Override
	    protected void onPostExecute(ArrayList<Development> developments) {
	         Log.i("onPostExecute","do the page layout here");
	         JsonDownloaderAsyncTask.dialog.dismiss();
	         runOnPostExecute(developments);
	    }
		
		private void runOnPostExecute(ArrayList<Development> developments) {
	    	if (developments.size() < 1){
	            Toast msg = Toast.makeText(context, "No results found, Please try again",
	                                Toast.LENGTH_LONG);
	                   msg.show();
	                   Intent i = new Intent(context, XYInputActivity.class);
	                   context.startActivity(i);
		     } else {      
		            Intent i = new Intent(context, DevPlanPlacesActivity.class);   
		                   Bundle b = new Bundle();
		                   b.putParcelableArrayList("developmentsObj", developments);
		                   i.putExtras(b);      
		                   //i.putExtra("address", address);
		                   context.startActivity(i);
		     }
	    	
		}
		
	 }
	 
    
		



