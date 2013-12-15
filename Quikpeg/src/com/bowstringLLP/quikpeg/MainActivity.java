package com.bowstringLLP.quikpeg;

import java.lang.reflect.Field;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.bowstringLLP.quikpeg.MainFragment.ListItemClickListener;
import com.bowstringLLP.quikpeg.NoticeDialogFragment.NoticeDialogListener;

public class MainActivity extends FragmentActivity implements
		NoticeDialogListener, ListItemClickListener {

	public static final String LOCATION_LATITUDE = "com.bowstringLLP.oneclickalcohol.LOCATION_LATITUDE";
	public static final String LOCATION_LONGITUDE = "com.bowstringLLP.oneclickalcohol.LOCATION_LONGITUDE";
	public static final String STORE_RECORD = "com.bowstringLLP.oneclickalcohol.STORE_RECORD";
	
	static ProgressDialog dialog;
	static boolean isLocationUpdated = false;
	int offset = 20;
	static RecordsUpdateListener recListener;
	static SharedPreferences settings;
	LocationManager locationManager;
	static RecordBuilder builder;
	static Location currentLocation;
	boolean isTimerFinished = false;
	static List<Records> records;
	static Records selectedRecord;
	CountDownTimer timer;
	private TabsClass tab;

	static enum Mode {
		NORMAL, LASTGOODSEARCH, DRY, DAYBEFOREDRY
	};

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(android.os.Build.VERSION.SDK_INT < 11)
		{
			setContentView(R.layout.tabs_layout);
			tab = new TabsClass(savedInstanceState, this);
		}
		else
		{// setup action bar for tabs
		    ActionBar actionBar = getActionBar();
		    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		    actionBar.setDisplayShowTitleEnabled(false);

		    Tab tab = actionBar.newTab()
		                       .setText("List")
		                       .setTabListener(new TabListener<MainFragment>(
		                               this, "List", MainFragment.class));
		    actionBar.addTab(tab);

		    tab = actionBar.newTab()
		                   .setText("Dry")
		                   .setTabListener(new TabListener<DryFragment>(
		                           this, "Dry", DryFragment.class));
		    actionBar.addTab(tab);
			getOverflowMenu();
			//setContentView(R.layout.activity_main);
		}
		
		if(builder==null)
			builder = new RecordBuilder(getApplicationContext());

		MainFragment mainFrag = ((MainFragment) getSupportFragmentManager().findFragmentByTag("List"));
		
		//if (mainFrag == null) {
			settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			settings.registerOnSharedPreferenceChangeListener(prefListener);
			settings.edit().putString("Mode", Mode.NORMAL.toString()).apply();

			fetchRecords(true);
			
			if (mainFrag == null) {
			mainFrag = new MainFragment();
			FragmentTransaction localFragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			localFragmentTransaction.add(R.id.fragment_container,
					mainFrag, "MAIN");
			localFragmentTransaction.addToBackStack(null);
			localFragmentTransaction.commit();
		}
	}
	
	public void onDialogContinueClick(DialogFragment paramDialogFragment) {
		builder.readGoodRecords();
		setRecordList(settings.getBoolean("Status", true));
	}

	public void onDialogNegativeClick(DialogFragment paramDialogFragment) {
		finish();
	}

	public void onDialogRetryClick(DialogFragment paramDialogFragment) {
		fetchRecords(true);
	}

	@Override
	public void onListItemClick(int position) {
		selectedRecord = records.get(position);
		DetailsFragment detailsFrag = (DetailsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.details_fragment);

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
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack so the user can
			// navigate back
			transaction.replace(R.id.fragment_container, detailsFrag);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}
	}

	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (timer != null)
			{
				timer.cancel();
				timer=null;
			}			

			isLocationUpdated = true;
			
				currentLocation = location;

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						
						if (MainActivity.dialog == null || (MainActivity.dialog != null && MainActivity.dialog.isShowing() == false)) {
							MainActivity.dialog = ProgressDialog.show(getParent(), null, "Loading");
							MainActivity.dialog.setCancelable(true);
							MainActivity.dialog
									.setCanceledOnTouchOutside(false);
						}
					}

					@Override
					protected Void doInBackground(Void... params) {
						builder.buildRecordList(currentLocation);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						setRecordList(settings.getBoolean("Status", true));
					}

				}.execute();

			}

		public void onProviderDisabled(String paramString) {
		}

		public void onProviderEnabled(String paramString) {
		}

		public void onStatusChanged(String paramString, int paramInt,
				Bundle paramBundle) {
		}
	};

	SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			if (key == "Mode") {
				Mode mode = Mode.valueOf(prefs.getString("Mode", "NORMAL"));
				switch (mode) {
				case NORMAL:
					Toast.makeText(getApplicationContext(), "Normal Mode",
							Toast.LENGTH_LONG).show();
					break;
				case DRY:
					Toast.makeText(
							getApplicationContext(),
							"Dry Day: All shops would be closed today on account of "
									+ builder.getDryDay().holidayName,
							Toast.LENGTH_LONG).show();
					break;
				case DAYBEFOREDRY:
					Toast.makeText(
							getApplicationContext(),
							"Dry Day Tomorrow: Please stock up on your booz as all shops will be closed tomorrow on account of "
									+ builder.getDryDay().holidayName,
							Toast.LENGTH_LONG).show();
					break;
				case LASTGOODSEARCH:
					Toast.makeText(getApplicationContext(), "LAST GOOD SEARCH",
							Toast.LENGTH_LONG).show();
					break;
				}
			} else if(key == "Status")
				setRecordList(prefs.getBoolean(key, true));
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		settings.edit().apply();
		if (builder.getMasterRecordList() != null
				&& builder.getMasterRecordList().size() != 0)
			builder.writeGoodRecords();

		if(dialog!=null)
		{	dialog.dismiss();
		dialog = null;
		}
		// EasyTracker.getInstance().activityStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		builder.closeDatabase();
	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(getApplicationContext());
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@TargetApi(11)
	private void initializeActionBar(boolean paramBoolean) {
		getActionBar().setDisplayHomeAsUpEnabled(paramBoolean);
	}

