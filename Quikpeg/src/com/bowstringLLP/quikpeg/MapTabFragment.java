package com.bowstringLLP.quikpeg;

import java.util.List;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bowstringLLP.quikpeg.MainActivity.RecordsUpdateListener;
import com.bowstringLLP.quikpeg.MainFragment.ListItemClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTabFragment extends Fragment implements RecordsUpdateListener{

	GoogleMap mMap = null;
	ListItemClickListener mListener;
	List<Records> records;
	View mapView;
	
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
		if(mapView == null)
		{
			mapView = inflater.inflate(R.layout.activity_map, container, false);
			return mapView;
		}
		else
		{
			ViewGroup grp = (ViewGroup) mapView.getParent();
			grp.removeAllViews();
			return mapView;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setUpMapIfNeeded();
		mListener.fetchRecords(false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.main_map, menu);
		
		Intent prefsIntent = new Intent(getActivity(), SettingsActivity.class);
		MenuItem preferences = menu.findItem(R.id.action_settings);
		preferences.setIntent(prefsIntent);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		// The intent does not have a URI, so declare the "text/plain" MIME type
		emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.suggestionRecipients)); // recipients
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
		MenuItem suggest = menu.findItem(R.id.Suggestion);
		suggest.setIntent(emailIntent);

		Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
		MenuItem about = menu.findItem(R.id.About);
		about.setIntent(aboutIntent);
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.isCheckable())
			item.setChecked(!item.isChecked());
		
		switch (item.getItemId()) {
		case android.R.id.home:
			getActivity().onBackPressed();
			break;
		case R.id.satellite: 
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); 
			break;
		case R.id.map: 
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); 
			break;
		case R.id.hybrid: 
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); 
			break;
		case R.id.Refresh:
				mListener.fetchRecords(true);
				break;
		default:
			startActivity(item.getIntent());
		}
		return super.onOptionsItemSelected(item);
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
		if(records != null)
		{
			this.records = records;
			for(int i=0; i<records.size(); i++)
			{
				mMap.addMarker(new MarkerOptions()
			    .position(new LatLng(records.get(i).latitude, records.get(i).longitude))
			    .title(records.get(i).name)
			    .snippet(records.get(i).address));
			}

			mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					for(int i=0; i<MapTabFragment.this.records.size(); i++)
						if(MapTabFragment.this.records.get(i).name.matches(marker.getTitle()) && MapTabFragment.this.records.get(i).address.matches(marker.getSnippet()))
						{
							mListener.onListItemClick(i);
							break;
						}
				}
			});
			
			String str = getActivity().getSharedPreferences("com.bowstringLLP.quikpeg_preferences", Context.MODE_PRIVATE).getString("Mode", "NORMAL");
			switch(MainActivity.Mode.valueOf(str))
			{
			case	NORMAL:
			case	DAYBEFOREDRY:
				getActivity().setTitle(getString(R.string.mainTitle));
				getActivity().setTitleColor(Color.parseColor("#ffffff"));
				break;

			case	DRY:
				getActivity().setTitle("DRY DAY");
				getActivity().setTitleColor(Color.parseColor("#D89020"));
				break;

			case	LASTGOODSEARCH:
				getActivity().setTitle("LAST GOOD SEARCH");
				getActivity().setTitleColor(Color.parseColor("#D89020"));
			}
			
			LatLng loc = new LatLng(records.get(0).latitude, records.get(0).longitude);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
			
			if(MainActivity.dialog != null)
				MainActivity.dialog.dismiss();
		}
	}
}
