package com.bowstringLLP.quikpeg;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bowstringLLP.quikpeg.MainActivity.RecordsUpdateListener;
import com.bowstringLLP.quikpeg.MainFragment.ListItemClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTabFragment extends Fragment implements RecordsUpdateListener{

	GoogleMap mMap = null;
	ListItemClickListener mListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ListItemClickListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ListItemClickListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.activity_map, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setUpMapIfNeeded();
		mListener.fetchRecords(false);
	}
	
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }
	    mMap.setMyLocationEnabled(true);
}

	@Override
	public void onRecordsUpdated(List<Records> records) {
		for(int i=0; i<records.size(); i++)
		{
			Marker marker = mMap.addMarker(new MarkerOptions()
		    .position(new LatLng(records.get(i).latitude, records.get(i).longitude))
		    .title(records.get(i).name)
		    .snippet(records.get(i).address));
		}
	}
}
