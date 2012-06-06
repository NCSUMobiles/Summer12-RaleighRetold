package com.citycamp.raleighretold;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class DummyMapActivity extends MapActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maptabview);
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
	    VenueItemizedOverlay itemizedoverlay = new VenueItemizedOverlay(drawable, this);

	    
	    GeoPoint fayCityPlazaPoint = getPoint(35.77509,-78.63944);
	    GeoPoint berkeleyCafePoint = getPoint(35.77692,-78.64282);
	    GeoPoint deepSouthPoint = getPoint(35.77455,-78.64383);
	    GeoPoint fiveStarPoint = getPoint(35.77852,-78.64621);
	    GeoPoint fletcherOperaTheaterPoint = getPoint(35.77148,-78.63955);
	    GeoPoint theHiveatBusyBeeCafePoint = getPoint(35.77750,-78.63817);
	    GeoPoint kingsBarcadePoint = getPoint(35.77704,-78.63994);
	    GeoPoint theLincolnTheatrePoint = getPoint(35.77414,-78.63745);
	    GeoPoint thePourHousePoint = getPoint(35.77727,-78.63679);
	    GeoPoint slimsPoint = getPoint(35.77744,-78.63812);
	    GeoPoint tirNaNogPoint = getPoint(35.77738,-78.63666);
	    GeoPoint theUnionPoint = getPoint(35.77573,-78.64467);
	    GeoPoint whiteCollarCrimePoint = getPoint(35.77553,-78.64404);

	    //mapView.getController().animateTo(fayCityPlazaPoint);  // centers the map on this point	    
	    mapView.getController().setCenter(fayCityPlazaPoint);	 // centers the map on this point	    
	    mapView.getController().setZoom(16);
	    OverlayItem overlayitem = new OverlayItem(fayCityPlazaPoint, "Fayetteville Street in 1955", "450 Fayetteville St.");
	    OverlayItem overlayitem2 = new OverlayItem(berkeleyCafePoint, "Craven House History", "217 W. Martin St.");
	    OverlayItem overlayitem3 = new OverlayItem(deepSouthPoint, "Bar Brawls of the 1930s", "430 S. Dawson St.");
	    OverlayItem overlayitem4 = new OverlayItem(fiveStarPoint, "Soldiers March on Martin St", "511 W. Hargett St.");
	    OverlayItem overlayitem5 = new OverlayItem(fletcherOperaTheaterPoint, "History of Fletcher Opera Theatre", "2 E South St");
	    OverlayItem overlayitem6 = new OverlayItem(theHiveatBusyBeeCafePoint, "African American Business History of Hargett St", "225 S. Wilmington St.");
	    OverlayItem overlayitem7 = new OverlayItem(kingsBarcadePoint, "The first Hotel in Raleigh", "14 W. Martin St.");
	    OverlayItem overlayitem8 = new OverlayItem(theLincolnTheatrePoint, "WWII Stories in 5 Points", "126 E. Cabarrus St.");
	    OverlayItem overlayitem9 = new OverlayItem(thePourHousePoint, "When Oakwood was a new neighborhood", "224 S. Blount St.");
	    OverlayItem overlayitem10 = new OverlayItem(slimsPoint, "My favorite memory from Childhood", "227 S. Wilmington St.");
	    OverlayItem overlayitem11 = new OverlayItem(tirNaNogPoint, "The first Suburb of Raleigh", "218 S. Blount St.");
	    OverlayItem overlayitem12 = new OverlayItem(theUnionPoint, "The Raleigh City Flag History", "327 W. Davie St., No. 114");
	    OverlayItem overlayitem13 = new OverlayItem(whiteCollarCrimePoint, "Civil War History", "319 West Davie St.");
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);	    
	    itemizedoverlay.addOverlay(overlayitem2);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem3);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem4);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem5);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem6);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem7);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem8);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem9);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem10);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem11);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem12);
	    mapOverlays.add(itemizedoverlay);
	    itemizedoverlay.addOverlay(overlayitem13);
	    mapOverlays.add(itemizedoverlay);

	}
	
	private GeoPoint getPoint(double lat, double lon) {
		return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
	public void onHomeClick(View v) {
		Log.i("inside onHomeClick","inside onHomeClick");
		UIUtils.goHome(this);
	}

	public void onSearchClick(View v) {
		Log.i("inside onSearchClick","inside onSearchClick");
		UIUtils.goSearch(this);
	}
	
}
