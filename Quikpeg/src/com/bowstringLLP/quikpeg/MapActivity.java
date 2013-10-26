package com.bowstringLLP.quikpeg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends FragmentActivity{

	GoogleMap mMap = null;
	Double currentLatitude, currentLongitude;
	Records record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		// Show the Up button in the action bar.
		if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
			initializeActionBar();
		}
		
		if (MainActivity.dialog == null) {
			MainActivity.dialog = ProgressDialog.show(
					this, null, "Loading");
			MainActivity.dialog.setCancelable(true);
			MainActivity.dialog
					.setCanceledOnTouchOutside(false);
		} else if (MainActivity.dialog.isShowing() == false)
			MainActivity.dialog.show();
		
		record = (Records) getIntent().getSerializableExtra(MainActivity.STORE_RECORD);
		currentLatitude = getIntent().getDoubleExtra(MainActivity.LOCATION_LATITUDE,0);
		currentLongitude = getIntent().getDoubleExtra(MainActivity.LOCATION_LONGITUDE,0);
		setTitle("Directions to " + record.name);
		setUpMapIfNeeded();
		try
		{
			Location loc = new Location("MyProvider");
			loc.setLatitude(currentLatitude);
			loc.setLongitude(currentLongitude);
			GetDistance(loc);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@TargetApi(11)
	private void initializeActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.isCheckable())
			item.setChecked(!item.isChecked());
		
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			onBackPressed();/*
			if(getSupportFragmentManager().getBackStackEntryCount()>0)
				getSupportFragmentManager().popBackStack();*/
			break;
		case R.id.map_refresh:
			GetDistance(mMap.getMyLocation());
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
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.

	        }
	    }
	    mMap.setMyLocationEnabled(true);
	}

	private void GetDistance(Location locOrigin) {
		try
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

			StringBuilder urlString = new StringBuilder();
			//urlString.append("http://maps.googleapis.com/maps/api/distancematrix/json?");
			//urlString.append("origins=");//from
			//urlString.append( Double.toString(locOrigin.getLatitude() / 1E6));
			//urlString.append(",");
			//urlString.append( Double.toString(locOrigin.getLongitude()/ 1E6));
			//urlString.append("&destinations=");//to
			//urlString.append( Double.toString(latDest/ 1E6));
			//urlString.append(",");
			//urlString.append( Double.toString(longDest/ 1E6));
			//urlString.append("&mode=driving&sensor=true");
			//Log.d("xxx","URL="+urlString.toString());
			urlString.append("http://maps.googleapis.com/maps/api/distancematrix/json?origins=12.994,77.663317&destinations=12.585187,77.046223&mode=driving&sensor=true");

			// get the JSON And parse it to get the directions data.
			HttpURLConnection urlConnection= null;
			URL url = null;

			try
			{
				System.setProperty("http.keepAlive", "false");
				url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin="+locOrigin.getLatitude()+","+locOrigin.getLongitude()+"&destination="+record.latitude.toString()+","+record.longitude.toString()+"&mode=driving&sensor=true");
				urlConnection=(HttpURLConnection)url.openConnection();
				urlConnection.setRequestMethod("GET");
				//urlConnection.setDoOutput(true);
				//urlConnection.setDoInput(true);
				//urlConnection.connect();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			//InputStream inStream = urlConnection.getInputStream();
			//try{
			BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}


			String temp, response = "";
			while((temp = bReader.readLine()) != null){
				//Parse data
				response += temp;
			}
			//Close the reader, stream & connection
			bReader.close();
			//inStream.close();
			urlConnection.disconnect();

			JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
			JSONArray routes = object.getJSONArray("routes");

			JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");

			JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");

			Double[] startLat = new Double[30];
			Double[] startLng = new Double[30];
			Double[] endLat = new Double[30];
			Double[] endLng = new Double[30];

			for(int i=0; i<steps.length(); i++)
			{
				startLat[i] = Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").getString("lat"));
				startLng[i] = Double.parseDouble(steps.getJSONObject(i).getJSONObject("start_location").getString("lng"));

				endLat[i] = Double.parseDouble(steps.getJSONObject(i).getJSONObject("end_location").getString("lat"));
				endLng[i] = Double.parseDouble(steps.getJSONObject(i).getJSONObject("end_location").getString("lng"));
			}

			initialiseMap(DecodePolylinePoints(routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")));
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			if(MainActivity.dialog!=null)
				MainActivity.dialog.dismiss();
		}
	}

private void initialiseMap(List<LatLng> poly) {
	
	int i = poly.size();
	Double maxLat, minLat, maxLng, minLng;
			
	Polyline line = mMap.addPolyline(new PolylineOptions().addAll(poly));
	line.setWidth(5);
	line.setColor(Color.BLUE);
	
	if(poly.get(0).latitude>poly.get(i-1).latitude)
	{
		maxLat = poly.get(0).latitude;
		minLat = poly.get(i-1).latitude;
	}
	else
	{
		minLat = poly.get(0).latitude;
		maxLat = poly.get(i-1).latitude;
	}
	
	if(poly.get(0).longitude>poly.get(i-1).longitude)
	{
		maxLng = poly.get(0).longitude;
		minLng = poly.get(i-1).longitude;
	}
	else
	{
		minLng = poly.get(0).longitude;
		maxLng = poly.get(i-1).longitude;
	}
	
	LatLng northEast = new LatLng(maxLat, maxLng);
	LatLng southWest = new LatLng(minLat, minLng);
	
	mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(southWest,northEast),400,400,10));
	Marker marker = mMap.addMarker(new MarkerOptions()
    .position(new LatLng(record.latitude, record.longitude))
    .title(record.name)
    .snippet(record.address));
	marker.showInfoWindow();
	}

private List<LatLng> DecodePolylinePoints(String encodedPoints) 
{
    if (encodedPoints == null || encodedPoints == "") return null;
    List<LatLng> poly = new ArrayList<LatLng>();
    char[] polylinechars = encodedPoints.toCharArray();
    int index = 0;

    int currentLat = 0;
    int currentLng = 0;
    int next5bits;
    int sum;
    int shifter;
   
    try
    {
        while (index < polylinechars.length)
        {
            // calculate next latitude
            sum = 0;
            shifter = 0;
            do
            {
                next5bits = (int)polylinechars[index++] - 63;
                sum |= (next5bits & 31) << shifter;
                shifter += 5;
            } while (next5bits >= 32 && index < polylinechars.length);

            if (index >= polylinechars.length)
                break;

            currentLat += (sum & 1) == 1 ? ~(sum >> 1) : (sum >> 1);

            //calculate next longitude
            sum = 0;
            shifter = 0;
            do
            {
                next5bits = (int)polylinechars[index++] - 63;
                sum |= (next5bits & 31) << shifter;
                shifter += 5;
            } while (next5bits >= 32 && index < polylinechars.length);

            if (index >= polylinechars.length && next5bits >= 32)
                break;

            currentLng += (sum & 1) == 1 ? ~(sum >> 1) : (sum >> 1);
            LatLng p = new LatLng((double) currentLat / 100000.0,(double) currentLng / 100000.0);
            poly.add(p);
        } 
    }
    catch (Exception ex)
    {
        // logo it
    }
    return poly;
}

}