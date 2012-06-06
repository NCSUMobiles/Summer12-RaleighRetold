package com.citycamp.raleighretold;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class VenueItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	
	public VenueItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;

	}
	
	// add new overlay items to the array
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);		
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
//	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//	  dialog.setTitle(item.getTitle());
//	  dialog.setMessage(item.getSnippet());
//	  dialog.show();
	  Intent i = new Intent(mContext, EventsActivity.class);
      // put extras for the items over to the Events activity
      i.putExtra("title", item.getTitle());
      i.putExtra("snippet",item.getSnippet());        
      mContext.startActivity(i);  
      return true;
	}
	

}
