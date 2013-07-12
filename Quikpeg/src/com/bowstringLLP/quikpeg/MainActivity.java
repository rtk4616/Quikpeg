package com.bowstringLLP.quikpeg;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.bowstringLLP.quikpeg.MainFragment.ListItemClickListener;
import com.bowstringLLP.quikpeg.NoticeDialogFragment.NoticeDialogListener;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends FragmentActivity implements NoticeDialogListener, ListItemClickListener{

	LocationFinder locFind;
	Location currentLocation;
	RecordBuilder builder;
	static boolean isLocationUpdated = false;
	static Long timeElapsed = (long) 0;
	List<Records> records;
	static SharedPreferences settings;
	DetailsFragment detailsFrag;
	MainFragment mainFrag;
	Records selectedRecord;
	static ProgressDialog dialog;
	static enum Mode {NORMAL, LASTGOODSEARCH, DRY, DAYBEFOREDRY};
	Mode mode;
	static RecordsUpdateListener recListener;

	public final static String STORE_RECORD = "com.bowstringLLP.oneclickalcohol.STORE_RECORD";
	public final static String LOCATION_LATITUDE = "com.bowstringLLP.oneclickalcohol.LOCATION_LATITUDE";
	public final static String LOCATION_LONGITUDE = "com.bowstringLLP.oneclickalcohol.LOCATION_LONGITUDE";

	public interface RecordsUpdateListener
	{
		public void onRecordsUpdated(List<Records> records);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		timeElapsed = (long) 0;
		getOverflowMenu();
		
		locFind = new LocationFinder();
		builder = new RecordBuilder(this);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		settings.registerOnSharedPreferenceChangeListener(prefListener);
		settings.edit().putString("Mode", Mode.NORMAL.toString()).apply();

		fetchRecords(true);
	}

	@TargetApi(11)
	private void initializeActionBar(boolean flag) {
		getActionBar().setDisplayHomeAsUpEnabled(flag);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			if(key == "Mode")
			{
				mode = Mode.valueOf(prefs.getString("Mode", "NORMAL"));
				switch(mode)
				{
				case NORMAL:	Toast.makeText(getApplicationContext(), "Normal Mode", Toast.LENGTH_LONG).show();
				break;
				case DRY:	Toast.makeText(getApplicationContext(), "Dry Day: All shops would be closed today on account of " + builder.getDryDay().holidayName, Toast.LENGTH_LONG).show();
				break;
				case DAYBEFOREDRY:	Toast.makeText(getApplicationContext(), "Dry Day Tomorrow: Please stock up on your booz as all shops will be closed tomorrow on account of " + builder.getDryDay().holidayName, Toast.LENGTH_LONG).show();
				break;
				case LASTGOODSEARCH:	Toast.makeText(getApplicationContext(), "LAST GOOD SEARCH", Toast.LENGTH_LONG).show();
				break;
				}
			}
			else
			{
				setRecordList(prefs.getBoolean(key, true));
			}
		}
};

