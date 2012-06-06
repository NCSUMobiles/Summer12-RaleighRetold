package com.citycamp.raleighretold;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class DevPlanPlacesActivity extends MapActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maptabview);	    
	    
	    ArrayList<Development> developments = getIntent().getParcelableArrayListExtra("developmentsObj");
	    	    
        Iterator<Development> iter = developments.iterator();
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        List<Overlay> mapOverlays = mapView.getOverlays();
        
        while(iter.hasNext()) {
        	
        	Development dev = iter.next();
		    Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		    VenueItemizedOverlay itemizedoverlay = new VenueItemizedOverlay(drawable, this);
	
		    GeoPoint devLoc = getPoint(dev.getLatitude(),dev.getLongitude());
	
		    mapView.getController().setCenter(devLoc);	 // centers the map on this point
		    mapView.getController().setZoom(18);
		    OverlayItem overlayitem0 = new OverlayItem(devLoc, dev.getCaseName(), dev.getCaseYear());
		      
		    itemizedoverlay.addOverlay(overlayitem0);
		    mapOverlays.add(itemizedoverlay);	
		    
        }
        
//        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
//        
//        myLocationOverlay.enableMyLocation();
//        myLocationOverlay.enableCompass();
//        mapOverlays.add(myLocationOverlay);        

	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
//	public void onHomeClick(View v) {
//		Log.i("inside onHomeClick","inside onHomeClick");
//		UIUtils.goHome(this);
//	}
//
//	public void onSearchClick(View v) {
//		Log.i("inside onSearchClick","inside onSearchClick");
//		UIUtils.goSearch(this);
//	}
	
}
