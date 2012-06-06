package com.citycamp.raleighretold;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class XYInputActivity extends Activity {

	private String strAddress = "";
	Geocoder geocoder;
	private String latStr = "";
	private String lngStr = "";
	private String lngLat = "";
	private static final String area = "100";
	String addressString = "No address found";
//	LocationManager locationManager;
//	LocationListener locationListener;
	Intent locatorService = null;
    AlertDialog alertDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit);
		TextView searchaddress = (TextView)findViewById(R.id.searchdescr);
		searchaddress.setText(Html.fromHtml(getString(R.string.searchaddress)));							
	}
	
//	public XYInputActivity(Context context) {
//		this.context = context;
//	}
	
	/** Handle address search */
    public void onSubmitClick(View v) {
    	
    	final EditText et;	
		et = (EditText) findViewById(R.id.edittext);
		strAddress = et.getText().toString();
		
		// use geocoder to get lat long from address
		Geocoder coder = new Geocoder(this);
		List<Address> address = null;    			
		
		    try {
				address = coder.getFromLocationName(strAddress,1);
			
		    if (address == null || address.isEmpty()) {
		    	Toast msg1 = Toast.makeText(getBaseContext(), "Invalid Search Entry Please Try Again",
						Toast.LENGTH_LONG);
				msg1.show();		    
			} else {
				Address location = address.get(0);
				Log.i("lat from Address Search",""+location.getLatitude());
				Log.i("lng from Address Search",""+location.getLongitude());
				latStr = Double.toString(location.getLatitude());
				lngStr = Double.toString(location.getLongitude());
				lngLat = lngStr + "," + latStr;
//				JsonDownloaderAsyncTask jsonDl = new JsonDownloaderAsyncTask();
//				jsonDl.download(this, lngLat, area);
				
				
											
				StringBuilder sb = new StringBuilder();
				if (address.size() > 0) {
					 for (int cnt = 0; cnt < location.getMaxAddressLineIndex(); cnt++)
	                        sb.append(location.getAddressLine(cnt)).append("\n");
				}
				addressString = sb.toString();
				Log.i("Address!!!!!!!!",addressString);
				
				// Citycamp
				Toast msg1 = Toast.makeText(getBaseContext(), "Searching around "+addressString,
						Toast.LENGTH_LONG);
				msg1.show();
				
				startActivity(new Intent(this, DummyMapActivity.class));
				
			}	   

		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
    }
    
    	
    public void onLocationClick(View v) {

    	startActivity(new Intent(this, DummyMapActivity.class));
//    	if (!startService()) {
//            CreateAlert("Error!", "Service Cannot be started");
//        } else {
////            Toast.makeText(XYInputActivity.this, "Service Started",
////                    Toast.LENGTH_LONG).show();        	
//        }
    }
    
    public boolean stopService() {
        if (this.locatorService != null) {
            this.locatorService = null;
        }
        return true;
    }

    public boolean startService() {
        try {
            FetchCordinates fetchCordinates = new FetchCordinates();
            fetchCordinates.execute();
            return true;
        } catch (Exception error) {
            return false;
        }
    }

    public AlertDialog CreateAlert(String title, String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle(title);

        alert.setMessage(message);

        return alert;
    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {
        ProgressDialog progDailog = null;

        public double lati = 0.0;
        public double longi = 0.0;

        public LocationManager mLocationManager;
        public ChadsLocationListener mChadsLocationListener;

        @Override
        protected void onPreExecute() {
        	mChadsLocationListener = new ChadsLocationListener();
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 0, 0,
//                    mVeggsterLocationListener);
            
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0,
                    mChadsLocationListener);

            progDailog = new ProgressDialog(XYInputActivity.this);
            progDailog.setMessage("Loading GPS. Please Wait...");
            progDailog.setIndeterminate(true);
            progDailog.setCancelable(true);
            progDailog.show();

        }

        protected void onPostExecute(String result) {
            progDailog.dismiss();

//            Toast.makeText(XYInputActivity.this,
//                    "LATITUDE :" + lati + " LONGITUDE :" + longi,
//                    Toast.LENGTH_LONG).show();            
            
            lngLat = longi + "," + lati;
            
            mLocationManager.removeUpdates(mChadsLocationListener);
            
            JsonDownloaderAsyncTask jsonDl = new JsonDownloaderAsyncTask();
			jsonDl.download(XYInputActivity.this, lngLat, area);
            
        }

        @Override
        protected String doInBackground(String... params) {
            while (this.lati == 0.0) {
            	
            }
            return null;
        }

        public class ChadsLocationListener implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                int lat = (int) location.getLatitude(); // * 1E6);
                int log = (int) location.getLongitude(); // * 1E6);
                int acc = (int) (location.getAccuracy());

                String info = location.getProvider();
                try {
                    lati = location.getLatitude();
                    longi = location.getLongitude();                    
                } catch (Exception e) {
                    // progDailog.dismiss();
                    // Toast.makeText(getApplicationContext(),"Unable to get Location"
                    // , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("OnProviderDisabled", "OnProviderDisabled");
                mLocationManager.removeUpdates(mChadsLocationListener);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("onProviderEnabled", "onProviderEnabled");
                mLocationManager.removeUpdates(mChadsLocationListener);
            }

            @Override
            public void onStatusChanged(String provider, int status,
                    Bundle extras) {
                Log.i("onStatusChanged", "onStatusChanged");
                mLocationManager.removeUpdates(mChadsLocationListener);

            }

        }

    }
//    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//    	
//    	locationListener = new LocationListener() {
//    		@Override
//    	    public void onLocationChanged(Location location) {
//    	    	Double lat = location.getLatitude();
//    	    	Double lng = location.getLongitude();
//    	    	Log.i("lat,lng from Location GPS manager",""+lat+" "+lng);
//    	    	latStr = Double.toString(lat);
//				lngStr = Double.toString(lng);
//				lngLat = lngStr + "," + latStr;
//				
//				JsonDownloaderAsyncTask jsonDl = new JsonDownloaderAsyncTask();
//				jsonDl.download(XYInputActivity.this, lngLat, area);
//				
//				locationManager.removeUpdates(locationListener);			
//    	    }
//    		@Override
//    	    public void onStatusChanged(String provider, int status, Bundle extras) {
//    	    	Log.i("onStatusChanged",provider);
//    	    	locationManager.removeUpdates(locationListener);
//    	    }
//    		@Override
//    	    public void onProviderEnabled(String provider) {
//    	    	Log.i("onProviderEnabled",provider);
//    	    	locationManager.removeUpdates(locationListener);
//    	    }
//    		@Override
//    	    public void onProviderDisabled(String provider) {
//    	    	Log.i("onProviderDisabled",provider);
//    	    	locationManager.removeUpdates(locationListener);
//    	    }
//    	  };
//    	  
//    	// Register the listener with the Location Manager to receive location updates
//    	// CHF - 05/15/2012 be careful here the integers control the frequency of the updates to your location
//    	// second is the minimum time interval between notifications and the third is the minimum change
//    	// in distance between notifications—setting both to zero requests location notifications 
//    	// as frequently as possible
//    
//    	  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//    	  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    	
    	
//    }
	

}