/*	private boolean isSameProvider(String paramString1, String paramString2) {
		if (paramString1 == null)
			return paramString2 == null;
		return paramString1.equals(paramString2);
	}*/

	private void setRecordList(boolean prefrence) {

		String str;
		if (prefrence) {
			if (isLocationUpdated)
				str = getString(R.string.storesUnavailable);
			else
				str = getString(R.string.locRecUnavailable);

			records = builder.getMasterRecordList();
		} else {
			records = builder.getOpenRecordList();
			str = getString(R.string.storesClosed);
		}

		if (records == null)
			showNoticeDialog(str);
		else
			recListener.onRecordsUpdated(records, offset);
	}

	public void fetchRecords(boolean shouldRefresh) {
		if (shouldRefresh) {
			
			if (MainActivity.dialog == null || (MainActivity.dialog != null && MainActivity.dialog.isShowing() == false)) {
				MainActivity.dialog = ProgressDialog.show(this, null, "Loading");
				MainActivity.dialog.setCancelable(true);
				MainActivity.dialog
						.setCanceledOnTouchOutside(false);
			}
			offset = 20;
			locationManager = ((LocationManager) getSystemService("location"));
			isLocationUpdated = false;
			findLocation(listener);

			timer = new CountDownTimer(60000, 1000) {

				public void onFinish() {
					isTimerFinished = true;
					locationManager.removeUpdates(listener);
					showNoticeDialog(getString(R.string.offTheGrid));

					if (settings.getString("Mode", "NORMAL") != MainActivity.Mode.DRY
							.toString())
						settings.edit().putString("Mode",
								Mode.LASTGOODSEARCH.toString());
				}

				public void onTick(long paramLong) {
					if (dialog == null || dialog.isShowing() == false)
						cancel();
				}
			}.start();
		} else
			recListener.onRecordsUpdated(records, offset);
	}

	public void findLocation(LocationListener paramLocationListener) {
		this.locationManager.requestSingleUpdate("network",
				paramLocationListener, null);
		this.locationManager.requestSingleUpdate("gps", paramLocationListener,
				null);
	}

	public void onClickMap(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra(STORE_RECORD, selectedRecord);
		intent.putExtra(
				LOCATION_LATITUDE,
				getIntent().getDoubleExtra(LOCATION_LATITUDE,
						currentLocation.getLatitude()));
		intent.putExtra(
				LOCATION_LONGITUDE,
				getIntent().getDoubleExtra(LOCATION_LONGITUDE,
						currentLocation.getLongitude()));
		startActivity(intent);
	}

	public void showNoticeDialog(String paramString) {
		DialogFragment dialog = NoticeDialogFragment.newInstance(paramString);
		dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
	}

	public interface RecordsUpdateListener {
		public void onRecordsUpdated(List<Records> paramList, int paramInt);
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@SuppressLint("NewApi")
	protected void onSaveInstanceState(Bundle outState) {
		if(android.os.Build.VERSION.SDK_INT < 11)
			outState.putString("tab", tab.mTabHost.getCurrentTabTag());
		else
			outState.putString("tab", getActionBar().getSelectedTab().getText().toString());
		super.onSaveInstanceState(outState);
	}
}