@Override
protected void onStop()
{
	super.onStop();
		settings.edit().apply();
		if(builder.getMasterRecordList() !=null && builder.getMasterRecordList().size() != 0)
			builder.writeGoodRecords();		

		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		builder.closeDatabase();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.Refresh:
			onDialogRetryClick(null);
			break;
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			startActivity(item.getIntent());
		}
		return true;
	}
	
	private void getOverflowMenu() {

	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	     
	}
	
	public void showNoticeDialog(String s) {
		//initialiseFragments();
		// Create an instance of the dialog fragment and show it
		DialogFragment dialog = NoticeDialogFragment.newInstance(s);
		dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
	}

	// The dialog fragment receives a reference to this Activity through the
	// Fragment.onAttach() callback, which it uses to call the following methods
	// defined by the NoticeDialogFragment.NoticeDialogListener interface

	@Override
	public void onDialogContinueClick(DialogFragment dialog) {
		builder.readGoodRecords();		
		if(findViewById(R.id.list) == null)
			loadFragments();	

		setRecordList(settings.getBoolean("Status", true));
	}

	@Override
	public void onDialogRetryClick(DialogFragment dialog) {
		new BuildListTask().execute(null,null,null);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		finish();
	}

	private void loadFragments() {
		
			setContentView(R.layout.activity_main);
			
			// Check that the activity is using the layout version with
			// the fragment_container FrameLayout
			if (findViewById(R.id.fragment_container) != null) {

				/*// Create an instance of ExampleFragment
				mainFrag = new MainFragment();

				// In case this activity was started with special instructions from an Intent,
				// pass the Intent's extras to the fragment as arguments
				mainFrag.setArguments(getIntent().getExtras());

				// Add the fragment to the 'fragment_container' FrameLayout
				getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFrag).commit();
				//getSupportFragmentManager().executePendingTransactions();*/
				
				new TabInterfaceManager(this);
			}
			else
			{
				mainFrag =  (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
				detailsFrag = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details_fragment);
				detailsFrag.updateDetailView(records.get(0));
			}	
	}
	
	@Override
	public void onListItemClick(int position) {
		selectedRecord = records.get(position);
		detailsFrag = (DetailsFragment)
				getSupportFragmentManager().findFragmentById(R.id.details_fragment);

		if (detailsFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			detailsFrag.updateDetailView(selectedRecord);
		} else {
			// Otherwise, we're in the one-pane layout and must swap frags...

			// Create fragment and give it an argument for the selected article
			detailsFrag = new DetailsFragment();
			Bundle args = new Bundle();
			args.putSerializable(STORE_RECORD, selectedRecord);
			detailsFrag.setArguments(args);
			setTitle(selectedRecord.name);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			// Replace whatever is in the fragment_container view with this fragment,
			// and add the transaction to the back stack so the user can navigate back
			transaction.replace(R.id.fragment_container, detailsFrag);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
			
			if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.HONEYCOMB) {
				initializeActionBar(true);
			}
		}
	}			
	
	private void setRecordList(boolean prefrence) {
		
		String str;
		if(prefrence)
		{
			if(isLocationUpdated)
				str = getString(R.string.storesUnavailable);
			else
				str = getString(R.string.locRecUnavailable);

			records = builder.getMasterRecordList();
		}
		else
		{
			records = builder.getOpenRecordList();
			str = getString(R.string.storesClosed);
		}		

		if(records == null)
			showNoticeDialog(str);
		else
			recListener.onRecordsUpdated(records);
	}

	@Override
	public void fetchRecords(boolean shouldRefresh)
	{
		if(shouldRefresh)
			new BuildListTask().execute(null,null,null);
		else
			recListener.onRecordsUpdated(records);
	}
	
	public void onClickMap(View view)
	{
		dialog = ProgressDialog.show(this, null, "Loading...");
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra(STORE_RECORD, selectedRecord);
		intent.putExtra(LOCATION_LATITUDE, getIntent().getDoubleExtra(LOCATION_LATITUDE,currentLocation.getLatitude()));
		intent.putExtra(LOCATION_LONGITUDE, getIntent().getDoubleExtra(LOCATION_LONGITUDE,currentLocation.getLongitude()));
		startActivity(intent);
	}

	LocationManager locationManager;
	
	public class BuildListTask extends AsyncTask<Void, Void, Void> {
		boolean isTimerFinished = false;
		CountDownTimer timer;
		
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			if(findViewById(R.id.list) != null)
				dialog = ProgressDialog.show(MainActivity.this, null, "Loading...");			
			
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locFind.setLocationManager(locationManager);
			
			timer = new CountDownTimer(60000,1000)
			{

				@Override
				public void onFinish() {
					isTimerFinished = true;
				}

				@Override
				public void onTick(long arg0) {
					// TODO Auto-generated method stub
					
				}
				
			};
		}
		
		@Override
		protected Void doInBackground(Void... voids) {
			isLocationUpdated = false;
			timeElapsed = System.currentTimeMillis();
			findLocation(listener);
			
			while(true)
			{
				if(isCancelled() || isLocationUpdated)
					break;
				else if (isTimerFinished)
				{
					showNoticeDialog(getString(R.string.offTheGrid));
				if(settings.getString("Mode", "NORMAL") != Mode.DRY.toString())
					settings.edit().putString("Mode", Mode.LASTGOODSEARCH.toString());

				BuildListTask.this.cancel(true);
				}
			}
			return null;
		}

		LocationListener listener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				timer.cancel();
				isLocationUpdated = true;
				if(isBetterLocation(location, currentLocation))
				{
					if(location == null)
						return;

					currentLocation = location;
					builder.buildRecordList(currentLocation);
					runOnUiThread(new Runnable() {
					     public void run() {
					    	 if(findViewById(R.id.list) == null)
								loadFragments();

							setRecordList(settings.getBoolean("Status", true));
					    }
					});					
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}
		};		
		
		@Override
		protected void onPostExecute(Void voids)
		{
			super.onPostExecute(null);
			if(dialog != null)
				dialog.dismiss();
		}
		
		@Override
		protected void onCancelled()
		{
			locationManager.removeUpdates(listener);
			timer.cancel();
			if(dialog != null)
				dialog.dismiss();			
		}
	}	
	public boolean isBetterLocation(Location location, Location currentBestLocation) {
		final int TWO_MINUTES = 1000 * 60 * 2;
		
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	public Location findLocation(LocationListener listener)
	{
		Looper.prepare();
		locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
		locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
		Looper.loop();
		return currentLocation;
	}
}
